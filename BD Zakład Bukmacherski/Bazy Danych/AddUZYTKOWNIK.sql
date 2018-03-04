declare zmienna number(10);
begin 
zmienna:=1;
loop
insert into Uzytkownik (Uzytkownik_id,imie,nazwisko,email,nr_telefonu) VALUES (zmienna,dbms_random.string('A', 15),dbms_random.string('A', 15),dbms_random.string('A', 20),dbms_random.value(100000000,999999999));
 zmienna:=zmienna+1;
 exit when zmienna=2001;
 end loop;
 end;