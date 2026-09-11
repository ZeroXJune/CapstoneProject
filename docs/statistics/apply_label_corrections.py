import zipfile,re,shutil,os
U="as-supplied/"
OUT="./"
os.makedirs(OUT,exist_ok=True)
JOBS=[("WEIGTHEDMEAN_Passenger.xlsx","WEIGTHEDMEAN_Passenger_corrected.xlsx",
       [("W25","STRONGLY AGREE"),("W26","STRONGLY AGREE"),("W28","STRONGLY AGREE")]),
      ("WEIGTHEDMEAN_Driver.xlsx","WEIGTHEDMEAN_Driver_corrected.xlsx",
       [("G22","AGREE"),("W26","AGREE")]),
      ("WEIGTHEDMEAN_Admin.xlsx","WEIGTHEDMEAN_Admin_corrected.xlsx",
       [("G14","AGREE")])]
for src,dst,fixes in JOBS:
    zin=zipfile.ZipFile(U+src)
    ss=zin.read("xl/sharedStrings.xml").decode("utf-8")
    idx={}
    for i,s in enumerate(re.findall(r'<si>(.*?)</si>',ss,re.S)):
        t="".join(re.findall(r'<t[^>]*>(.*?)</t>',s,re.S)).strip().upper()
        if t in ("AGREE","STRONGLY AGREE") and t not in idx: idx[t]=i
    sheet=zin.read("xl/worksheets/sheet1.xml").decode("utf-8")
    for cell,want in fixes:
        m=re.search(r'(<c r="%s"[^>]*t="s"[^>]*>)<v>(\d+)</v>(</c>)'%cell,sheet)
        assert m, f"{src} {cell} not found or not a shared string"
        old=int(m.group(2)); new=idx[want]
        assert old!=new, f"{cell} already {want}"
        sheet=sheet[:m.start()]+f'{m.group(1)}<v>{new}</v>{m.group(3)}'+sheet[m.end():]
        print(f"  {dst} {cell}: sharedString {old} -> {new} ({want})")
    zout=zipfile.ZipFile(OUT+dst,"w",zipfile.ZIP_DEFLATED)
    for n in zin.namelist():
        zout.writestr(n, sheet.encode("utf-8") if n=="xl/worksheets/sheet1.xml" else zin.read(n))
    zout.close(); zin.close()
