select uzytkownik_id, imie, nazwisko from UZYTKOWNIK where uzytkownik_id=(select uzytkownik_id from Konto where Login ='DnhbTvCwAEYbVLHRHGth' and haslo='lgPpYfLYHQIqAxsKrduV');

select Srodki from konto where UZYTKOWNIK_ID=2;

select zaklad_id, status from zaklad where kupon_id =ANY(select kupon_id from Kupon where uzytkownik_id=2);

select wydarzenie_id from wydarzenie where wydarzenie_id =ANY(select wydarzenie_id from zaklad where kupon_id=3);

select uzytkownik.imie, uzytkownik.nazwisko, kupon_Id,kurs,postawione_srodki,wygrana as "mozliwa wygrana", konto.srodki as "pozostale srodki na koncie" from kupon join konto on kupon.UZYTKOWNIK_ID = konto.UZYTKOWNIK_ID join uzytkownik on kupon.UZYTKOWNIK_ID = uzytkownik.UZYTKOWNIK_ID where kupon.kupon_ID= 4 ;


select status from kupon where kupon_id =3;