declare zmienna number(10);
begin 
zmienna:=1;
loop
insert into Kupon (kupon_id,uzytkownik_id,kurs,POSTAWIONE_SRODKI,STATUS,WYGRANA) 
VALUES (zmienna,dbms_random.value(1,2000),dbms_random.value(1,3),dbms_random.value(1,5000),dbms_random.value(0,1),dbms_random.value(1,10000));
 zmienna:=zmienna+1;
 exit when zmienna=3001;
 end loop;
 end;