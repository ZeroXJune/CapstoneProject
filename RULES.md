# Realtime Database security rules

`database.rules.json` is the authoritative copy. The app talks to Firebase
directly with no server in between, so these rules are the only access control
that exists: anything they allow is allowed, whatever the interface shows.

## Deploying

```
npm install -g firebase-tools     # once
firebase login                    # once
firebase use <your-project-id>    # once
firebase deploy --only database
```

Deploy from this file rather than pasting into the console. A rule set that
lives only in the console has no history, no review, and no way to tell whether
what is running matches what was agreed.

## Why the extra `.validate` on `drivers/$uid/verificationStatus`

A `.write` granted at a shallower path cannot be revoked by a deeper one —
Firebase evaluates write rules top-down and stops at the first that grants
access. `drivers/$uid` grants the driver write over their whole record, so the
`verificationStatus` child rule that names an administrator did not *restrict*
anything: it only added the administrator as a second permitted writer. Any
driver could set their own `verificationStatus` to `APPROVED`, which is the one
value the whole verification workflow turns on — an approved driver reads every
open ride request (with the pickup coordinates and notes on it) and can accept
passengers without an administrator ever seeing their licence.

`.validate` is the mechanism that does restrict, because every validate rule on
the written data has to pass. The rule here says the value may only change when
an administrator is writing it, or when the record is being created (where
`PENDING` is the only value allowed).

The same pattern guards `complaints/$id/status`, so a reporter cannot mark their
own complaint resolved.

## Size limits

Every free-text and image field now carries a length limit. Without one, a
single account can write megabytes into a node the administrator screens read in
full — `users`, `rides`, `complaints` are all loaded whole by the admin app —
and exhaust the free tier's storage and download quota for everybody.

## Read scope

Two collection-level read grants were wider than any implemented feature needed and
have been narrowed:

- **`drivers`** was readable by every signed-in account. Only the administrator screens
  read the node whole (`AdminRepository.drivers()`); a passenger tracking their ride
  reads one record through `getDriverFlow(driverId)`. Leaving the collection open let
  any account pull every driver's live `currentLocation` in a single request and follow
  the whole fleet. The collection is now administrator-only and `drivers/$uid` stays
  readable, so nothing in the app changes. This narrows bulk enumeration; it does not
  stop someone who already knows a uid from watching that one driver, which needs the
  position to move off the driver record entirely — see below.
- **`profilePhotos/$uid`** was readable by every signed-in account. `ProfileViewModel`
  only ever loads the signed-in user's own avatar, so it is now scoped to the owner and
  administrators.

## A rule that would have frozen rides

The `.validate` on `rides/$rideId` requires the driver to be `APPROVED`. Firebase
evaluates a `.validate` at every ancestor of the write location, so as first written it
would also have run when the driver advanced the ride's `status` and when the passenger
attached their contact details. An administrator revoking a driver mid-ride would then
have made that ride unfinishable by anyone — the same permanent-stuck-ride state the
audit records as C-03, reachable through a legitimate administrator action. The check is
now gated on `data.exists()`, so it applies only when the ride is created.

Verify this one in the Rules Playground before relying on it: the exact ancestor-validate
behaviour is the reason the bug existed, and it is worth confirming rather than reasoning
about twice.

## A useful side effect

`registerDriver` writes the whole `drivers/{uid}` node with `setValue`, which resets
`verificationStatus` to `PENDING`. The new `.validate` refuses that write for a driver
whose record already exists, which turns the silent de-approval and history wipe recorded
as H-06 into a visible error. That is a better failure than the one it replaces, but it is
not the fix — the client should still distinguish "record not loaded yet" from "no record"
and use `updateChildren`.

## What these rules still do not fix

- **Ride creation is not validated against a request.** A driver may create a
  `rides/$rideId` naming any passenger, because the rule can only check that the
  new record's `driverId` is the caller. A fabricated completed ride inflates the
  driver's totals and the exported reports. Fixing this properly needs the
  accept step to be a server-side transaction (a Cloud Function), which is a
  paid-plan feature.
- **`estimatedFare` is supplied by the client.** The rules bound it to a sane
  range and freeze it after creation, but they cannot recompute it from the
  posted table. A modified client can book at a price it chose.
- **Ratings are not tied to a completed ride.** Any signed-in account may write
  `driverRatings/{driver}/{own uid}` for a driver it has never travelled with.
- **Notifications are writable by any approved driver to any user's node.** The
  write cannot be limited to the node's owner, because the whole point is that
  one party writes to another. Titles and messages are length-capped, but a
  hostile approved driver can still send a plausible-looking message to any user
  whose uid it knows.
- **A driver can write their own cached rating.** `drivers/$uid` is theirs to write and
  the average lives on it, so the range check (0–5) bounds the value but not the lie: a
  driver can set `rating` to 5 and `ratingCount` to anything. The authoritative ratings
  under `driverRatings/` are correct; the figure the admin screens and exported reports
  read is the cache.
- **Notification volume is unbounded.** Title and message are length-capped, but nothing
  limits how many notifications one sender writes into another user's feed.
- **Only string fields are size-capped.** The `$other` limit on `users/$uid` bounds
  strings; a nested object written under an unexpected key is still unbounded. Closing
  this properly needs an explicit allow-list of fields per node.
- **Driver position sits on a record other accounts can read by uid.** Narrowing the
  collection read stops enumeration, not targeted following. The clean fix is to move
  `currentLocation` off `drivers/{uid}` into a per-ride node written by that ride's
  driver and readable only by its two parties — a schema change, not a rules change.

## Fare bounds

`config` was administrator-only for writes and otherwise unvalidated, so a mistyped rate
went straight into the table that prices every ride. The admin fare dialog gates Save on
`toDoubleOrNull() != null` and nothing else, which accepts a negative fare and a fare of
999999 equally. Rates, the two minimums and the flat rates are now bounded to 0–1000, and
coordinates to real latitudes and longitudes.

This bounds the damage; it does not remove the need for a range check in the dialog,
which should refuse the value where the administrator can see why rather than letting the
write fail against a rule.

## Keeping the two caps in step

`complaints/$id/adminNote` is capped at 2000 characters and the notification that relays
it verbatim was capped at 500. A note between the two lengths updated the complaint and
then failed the notification write — and `SupportRepository.updateComplaint` does not
isolate the second call, so the administrator was told the whole update failed after it
had already succeeded, and would reasonably retry. The notification message cap now
matches the note it carries.

The underlying shape is still wrong: the complaint update and the notification are two
writes reported as one outcome. Wrapping the notify call the way `AdminViewModel`
already does for verification decisions would fix it properly.

## Cancelling a ride

`rides/$rideId` is the driver's to write, which is right for the status
progression and wrong for the one case the lifecycle never had: a ride that is
not going to finish. The passenger can now write `status`, but only the value
`CANCELLED`, and only while the ride is `ACCEPTED`, `DRIVER_ARRIVING` or
`DRIVER_ARRIVED` — once they are in the tricycle it is between them and the
driver. The driver keeps the whole progression, including `CANCELLED` and
`NO_SHOW`, because a breakdown does not wait for a convenient status.

`completedAt` is writable by either party, since either can be the one who ends
the ride, and `actualFare` is bounded like every other money field.

## Ratings finally check the ride

Keying a rating by the rater meant a rule could only ask "are you writing under
your own uid". Keying it by ride lets it ask the question that matters: the
writer must be the passenger named on that ride, the driver must be the one
named on it, and the ride must have completed. Ratings by an account that never
travelled with the driver — H-13 — are refused at the database rather than
merely absent from the interface.

The value shape is accepted either as a bare number, which is what ratings
written before this change look like, or as `{stars, raterId}`. `getRatingsFlow`
reads both, so a driver's history is not erased by the upgrade.

## A passenger reading their own request

Recovering a pending request on launch needs the passenger to ask
`rideRequests` a scoped question, and the collection was readable only by an
approved driver or an administrator — the per-request rule underneath is no help
to a client that does not yet know the id. The collection read now also admits
`orderByChild('passengerId').equalTo(<own uid>)`, the same shape `rides` and
`complaints` use, with the matching `.indexOn`. It grants a passenger their own
requests and nothing wider.
