 declare zmienna number(10);
begin 
zmienna:=1;
loop
insert into Zaklad (ZAKLAD_ID,KUPON_ID,WYDARZENIE_ID,DATA_ROZPOCZECIA,DATA_ZAKONCZENIA,STATUS) 
VALUES (zmienna,dbms_random.value(1,3000),dbms_random.value(1,10001),TO_DATE(TRUNC(DBMS_RANDOM.value(TO_CHAR(date '2000-01-01','J'),TO_CHAR(SYSDATE,'J'))),'J'),TO_DATE(TRUNC(DBMS_RANDOM.value(TO_CHAR(date '2000-01-01','J'),TO_CHAR(SYSDATE,'J'))),'J'),dbms_random.value(0,1));
 zmienna:=zmienna+1;
 exit when zmienna=15001;
 end loop;
 end;