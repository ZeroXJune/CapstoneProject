import subprocess, os
OUT='/home/user/CapstoneProject/docs/figures'
GREEN='#16A34A'; DEEP='#166534'; LIGHT='#DCFCE7'; PALE='#F0FDF4'; GREY='#475569'; AMBER='#FEF3C7'; BLUE='#DBEAFE'
HEAD=f'''
  graph [fontname="Helvetica", fontsize=11, bgcolor="white", pad=0.3];
  node  [fontname="Helvetica", fontsize=10, color="{DEEP}", fontcolor="#0F172A"];
  edge  [fontname="Helvetica", fontsize=9, color="{GREY}", fontcolor="{GREY}"];
'''
def render(name, src, engine='dot'):
    path=os.path.join(OUT,name+'.png')
    p=subprocess.run([engine,'-Tpng','-Gdpi=170','-o',path],input=src.encode(),capture_output=True)
    print(('ok   ' if p.returncode==0 else 'FAIL '),name, p.stderr.decode()[:300])

# ------------------------------------------------------------------ Use case
render('fig07_use_case', f'''
digraph G {{
  rankdir=LR; {HEAD}
  ranksep=1.8; nodesep=0.18;
  node [shape=box, style="filled", fillcolor="{LIGHT}", fontcolor="#0F172A", width=1.4];
  pas [label="Passenger"]; drv [label="Driver"]; adm [label="Administrator"];

  node [shape=ellipse, style=filled, fillcolor="{PALE}", color="{DEEP}", width=2.5, fixedsize=true, height=0.5, fontsize=9];
  u1  [label="Register an account"];
  u2  [label="Sign in / stay signed in"];
  u3  [label="Manage profile and photo"];
  u4  [label="Book a ride"];
  u5  [label="View fare before booking"];
  u6  [label="Track ride status"];
  u7  [label="Rate a completed ride"];
  u8  [label="View ride history"];
  u9  [label="File a concern"];
  u10 [label="Receive notifications"];
  u11 [label="Submit driver credentials"];
  u12 [label="Go online / offline"];
  u13 [label="Accept a ride request"];
  u14 [label="Advance ride status"];
  u15 [label="View earnings"];
  u16 [label="Verify or reject a driver"];
  u17 [label="Review and resolve concerns"];
  u18 [label="Maintain the fare table"];
  u19 [label="Monitor live activity"];
  u20 [label="Export activity reports"];

  pas -> u1; pas -> u2; pas -> u3; pas -> u4; pas -> u6;
  pas -> u7; pas -> u8; pas -> u9; pas -> u10;
  drv -> u1; drv -> u2; drv -> u3; drv -> u8; drv -> u9; drv -> u10;
  drv -> u11; drv -> u12; drv -> u13; drv -> u14; drv -> u15;
  adm -> u2; adm -> u3; adm -> u10;
  adm -> u16; adm -> u17; adm -> u18; adm -> u19; adm -> u20;
  u4 -> u5 [label="  «include»  ", style=dashed, arrowhead=vee, color="{DEEP}", fontcolor="{DEEP}"];
  u13 -> u16 [label="  «requires»  ", style=dashed, arrowhead=vee, color="{DEEP}", fontcolor="{DEEP}"];
}}
''')

# ------------------------------------------------------------------ Activity: book a ride

# ------------------------------------------------------------------ Class diagram
render('fig10_class_diagram', f'''
digraph G {{
  rankdir=TB; {HEAD}
  node [shape=record, style=filled, fillcolor="{PALE}", fontsize=9];
  edge [arrowhead=vee];

  screen [label="{{\\<\\<composable\\>\\> Screens|+ PassengerHomeScreen()\\l+ DriverHomeScreen()\\l+ AdminDashboardScreen()\\l+ MainAppScreen()\\l}}", fillcolor="{LIGHT}"];

  vm [label="{{\\<\\<abstract\\>\\> ViewModel|# viewModelScope: CoroutineScope\\l}}", fillcolor="{BLUE}"];
  pvm [label="{{PassengerViewModel|- rideRepository: RideRepository\\l- fareRepository: FareRepository\\l+ activeRides: StateFlow\\l+ fareStops: StateFlow\\l+ requestRide(...)\\l+ cancelPendingRequest()\\l}}"];
  dvm [label="{{DriverViewModel|- rideRepository: RideRepository\\l- driverRepository: DriverRepository\\l+ openRequests: StateFlow\\l+ earnings: StateFlow\\l+ acceptRequest(...)\\l+ advanceStatus(...)\\l}}"];
  avm [label="{{AdminViewModel|+ drivers: StateFlow\\l+ fareStops: StateFlow\\l+ complaints: StateFlow\\l+ approveDriver(id)\\l+ importOfficialRates()\\l}}"];

  repo [label="{{Repositories|+ AuthRepository\\l+ RideRepository\\l+ DriverRepository\\l+ AdminRepository\\l+ FareRepository\\l+ SupportRepository\\l}}", fillcolor="{LIGHT}"];
  svc  [label="{{FirebaseService|- database: FirebaseDatabase\\l+ createRideRequest(r)\\l+ getOpenRideRequestsFlow()\\l+ getFareStopsFlow()\\l+ importFareStops(list)\\l}}", fillcolor="{AMBER}"];

  ride [label="{{Ride|+ id: String\\l+ passengerId: String\\l+ driverId: String\\l+ fareStopId: String\\l+ fareType: FareType\\l+ status: RideStatus\\l+ estimatedFare: Double\\l}}"];
  stop [label="{{FareStop|+ id: String\\l+ zone: String\\l+ name: String\\l+ regularFare: Double\\l+ discountedFare: Double\\l+ active: Boolean\\l}}"];
  usr  [label="{{User|+ id: String\\l+ email: String\\l+ userType: UserType\\l}}"];
  eng  [label="{{\\<\\<object\\>\\> FareEngine|+ quote(config, stop, type, n): FareQuote\\l+ minimumFor(config, type): Double\\l}}", fillcolor="{AMBER}"];

  screen -> pvm; screen -> dvm; screen -> avm;
  pvm -> vm [arrowhead=onormal]; dvm -> vm [arrowhead=onormal]; avm -> vm [arrowhead=onormal];
  pvm -> repo; dvm -> repo; avm -> repo;
  repo -> svc;
  svc -> ride [arrowhead=diamond, dir=back]; svc -> stop [arrowhead=diamond, dir=back]; svc -> usr [arrowhead=diamond, dir=back];
  pvm -> eng [style=dashed];
  eng -> stop [style=dashed];
  ride -> stop [label=" priced by ", style=dashed];
}}
''')

# ------------------------------------------------------------------ Firebase schema tree
render('fig11_database_schema', f'''
digraph G {{
  rankdir=LR; {HEAD}
  node [shape=box, style="filled", fillcolor="{PALE}", fontsize=9];
  root [label="trikride-db", fillcolor="{GREEN}", fontcolor=white, shape=box, style="rounded,filled"];

  users [label="users", fillcolor="{LIGHT}"];
  drivers [label="drivers", fillcolor="{LIGHT}"];
  requests [label="rideRequests", fillcolor="{LIGHT}"];
  rides [label="rides", fillcolor="{LIGHT}"];
  config [label="config", fillcolor="{LIGHT}"];
  compl [label="complaints", fillcolor="{LIGHT}"];
  notif [label="notifications", fillcolor="{LIGHT}"];
  photo [label="profilePhotos", fillcolor="{LIGHT}"];

  uk [label="{{uid}}\\l  email, phoneNumber\\l  firstName, lastName\\l  birthDate, userType\\l  profileImageUrl\\l  createdAt, updatedAt\\l", shape=box];
  dk [label="{{uid}}\\l  licenseNumber, licenseExpiry\\l  tricycleNumber\\l  verificationStatus\\l  isAvailable, rating\\l  totalRides, documents[]\\l"];
  rq [label="{{requestId}}\\l  passengerId\\l  pickupLocation, dropoffLocation\\l  passengerCount, luggage\\l  estimatedFare, fareStopId\\l  fareType, requestedAt, expiresAt\\l"];
  rd [label="{{rideId}}\\l  passengerId, driverId\\l  pickupLocation, dropoffLocation\\l  status, estimatedFare, actualFare\\l  fareStopId, fareType\\l  requestedAt … completedAt\\l"];
  cf [label="fare\\l  minimumRegular, minimumDiscounted\\l  poblacionFlat, terminalRoundTrip\\l  chargePerPassenger, source\\l"];
  cs [label="fareStops/{{stopId}}\\l  zone, name\\l  regularFare, discountedFare\\l  active, needsReview\\l  confidence, note\\l"];
  ck [label="{{complaintId}}\\l  reporterId, reporterName\\l  reporterType, category\\l  description, status\\l  adminNote, createdAt, resolvedAt\\l"];
  nk [label="{{uid}}/{{notificationId}}\\l  title, message, type\\l  read, createdAt\\l"];

  root -> users -> uk;
  root -> drivers -> dk;
  root -> requests -> rq;
  root -> rides -> rd;
  root -> config; config -> cf; config -> cs;
  root -> compl -> ck;
  root -> notif -> nk;
  root -> photo -> pk;
}}
''')

# ------------------------------------------------------------------ Waterfall
render('fig12_waterfall', f'''
digraph G {{
  rankdir=TB; {HEAD}
  node [shape=box, style="filled", fillcolor="{LIGHT}", width=2.9, height=0.55];
  splines=ortho;
  r [label="Requirements Analysis"];
  d [label="System Design"];
  i [label="Implementation"];
  t [label="Testing"];
  p [label="Deployment"];
  m [label="Maintenance", fillcolor="{GREEN}", fontcolor=white];
  r->d->i->t->p->m;
  d->r [style=dashed, label=" review ", constraint=false];
  i->d [style=dashed, label=" review ", constraint=false];
  t->i [style=dashed, label=" defects ", constraint=false];
  p->t [style=dashed, label=" issues ", constraint=false];
}}
''')

# ------------------------------------------------------------------ Screen / navigation flow
render('fig13_screen_flow', f'''
digraph G {{
  rankdir=TB; {HEAD}
  node [shape=box, style="rounded,filled", fillcolor="{PALE}", fontsize=9, width=1.9];
  launch [label="App launch", fillcolor="{GREEN}", fontcolor=white];
  d0 [shape=diamond, label="Session\\nexists?", fillcolor="{AMBER}", style=filled, width=1.4, height=0.9, fixedsize=true, fontsize=8];
  d1 [shape=diamond, label="First\\nlaunch?", fillcolor="{AMBER}", style=filled, width=1.4, height=0.9, fixedsize=true, fontsize=8];
  wb [label="Welcome-back screen"];
  onb [label="Onboarding carousel\\n(5 slides)"];
  splash [label="Splash"];
  login [label="Login\\n(remember me)"];
  reg [label="Register\\n(password rules,\\nbirthdate picker,\\nterms consent)"];
  pick [label="Account type\\nselection"];
  d2 [shape=diamond, label="Role", fillcolor="{AMBER}", style=filled, width=1.2, height=0.8, fixedsize=true, fontsize=8];
  pd [label="Passenger:\\nHome · History ·\\nSupport · Profile", fillcolor="{LIGHT}"];
  dd [label="Driver:\\nDashboard · Requests ·\\nHistory · Profile", fillcolor="{LIGHT}"];
  ad [label="Admin:\\nVerify · Concerns ·\\nMonitor · Fares · Profile", fillcolor="{LIGHT}"];

  launch -> d0;
  d0 -> wb [label=" yes "];
  d0 -> d1 [label=" no "];
  d1 -> onb [label=" yes "];
  d1 -> splash [label=" no "];
  onb -> login; splash -> login;
  login -> reg [dir=both, label=" switch "];
  wb -> d2;
  login -> d2; reg -> d2;
  d2 -> pick [label=" unknown ", style=dashed];
  pick -> d2;
  d2 -> pd [label=" passenger "];
  d2 -> dd [label=" driver "];
  d2 -> ad [label=" admin "];
}}
''')
