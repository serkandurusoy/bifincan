/* check for attendance_time > '2012-10-05 11:48:00' since the counter was reset to zero at this time*/
select
    u.id,
    u.points_balance,
    (select count(*) from survey_answer s where s.user_id = u.id) as count,
    (select count(s2.user_id) from survey_answer s2 where s2.user_id = u.id and s2.response_time < s2.expected_time group by s2.user_id) as fast_responses,
    u.birthday,
    u.create_date,
    u.email,
    u.firstname,
    u.lastname,
    u.id_verified,
    u.extra_invitations,
    u.marketing_professional,
    u.active 
from
    user u
where
    active = 1
;


select u1.id,u1.create_date,u1.birthday,u1.email,u1.firstname,u1.lastname,id_verified,(select count(u2.id) from user u2, user_invitation i where u2.active = 0  and
    u2.user_invitation = i.id and
    i.invited_by = u1.id) as cnt, (select concat(u3.firstname,' ',u3.lastname) from user u3, user_invitation i2 where u1.user_invitation = i2.id and i2.invited_by = u3.id) as daveteden from user u1 where u1.active order by cnt desc,create_date desc;

select * from user_invitation where opted_out = 0 and (invited_by in (select u1.id from user u1 where u1.active and (select count(u2.id) from user u2, user_invitation i where u2.active = 0  and u2.user_invitation = i.id and i.invited_by = u1.id) > 0) or invited_by in (select id from user where active = 0) or invited_by in (select user from product_comment where num_of_dislikes - num_of_likes > 3) or invited_by in (select user from blog_comment where num_of_dislikes - num_of_likes > 3));

select id,active,activity_level,points_balance,fast_responses,birthday,create_date,email,firstname,lastname,user_invitation,founder,extra_invitations,id_verified,marketing_professional from user where fast_responses > 0 and active = 1 order by fast_responses desc;

select * from user where active = 1 and id in (select user_id from survey_answer where id in (select survey_answer_id from survey_answer_option where survey_option_id in (891,892,893,895,896,898,899,900,901,902,903,904,905,906,914,915,916,917,918,919,921,922,925,926,927,928,929,931,932,933,934,935,937,938,939,940,941,945,946,947,948,1323,1324,1325,1327,1328,1329,1330,1331,1332)));

select * from product_comment where user in (select id from user where active = 0) and num_of_dislikes - num_of_likes < 3;
select * from blog_comment where user in (select id from user where active = 0) and num_of_dislikes - num_of_likes < 3 ;

select * from rating order by id desc limit 10;
select * from product_comment order by id desc limit 10;
select * from blog_comment order by id desc limit 10;
select * from address order by id desc limit 10; 
select * from user order by id desc limit 10;
select * from user where firstname like ' %' or firstname like '%  %' or firstname like '% ' or lastname like ' %' or lastname like '%  %' or lastname like '% ';

select * from user where datediff(curdate(),birthday)/365.25 < 14 and datediff(curdate(),birthday)/365 >= 2 and active;

select a1.active, a1.id, a1.email, a1.gsm_prefix, a1.gsm_number, a1.gsm_verified from user a1 
where 
0 < (select count(a2.email) from user_aud a2 where a2.id <> a1.id and a2.email = a1.email) 
or 
0 < (select count(a3.email) from user a3 where a3.id <> a1.id and a3.email = a1.email) 
or
0 < (select count(a4.gsm_number) from user_aud a4 where a4.id <> a1.id and a4.gsm_number = a1.gsm_number) 
or 
0 < (select count(a5.gsm_number) from user a5 where a5.id <> a1.id and a5.gsm_number = a1.gsm_number);


select * from survey_question; /* 24 */
select * from survey_option where question_id = 24; /* 91 evet 92 hayir */
select * from brand where name like '%donald%'; /* turkcell 522 vodafone 548 avea 41 migros 342 ülker 526 arçelik 30 vestel 542 thy 505 türk telekom 521 akbank 10 ford 199 beko 56 coca cola 119 pepsi 394*/
select * from user where active=1 and id in (select user_id from survey_answer where id in (select survey_answer_id from survey_answer_option where survey_option_id = 92 and survey_answer_id in (select id from survey_answer where survey_question_id = 24 and 
	brand_id in (522,548,41,342,526,542,30,505,521,10,199,119,394)
)));

/*
delete from survey_answer where brand_id = 56 and user_id = 1616;
delete from survey_answer_option where survey_answer_id in (select id from survey_answer where brand_id = 56 and user_id = 1616);
*/


select id,email,firstname,points_balance,create_date,datediff(curdate(),create_date)+1,points_balance/(datediff(curdate(),create_date)+1) from user where active = 1 and (datediff(curdate(),create_date)+1) < 40;

select
    id,birthday,datediff(curdate(),birthday)/360,email,firstname,points_balance,create_date,datediff(curdate(),create_date)+0.5,points_balance/(datediff(curdate(),create_date)+0.5)
from
    user
where
    active = 1 and
    user_invitation in
        (select id from user_invitation where invited_by in
                            (select id from user where active = 0 and create_date > '2012-08-20 00:00')
        )
order by
    points_balance desc
;






/* DIKKAT bu adreslere: goktugzafer@hotmail.com , 541 8180302  , ssafak95@gmail.com , 0534 8993943, musatafa_45@msn.com  5696532 // id 1405 ve 1890 ve 1833*/
select * from user_aud where email in ('goktugzafer@hotmail.com','ssafak95@gmail.com','musatafa_45@msn.com') or gsm_number in (8180302,8993943,5696532);
select distinct id from user_aud where email in ('goktugzafer@hotmail.com','ssafak95@gmail.com') or gsm_number in (8180302,8993943);
select * from address where user in (1405,1890,1833);
select * from address where area in (select area from address where user in (1405,1890,1833));



select
/*
    (a.user_id*FLOOR(1+RAND()*20)+FLOOR(1+RAND()*20)) as 'SRUID', -- bu kismen random ama cok daha iyi olabilir 
    (a.user_id*3+7) as 'SRUID', -- bu Turkcell icin 
    (a.user_id*5-3) as 'SRUID', -- bu Pronet icin 
    (a.user_id*7-1) as 'SRUID', -- bu contrex icin 
    (a.user_id*4-3) as 'SRUID', -- bu zarakol icin 
    (a.user_id*2-2) as 'SRUID', -- bu stike icin 
    (a.user_id*5-3) as 'SRUID', -- bu sinangil icin 
    (a.user_id*4-1) as 'SRUID', -- bu refika icin 
    (a.user_id*3+1) as 'SRUID', -- bu she icin 
    (a.user_id*2+1) as 'SRUID', -- bu gliss icin 
    (a.user_id*2+3) as 'SRUID', -- bu babylon icin  // soundgaden ile ayni
    (a.user_id*2+3) as 'SRUID', -- bu babylon soundgarden icin // babylon ile ayni
    (a.user_id*3-2) as 'SRUID', -- bu kasmpasa icin 
*/
    (a.user_id*2+3) as 'SRUID', -- bu babylon soundgarden icin // babylon ile ayni
    case (u.founder=0 and u.marketing_professional = 0 and u.active = 1)
        when 0 then 'Hayır'
        when 1 then 'Evet' 
    end as 'TAMAMEN TEMİZ', 
    case u.id_verified
        when 0 then 'Hayır'
        when 1 then 'Evet' 
    end as 'KİMLİĞİ GÖRÜLDÜ', 
    cit.name as 'ŞEHİR',
    round((datediff(curdate(),u.birthday)+1)/365.25) as 'YAŞ',
    case u.gender
        when 0 then 'Kadın'
        when 1 then 'Erkek' 
    end as 'CİNSİYETİ', 
    case u.marital_status
        when 0 then 'Evli'
        when 1 then 'Bekar' 
    end as 'MEDENİ DURUMU', 
    case u.has_children
        when 0 then 'Yok'
        when 1 then 'Var' 
    end as 'ÇOCUĞU', 
    case u.education_type
        when 0 then 'Yok'
        when 1 then 'İlköğretim' 
        when 2 then 'Lise' 
        when 3 then 'Yükseköğrenim'
        when 4 then 'Üniversite'
        when 5 then 'Lisansüstü'
        when 6 then 'Doktora veya üzeri'
    end as 'EĞİTİM DURUMU', 
    case u.employment_status
        when 0 then 'Çalışmıyor'
        when 1 then 'Yarı zamanlı' 
        when 2 then 'Tam zamanlı' 
        when 3 then 'Kendi işi'
    end as 'ÇALIŞMA DURUMU', 
    case u.monthly_income
        when 0 then 'yok'
        when 1 then '1000\'den az' 
        when 2 then '1000 - 2000' 
        when 3 then '2000 - 3000'
        when 4 then '3000 - 5000'
        when 5 then '5000 +'
    end as 'AYLIK GELİRİ', 
    case u.gsm_operator
        when 1 then 'Turkcell' 
        when 2 then 'Vodafone' 
        when 3 then 'Avea'
    end as 'GSM OPERATÖRÜ', 
    q.title as 'SORU',
    o.title as 'YANIT'
from
    survey_answer_option ao,
    survey_answer a,
    user u,
    survey_question q,
    survey_option o,
    address adr,
    area are,
    district dis,
    county cou,
    city cit
where
/*
    ao.survey_option_id in (select id from survey_option where question_id in (12,37,44,53,90)) and -- bu turkcell icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (52))) and -- bu contrex icin
    ao.survey_option_id in (select id from survey_option where question_id in (301,302,303,304,305)) and -- bu zarakol icin
    ao.survey_option_id in (select id from survey_option where question_id in (306,307,308,309,310,311,312,313)) and -- bu stike icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (54,55,56,57))) and -- bu pronet icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (75,76))) and -- bu she icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (74))) and -- bu sinangil icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (73))) and -- bu refika icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (72))) and -- bu gliss icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (81))) and -- bu babylon icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (84))) and -- bu kasimpasa icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (86))) and -- bu babylon soundgarden icin
*/
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (81,86))) and -- bu babylon ve soundgarden icin birlikte
    ao.survey_answer_id = a.id and
    a.user_id = u.id and
    a.survey_question_id = q.id and
    ao.survey_option_id = o.id and
    adr.primary_address = 1 and
    u.id = adr.user and
    adr.area = are.id and
    are.district = dis.id and
    dis.county = cou.id and
    cou.city = cit.id
;


select
    count(distinct a.user_id)
from
    survey_answer_option ao,
    survey_answer a,
    user u,
    survey_question q,
    survey_option o,
    address adr,
    area are,
    district dis,
    county cou,
    city cit
where
/*
    ao.survey_option_id in (select id from survey_option where question_id in (12,37,44,53,90)) and -- bu turkcell icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (52))) and -- bu contrex icin
    ao.survey_option_id in (select id from survey_option where question_id in (301,302,303,304,305)) and -- bu zarakol icin
    ao.survey_option_id in (select id from survey_option where question_id in (306,307,308,309,310,311,312,313)) and -- bu stike icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (54,55,56,57))) and -- bu pronet icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (75,76))) and -- bu she icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (74))) and -- bu sinangil icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (73))) and -- bu refika icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (81))) and -- bu babylon icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (84))) and -- bu kasimpasa icin
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (86))) and -- bu babylon soundgarden icin
*/
    ao.survey_option_id in (select id from survey_option where question_id in (select id from survey_question where survey_id in (81,86))) and
    ao.survey_answer_id = a.id and
    a.user_id = u.id and
    u.founder = 0 and
    u.marketing_professional = 0 and
    u.active = 1 and
    a.survey_question_id = q.id and
    ao.survey_option_id = o.id and
    adr.primary_address = 1 and
    u.id = adr.user and
    adr.area = are.id and
    are.district = dis.id and
    dis.county = cou.id and
    cou.city = cit.id
;


