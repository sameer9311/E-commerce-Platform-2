select * from table_feedbacks where product_id = 'P1';
select * from table_feedbacks where product_id = 'P1' and rating = 5;
select * from table_feedbacks where seller_id = 'S3';
select * from table_feedbacks where seller_id = 'S3' and rating = 5;
select * from table_feedbacks where seller_id = 'S_2' and product_id = 'P1';
select * from table_feedbacks where seller_id = 'S_2' and product_id = 'P1' and rating = 5;

select product_id, AVG(rating) from table_feedbacks group by product_id;

select seller_id, AVG(rating) from table_feedbacks group by seller_id;

select product_id, seller_id, AVG(rating) from table_feedbacks group by product_id, seller_id;

select * from (select product_id, AVG(rating) as ratings from table_feedbacks group by product_id) as T1 where product_id = 'P1';
select product_id, AVG(rating) as ratings from table_feedbacks group by product_id;
select product_id, seller_id, rating, AVG(rating) from table_feedbacks group by product_id, seller_id;