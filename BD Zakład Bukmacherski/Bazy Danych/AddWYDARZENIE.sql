declare zmienna number(10);
begin 
zmienna:=1;
loop
insert into Wydarzenie (wydarzenie_id,dyscyplinanazwa,druzyna1,druzyna2,data_rozpoczecia,data_zakonczenia,kurs1,kurs2,kurs3,kto_wygral)
VALUES (zmienna,(SELECT nazwa FROM( SELECT nazwa FROM dyscyplina ORDER BY dbms_random.value )WHERE rownum = 1),dbms_random.string('A', 20),dbms_random.string('A', 20),TO_DATE(TRUNC(DBMS_RANDOM.value(TO_CHAR(date '2000-01-01','J'),TO_CHAR(SYSDATE,'J'))),'J'),TO_DATE(TRUNC(DBMS_RANDOM.value(TO_CHAR(date '2000-01-01','J'),TO_CHAR(SYSDATE,'J'))),'J'),dbms_random.value(1,3),dbms_random.value(1,3),dbms_random.value(1,3),dbms_random.value(1,3));
 zmienna:=zmienna+1;
 exit when zmienna=10001;
 end loop;
 end;