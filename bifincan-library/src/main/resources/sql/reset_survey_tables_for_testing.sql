delete from rating;
delete from quiz_answer;
delete from survey_answer_option;
delete from survey_answer;
delete from survey_product_status_for_order_details;
delete from user_session;
delete from survey_instance;

update order_details set survey_completed = 0 where id in (3,4,5);