insert into table_users_visits (user_id, product_id, view_count, buy_count) values('USER_9', 'P1', 1, 0)
on duplicate key update view_count = view_count + 1;

insert into table_users_visits (user_id, product_id, view_count, buy_count) values('USER_9', 'P2', 0, 1)
on duplicate key update buy_count = buy_count + 1;

commit;

select * from table_users_visits where product_id = 'P1' and user_id = 'USER_9';
select SUM(view_count) as views, SUM(buy_count) as buys from table_users_visits where product_id = 'P1';
select product_id, view_count as views, buy_count as buys from table_users_visits where user_id = 'USER_9' order by views desc, buys desc;
select distinct(product_id), SUM(view_count), SUM(buy_count) from table_users_visits group by product_id;

