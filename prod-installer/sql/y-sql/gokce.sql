/* hediye yorumlari kontrolu */
select id,content,product_id,user,num_of_dislikes,num_of_likes from product_comment order by id desc limit 360;

/* bilog yorumlari kontrolu */
select id,content,blog_post_id,user,num_of_dislikes,num_of_likes from blog_comment order by id desc limit 450;

/* email adresinden kullanici arama */
select id,active, id_verified, activity_level,birthday, gender, marital_status, has_children, create_date, email, firstname, lastname, employment_status, education_type, monthly_income from user where email like '%bysuryaz%';

/* isminden kullanici arama */
select id,active, id_verified, activity_level,birthday, gender, marital_status, has_children, create_date, email, firstname, lastname, employment_status, education_type, monthly_income from user where firstname like '%azize%';

/* soyadindan kullanici arama */
select id,active, id_verified, activity_level,birthday, gender, marital_status, has_children, create_date, email, firstname, lastname, employment_status, education_type, monthly_income from user where lastname like '%çengel%';

/* idsinden kullanici arama */
select id,active, id_verified, activity_level,birthday, gender, marital_status, has_children, create_date, email, firstname, lastname, employment_status, education_type, monthly_income from user where id = 891;

/* kullanici adindan kullanici arama */
select id,active, id_verified, activity_level,birthday, gender, marital_status, has_children, create_date, email, firstname, lastname, employment_status, education_type, monthly_income from user where username like '%xxxx%';
