/* CONFIGURATIONS */
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('invitationPoints', 'Number of points won when an invitation is sent to a friend', '10');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('invitationSuccessPoints', 'Number of points won when an invitation is activated by the person who received it', '50');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('signupPoints', 'Number of points won when a signup is completed', '50');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('loginPoints', 'Number of points won when a user logins (once per day)', '10');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('ratingPoints', 'Number of points won when a rating submitted', '10');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('socialPoints', 'Number of points won when a social activity takes place', 1);


INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('numInvitationsPerDay', 'Number of maximum invitations that a user send daily', '5');	
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('daysRequireBeforeNextOrder', 'The time needed to pass before being able to put another order', '7');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('timeRequiredBeforeProductSurveys', 'The time needed to pass before taking another survey', '7');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('maxNoOfWelcomeProducts', 'The maximum number of products that compose a welcome package', '1');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('noOfOrderableProducts', 'The maximum number of orderable products per each package', '3');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('noOfBonusOrderableProducts', 'The number of bounus orderable products based on user level activity', '1');

INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('maxNoOfSurpiseProducts', 'The maximum number of products sent as surprise products, as surprize addition to the normal cart content', '1');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('daysRequireBeforeProdIsOrderable', 'The time needed to pass before a product becomes orderable againg after its last order', '90');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('blogCommentPoints', 'The number of points gained by user for each blog comment he makes', '3');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('productCommentPoints', 'The number of points gained by user for each product comment (not rating) he makes', '5');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('applicationLocale', 'The locale of the application, tr or en', 'en');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('applicationCanonicalURL', 'Canonical URL of the application, http://localhost:8084 for development and https://www.bifincan.com for production', 'http://localhost:8084');

INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('smsGatewayUsername', 'The username for accessing the default SMS gateway', 'bifincanotp-mb4003');
INSERT INTO `configuration` (`config_key`, `config_description`, `config_value`) VALUES ('smsGatewayPassword', 'The password for accessing the default SMS gateway', '5265');

/* ADDRESS DEFINITIONS */
insert  into `global_region`(`id`,`jpaversion`,`name`) values (1,1,'Asia');
insert  into `country`(`id`,`jpaversion`,`name`,`global_region`) values (1,1,'Pakistan',1);
insert  into `local_region`(`id`,`jpaversion`,`name`,`country`) values (1,1,'Punjab',1);
insert  into `city`(`id`,`jpaversion`,`household`,`name`,`population`,`local_region`) values (3,1,100,'Lahore',100,1);
insert  into `city`(`id`,`jpaversion`,`household`,`name`,`population`,`local_region`) values (4,1,11,'Jehlum',100,1);
insert  into `county`(`id`,`jpaversion`,`name`,`phone_prefix`,`city`) values (3,1,'Samanabad',42,3);
insert  into `district`(`id`,`jpaversion`,`name`,`postal_code`,`county`) values (1,1,'Samanabad District','1234',3);
insert  into `area`(`id`,`jpaversion`,`name`,`district`) values (1,1,'New Samanabad',1);
insert  into `address_type`(`id`,`jpaversion`,`name`) values (1,1,'Home Address');
insert  into `address_type`(`id`,`jpaversion`,`name`) values (2,1,'Office Address');


/* GSM DEFINITIONS */
insert  into `gsm_operator`(`id`,`jpaversion`,`name`) values (1,1,'Vodafone');
insert  into `gsm_operator`(`id`,`jpaversion`,`name`) values (2,2,'MTN');
insert  into `gsm_operator`(`id`,`jpaversion`,`name`) values (3,1,'Ufone');
insert  into `gsm_operator`(`id`,`jpaversion`,`name`) values (4,1,'Mobilink');
insert  into `gsm_prefix`(`id`,`jpaversion`,`code`) values (1,1,300);
insert  into `gsm_prefix`(`id`,`jpaversion`,`code`) values (2,1,333);
insert  into `gsm_prefix`(`id`,`jpaversion`,`code`) values (3,1,334);
insert  into `gsm_prefix`(`id`,`jpaversion`,`code`) values (4,1,321);


/* USER DEFINITIONS */
insert  into `user`(`id`,`jpaversion`,`active`,`founder`,`birthday`,`create_date`,`email`,`email_verification_sent_time`,`email_verified`,`firstname`,`gsm_number`,`gsm_verification_sent_time`,`gsm_verified`,`lastname`,`password`,`points_balance`,`accept_site_mail`,`accept_thirdparty_mail`,`accept_thirdparty_sms`,`username`,`user_invitation`,`gsm_operator`,`gsm_prefix`, `education_type`, `employment_status`, `gender`, `has_children`, `marital_status`, `monthly_income`, `activity_level`,`ambassador`, `extra_invitations`, `fast_responses`, `id_verified`, `marketing_professional`) values (2,1,1,0,'2011-11-11','2011-11-11 11:45:40','usman.naveed@gmail.com','2011-11-11 11:45:40',1,'Usman',1231231,'2011-11-11 11:45:40',0,'Naveed','53863db2fde71dd1db544036e9b673c0',5,1,1,1,'usmannaveed',NULL,1,1,5, 3, 1, 0, 0, 4, 1, 0, 0, 0, 1, 1);
insert  into `user`(`id`,`jpaversion`,`active`,`founder`,`birthday`,`create_date`,`email`,`email_verification_sent_time`,`email_verified`,`firstname`,`gsm_number`,`gsm_verification_sent_time`,`gsm_verified`,`lastname`,`password`,`points_balance`,`accept_site_mail`,`accept_thirdparty_mail`,`accept_thirdparty_sms`,`username`,`user_invitation`,`gsm_operator`,`gsm_prefix`, `education_type`, `employment_status`, `gender`, `has_children`, `marital_status`, `monthly_income`, `activity_level`,`ambassador`, `extra_invitations`, `fast_responses`, `id_verified`, `marketing_professional`) values (3,1,1,1,'2011-11-11','2011-11-11 11:45:40','serkan.durusoy@dna-tr.com','2011-11-11 11:45:40',1,'Serkan',1231232,'2011-11-11 11:45:40',1,'Durusoy','8cd748077e7b8600bfc34e00e414db9e',5,1,1,1,'serkan',NULL,1,1,5, 3, 1, 0, 0, 4, 1, 0, 0, 0, 1, 1);
insert  into `user`(`id`,`jpaversion`,`active`,`founder`,`birthday`,`create_date`,`email`,`email_verification_sent_time`,`email_verified`,`firstname`,`gsm_number`,`gsm_verification_sent_time`,`gsm_verified`,`lastname`,`password`,`points_balance`,`accept_site_mail`,`accept_thirdparty_mail`,`accept_thirdparty_sms`,`username`,`user_invitation`,`gsm_operator`,`gsm_prefix`, `education_type`, `employment_status`, `gender`, `has_children`, `marital_status`, `monthly_income`, `activity_level`,`ambassador`, `extra_invitations`, `fast_responses`, `id_verified`, `marketing_professional`) values (4,1,1,0,'2011-11-11','2011-11-11 11:45:40','hantsy.bai@dna-tr.com','2011-11-11 11:45:40',1,'hantsy',1231233,'2011-11-11 11:45:40',0,'Bai','6564a607cded81d98845c0ab9bee5e82',5,1,1,1,'hantsy',NULL,1,1,5, 3, 1, 0, 0, 4, 1, 0, 0, 0, 1, 1);
insert  into `user`(`id`,`jpaversion`,`active`,`founder`,`birthday`,`create_date`,`email`,`email_verification_sent_time`,`email_verified`,`firstname`,`gsm_number`,`gsm_verification_sent_time`,`gsm_verified`,`lastname`,`password`,`points_balance`,`accept_site_mail`,`accept_thirdparty_mail`,`accept_thirdparty_sms`,`username`,`user_invitation`,`gsm_operator`,`gsm_prefix`, `education_type`, `employment_status`, `gender`, `has_children`, `marital_status`, `monthly_income`, `activity_level`,`ambassador`, `extra_invitations`, `fast_responses`, `id_verified`, `marketing_professional`) values (5,2,1,0,'2011-11-11','2011-11-11 11:45:40','vnikolaou@lycos.com','2011-11-11 11:45:40',1,'Vagelis',1231234,'2011-11-11 11:45:40',0,'Nikolaou','e3c19e8f3fd56ba206cde31ed0fa5e1d',10,1,1,1,'vagelis',NULL,1,1,5, 3, 1, 0, 0, 4, 1, 0, 0, 0, 1, 1);
insert  into `address`(`id`,`jpaversion`,`active`,`apartment_number`,`building_number`,`phone_extension`,`phone_number`,`primary_address`,`short_name`,`street_name`,`address_type`,`area`,`user`) values (2,0,1,'A','174',NULL,NULL,1,'My Home Address Test ','93',1,1,4);

insert  into `address`(`id`,`jpaversion`,`active`,`apartment_number`,`building_number`,`phone_extension`,`phone_number`,`primary_address`,`short_name`,`street_name`,`address_type`,`area`,`user`) values (1,0,1,'A','174',NULL,NULL,1,'My Home Address','93',1,1,3);
insert  into `address`(`id`,`jpaversion`,`active`,`apartment_number`,`building_number`,`phone_extension`,`phone_number`,`primary_address`,`short_name`,`street_name`,`address_type`,`area`,`user`) values (14,0,1,'A','174',245,4980552,0,'My Home Address','93',1,1,5);
insert  into `address`(`id`,`jpaversion`,`active`,`apartment_number`,`building_number`,`phone_extension`,`phone_number`,`primary_address`,`short_name`,`street_name`,`address_type`,`area`,`user`) values (15,0,1,'A','174',NULL,NULL,0,'My Home Address','93',1,1,3);


/* PRODUCT DEFINITIONS */
insert  into `company`(`id`,`jpaversion`,`name`) values (1,0,'Mcdonald');
insert  into `company`(`id`,`jpaversion`,`name`) values (2,0,'Samsung');
INSERT INTO `company` (`id`, `jpaversion`, `name`) VALUES (3, 0, 'XYZ Entertainment');


insert  into `brand`(`id`,`jpaversion`,`create_date`,`logo`,`name`,`url_blog`,`url_facebook`,`url_twitter`,`url_website`,`company_id`,`country_id`,`slug`) values (1,0,'2011-08-24 00:00:00',NULL,'McDonalds',NULL,NULL,NULL,NULL,1,1,'mcdonalds');
insert  into `brand`(`id`,`jpaversion`,`create_date`,`logo`,`name`,`url_blog`,`url_facebook`,`url_twitter`,`url_website`,`company_id`,`country_id`,`slug`) values (2,0,'2011-08-24 00:00:00',NULL,'Samsung',NULL,NULL,NULL,NULL,2,1,'samsung');
INSERT INTO `brand` (`id`, `jpaversion`, `create_date`, `logo`, `name`, `slug`, `url_blog`, `url_facebook`, `url_twitter`, `url_website`, `company_id`, `country_id`) VALUES (3, 0, '2011-12-22 08:40:08', NULL, 'XYZ Entertainment', 'xyz', NULL, NULL, NULL, NULL, 3, 1);

insert  into `product_category_1`(`id`,`jpaversion`,`name`,`business_type`) values (1,0,'Food and beverages','PRODUCT');
insert  into `product_category_1`(`id`,`jpaversion`,`name`,`business_type`) values (2,0,'Profile Survey','SERVICE');
insert  into `product_category_1`(`id`,`jpaversion`,`name`,`business_type`) values (3,0,'Movie','PRODUCT');
insert  into `product_category_1`(`id`,`jpaversion`,`name`,`business_type`) values (4,0,'Electronic Products','PRODUCT');

insert  into `product_category_2`(`id`,`jpaversion`,`name`,`product_category_1`) values (1,0,'Food and beverages',1);
insert  into `product_category_2`(`id`,`jpaversion`,`name`,`product_category_1`) values (2,0,'Profile Survey',2);
insert  into `product_category_2`(`id`,`jpaversion`,`name`,`product_category_1`) values (3,0,'Movie',3);
insert  into `product_category_2`(`id`,`jpaversion`,`name`,`product_category_1`) values (4,0,'Electronic Products',4);

insert  into `product_category_3`(`id`,`jpaversion`,`name`,`product_category_2`) values (1,0,'Food and beverages',1);
insert  into `product_category_3`(`id`,`jpaversion`,`name`,`product_category_2`) values (2,0,'Profile Survey',2);
insert  into `product_category_3`(`id`,`jpaversion`,`name`,`product_category_2`) values (3,0,'Movie',3);
insert  into `product_category_3`(`id`,`jpaversion`,`name`,`product_category_2`) values (4,0,'Electronic Products',4);

insert  into `brand_product_category_3`(`brand_id`,`categories_id`) values (1,1);
insert  into `brand_product_category_3`(`brand_id`,`categories_id`) values (2, 4);
INSERT INTO `brand_product_category_3` (`brand_id`, `categories_id`) VALUES (3, 3);

INSERT INTO `product_price_source` (`id`, `jpaversion`, `name`, `url`) VALUES (1, 1, 'Source1', 'http://www.google.com');

INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (3, 1, 1, '3', 'tv1', '2011-10-10 12:00:00', NULL, NULL, NULL, 1, 'tv1', 2, 0, 1, 10.00, 15, 90, 'tv1', 'tv1-slug', 1, 0, 2, 4, 1, 1, 0, 'tv1');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (4, 1, 1, '4', 'tv2', '2011-10-11 12:00:00', NULL, NULL, NULL, 2, 'tv2', 3, 0, 2, 20.00, 25, 80, 'tv2', 'tv2-slug', 0, 1, 2, 4, 1, 1, 0, 'tv2');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (5, 1, 1, '7', 'tv3', '2011-11-24 09:42:47', NULL, NULL, NULL, 0, 'tv3', 100, 1, 5, 15.00, 22, 100, 'tv3', 'tv3-slug', 0, 0, 2, 4, 1, 1, 0, 'tv3');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (6, 1, 0, '8', 'tv4', '2011-11-25 09:44:38', NULL, NULL, NULL, 1, 'tv4', 1, 1, 6, 12.00, 23, 80, 'tv4', 'tv4-slug', 0, 0, 2, 4, 1, 1, 0, 'tv4');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (9, 1, 1, '11', 'hi-fi 10', '2011-11-30 09:51:40', NULL, NULL, NULL, 2, 'hi-fi 10', 23, 1, 15, 100.00, 18, 25, 'hi-fi 10', 'hi-fi-10-slug', 0, 0, 2, 4, 1, 1, 0, 'hi-fi-10');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (10, 1, 1, '12', 'hi-fi 20', '2011-10-29 09:56:45', NULL, NULL, NULL, 3, 'hi-fi 20', 5, 1, 17, 70.00, 25, 30, 'hi-fi 20', 'hi-fi-20-slug', 0, 0, 2, 4, 1, 1, 0, 'hi-fi-20');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (1, 1, 1, '1', 'hamb1', '2011-10-12 12:00:00', NULL, NULL, NULL, 1, 'hamb1', 4, 1, 3, 30.00, 35, 70, 'hamb1', 'hamb1-slug', 0, 0, 1, 1, 1, 1, 0, 'hamb1');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (2, 1, 1, '2', 'hamb2', '2011-10-13 12:00:00', NULL, NULL, NULL, 2, 'hamb2', 5, 1, 4, 40.00, 45, 50, 'hamb2', 'hamb2-slug', 0, 0, 1, 1, 1, 1, 0, 'hamb2');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (7, 1, 0, '9', 'hamb3', '2011-11-26 09:45:49', NULL, NULL, NULL, 2, 'hamb3', 0, 0, 2, 1.00, 1, 200, 'hamb3 for surprise !', 'hamb3-slug', 1, 0, 1, 1, 1, 1, 0, 'hamb3');
INSERT INTO `product` (`id`, `jpaversion`, `active`, `barcode`, `description`, `entry_date`, `image_detail`, `image_large`, `image_small`, `loss`, `name`, `order_quantity`, `orderable_product`, `points_gained_per_question`, `price_money`, `price_points`, `stock_quantity`, `short_description`, `slug`, `surprise_product`, `welcome_product`, `brand_id`, `category_id`, `country_id`, `price_source_id`, `classifier`, `keywords`) VALUES (8, 1, 0, '10', 'hamb4', '2011-11-29 09:47:07', NULL, NULL, NULL, 1, 'hamb4', 100, 0, 2, 2.00, 2, 300, 'hamb4-for welcome !', 'hamb4-slug', 0, 1, 1, 1, 1, 1, 0, 'hamb4');


/* ORDERS */
INSERT INTO `orders` (`id`, `jpaversion`, `placed_time`, `received_time`, `sent_time`, `address_id`) VALUES (1, 1, '2011-11-15 11:06:04', '2011-11-16 11:06:06', '2011-11-17 11:06:09', 1);
INSERT INTO `orders` (`id`, `jpaversion`, `placed_time`, `received_time`, `sent_time`, `address_id`) VALUES (2, 1, '2011-10-15 11:06:18', '2011-10-16 11:06:20', '2011-10-17 11:06:21', 1);

INSERT INTO `order_details` (`id`, `jpaversion`, `survey_completed`, `order_detail_type`, `order_id`, `product_id`) VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO `order_details` (`id`, `jpaversion`, `survey_completed`, `order_detail_type`, `order_id`, `product_id`) VALUES (2, 1, 1, 2, 1, 2);
INSERT INTO `order_details` (`id`, `jpaversion`, `survey_completed`, `order_detail_type`, `order_id`, `product_id`) VALUES (3, 1, 0, 2, 2, 3);
INSERT INTO `order_details` (`id`, `jpaversion`, `survey_completed`, `order_detail_type`, `order_id`, `product_id`) VALUES (4, 1, 0, 2, 2, 4);
INSERT INTO `order_details` (`id`, `jpaversion`, `survey_completed`, `order_detail_type`, `order_id`, `product_id`) VALUES (5, 1, 0, 0, 2, 8);

/* POINTS */
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (1, 1, '2011-11-10 13:27:48', 10, 223, 23, 2, 33, 4, 33, 44, 454, 0, 0, 3);
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (2, 2, '2011-11-11 17:52:08', 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 3);
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (3, 1, '2011-11-12 18:30:41', 32, 3, 4, 54, 24, 222, 64, 57, 3, 0, 0, 3);
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (4, 1, '2011-11-13 18:31:22', 765, 2, 53, 456, 223, 34, 63, 16, 76, 0, 0, 3);
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (5, 1, '2011-11-14 18:32:56', 42, 2, 44, 44, 22, 34, 344, 34, 343, 0, 0, 3);
INSERT INTO `point_transaction_accounting` (`id`, `jpaversion`, `create_date`, `generic_survey_points`, `invitation_points`, `invitation_success_points`, `login_points`, `product_order`, `product_survey`, `quiz_points`, `rating_points`, `signup_points`, `blog_comment_points`, `product_comment_points`, `user_id`) VALUES (6, 1, '2011-11-15 10:46:18', 2, 3, 3, 45, 3, 7, 9, 11, 23, 0, 0, 3);


/* SURVEYS */
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROD_CAT', 1, 1, 1, 'Water Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROD_CAT', 2, 1, 1, 'Cinema Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROFILE', 3, 1, 1, 'Profile Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('BRAND', 4, 1, 1, 'Brand 1 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('BRAND_PROD_CAT', 5, 1, 1, 'Brand 1 Prod 1 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('BRAND_PROD_CAT', 6, 1, 1, 'Brand 2 Prod 1 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROD', 7, 1, 1, 'TV1 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROD', 8, 1, 1, 'TV2 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('PROD', 9, 1, 1, 'Hamb4 Survey', '2012-02-27 00:00:00', 0);
INSERT INTO `survey` (`type`, `id`, `jpaversion`, `active`, `title`, `create_date`, `prioritized`) VALUES ('BRAND', 10, 1, 1, 'Brand 2 Survey', '2012-02-27 00:00:00', 0);



INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (1, 1, 1, 1, 0, 'how often do you drink water?', 1);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (2, 1, 1, 1, 0, 'how many times do you go to the movies every year?', 2);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (3, 1, 1, 1, 0, 'Do you like speed star?', 3);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (4, 1, 1, 2, 0, 'How many kids you have?', 3);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (5, 1, 1, 3, 0, 'Are you doctor?', 3);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (6, 1, 1, 4, 0, 'How many times you drink water?', 3);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (7, 1, 1, 1, 0, 'Do you like our products 1?', 4);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (8, 1, 1, 1, 0, 'Do you like our products 2?', 5);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (9, 1, 1, 1, 0, 'Digital brand of year?', 6);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (10, 1, 1, 1, 0, 'Do you like TV1?', 7);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (11, 1, 1, 2, 0, 'Do you like TV1 colors?', 7);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (12, 1, 1, 1, 0, 'Do you like TV2?', 8);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (13, 1, 1, 2, 0, 'Do you like TV2 colors?', 8);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (14, 1, 1, 1, 0, 'Do you like Hamb4?', 9);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (15, 1, 1, 1, 1, 'What are the  benefits of our brand?', 10);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (16, 1, 1, 2, 0, 'Rate our qualiity & reliability', 10);
INSERT INTO `survey_question` (`id`, `jpaversion`, `active`, `position`, `select_multiple`, `title`, `survey_id`) VALUES (17, 1, 1, 3, 0, 'Do you like TV1 sound quality', 7);



INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (1, 1, 1, 'Daily', 1);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (2, 1, 1, 'After 2 hours', 1);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (3, 1, 1, 'After every 3 hours', 1);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (4, 1, 1, 'after every 4 hours', 1);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (5, 1, 1, '5 Times', 2);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (6, 1, 1, 'Less then 10 times', 2);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (7, 1, 1, 'More than 10 times', 2);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (8, 1, 1, 'Yes', 3);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (9, 1, 1, 'No', 3);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (10, 1, 1, '3', 4);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (11, 1, 1, '2', 4);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (12, 1, 1, 'Yes', 5);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (13, 1, 1, 'No', 5);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (14, 1, 1, '5 Times', 6);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (15, 1, 1, '10 times', 6);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (16, 1, 1, 'Yes', 7);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (17, 1, 1, 'No', 7);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (18, 1, 1, 'Yes', 8);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (19, 1, 1, 'No', 8);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (20, 1, 1, '100%', 9);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (21, 1, 1, '90%', 9);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (22, 1, 1, 'Less 80%', 9);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (23, 1, 1, 'Less 70%', 9);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (24, 1, 1, 'Yes', 10);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (25, 1, 1, 'No', 10);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (26, 1, 1, 'Yes', 11);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (27, 1, 1, 'No', 11);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (28, 1, 1, 'Yes', 12);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (29, 1, 1, 'No', 12);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (30, 1, 1, 'Yes', 13);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (31, 1, 1, 'No', 13);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (32, 1, 1, 'Yes', 14);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (33, 1, 1, 'No', 14);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (34, 1, 1, 'Quality', 15);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (35, 1, 1, 'Reliaibility', 15);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (36, 1, 1, 'After sales service', 15);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (37, 1, 1, 'Pure', 16);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (38, 1, 1, 'Good', 16);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (39, 1, 1, 'Very Good', 16);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (40, 1, 1, 'Yes', 17);
INSERT INTO `survey_option` (`id`, `jpaversion`, `active`, `title`, `question_id`) VALUES (41, 1, 1, 'No', 17);



INSERT INTO `survey_brand_product_category` (`survey_id`, `product_category3_id`) VALUES (5, 3);
INSERT INTO `survey_brand_product_category` (`survey_id`, `product_category3_id`) VALUES (6, 1);

INSERT INTO `survey_product` (`survey_id`, `product_id`) VALUES (7, 3);
INSERT INTO `survey_product` (`survey_id`, `product_id`) VALUES (8, 4);
INSERT INTO `survey_product` (`survey_id`, `product_id`) VALUES (9, 8);


INSERT INTO `survey_product_category` (`survey_id`, `product_category3_id`) VALUES (1, 1);
INSERT INTO `survey_product_category` (`survey_id`, `product_category3_id`) VALUES (2, 3);
INSERT INTO `survey_product_category` (`survey_id`, `product_category3_id`) VALUES (5, 4);
INSERT INTO `survey_product_category` (`survey_id`, `product_category3_id`) VALUES (6, 4);

INSERT INTO `survey_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `survey_id`, `option_id`) VALUES (1, 0, 4, '30', 1, 0, 4, NULL);
INSERT INTO `survey_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `survey_id`, `option_id`) VALUES (2, 0, 5, '40', 1, 0, 4, NULL);
INSERT INTO `survey_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `survey_id`, `option_id`) VALUES (3, 0, 4, '20', 1, 0, 5, NULL);
INSERT INTO `survey_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `survey_id`, `option_id`) VALUES (4, 0, 5, '29', 1, 0, 5, NULL);
INSERT INTO `survey_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `survey_id`, `option_id`) VALUES (5, 0, 0, '4', 1, 1, 3, NULL);


INSERT INTO `survey_question_criteria` (`id`, `jpaversion`, `rule`, `option_id`, `question_id`) VALUES (1, 1, 0, 24, 11);
INSERT INTO `survey_question_criteria` (`id`, `jpaversion`, `rule`, `option_id`, `question_id`) VALUES (2, 1, 1, 29, 13);
INSERT INTO `survey_question_criteria` (`id`, `jpaversion`, `rule`, `option_id`, `question_id`) VALUES (3, 1, 0, 34, 16);
INSERT INTO `survey_question_criteria` (`id`, `jpaversion`, `rule`, `option_id`, `question_id`) VALUES (4, 1, 0, 35, 16);
INSERT INTO `survey_question_criteria` (`id`, `jpaversion`, `rule`, `option_id`, `question_id`) VALUES (5, 1, 1, 25, 17);

INSERT INTO `product_order_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `product_id`, `survey_option_id`) VALUES (5, 0, 0, '3', 1, 1, 9, NULL);
INSERT INTO `product_order_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `product_id`, `survey_option_id`) VALUES (3, 0, 4, '30', 1, 0, 5, NULL);
INSERT INTO `product_order_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `product_id`, `survey_option_id`) VALUES (4, 0, 5, '40', 1, 0, 5, NULL);
INSERT INTO `product_order_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `product_id`, `survey_option_id`) VALUES (1, 0, 4, '41', 1, 0, 1, NULL);
INSERT INTO `product_order_criteria` (`id`, `jpaversion`, `operator_type`, `operator_value`, `operator_value_type`, `property_type`, `product_id`, `survey_option_id`) VALUES (2, 0, 5, '50', 1, 0, 1, NULL);


INSERT INTO `quiz_question` (`id`, `jpaversion`, `active`, `points`, `question`, `product_id`) VALUES (1, 1, 1, 1, 'How many inches does the television TV1 screen have?', 3);
INSERT INTO `quiz_question` (`id`, `jpaversion`, `active`, `points`, `question`, `product_id`) VALUES (2, 1, 1, 1, 'How many inches does the television TV2 screen have?', 4);
INSERT INTO `quiz_question` (`id`, `jpaversion`, `active`, `points`, `question`, `product_id`) VALUES (3, 1, 1, 1, 'Does the hamb4 have ketchup inside?', 8);

INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (1, 1, 1, 1, '27', 1);
INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (2, 1, 1, 0, '32', 1);
INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (3, 1, 1, 0, '19', 2);
INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (4, 1, 1, 1, '21', 2);
INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (5, 1, 1, 1, 'Yes', 3);
INSERT INTO `quiz_option` (`id`, `jpaversion`, `active`, `correct_option`, `option_text`, `quiz_question_id`) VALUES (6, 1, 1, 0, 'No', 3);


/*Admin user*/
/* USER DEFINITIONS */
insert  into `admin_user`(`id`,`jpaversion`,`active`,`create_date`,`email`,`firstname`,`lastname`,`password`,`username`,`role_type`) values (2,1,1,'2011-11-11 11:45:40','usman.naveed@gmail.com','Usman','Naveed','53863db2fde71dd1db544036e9b673c0','usmannaveed','ROLE_ADMINISTRATOR');
insert  into `admin_user`(`id`,`jpaversion`,`active`,`create_date`,`email`,`firstname`,`lastname`,`password`,`username`,`role_type`) values (3,1,1,'2011-11-11 11:45:40','serkan.durusoy@dna-tr.com','Serkan','Durusoy','8cd748077e7b8600bfc34e00e414db9e','serkan','ROLE_ADMINISTRATOR');
insert  into `admin_user`(`id`,`jpaversion`,`active`,`create_date`,`email`,`firstname`,`lastname`,`password`,`username`,`role_type`) values (4,1,1,'2011-11-11 11:45:40','hantsy.bai@dna-tr.com','hantsy','Bai','36ecadfe1ad7d54a7c25017295440c90','hantsy','ROLE_ADMINISTRATOR');
insert  into `admin_user`(`id`,`jpaversion`,`active`,`create_date`,`email`,`firstname`,`lastname`,`password`,`username`,`role_type`) values (5,2,1,'2011-11-11 11:45:40','vnikolaou@lycos.com','Vagelis','Nikolaou','e3c19e8f3fd56ba206cde31ed0fa5e1d','vagelis','ROLE_ADMINISTRATOR');


