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
