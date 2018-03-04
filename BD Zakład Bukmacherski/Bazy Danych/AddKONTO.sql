declare zmienna number(10);
begin 
zmienna:=1;
loop
insert into Konto (Uzytkownik_id,Uprawnienia_id,login,haslo,srodki) VALUES (zmienna,dbms_random.value(1,31),dbms_random.string('A', 20),dbms_random.string('A', 20),dbms_random.value(1,10000));
 zmienna:=zmienna+1;
 exit when zmienna=2001;
 end loop;
 end;