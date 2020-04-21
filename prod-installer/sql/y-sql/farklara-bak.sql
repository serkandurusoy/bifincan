select
     u.id,
     u.username,
     u.points_balance,
     sum(
        a.blog_comment_points +
        a.generic_survey_points +
        a.invitation_points +
        a.invitation_success_points + 
        a.login_points +
        a.product_comment_points + 
        a.quiz_points +
        a.rating_points + 
        a.signup_points + 
        a.social_points +
        a.voting_points +
        a.product_survey -
        a.product_order
        ),
        u.points_balance - 
     sum(
        a.blog_comment_points +
        a.generic_survey_points +
        a.invitation_points +
        a.invitation_success_points + 
        a.login_points +
        a.product_comment_points + 
        a.quiz_points +
        a.rating_points + 
        a.signup_points + 
        a.social_points +
        a.voting_points +
        a.product_survey -
        a.product_order)
   from
      user u, point_transaction_accounting a
    where
    a.user_id = u.id
    group by u.id,u.username,u.points_balance,a.user_id
    ;
