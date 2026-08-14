import subprocess, os, textwrap

OUT = '/home/user/CapstoneProject/docs/figures'
os.makedirs(OUT, exist_ok=True)

GREEN   = '#16A34A'
DEEP    = '#166534'
LIGHT   = '#DCFCE7'
PALE    = '#F0FDF4'
GREY    = '#475569'
PAPER   = '#FFFFFF'
AMBER   = '#FEF3C7'
BLUE    = '#DBEAFE'

HEAD = f'''
  graph [fontname="Helvetica", fontsize=11, bgcolor="{PAPER}", pad=0.3];
  node  [fontname="Helvetica", fontsize=10, color="{DEEP}", fontcolor="#0F172A"];
  edge  [fontname="Helvetica", fontsize=9, color="{GREY}", fontcolor="{GREY}"];
'''

def render(name, src, engine='dot'):
    path = os.path.join(OUT, name + '.png')
    p = subprocess.run([engine, '-Tpng', '-Gdpi=170', '-o', path],
                       input=src.encode(), capture_output=True)
    if p.returncode != 0:
        print('FAIL', name, p.stderr.decode()[:400])
    else:
        print('ok  ', name, os.path.getsize(path)//1024, 'KB')

# ---------------------------------------------------------------- Fig 1 research flow

# ---------------------------------------------------------------- Fig 2 conceptual framework IPO
render('fig02_conceptual_framework', f'''
digraph G {{
  rankdir=LR; {HEAD}
  node [shape=none, margin=0];
  input [label=<
    <TABLE BORDER="1" CELLBORDER="0" CELLSPACING="0" CELLPADDING="7" COLOR="{DEEP}" BGCOLOR="{PALE}">
      <TR><TD BGCOLOR="{DEEP}"><FONT COLOR="white"><B>INPUT</B></FONT></TD></TR>
      <TR><TD ALIGN="LEFT">Passenger ride requests<BR ALIGN="LEFT"/>(pickup, destination, seats)</TD></TR>
      <TR><TD ALIGN="LEFT">Driver availability status</TD></TR>
      <TR><TD ALIGN="LEFT">Driver credentials for<BR ALIGN="LEFT"/>onboarding</TD></TR>
      <TR><TD ALIGN="LEFT">FeTODAT published fare table</TD></TR>
      <TR><TD ALIGN="LEFT">Account credentials and<BR ALIGN="LEFT"/>assigned role</TD></TR>
    </TABLE>>];
  process [label=<
    <TABLE BORDER="1" CELLBORDER="0" CELLSPACING="0" CELLPADDING="7" COLOR="{DEEP}" BGCOLOR="{PALE}">
      <TR><TD BGCOLOR="{DEEP}"><FONT COLOR="white"><B>PROCESS</B></FONT></TD></TR>
      <TR><TD ALIGN="LEFT">Authenticate and route by role</TD></TR>
      <TR><TD ALIGN="LEFT">Verify driver documents</TD></TR>
      <TR><TD ALIGN="LEFT">Price the ride from the<BR ALIGN="LEFT"/>posted rate table</TD></TR>
      <TR><TD ALIGN="LEFT">Broadcast request to<BR ALIGN="LEFT"/>available drivers</TD></TR>
      <TR><TD ALIGN="LEFT">Match on first acceptance</TD></TR>
      <TR><TD ALIGN="LEFT">Track ride status changes</TD></TR>
      <TR><TD ALIGN="LEFT">Record concerns and<BR ALIGN="LEFT"/>notify both parties</TD></TR>
    </TABLE>>];
  output [label=<
    <TABLE BORDER="1" CELLBORDER="0" CELLSPACING="0" CELLPADDING="7" COLOR="{DEEP}" BGCOLOR="{PALE}">
      <TR><TD BGCOLOR="{DEEP}"><FONT COLOR="white"><B>OUTPUT</B></FONT></TD></TR>
      <TR><TD ALIGN="LEFT">Confirmed booking with an<BR ALIGN="LEFT"/>assigned driver and fare</TD></TR>
      <TR><TD ALIGN="LEFT">Verified driver profile</TD></TR>
      <TR><TD ALIGN="LEFT">Live ride status for<BR ALIGN="LEFT"/>both parties</TD></TR>
      <TR><TD ALIGN="LEFT">Ride and concern records</TD></TR>
      <TR><TD ALIGN="LEFT">Monthly and yearly<BR ALIGN="LEFT"/>activity reports</TD></TR>
    </TABLE>>];
  input -> process -> output;
  output -> process [label="  feedback: evaluation\\n  and fare corrections  ",
                     style=dashed, constraint=false];
}}
''')

# ---------------------------------------------------------------- Fig 3 system architecture
render('fig03_system_architecture', f'''
digraph G {{
  rankdir=TB; {HEAD}
  compound=true;
  node [shape=box, style="rounded,filled", fontsize=10];

  subgraph cluster_client {{
    label="Presentation layer — Android application"; style="rounded"; color="{DEEP}"; fontcolor="{DEEP}"; bgcolor="{PALE}";
    p [label="Passenger UI\\n(book, track, history,\\nsupport, profile)", fillcolor="{LIGHT}"];
    d [label="Driver UI\\n(online toggle, requests,\\nlifecycle, earnings)", fillcolor="{LIGHT}"];
    a [label="Admin UI\\n(verify, concerns, monitor,\\nfares, reports)", fillcolor="{LIGHT}"];
  }}

  subgraph cluster_logic {{
    label="Application layer — ViewModels and domain logic"; style="rounded"; color="{DEEP}"; fontcolor="{DEEP}"; bgcolor="{PALE}";
    vm [label="AuthViewModel · PassengerViewModel · DriverViewModel\\nAdminViewModel · ProfileViewModel · SupportViewModel", fillcolor="{LIGHT}"];
    eng [label="FareEngine\\nReportBuilder\\nPasswordRules", fillcolor="{AMBER}"];
  }}

  subgraph cluster_data {{
    label="Data layer — repositories"; style="rounded"; color="{DEEP}"; fontcolor="{DEEP}"; bgcolor="{PALE}";
    repo [label="AuthRepository · RideRepository · DriverRepository\\nAdminRepository · FareRepository · SupportRepository", fillcolor="{LIGHT}"];
    svc  [label="FirebaseService\\n(single point of database access)", fillcolor="{LIGHT}"];
  }}

  subgraph cluster_cloud {{
    label="Backend as a service — Google Firebase (free tier)"; style="rounded"; color="{DEEP}"; fontcolor="{DEEP}"; bgcolor="{BLUE}";
    auth [label="Authentication\\nemail and password", fillcolor="white"];
    rtdb [label="Realtime Database\\nusers · drivers · rides · requests\\nconfig · complaints · profilePhotos", fillcolor="white"];
    fcm  [label="Cloud Messaging\\npush notifications", fillcolor="white"];
  }}

  p -> vm [ltail=cluster_client, lhead=cluster_logic, label="  state and events  "];
  vm -> eng [style=dashed, label=" pricing, reports "];
  vm -> repo [lhead=cluster_data, label="  suspend calls and Flows  "];
  repo -> svc;
  svc -> auth [lhead=cluster_cloud, label="  HTTPS / TLS 1.2+\\n  persistent WebSocket  "];
}}
''')

# ---------------------------------------------------------------- Fig 4 context diagram
render('fig04_context_diagram', f'''
digraph G {{
  rankdir=LR; {HEAD}
  node [shape=box, style="filled", fillcolor="{LIGHT}", height=0.55];
  sys [label="0\\n\\nSmart Tricycle Ride and\\nDriver Onboarding System\\n(TrikRide)",
       shape=circle, style=filled, fillcolor="{GREEN}", fontcolor=white, width=2.3, fixedsize=true];
  pas [label="Passenger\\n(student, faculty, staff)"];
  drv [label="Tricycle Driver"];
  adm [label="System Administrator"];
  fed [label="FeTODAT\\n(fare authority)", fillcolor="{AMBER}"];

  pas -> sys [label="  registration, ride request,\\n  rating, concern  "];
  sys -> pas [label="  fare quotation, driver\\n  assignment, status, receipt  "];
  drv -> sys [label="  credentials, availability,\\n  acceptance, status update  "];
  sys -> drv [label="  verification result, ride\\n  request, passenger details  "];
  adm -> sys [label="  verification decision, fare\\n  corrections, concern resolution  "];
  sys -> adm [label="  pending drivers, activity\\n  monitor, exported reports  "];
  fed -> sys [label="  published fare table  "];
  {{rank=same; pas; drv;}}
}}
''')

# ---------------------------------------------------------------- Fig 5 DFD level 1

# ---------------------------------------------------------------- Fig 6 ERD
render('fig06_erd', f'''
digraph G {{
  rankdir=LR; {HEAD}
  node [shape=none, margin=0];
  edge [arrowhead=none, fontsize=9];

  user [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>USER</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">id</TD><TD ALIGN="LEFT">PK, string</TD></TR>
    <TR><TD ALIGN="LEFT">email</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">phoneNumber</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">firstName</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">lastName</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">birthDate</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">userType</TD><TD ALIGN="LEFT">enum</TD></TR>
    <TR><TD ALIGN="LEFT">profileImageUrl</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">createdAt</TD><TD ALIGN="LEFT">string</TD></TR>
   </TABLE>>];

  driver [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>DRIVER</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">userId</TD><TD ALIGN="LEFT">PK, FK</TD></TR>
    <TR><TD ALIGN="LEFT">licenseNumber</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">licenseExpiry</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">tricycleNumber</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">verificationStatus</TD><TD ALIGN="LEFT">enum</TD></TR>
    <TR><TD ALIGN="LEFT">isAvailable</TD><TD ALIGN="LEFT">boolean</TD></TR>
    <TR><TD ALIGN="LEFT">rating</TD><TD ALIGN="LEFT">double</TD></TR>
    <TR><TD ALIGN="LEFT">totalRides</TD><TD ALIGN="LEFT">int</TD></TR>
   </TABLE>>];

  ride [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>RIDE</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">id</TD><TD ALIGN="LEFT">PK, string</TD></TR>
    <TR><TD ALIGN="LEFT">passengerId</TD><TD ALIGN="LEFT">FK</TD></TR>
    <TR><TD ALIGN="LEFT">driverId</TD><TD ALIGN="LEFT">FK</TD></TR>
    <TR><TD ALIGN="LEFT">fareStopId</TD><TD ALIGN="LEFT">FK</TD></TR>
    <TR><TD ALIGN="LEFT">fareType</TD><TD ALIGN="LEFT">enum</TD></TR>
    <TR><TD ALIGN="LEFT">status</TD><TD ALIGN="LEFT">enum</TD></TR>
    <TR><TD ALIGN="LEFT">passengerCount</TD><TD ALIGN="LEFT">int</TD></TR>
    <TR><TD ALIGN="LEFT">estimatedFare</TD><TD ALIGN="LEFT">double</TD></TR>
    <TR><TD ALIGN="LEFT">requestedAt</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">completedAt</TD><TD ALIGN="LEFT">string</TD></TR>
   </TABLE>>];

  fare [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>FARE_STOP</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">id</TD><TD ALIGN="LEFT">PK, string</TD></TR>
    <TR><TD ALIGN="LEFT">zone</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">name</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">regularFare</TD><TD ALIGN="LEFT">double</TD></TR>
    <TR><TD ALIGN="LEFT">discountedFare</TD><TD ALIGN="LEFT">double</TD></TR>
    <TR><TD ALIGN="LEFT">active</TD><TD ALIGN="LEFT">boolean</TD></TR>
    <TR><TD ALIGN="LEFT">needsReview</TD><TD ALIGN="LEFT">boolean</TD></TR>
   </TABLE>>];

  compl [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>COMPLAINT</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">id</TD><TD ALIGN="LEFT">PK, string</TD></TR>
    <TR><TD ALIGN="LEFT">reporterId</TD><TD ALIGN="LEFT">FK</TD></TR>
    <TR><TD ALIGN="LEFT">category</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">description</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">status</TD><TD ALIGN="LEFT">enum</TD></TR>
    <TR><TD ALIGN="LEFT">adminNote</TD><TD ALIGN="LEFT">string</TD></TR>
   </TABLE>>];

  notif [label=<
   <TABLE BORDER="1" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4" COLOR="{DEEP}">
    <TR><TD BGCOLOR="{DEEP}" COLSPAN="2"><FONT COLOR="white"><B>NOTIFICATION</B></FONT></TD></TR>
    <TR><TD ALIGN="LEFT">id</TD><TD ALIGN="LEFT">PK, string</TD></TR>
    <TR><TD ALIGN="LEFT">userId</TD><TD ALIGN="LEFT">FK</TD></TR>
    <TR><TD ALIGN="LEFT">title</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">message</TD><TD ALIGN="LEFT">string</TD></TR>
    <TR><TD ALIGN="LEFT">read</TD><TD ALIGN="LEFT">boolean</TD></TR>
   </TABLE>>];

  user -> driver [taillabel="1", headlabel="0..1", labeldistance=2.2];
  user -> ride   [taillabel="1", headlabel="0..*", labeldistance=2.2, label="books"];
  driver -> ride [taillabel="1", headlabel="0..*", labeldistance=2.2, label="serves"];
  fare -> ride   [taillabel="1", headlabel="0..*", labeldistance=2.2, label="prices"];
  user -> compl  [taillabel="1", headlabel="0..*", labeldistance=2.2, label="files"];
  user -> notif  [taillabel="1", headlabel="0..*", labeldistance=2.2, label="receives"];
}}
''')
