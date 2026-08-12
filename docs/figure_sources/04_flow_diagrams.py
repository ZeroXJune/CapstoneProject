import subprocess, os
OUT='/home/user/CapstoneProject/docs/figures'
GREEN='#16A34A'; DEEP='#166534'; LIGHT='#DCFCE7'; GREY='#475569'; AMBER='#FEF3C7'
HEAD=f'''
  graph [fontname="Helvetica", fontsize=11, bgcolor="white", pad=0.25];
  node  [fontname="Helvetica", fontsize=10, color="{DEEP}", fontcolor="#0F172A"];
  edge  [fontname="Helvetica", fontsize=9, color="{GREY}", fontcolor="{GREY}"];
'''
def render(name, src):
    path=os.path.join(OUT,name+'.png')
    p=subprocess.run(['dot','-Tpng','-Gdpi=170','-o',path],input=src.encode(),capture_output=True)
    print(('ok   ' if p.returncode==0 else 'FAIL '),name, p.stderr.decode()[:200])

# Research flow, folded into two columns so it fits a page at a readable size.
render('fig01_research_flow', f'''
digraph G {{
  rankdir=TB; {HEAD}
  ranksep=0.42; nodesep=0.5;
  node [shape=box, style="rounded,filled", fillcolor="{LIGHT}", width=2.7, height=0.6, fixedsize=true, fontsize=9.5];

  a [label="Problem identification and\\nneeds assessment"];
  b [label="Review of related literature\\nand systems"];
  c [label="System planning and design"];
  d [label="System development"];
  e [label="System integration"];
  f [label="Testing"];
  g [label="Deployment to respondents"];
  h [label="Evaluation using ISO/IEC 25010"];
  i [label="Data collection"];
  j [label="Data analysis\\n(weighted mean, t-test)"];
  k [label="Findings, conclusions and\\nrecommendations", fillcolor="{GREEN}", fontcolor=white];

  a->b->c->d->e->f;
  f->d [label="  defects  ", style=dashed, constraint=false];
  g->h->i->j->k;

  {{rank=same; a; g;}}
  {{rank=same; b; h;}}
  {{rank=same; c; i;}}
  {{rank=same; d; j;}}
  {{rank=same; e; k;}}

  f -> g [label="  continue  ", color="{DEEP}", fontcolor="{DEEP}", constraint=false];
}}
''')

# Activity diagram, folded into two columns for the same reason.
render('fig08_activity_booking', f'''
digraph G {{
  rankdir=TB; {HEAD}
  ranksep=0.34; nodesep=0.55;
  node [shape=box, style="rounded,filled", fillcolor="{LIGHT}", width=2.4, height=0.52, fixedsize=true, fontsize=9];

  start [shape=circle, label="", width=0.26, height=0.26, style=filled, fillcolor="{DEEP}"];
  a1 [label="Open the app"];
  d1 [shape=diamond, label="Signed in?", style=filled, fillcolor="{AMBER}", width=1.5, height=0.85, fontsize=8.5];
  login [label="Sign in or register"];
  a2 [label="Tap Book Ride"];
  a3 [label="Select pickup point"];
  a4 [label="Search and select\\ndestination stop"];
  a5 [label="Choose rate column"];
  a6 [label="Set passengers and luggage"];
  a7 [label="System quotes the fare\\nfrom the posted table"];
  d2 [shape=diamond, label="Fare\\nacceptable?", style=filled, fillcolor="{AMBER}", width=1.7, height=0.95, fontsize=8.5];

  a8 [label="Submit the request"];
  a9 [label="Broadcast to\\navailable drivers"];
  d3 [shape=diamond, label="Driver\\nfound?", style=filled, fillcolor="{AMBER}", width=1.5, height=0.9, fontsize=8.5];
  a14 [label="Request expires\\nafter 5 minutes"];
  a10 [label="Driver accepts"];
  a11 [label="Both parties notified;\\nride created"];
  a12 [label="Driver advances status\\nto completion"];
  a13 [label="Passenger rates the ride"];
  stop [shape=doublecircle, label="", width=0.24, height=0.24, style=filled, fillcolor="{DEEP}"];

  start->a1->d1;
  d1->login [label="  no"];
  login->a2;
  d1->a2 [label="  yes"];
  a2->a3->a4->a5->a6->a7->d2;
  d2->a4 [label="  no", constraint=false];
  d2->a8 [label="  yes", color="{DEEP}", fontcolor="{DEEP}"];
  a8->a9->d3;
  d3->a14 [label="  no"];
  d3->a10 [label="  yes"];
  a10->a11->a12->a13->stop;
  a14->stop;

  {{rank=same; start; a8;}}
  {{rank=same; a1; a9;}}
  {{rank=same; d1; d3;}}
  {{rank=same; login; a14; a10;}}
  {{rank=same; a2; a11;}}
  {{rank=same; a3; a12;}}
  {{rank=same; a4; a13;}}
  {{rank=same; a5; stop;}}
}}
''')
