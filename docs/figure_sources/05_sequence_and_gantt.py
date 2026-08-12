import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from matplotlib.patches import FancyArrowPatch, Rectangle
import matplotlib.dates as mdates
import datetime as dt

OUT='/home/user/CapstoneProject/docs/figures'
GREEN='#16A34A'; DEEP='#166534'; LIGHT='#DCFCE7'; GREY='#475569'; AMBER='#F59E0B'

# ---------------------------------------------------------------- Sequence diagram
actors = ["Passenger\n(app)", "PassengerViewModel", "RideRepository", "Firebase\nRealtime Database",
          "DriverViewModel", "Driver\n(app)"]
messages = [
    (0,1,"tap Find a Driver"),
    (1,1,"FareEngine.quote(config, stop, type, n)"),
    (1,2,"requestRide(pickup, stop, fareType, seats)"),
    (2,3,"write /rideRequests/{id}"),
    (3,4,"child event: new open request", True),
    (4,5,"render request card with countdown"),
    (5,4,"tap Accept"),
    (4,2,"acceptRequest(driverId, request)"),
    (2,3,"write /rides/{id}, delete /rideRequests/{id}"),
    (3,1,"child event: active ride", True),
    (1,0,"switch to tracking view"),
    (3,4,"child event: active ride", True),
    (5,4,"advance status"),
    (4,2,"updateRideStatus(rideId, next)"),
    (2,3,"patch /rides/{id}/status"),
    (3,1,"status change", True),
    (1,0,"timeline updates; on COMPLETED show receipt"),
]

fig, ax = plt.subplots(figsize=(13.5, 11))
x = {i: i*2.35 for i in range(len(actors))}
top = 1.0
bottom = -len(messages)*0.62 - 1.2

for i, a in enumerate(actors):
    ax.add_patch(Rectangle((x[i]-0.95, top-0.42), 1.9, 0.72,
                 facecolor=LIGHT, edgecolor=DEEP, lw=1.2, zorder=3))
    ax.text(x[i], top-0.06, a, ha='center', va='center', fontsize=9.5,
            fontweight='bold', color='#0F172A', zorder=4)
    ax.plot([x[i], x[i]], [top-0.42, bottom+0.5], color=GREY, lw=0.9,
            ls=(0,(4,4)), zorder=1)

y = top - 1.05
for m in messages:
    src, dst, label = m[0], m[1], m[2]
    is_return = len(m) > 3 and m[3]
    color = GREEN if not is_return else AMBER
    style = '-' if not is_return else '--'
    if src == dst:
        ax.add_patch(FancyArrowPatch((x[src], y), (x[src]+0.55, y-0.16),
            connectionstyle="arc3,rad=-1.6", arrowstyle='-|>', mutation_scale=11,
            color=color, lw=1.3, zorder=5))
        ax.text(x[src]+0.85, y-0.02, label, fontsize=8.2, va='center', ha='left', color='#0F172A')
    else:
        ax.add_patch(FancyArrowPatch((x[src], y), (x[dst], y), arrowstyle='-|>',
            mutation_scale=12, color=color, lw=1.4, linestyle=style, zorder=5))
        mid = (x[src]+x[dst])/2
        ax.text(mid, y+0.10, label, fontsize=8.2, ha='center', va='bottom', color='#0F172A')
    y -= 0.62

ax.text(x[0]-0.95, bottom+0.15,
        "Solid = call     Dashed = database push to a live listener",
        fontsize=8.5, color=GREY, style='italic')
ax.set_xlim(-1.5, x[len(actors)-1]+1.5)
ax.set_ylim(bottom, top+0.5)
ax.axis('off')
plt.tight_layout()
plt.savefig(f'{OUT}/fig09_sequence_booking.png', dpi=170, bbox_inches='tight', facecolor='white')
plt.close()
print('ok  fig09_sequence_booking')

# ---------------------------------------------------------------- Gantt chart
tasks = [
    ("Problem identification and needs assessment", "2025-08-01", "2025-09-15", GREEN),
    ("Review of related literature and systems",    "2025-09-01", "2025-10-15", GREEN),
    ("Proposal writing and defense",                "2025-10-01", "2025-11-30", GREEN),
    ("Requirements analysis and documentation",     "2025-11-15", "2025-12-31", GREEN),
    ("System design (diagrams, database, UI)",      "2025-12-01", "2026-01-31", GREEN),
    ("Development: authentication and onboarding",  "2026-01-05", "2026-02-28", DEEP),
    ("Development: booking and matching",           "2026-02-01", "2026-03-31", DEEP),
    ("Development: admin, fares and reports",       "2026-03-01", "2026-04-30", DEEP),
    ("Fare table transcription and verification",   "2026-04-01", "2026-05-15", AMBER),
    ("Unit and integration testing",                "2026-04-15", "2026-05-31", AMBER),
    ("Pilot deployment to respondents",             "2026-06-01", "2026-06-30", AMBER),
    ("User acceptance testing and evaluation",      "2026-06-15", "2026-07-31", AMBER),
    ("Data analysis and interpretation",            "2026-07-15", "2026-08-15", '#3B82F6'),
    ("Final manuscript and oral defense",           "2026-08-01", "2026-09-30", '#3B82F6'),
]
fig, ax = plt.subplots(figsize=(13, 6.4))
for i, (name, s, e, c) in enumerate(reversed(tasks)):
    sd = dt.datetime.strptime(s, "%Y-%m-%d"); ed = dt.datetime.strptime(e, "%Y-%m-%d")
    ax.barh(i, (ed-sd).days, left=sd, height=0.58, color=c, edgecolor='white', lw=0.8)
ax.set_yticks(range(len(tasks)))
ax.set_yticklabels([t[0] for t in reversed(tasks)], fontsize=9.5)
ax.xaxis.set_major_locator(mdates.MonthLocator(interval=1))
ax.xaxis.set_major_formatter(mdates.DateFormatter('%b\n%Y'))
ax.grid(axis='x', color='#E2E8F0', lw=0.8)
ax.set_axisbelow(True)
for spine in ('top','right','left'):
    ax.spines[spine].set_visible(False)
ax.spines['bottom'].set_color('#CBD5E1')
ax.tick_params(axis='x', labelsize=8.5, colors=GREY)
ax.set_xlim(dt.datetime(2025,7,20), dt.datetime(2026,10,10))
plt.tight_layout()
plt.savefig(f'{OUT}/fig14_gantt.png', dpi=170, bbox_inches='tight', facecolor='white')
plt.close()
print('ok  fig14_gantt')
