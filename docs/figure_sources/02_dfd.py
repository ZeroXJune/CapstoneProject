import subprocess, os
OUT='/home/user/CapstoneProject/docs/figures'
GREEN='#16A34A'; DEEP='#166534'; LIGHT='#DCFCE7'; PALE='#F0FDF4'; GREY='#475569'; AMBER='#FEF3C7'
HEAD=f'''
  graph [fontname="Helvetica", fontsize=11, bgcolor="white", pad=0.3];
  node  [fontname="Helvetica", fontsize=10, color="{DEEP}", fontcolor="#0F172A"];
  edge  [fontname="Helvetica", fontsize=9, color="{GREY}", fontcolor="{GREY}"];
'''
def render(name, src, engine='dot'):
    path=os.path.join(OUT,name+'.png')
    p=subprocess.run([engine,'-Tpng','-Gdpi=170','-o',path],input=src.encode(),capture_output=True)
    print(('ok   ' if p.returncode==0 else 'FAIL '),name, p.stderr.decode()[:300])

# DFD level 1 — laid out as three ranks so the flows read left to right.
render('fig05_dfd_level1', f'''
digraph G {{
  rankdir=LR; {HEAD}
  nodesep=0.45; ranksep=1.5; splines=spline;

  node [shape=box, style="filled", fillcolor="{LIGHT}", fontcolor="#0F172A", width=1.5];
  pas [label="Passenger"]; drv [label="Driver"]; adm [label="Administrator"];

  node [shape=circle, style=filled, fillcolor="{GREEN}", fontcolor="white",
        width=1.3, fixedsize=true, fontsize=9];
  p1 [label="1.0\\nManage\\nAccounts"];
  p2 [label="2.0\\nOnboard\\nDrivers"];
  p3 [label="3.0\\nPrice and\\nBook Ride"];
  p4 [label="4.0\\nMatch and\\nTrack Ride"];
  p5 [label="5.0\\nHandle\\nConcerns"];
  p6 [label="6.0\\nMonitor and\\nReport"];

  node [shape=box, style="filled", fillcolor="{AMBER}", fontcolor="#0F172A",
        width=2.0, height=0.38, fixedsize=true, fontsize=9];
  d1 [label="D1  users"];
  d2 [label="D2  drivers"];
  d3 [label="D3  fareStops"];
  d4 [label="D4  rideRequests"];
  d5 [label="D5  rides"];
  d6 [label="D6  complaints"];
  d7 [label="D7  notifications"];

  {{rank=same; pas; drv; adm;}}
  {{rank=same; p1; p2; p3; p4; p5; p6;}}
  {{rank=same; d1; d2; d3; d4; d5; d6; d7;}}

  pas -> p1 [label="sign-up"];
  drv -> p1;
  p1 -> d1 [dir=both];

  drv -> p2 [label="credentials"];
  adm -> p2 [label="decision"];
  p2 -> d2 [dir=both];
  p2 -> d7;

  pas -> p3 [label="request"];
  p3 -> d3 [dir=back, label="posted rate"];
  p3 -> d4;
  adm -> p3 [label="fare edits", style=dashed];
  p3 -> d3 [style=dashed];

  drv -> p4 [label="accept, status"];
  p4 -> d4 [dir=back];
  p4 -> d5 [dir=both];
  p4 -> d7;

  pas -> p5 [label="concern"];
  drv -> p5;
  adm -> p5 [label="resolution"];
  p5 -> d6 [dir=both];
  p5 -> d7;

  adm -> p6 [dir=back, label="reports"];
  p6 -> d5 [dir=back];
  p6 -> d2 [dir=back];
  p6 -> d6 [dir=back];
}}
''')
