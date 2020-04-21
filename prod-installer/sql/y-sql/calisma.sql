select * from user where username like '%muslera%';
select * from user where birthday like '%-01-23';
select * from address where user in (3388,3405);
select * from orders where address_id = 1836;
select * from user where firstname like '%Güneş%' order by firstname,lastname collate utf8_general_ci;
select * from user where lastname like '%pınarbaşı%' order by lastname,firstname collate utf8_general_ci;
select * from user where email like '%femset%' order by email;
select * from user where id in (862);
select id,active,birthday,email,firstname,lastname,username from user where concat(firstname,' ',lastname) in (select concat(firstname,' ',lastname) from user group by firstname,lastname having count(concat(firstname,' ',lastname)) > 1) order by lastname,firstname;
select * from user where id in (select invited_by from user_invitation where id in (select user_invitation from user where id in (1024)));
select * from user where user_invitation in (select id from user_invitation where invited_by in (1634));
select * from orders where address_id in (select id from address where user in (3261));
select * from order_details where order_id in (select id from orders where address_id in (select id from address where user in (3261)));
select * from product where id in (select product_id from order_details where order_id in (select id from orders where address_id in (select id from address where user in (3261))));
select * from user_login where user in (3182) order by ip_address desc;
select * from user_invitation where email like '%merve_cek%';
select * from user_invitation order by id desc limit 100;
select distinct user from user_login where ip_address in (select ip_address from user_login where user in (3415));

select u.active,u.points_balance,u.id_verified,u.id,u.firstname,u.lastname,u.email,u.username,u.birthday,date_format(u.birthday,'%m-%d')as ay_gun,u.create_date, concat(gp.code,' ',u.gsm_number) as gsm_number,concat(a.name,' ',ad.street_name,' ',ad.building_number,' ',ad.apartment_number,' ',d.name,' ',d.postal_code,' ',c.name,' ',ci.name)
from user u, gsm_prefix gp, address ad, area a, district d, county c, city ci
where u.id in (367,394,395) and
u.gsm_prefix = gp.id and
u.id = ad.user and
ad.area = a.id and
a.district = d.id and
d.county = c.id and
c.city = ci.id
;

select * from user where id in (select user from address where id in (select address_id from orders where id in (select order_id from order_details where product_id = 18 and order_detail_type = 2)));

select id,user_id,create_date,product_order from point_transaction_accounting where product_order>=1200 and user_id in (select user from address where id in (select address_id from orders where id in (select order_id from order_details where product_id = 18 and order_detail_type = 2))) order by user_id,create_date;


select * from product;

select * from configuration;

select * from point_transaction_accounting where user_id in (5,8,2990);

select * from order_details where product_id = 28;

select count(*) from user where active = 0;

/* MUTLAKA KONTROL ET 3087'MI DAVET ETTIKLERINDEN YAKALA */

select * from user where active = 0 and activity_level > 1;

select * from product;


select * from survey_question;

select * from survey_option where id >= 1685 order by id;

select * from survey_answer where user_id = 3298 order by attendance_time desc limit 20;

select * from user_invitation where email like '%pencereci%';


select * from user where active and id_verified=0 and user_invitation in (select id from user_invitation where invited_by in (select id from user where user_invitation in (select id from user_invitation where invited_by in (872))))
and id not in (select id from user where active and id_verified = 0 and user_invitation in (select id from user_invitation where invited_by in (872)) and id not in (select user from address where area in (select id from area where district in (select id from district where county in (select id from county where city = 41)))))
and id not in (select id from user where active and id_verified = 0 and id in (select user from address where area in (select id from area where district in (select id from district where county in (select id from county where city = 41)))))
;

select * from survey_option where question_id in (select id from survey_question where survey_id = 72);


select * from product;

select * from rating where order_detail_id in (select id from order_details where product_id = 24);

select url,count(url) from social_point group by url;

select * from configuration;

select * from address where street_name like '%yüksekova%' collate utf8_general_ci;
select * from address where area like '53__';

select * from survey_answer where attendance_time > '2013-03-31';

select count(*) from order_details;

select * from district;

select * from survey_answer where user_id = 3450 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332));
select * from survey_answer_option where survey_answer_id in (2568767);
/* update survey_answer_option set survey_option_id = 1326 where survey_answer_id = 2449498; */
select * from survey_question where id = 292;
select * from survey_option where id = 1331;
select * from survey_option where question_id = 292;
select * from product;

select * from user where concat(firstname,lastname,email,username) like '%sevim%';

select * from address where
	area = 41698
	or concat(building_number,street_name) like '%avcılar%'
	or concat(building_number,street_name) like '%avcilar%'
;

select * from address where
street_name like '%gumushane%' collate utf8_general_ci
;


select * from user_aud where id = 3222;

select * from user_login where user=1271;

select * from district where id in (1426,2213);

select count(*) from user where gender = 0 and active=1 and id in (select user from user_login where login_time > '2013-02-25');

select * from user_invitation where email like '%candanoz%';

select * from user_invitation where invited_by = 2932;

select * from user_invitation where email in ('sakyelken@tezmanholding.com','suphiagirbas@hotmail.com');


select * from address where user in (1392,1770,3106);


select * from user where id in (1392,1770,3106);

select * from user_login where user = 1219;

select count(*) from user where id in (select distinct invited_by from user_invitation);


select count(*) from user where datediff(curdate(),birthday)/365.25 <= 18 and active;

select * from user where active and create_date > '2013-02-24' and points_balance < 111;

select count(*) from user where gender = 0 and active and datediff(curdate(),birthday)/365.25 > 30 and datediff(curdate(),birthday)/365.25 <= 50;

select count(*) from user where active and activity_level > 1 and gender = 0 and datediff(curdate(),birthday)/365.25 > 30;

select * from product;

select * from county where id in (457,467);

select * from user_invitation order by id desc limit 1000;

select * from user where id in (3126);

select * from address where concat(building_number,' ',street_name) like '%karao%' or area = 4309;

select * from survey_answer where user_id=3113;

select * from user where id in (c);

select * from address where id in (select id from address_aud where revtype = 1);

select * from survey_answer_option where survey_answer_id = 2094162;

select * from rating where user_id = 190;

select * from user where id in (1003,1366,1679,2132,3120);

select * from user order by id desc limit 100;

select * from user_invitation where email like '%kalpsi%';
select * from product;
select * from user_invitation where invited_by = 2166;
select * from user where user_invitation in (select id from user_invitation where invited_by = 3065);

select * from user_invitation where invited_by = 2 order by id desc limit 20;

select * from address where street_name like '%4207%';

select * from user_invitation where email = '%elifoz%';

select * from orders where placed_time > '2013-01-01' and id in (select order_id from order_details where product_id = 1);

select * from user where id in (2623,3090);

select * from user where active=0 and activity_level <> 1;

select * from user_aud where gsm_number = '6905568';

select * from address where id = 2557;
select * from user where id = 2548;


select count(*) from user where datediff(curdate(),birthday)/365.25 >=20 and gender = 0 and active and activity_level > 1;

select * from user_login where user in (572,570);

select * from address where area = 4381 or street_name like '%Becerikli%' or street_name like '%libya%';
select * from address where user in (400,401,432,3091,540);

select * from user where id in (select user from address where id in (select address_id from orders where received_time is null and sent_time is not null));

select count(*) from user where id_verified;

select * from product_comment_rating where user = 1025;

select * from order_details where product_id = 9 order by id desc limit 20;

select * from user where id in (888,1601);

select * from product;

select * from product_order_criteria;

select * from product_comment where id = 4347;

select * from user_invitation where invited_by = 2067;

select * from rating where user_id = 190;
select * from order_details where id = 190;
select * from orders where id = 190;
select * from product where id = 9;
select * from user where id = 190;

select * from user_login where user=190;

select * from survey_question where survey_id in (select id from survey where title like '%kontrol%');

select * from survey_option where question_id = 199;

select * from product;

select * from user where id in (select user from address where id in (select address_id from orders where id in (select order_id from order_details where product_id = 26)));


select * from point_transaction_accounting where product_order >= 1400 and create_date > '2013-02-25' and user_id in (select id from user where id in (select user from address where id in (select address_id from orders where id in (select order_id from order_details where product_id = 26))));


select * from user_invitation where create_time like '%00:01';

select * from user_invitation where invited_by = 2623;
select * from user_invitation where email like '%nurhan.eksi%';
select * from user_invitation where email like '%ismailbib%';

select * from point_transaction_accounting where user_id = 3065;

select * from social_point where user = 3003;

select * from user_invitation where invited_by = 400;

select * from user_aud where id in (2163,3077);

select * from address where user in (2163,3077);

select * from user_invitation where email like '%tamerce%';


select * from blog_comment where user in (1024,421);

select * from user where id in (2623);

select * from survey;

select count(*) from user where active and points_balance > 1000 and activity_level > 1 and gender = 0;

select * from user where id in (1184,1249);

select * from address where user in (1184,1249);

select * from address where street_name like '%yenioc%' or area like 25213;

select count(*) from survey_answer where user_id = 288;

select * from user where id = 1121;




select * from user_login where user = 2710;

select * from user where active and datediff(curdate(),birthday)/365.25 >= 0 and datediff(curdate(),birthday)/365.25 <= 19 order by points_balance desc;

select count(*) from survey_answer where attendance_time > '2013-02-26';
select count(distinct user) from user_login where login_time > '2013-02-26';


select * from user_invitation where create_time > '2013-02-25';

select * from product_comment where user = 1705 and num_of_dislikes >= num_of_likes;
select * from survey_question order by id desc limit 200;
select * from survey_option where id > 1550;


select count(*) from user where active and gender = 0 and points_balance > 800;

select * from rating where user_id = 2531;

select * from user where email like '%ziylan%';

select name,price_points,price_money from product;

select * from survey where type = 'PROFILE';

select * from address where user in (2548,3032);

select * from address where area in (14488,9283);

select * from user where id in (2376,2720,3032);


select * from user where datediff(curdate(),birthday)/365.25 < 18 and datediff(curdate(),birthday)/365 >= 1 and active;


select sum(points_balance) from user where active and activity_level>1;

select * from address where street_name like '%yıldız%';

select count(*) from user_invitation where opted_out = 1 and invited_by in (select id from user where active);

select * from orders order by id desc limit 100;

select * from user_login where user = 969;

select * from orders order by id desc limit 100;

select * from user_invitation where email like '%blogerma%';

select * from user where active = 1 order by points_balance desc limit 100;

select count(*) from orders where received_time is null and sent_time < '2013-01-31 00:00:00';

select count(*) from orders where received_time is null and sent_time >= '2013-01-31 00:00:00';
select count(*) from orders where sent_time is null;

select price_money,price_points from product;

select count(*) from user where active and email_verified and accept_site_mail and accept_thirdparty_mail;
select count(distinct user) from user_login where user in (select id from user where active and email_verified and accept_site_mail and accept_thirdparty_mail);

select * from user where active and id not in (select distinct user from user_login);

select distinct user from user_login where ip_address in (select distinct ip_address from user_login where user in (1405,1890));

select * from orders where received_time is null;
select * from user where id in (select user from address where id in (select address_id from orders where received_time is null));


select id from orders where received_time is null and address_id in (select id from address where user in (select id from user where accept_site_mail = 0 or accept_thirdparty_mail = 0 or accept_thirdparty_sms = 0));

select * from user where accept_site_mail = 0 or accept_thirdparty_mail = 0 or accept_thirdparty_sms = 0;

select * from user_aud where id = 1833;

select * from product;

select * from address where user in (2984,3010);

select * from district where id = 4198;

select * from user where active = 0;

select * from user where email like '%seyit%';

select * from user_aud where email like '%seyit%';

select * from user_invitation where email like '%zorro%';

select * from user where ambassador;

select * from product_order_criteria;

select * from user_login where user = 1327;

select * from product;


select * from user_invitation limit 10;
select count(*) from user_invitation;


select * from survey_answer where user_id = 1327 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332));
/* 292 */
select * from survey_answer_option where survey_answer_id = 1964239;
select * from survey_question where id = 292;
select * from survey_option where id = 1331;
select * from survey_option where question_id = 292;


select * from order_details limit 1000;
select (select count(*) from order_details where survey_completed)/(select count(*) from order_details)*100;

select (select count(*) from order_details where order_detail_type = 0)/(select count(*) from order_details)*100;
select count(*) from user where active;

select * from survey_option where question_id= 50;


select * from product_comment_rating where user = 2929;

/* bak bakalim atilabilir, zafer platstation istiyor, 2802 fors kullandim diye sallamis, 2336 da scooter bitti diye geyik yapmis*/
select * from user where email like '%zafer_gs%';
select * from user where id = 2802;
select * from user where id = 2838;
select * from user_invitation where invited_by = 2838;

select * from product;

select * from user_aud where id = 2623;

select * from user where id = 2929;

select * from address where user in (322,2919);

select * from user_aud where gsm_number in ('3123622','6905568');


select * from user where email in (
'Meltem.Aydin@Radissonblu.com',
'pinar.sezer@dominospizza.com.tr',
'nilay.canbazoglu@tchibo.com.tr',
'inci_0507@hotmail.com'
);


select * from address where user = 891;
select * from orders where address_id = 895;
select * from order_details where order_id in (select id from orders where address_id = 646);
select * from product where id in (select product_id from order_details where order_id in (select id from orders where address_id = 1065));
select * from order_details where order_id = 2858;
select * from order_details where product_id = 19;

select * from product where id in (1,5);

select * from address where street_name like '%58%' or area = 24507;

select * from user where id in (2902);

select * from user where email = 'onur@macline.com.tr';

select * from user where username like '%alv%';

select * from user_login where user = 2760;

select * from survey_answer where user_id = 2823;

select * from address_aud where user = 2823;
select * from address where user = 2902;

select * from address where street_name like '%2284%' or area = 48871;

select * from user where id in (1268,1291);
select * from address where user in (1268,1291);
select * from address where street_name like '%288%' or area = 25352;

select * from user where email like '%ozlemkizil%';

select datediff(curdate(),birthday)/365,email,birthday from user where  id in (2804,2988) ;

select * from user where id = 2988;

select * from user_login where user = 1268;


select * from orders where address_id in (select id from address where user in (1268,1291));


select * from user where id in (select invited_by from user_invitation where id in (select user_invitation from user where id = 2410));

select * from address where area = 25770;

select * from user where id = 642;

select * from address where user in (2823,642);

select * from user_login where user in (2823,642);


select * from address where street_name like '%Aslı%' or street_name like '%Asli%' or area = 39473; 

select * from user where user_invitation in (select id from user_invitation where invited_by = 2126);
select * from user where id = 2126;

select * from user where user_invitation in (select id from user_invitation where invited_by = 2126);
select * from user_invitation where invited_by = 2126;

select * from user where points_balance < 4300 and points_balance > 4100;

select * from user where email like '%rtyupm@hotmail.com%';

select * from user where active order by points_balance desc limit 20;


select * from address where area = 24377 or street_name like '%ahmet%' or street_name like '%engin%';

select * from user where email like '%aykut--%';
select * from user_invitation where id = 573;
select * from user where id in (1040,642);

select * from user_login where ip_address like '95.9.63.188';

select * from user where id = 1705;
select * from user_login where user = 1705;

select * from orders where address_id = 895;

select * from user_invitation where email like '%sny_ylmz_@hotmaild%';

select * from user where email like '%antido%';
select * from product where id = 2;

select * from product_comment where user = 2786;

select * from address where user = 261;
select * from orders where address_id = 265;
select * from order_details where order_id = 960;
select * from product where id = 5;


select * from point_transaction_accounting where user_id = 938;

select * from area where id = 24437;

select * from address where user = 2761;
select * from address where street_name like '%ukent%';

select * from address where user = 99;
select * from orders where address_id = 101;

select * from survey_answer where user_id = 11 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332));

select * from survey_question where id = 292;


select * from point_transaction_accounting where user_id=2752;

select * from user_invitation where email like '%marioxjardel9@gmail.com%';


select count(*) from user_invitation where opted_out = 0;

select * from product_comment where user = 2727;

select * from user_invitation where invited_by = 2687;



select * from user where ambassador or founder order by founder desc;

update user set activity_level = 1 where active = 0;


select count(*) from user where (datediff(curdate(),birthday)+1)/366 <= 18 and active;
select * from user where (datediff(curdate(),birthday)+1)/366 <= 18 and active order by points_balance desc;

select * from user_aud where id in (2835,2891);


select * from address where user = 2686;

select * from orders where address_id = 2700;
select * from order_details where order_id = 2835;
select * from configuration;
select * from product where id = 18;
select * from user_aud where id = 2117;

select * from user where email like '%inan_199%';

update user set activity_level = 1 where active = 0;

select * from user_login where user = 2471;

select * from product_comment where content like '%bedava%';

/* online hile org'dan

'indaclup_224@hotmail.com','ikbal1505@mynet.com','bayram4300@gmail.com','coldfeet@hotmail.com','patisserie_@hotmail.com','onr-37@hotmail.com 
werdan_atesman@hotmail.com 
nuks.ws.ws.ws.1@gmail.com 
simsek.mstf@hotmail.com
cagatayeren18@hotmail.com
furkanfbr@gmail.com 
iskender.teke@hotmail.com
gokay.gs1905@hotmail.com
bilalsel@ymail.com 
ali__savaskan@hotmail.com 
gokay.gs1905@hotmail.com 
raqunsteam@gmail.com 
drally_alfonso@hotmail.com 
turuncu47@hotmail.com
alaricdhuman@hotmail.com 
hamitcan3@gmail.com 
simsek.mstf@hotmail.com
oramaqomab@hotmail.com
onur.taskin16@hotmail.com
canberk_cano@hotmail.com
can_berk_cano@hotmail.com
enescan-37@hotmail.com 
osmandmx46@gmail.com
yigiterkan@windowslive.com tşkler 
yersizpanda@hotmail.com bekliyoruz 
reco256reco@hotmail.com
thenazzar@gmail.com yollayabilen 
crazy_onur147@hotmail.com gönderirsen 
waqrantali@hotmail.com 
furkanfbr@gmail.com 
nebi.irmak@gmail.com gönderebilirmisin
onur6886@windowslive.com
indirimliakbil@hotmail.com
KaraEL@windowslive.com 
hukumdarlar59@gmail.com 
Drally_alfonso@hotmail.com
joomla01@mynet.com bekliyorum
hakan@everestbursa.com 
alacaharun@gmail.com
bjk_fatih_93@hotmail.com
whosalvatore@hotmail.com
ertugrul.54@hotmail.com 
entschlackunqstee@hotmail.com 
ikbal1505@mynet.com
ilimiz_konya@hotmail.com
gokay.gs1905@hotmail.com 
sadiozkan@gmail.com
papidik16@gmail.com 
gek017@hotmail.com
yildiray_bjk_96@hotmail.com 
captain0120@hotmail.com 
b1lmem_k1@hotmail.com
buraktkn97@gmail.com teþekürler 
nzeki_1905@hotmail.com
jettvel524@hotmail.com
steamcan12@hotmail.com
oguzhnhcylmz@hotmail.com
dbpriest2@gmail.com 
erkutr58@hotmail.com
denizturan_1907@hotmail.com
bilalsel@ymail.com
dumbass.ayaz@hotmail.com
qarizmatik_24@hotmail.de 
kaankaraca@hotmail.com.tr 
selmanfo@gmail.com
night_walker_67@hotmail.com
jrhayko@hotmail.com
sansenpk@hotmail.com
patisserie_@hotmail.com 
simsek.mstf@hotmail.com 
 itzanm@mynet.com   
 analiz.aka.zebani_34@msn.com 
 opksoft@hotmail.com 
 bayram4300@gmail.com   
arifiyelee@hotmail.com 
Hayati.balkan@hotmail.com 
kadir_f245@hotmail.com bende 
erol_9889@hotmail.com
CoLDFeeT-_-@hotmail.com
xallonger@hotmail.com.tr 
alacaharun@gmail.com
burak.2692.61@gmail.com 
byemir_01@hotmail.com 
umutcankasap93@hotmail.com 
nuks.ws.ws.ws.1@gmail.com davetiye 
ihsanyanikoglu37@hotmail.com 
theexpendables18@gmail.com 
cazay_106@hotmail.com 
alacaharun@gmail.com
berker543@hotmail.com 
bayram4300@gmail.com davetiye 
twinkyhack@hotmail.com 
 cagatayeren18@hotmail.com a
 spotkurk@hotmail.com
 werdan_atesman@hotmail.com
varsa ikbal1505@mynet.com
gokselkizilay06@gmail.com 
ikbal1505@mynet.com
xalloger@hotmail.com.tr 
anatolianchild4@hotmail.com 
emre.93@windowslive.com 
Ertugrul_1905_78@hotmail.com
akankn@hotmail.com
celikeratak@gmail.com 
kanakan_crew@hotmail.com 
berker543@hotmail.com
ads_efsane_95@mynet.com gonderirsen 
celikeratak@gmail.com
e.acr34@hotmail.com
AntoniaGraza06@hotmail.com
HumanaPatraIII@hotmail.com
the0den@hotmail.com
i_dejavu@hotmail.com    
dursun.1903@mynet.com
kaankaraca@hotmail.com.tr 
mesto0@hotmail.com
zorbao6@hotmail.com     
ikbal1505@mynet.com
gsli_dursun@windowslive.com
oktayahmet.6@hotmail.com.tr 
tayfun_06_06@msn.com 
can_berk_cano@hotmail.com 
jrhayko@hotmail.com 
tekin.caglar@windowslive.com
yasinbarut67@hotmail.com
arifiyelee@hotmail.com
batuhan_7911@hotmail.com
selmanakyol@hotmail.com
danqer_120@hotmail.com


joomla02@mynet.com - Mert Atgın


select * from user where email in ('indaclup_224@hotmail.com','ikbal1505@mynet.com','bayram4300@gmail.com','coldfeet@hotmail.com','patisserie_@hotmail.com','onr-37@hotmail.com','werdan_atesman@hotmail.com','nuks.ws.ws.ws.1@gmail.com','simsek.mstf@hotmail.com','cagatayeren18@hotmail.com','furkanfbr@gmail.com','iskender.teke@hotmail.com','gokay.gs1905@hotmail.com','bilalsel@ymail.com','ali__savaskan@hotmail.com','gokay.gs1905@hotmail.com','raqunsteam@gmail.com','drally_alfonso@hotmail.com','turuncu47@hotmail.com','alaricdhuman@hotmail.com','hamitcan3@gmail.com','simsek.mstf@hotmail.com','oramaqomab@hotmail.com','onur.taskin16@hotmail.com','canberk_cano@hotmail.com','can_berk_cano@hotmail.com','enescan-37@hotmail.com','osmandmx46@gmail.com','yigiterkan@windowslive.com','yersizpanda@hotmail.com','reco256reco@hotmail.com','thenazzar@gmail.com','crazy_onur147@hotmail.com','waqrantali@hotmail.com','furkanfbr@gmail.com','nebi.irmak@gmail.com','onur6886@windowslive.com','indirimliakbil@hotmail.com','KaraEL@windowslive.com','hukumdarlar59@gmail.com','Drally_alfonso@hotmail.com','joomla01@mynet.com','hakan@everestbursa.com','alacaharun@gmail.com','bjk_fatih_93@hotmail.com','whosalvatore@hotmail.com','ertugrul.54@hotmail.com','entschlackunqstee@hotmail.com','ikbal1505@mynet.com','ilimiz_konya@hotmail.com','gokay.gs1905@hotmail.com','sadiozkan@gmail.com','papidik16@gmail.com','gek017@hotmail.com','yildiray_bjk_96@hotmail.com','captain0120@hotmail.com','b1lmem_k1@hotmail.com','buraktkn97@gmail.com','nzeki_1905@hotmail.com','jettvel524@hotmail.com','steamcan12@hotmail.com','oguzhnhcylmz@hotmail.com','dbpriest2@gmail.com','erkutr58@hotmail.com','denizturan_1907@hotmail.com','bilalsel@ymail.com','dumbass.ayaz@hotmail.com','qarizmatik_24@hotmail.de','kaankaraca@hotmail.com.tr','selmanfo@gmail.com','night_walker_67@hotmail.com','jrhayko@hotmail.com','sansenpk@hotmail.com','patisserie_@hotmail.com','simsek.mstf@hotmail.com','itzanm@mynet.com','analiz.aka.zebani_34@msn.com','opksoft@hotmail.com','bayram4300@gmail.com','arifiyelee@hotmail.com','Hayati.balkan@hotmail.com','kadir_f245@hotmail.com','erol_9889@hotmail.com','CoLDFeeT-_-@hotmail.com','xallonger@hotmail.com.tr','alacaharun@gmail.com','burak.2692.61@gmail.com','byemir_01@hotmail.com','umutcankasap93@hotmail.com','nuks.ws.ws.ws.1@gmail.com','ihsanyanikoglu37@hotmail.com','theexpendables18@gmail.com','cazay_106@hotmail.com','alacaharun@gmail.com','berker543@hotmail.com','bayram4300@gmail.com','twinkyhack@hotmail.com','cagatayeren18@hotmail.com','spotkurk@hotmail.com','werdan_atesman@hotmail.com','ikbal1505@mynet.com','gokselkizilay06@gmail.com','ikbal1505@mynet.com','xalloger@hotmail.com.tr','anatolianchild4@hotmail.com','emre.93@windowslive.com','Ertugrul_1905_78@hotmail.com','akankn@hotmail.com','celikeratak@gmail.com','kanakan_crew@hotmail.com','berker543@hotmail.com','ads_efsane_95@mynet.com','celikeratak@gmail.com','e.acr34@hotmail.com','AntoniaGraza06@hotmail.com','HumanaPatraIII@hotmail.com','the0den@hotmail.com','i_dejavu@hotmail.com','dursun.1903@mynet.com','kaankaraca@hotmail.com.tr','mesto0@hotmail.com','zorbao6@hotmail.com','ikbal1505@mynet.com','gsli_dursun@windowslive.com','oktayahmet.6@hotmail.com.tr','tayfun_06_06@msn.com','can_berk_cano@hotmail.com','jrhayko@hotmail.com','tekin.caglar@windowslive.com','yasinbarut67@hotmail.com','arifiyelee@hotmail.com','batuhan_7911@hotmail.com','selmanakyol@hotmail.com','danqer_120@hotmail.com','joomla02@mynet.com');

*/


select * from user where email like '%k.mera%';

select * from user_invitation where id =119;
select * from user where id = 8;

select * from user where id in (2029,1783);
select * from user_invitation where invited_by = 2312;

select * from user where lastname like '%inanır%' collate utf8_general_ci;

select * from user where email like ('joomla02@mynet.com');

select * from user_invitation where invited_by = 2417;
select * from user where user_invitation in (7981,7968,8041);

select * from user where id = 2218;
select * from user_invitation where id = 2348;
select * from user where id = 6;

select count(*) from user where active and activity_level > 1;

select * from orders where address_id = 192;

select * from product;

select * from user_invitation where email like '%emel.erden%';


select * from user where id = 2289;

select * from brand where name like '%caste%';

select * from product;

select * from address where user = 2599;

select * from address where street_name like '%nato%';

select * from orders where address_id = 1863;

select * from user where email like '%ahmet%';
select * from user_invitation where email like ('muratja@hotmail.com');
select * from user where id in (2551,2284);

select * from user where email in ('info@orhananilder.com.tr',
'sniperexper@hotmail.com',
'vatansever9008@gmail.com');

select * from user where id = 1458;

select * from point_transaction_accounting where user_id = 2423;

select * from user where user_invitation in (select id from user_invitation where invited_by = 855);


/* BUNU DA TEMIZLE BIR ARA */
/* comanci1@hotmail.com */

select * from brand;

select * from user_invitation where invited_by = 53;

select * from user_login where user = 2126 order by id desc;

select * from address where user = 2093;
select * from orders where address_id = 2098;

select * from user where id in (2477,2465);
select * from address where user = 2477;
select * from address where street_name like '%kayaş%';

select * from user where lastname like '%pinar%' or lastname like '%pınar%';

select
    sum(
        blog_comment_points +
        generic_survey_points +
        login_points +
        product_comment_points +
        product_survey +
        quiz_points +
        rating_points +
        signup_points +
        social_points +
        voting_points
    )
from
    point_transaction_accounting
where
    user_id = 2269 and
    create_date > '2012-10-22 00:00:00' and
    create_date < '2012-10-29 00:00:00'
;

select * from survey_answer where user_id = 1583 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332));

select * from survey_question where id = 1;
select * from survey_answer_option where survey_answer_id = 1773201;


select * from survey_option where id = 1333;

select * from survey_option;

/*
 *
 * Area: 43570 - Vezrköprü Cumhuriyet Mahallesi 2. Ziraat 542. Sokak
 * Area: 43715 - Cumhuriyet Mahallesi 2. Ziraat 542. Sokak
 *
 */

select * from survey_question;

 select * from address where street_name like '%cumhuriyet%';
 
 select * from user where active order by points_balance desc limit 100;
 
select * from address where user in (2118,1174);


select * from address where user = 2269;
/*ibrahim halil user = 2399 BU ADAMA DIKKAT */
select * from user_invitation where email like '%karael%';

/*DUPLIKE UYE OLABILIR MI HALIL ARICI*/
select * from user where firstname = 'Halil';
select * from user where email like '%soft.com%';
select * from survey_answer where user_id = 835 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948));
select * from survey_answer_option where survey_answer_id = 941906;


select * from survey_question;

select * from blog_comment where user = 2269;

select * from survey_answer where user_id = 1662 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948));

select * from product;

select * from survey_option where id = 921;

select * from user where id in (select user from address where id in (select address_id from orders where id in (select order_id from order_details where product_id = 1)));

select * from user_invitation where email like '%napsak%';

select * from address where user = 2083;

select * from orders where address_id = 2088;

select * from survey_answer where user_id = 1627;

select * from survey_question;

select  * from product;


select firstname,lastname,email from user where active and accept_site_mail and email_verified;

select count(distinct user) from product_comment where user in (select id from user where active);
select * from survey;
select * from brand where name like '%pronet%';

select * from user where id = 1895;
select * from user where email like '%volkin%';
select * from address where user = 214;
select * from orders where address_id = 218;
select * from order_details where order_id = 2045;

select * from user where id in (select invited_by from user_invitation where id in (1683,1772));
select * from user_login where user = 432;

select * from blog_post;
select * from product;


select * from survey_answer where user_id = 11 and id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332));

select * from survey_question where id = 291;
select * from survey_option where question_id = 195; /* 1326 */

select * from survey_question where id = 195;
select * from survey_option where id in (906);

select * from user_invitation where email like '%berke%';
select * from user_login where user = 469;

select * from user_invitation where id in (select user_invitation from user where email like '%barem%');
select * from user where id = 2049;

select * from address where user = 1652;
select * from orders where address_id = 1652;

select * from user where active and points_balance > 4999;


select * from user where extra_invitations > 0 order by extra_invitations desc;

select * from user where id = (select invited_by from user_invitation where id = (select user_invitation from user where id = 1652));

select count(*) from order_details;

select id, email, firstname, lastname, gsm_number from user where gsm_number > 999999;

select activity_level,count(activity_level) from user where active group by activity_level;

select count(distinct user) from user_login where user in (select id from user where active);
select * from user where active order by points_balance asc limit 200;

select u1.* from user u1 where u1.active = 1 and u1.id not in (select u2.user from user_login u2) order by create_date asc;

select * from user where active and points_balance = 110 order by create_date asc;

select * from user where lastname like '%uslu%';


select * from address where user in (select user from address group by user having count(user)>1);


select user,count(user) from product_comment group by user order by count(user) desc;
select user,count(user) from blog_comment group by user order by count(user) desc;

select * from user where id in (1205,383,519);
select * from blog_comment where num_of_dislikes - num_of_likes < 3 and num_of_likes - num_of_dislikes < 5 order by num_of_likes - num_of_dislikes desc;
select * from product_comment where num_of_dislikes - num_of_likes < 3 and num_of_likes - num_of_dislikes < 5  order by num_of_likes - num_of_dislikes desc;

select * from product_comment where content like '%yeni%' and num_of_dislikes - num_of_likes < 3 and user > 2;

select id,activity_level,points_balance,email,firstname,lastname,extra_invitations,ambassador from user where active = 1 and extra_invitations > 0 and id_verified order by extra_invitations desc;

select * from blog_comment where content like '%alaka%' order by id desc;


select * from user where email in (
'gulnur.sahin@excel.com.tr',
'eturan@atagp.com',
'dbayram@1l.com.tr',
'seyda.bekdik@pronet.com.tr',
'ebrukasikci@helyum.com.tr',
'sinansamed@hotmail.com',
'ebrukasikci@helyum.com.tr',
'luinnor@hotmail.com',
'selen.atac@bamarang.com.tr',
'gul.dogay@hkstrategies.com',
'alison.hutton@hkstrategies.com',
'seyda.uzun@hkstrategies.com',
'atifhoca@bugun.com.tr',
'mooserose@kadinlarkulubu.com',
'editor@campaigntr.com'
);

select * from order_details where order_id in (select id from orders where address_id in (select id from address where user in (502
,538,
1898)));

select * from product_category_3 where name like '%kolonya%';
select * from brand where name like '%est%';

select * from user_invitation where email like '%oguzhanm%';

select * from user where user_invitation = 6981;
select * from user where id = 1366;

select * from product where id in (3,4,9,11,14);

select * from product;

select * from brand;

select * from company;

select * from user where email like '%qa%' or email like '%qu%' ; 
select * from user where email like '%arnik%';
select * from address where user = 2373;
select * from address where street_name like '%vali%';

select * from user where id in (2340,2373);

select * from product_comment where product_id = 2 and num_of_likes>=num_of_dislikes order by id;

select * from address where user in (502
,538,
1898);

select * from user_invitation where email like '%hsnb%';

select * from address where street_name like '%seker%';

select * from user where
    gender = 0
    and (datediff(curdate(),birthday)+1)/365 >= 25
    and (datediff(curdate(),birthday)+1)/365 <= 45
    and monthly_income >= 3
    and education_type >= 2
    and active = 1
;

select * from product_comment where num_of_likes - num_of_dislikes > 5 order by product_id, id desc;

select product_id,count(product_id) from product_comment where num_of_likes - num_of_dislikes > -3 group by product_id;
select blog_post_id,count(blog_post_id) from blog_comment where num_of_likes - num_of_dislikes > -3 group by blog_post_id;

select * from survey where id in (select survey_id from survey_question where id in (12,37,44,53,90));
select * from survey_question where id in (12,37,44,53,90);
select id from survey_option where question_id in (12,37,44,53,90);

select * from survey_answer limit 10;

select * from survey_answer_option where survey_option_id in (select id from survey_option where question_id in (12,37,44,53,90));

select * from product_category_3;

select firstname,email from user where active and id not in (select id from user where active and founder=0 and ambassador = 0 and id in (53,3,2,6,41,11,891,2247,2226,1705,1679,1489,1327,1297,1275,1168,1109,1058,875,836,693,563,483,383,341,302,81,8,2932,2509,2479,2417,2313,2201,2193,2168,2084,2004,1955,1849,1685,1661,1620,1531,1376,1150,1145,1118,1069,988,893,865,799,793,754,715,642,565,523,463,351,234,116,42,31,7,5,2984,2818,2710,2521,2508,2487,2377,2361,2355,2341,2316,2293,2229,2196,2191,2185,2169,2166,2131,2108,2076,2057,2043,2019,2007,1985,1977,1946,1943,1886,1883,1872,1860,1823,1807,1749,1733,1732,1689,1665,1616,1578,1568,1530,1473,1431,1406,1362,1314,1289,1285,1283,1272,1262,1236,1222,1219,1218,1210,1209,1198,1189,1188,1180,1176,1172,1157,1155,1154,1153,1148,1105,1096,1095,1087,1086,1070,1067,1066,1060,1037,1017,1005,995,975,962,941,940,915,897,861,859,852,804,781,758,745,712,711,696,616,553,534,515,514,486,481,459,453,450,447,444,422,421,369,354,338,330,322,320,313,297,284,262,237,231,224,211,192,181,180,161,151,137,83,67,64,60,59,56,39,10));


select q.title,o.title from survey_question q, survey_option o where q.survey_id in (75,76) and o.question_id=q.id order by q.title,o.title;

select * from product_order_criteria;

select * from survey_question where survey_id = 73;
select count(*) from survey_option where question_id in (select id from survey_question where survey_id = 73);

select * from order_details where product_id = (select max(id) from product);

select * from user where ambassador or extra_invitations > 0;

select email,firstname from user where email_verified and accept_site_mail and accept_thirdparty_mail and 18 <= (datediff(curdate(),birthday)+1)/365 and active and id in (select user from address where area in (select id from area where district in (select id from district where county in (select id from county where city in (34))))) order by firstname,lastname;

select * from user where points_balance > 1000 and create_date <= '2013-04-30' and activity_level > 1 and 18 < (datediff(curdate(),birthday)+1)/365 and 40 > (datediff(curdate(),birthday)+1)/365 and active and id in (select user from address where area in (select id from area where district in (select id from district where county in (select id from county where city in (select id from city where name = 'İstanbul'))))) order by points_balance desc;

select email,firstname,(1200-points_balance) as kalan from user where email_verified and accept_site_mail and accept_thirdparty_mail and 18 <= (datediff(curdate(),birthday)+1)/365 and active and id not in (select user from address where id in (select address_id from orders where placed_time > '2013-05-05') union select user_id from survey_answer where id in (select survey_answer_id from survey_answer_option where survey_option_id in (1899,1898))) order by firstname,email;

select * from configuration;

select count(*)/0.6 from social_point;
select sum(p.social_points)/6 from point_transaction_accounting p;


select * from orders where received_time is null;

select activity_level, count(activity_level) from user where active=1 group by activity_level;


select u.active,u.id_verified,u.id,u.firstname,u.lastname,u.email,u.username,u.birthday,date_format(u.birthday,'%m-%d')as ay_gun,u.create_date, concat(gp.code,' ',u.gsm_number) as gsm_number,concat(a.name,' ',ad.street_name,' ',ad.building_number,' ',ad.apartment_number,' ',d.name,' ',d.postal_code,' ',c.name,' ',ci.name)
from user u, gsm_prefix gp, address ad, area a, district d, county c, city ci
where 
u.gsm_prefix = gp.id and
u.id = ad.user and
ad.area = a.id and
a.district = d.id and
d.county = c.id and
c.city = ci.id and
ci.name like '%maraş%' collate utf8_general_ci
;

select * from user where firstname like '%kubilay%';


select * from user where id in ('1679','1327','1168','875','693','483','383','341','302');

select * from user where id in ('1327','1168','875','693','483','383','302');


select * from user_login ul where ul.user in ('1679','1327','1168','875','693','483','383','341','302') order by user, login_time desc;

select * from user u where u.email in (
'cosku.c@gmail.com',
'didemmut@griworks.com'
);

UPDATE `bifincan`.`user` SET `active`='0', `accept_site_mail`='0', `accept_thirdparty_mail`='0', `accept_thirdparty_sms`='0' WHERE `email` in (
'cosku.c@gmail.com',
'didemmut@griworks.com'
);

select distinct user from user_login where ip_address in (select ip_address from user_login where user in (1197,1441));

select * from user u where u.email like '%my%ozer%';

select * from user u where u.email in (
'ser-gun-55@hotmail.com',
'rockkopatt@hotmail.com',
'eray_ist34@hotmail.com',
'selcukak34_55@hotmail.com',
'yigit_orhun@hotmail.com',
'erhanoz_17@hotmail.com',
'yeterrecep@gmail.com',
'gyesilay@hayat.com.tr',
'',
''
);


update user u set
	u.accept_site_mail = 0,
	u.accept_thirdparty_mail = 0,
	u.accept_thirdparty_sms = 0,
	u.email_verified = 0,
	u.fast_responses = 0,
	u.activity_level = 1
where
	u.active = 0
;
