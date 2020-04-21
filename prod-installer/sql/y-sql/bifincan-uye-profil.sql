/*KULLANICI SAYISI*/
select count(*) as users from user;

/*YAYINDA GÜN SAYISI*/
select datediff(curdate(),min(login_time))+1 as yayin_gunu from user_login;

/*CEVAPLANAN SORU SAYISI*/
select count(*) from survey_answer_option;

/*GÜNDE ORTALAMA CEVAPLANAN SORU SAYISI*/
select (select count(*) from survey_answer_option)/(select datediff(curdate(),min(login_time)+1) as yayin_gunu from user_login);

/*KULLANICI BAŞINA ORTALAMA CEVAPLANAN SORU SAYISI*/
select (select count(*) from survey_answer_option)/(select count(*) as users from user);

/*GÜNDE KULLANICI BAŞINA ORTALAM CEVAPLANAN SORU SAYISI*/
select 
    (select count(*) from survey_answer_option)
    /
    (select avg(datediff(curdate(),create_date)+1) from user)
    /
    (select count(*) as users from user)
;

/*AKTİVİTE*/
select 
    case 
        when points_balance/(datediff(curdate(),create_date)+1) <= 5 then '001 - 005'
        when 5 < points_balance/(datediff(curdate(),create_date)+1) and points_balance/(datediff(curdate(),create_date)+1) <= 15 then '005 - 015'
        when 15 < points_balance/(datediff(curdate(),create_date)+1) and points_balance/(datediff(curdate(),create_date)+1) <= 25 then '015 - 025'
        when 25 < points_balance/(datediff(curdate(),create_date)+1) and points_balance/(datediff(curdate(),create_date)+1) <= 50 then '026 - 050' 
        when 50 < points_balance/(datediff(curdate(),create_date)+1) and points_balance/(datediff(curdate(),create_date)+1) <= 100 then '051 - 100' 
        when 100 < points_balance/(datediff(curdate(),create_date)+1) then '101 +'
    end as aktivite, 
    count(*), 
    count(*)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    aktivite;
    
/*CİNSİYET*/
select 
    case gender
        when 0 then 'kadın'
        when 1 then 'erkek' 
    end as gender, 
    count(gender), 
    count(gender)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    gender;


/*ÇOCUK*/
select 
    case has_children
        when 0 then 'yok'
        when 1 then 'var' 
    end as has_children, 
    count(has_children), 
    count(has_children)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    has_children;


/*MEDENİ DURUM*/
select 
    case marital_status
        when 0 then 'evli'
        when 1 then 'bekar' 
    end as marital_status, 
    count(marital_status), 
    count(marital_status)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    marital_status;


/*ÇALIŞMA DURUMU*/
select 
    case employment_status
        when 0 then 'calismiyor'
        when 1 then 'yari-zamanli' 
        when 2 then 'tam-zamanli' 
        when 3 then 'kendi-isi'
    end as employment_status, 
    count(employment_status), 
    count(employment_status)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    employment_status;


/*EĞİTİM DURUMU*/
select 
    case education_type
        when 0 then 'yok'
        when 1 then 'ilköğretim' 
        when 2 then 'lise' 
        when 3 then 'yükseköğrenim'
        when 4 then 'üniversite'
        when 5 then 'lisansüstü'
        when 6 then 'doktora +'
    end as education_type, 
    count(education_type), 
    count(education_type)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    education_type;


/*GELİR DURUMU*/
select 
    case monthly_income
        when 0 then 'yok'
        when 1 then '1000\'den az' 
        when 2 then '1000 - 2000' 
        when 3 then '2000 - 3000'
        when 4 then '3000 - 5000'
        when 5 then '5000 +'
    end as monthly_income, 
    count(monthly_income), 
    count(monthly_income)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    monthly_income;
    

/*YAŞ*/
select 
    case 
        when 0 <= datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 19 then '14 - 18'
        when 18 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 22 then '19 - 22'
        when 22 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 26 then '23 - 26'
        when 26 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 30 then '27 - 30'
        when 30 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 35 then '31 - 35'
        when 35 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 40 then '36 - 40'
        when 40 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 50 then '41 - 50'
        when 50 < datediff(curdate(),birthday)/365 and datediff(curdate(),birthday)/365 <= 60 then '51 - 60'
        when 60 < datediff(curdate(),birthday)/365 then '60 +'
    end as yas, 
    count(*), 
    count(*)/(select count(*) from user)*100 as yuzde 
from 
    user 
group by 
    yas;
    
/*ŞEHİR*/
select
    ci.name,
    count(ci.name),
    count(ci.name)/(select count(*) from user)*100 as yuzde,
    ci.household/(select sum(household) from city)*100 as hane,
    ci.population/(select sum(population) from city)*100 as nufus    
from address a, area ar, district di, county co, city ci
where
    a.primary_address = 1 and
    a.area = ar.id and
    ar.district = di.id and
    di.county = co.id and
    co.city = ci.id
group by
    ci.name,
    ci.household,
    ci.population
;
