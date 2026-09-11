import openpyxl,re
ITEM=re.compile(r'^[A-F]\d+$')
X="/root/.claude/uploads/45f611ed-8eb7-593b-80cb-7e0fbd47b411/791b7c02-TrikRide_Survey_Tally.xlsx"
wb=openpyxl.load_workbook(X,data_only=True)
def esc(s): return str(s).replace("|","\\|")
def num(x):
    if x is None: return ""
    if isinstance(x,float): return str(int(x)) if x==int(x) else f"{x:.2f}"
    return str(x)
L=["# TrikRide — Survey Tally","",
   "Transcribed returns and the tally computed from them. Every table below is a Word table: click inside it, press Ctrl+A twice to select the table, then copy.","",
   "[[PB]]"]

SEC={"Passenger Responses":[("A","Functional Suitability",8),("B","Usability",6),("C","Efficiency",5),
                            ("D","Reliability",5),("E","Security",5),("F","Overall Satisfaction",5)],
     "Driver Responses":[("A","Usability",5),("B","Functionality",5),("C","Efficiency",5),("D","Reliability",5)],
     "Admin Responses":[("A","Functional Suitability",3),("B","Usability",2),("C","Reliability",2)]}
PROF={"Passenger Responses":("Student passengers — respondent profile",[2,3,4,5,6,7],
        ["Age","Gender","Course","Year level","Frequency of tricycle use","Average rides per week"]),
      "Driver Responses":("Tricycle drivers — respondent profile",[2,3,4,5,6,7],
        ["Age","Gender","Years driving","Owns smartphone","Internet access","Average trips per day"]),
      "Admin Responses":("System administrator — respondent profile",[2,3],["Age","Gender"])}
TITLE={"Passenger Responses":"Student Passengers","Driver Responses":"Tricycle Drivers","Admin Responses":"System Administrator"}

for sheet in SEC:
    ws=wb[sheet]
    codes=list(next(ws.iter_rows(min_row=4,max_row=4,values_only=True)))
    rows=[r for r in ws.iter_rows(min_row=6,max_row=ws.max_row,values_only=True) if r[0] is not None]
    L+= [f"## {TITLE[sheet]} — Raw Responses (N = {len(rows)})",""]
    t,idx,hdr=PROF[sheet]
    L+= [f"**{t}**","","| No. | "+" | ".join(hdr)+" |","|:---|"+"---:|"+"|".join([":---"]*(len(hdr)-1))+"|"]
    for r in rows: L.append("| "+str(r[0])+" | "+" | ".join(esc(num(r[i])) for i in idx)+" |")
    L.append("")
    for letter,name,n in SEC[sheet]:
        cols=[i for i in range(len(codes)) if codes[i] and ITEM.match(str(codes[i])) and str(codes[i]).startswith(letter)]
        L+= [f"**{letter}. {name} — item responses**","",
             "| No. | "+" | ".join(str(codes[i]) for i in cols)+" |",
             "|:---|"+"|".join(["---:"]*len(cols))+"|"]
        for r in rows: L.append("| "+str(r[0])+" | "+" | ".join(num(r[i]) for i in cols)+" |")
        L.append("")
    L.append("[[PB]]")

for sheet,title in [("Tally - Passenger","Student Passengers"),("Tally - Driver","Tricycle Drivers"),("Tally - Admin","System Administrator")]:
    ws=wb[sheet]
    L+= [f"## Tally — {title}","",
         "| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation |",
         "|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|"]
    for r in ws.iter_rows(min_row=5,max_row=ws.max_row,values_only=True):
        v=[("" if x is None else x) for x in r[:10]]
        if all(x=="" for x in v): continue
        if v[1]=="" and v[0]!="":                       # section heading row
            L.append(f"| | **{esc(v[0])}** | | | | | | | | |"); continue
        if str(v[1]).startswith("Category mean"):
            L.append(f"| | **{esc(v[1])}** | | | | | | | **{num(v[8])}** | **{esc(v[9])}** |"); continue
        if str(v[1]).startswith("Overall"):
            L.append(f"| | **{esc(v[1])}** | | | | | | | **{num(v[8])}** | **{esc(v[9])}** |"); continue
        L.append("| "+num(v[0])+" | "+esc(v[1])+" | "+" | ".join(num(v[i]) for i in range(2,9))+" | "+esc(v[9])+" |")
    L+= ["","[[PB]]"]
open("tally_paste.md","w").write("\n".join(L)+"\n")
print(len(L),"lines")
