SELECT seller_id, (base_cost * (1 - discount/100)) AS price
FROM table_sellers_products
WHERE product_id = 'P1' AND seller_quantity > 0
ORDER BY price, seller_id
LIMIT 1;

SELECT * from (
SELECT s.seller_id, seller_name, sp.product_id, category, brand_name, product_name, description, seller_quantity, total_quantity, base_cost, discount, (base_cost * (1 - discount/100)) AS price
FROM table_sellers s, table_sellers_products sp, table_products p
WHERE sp.product_id = p.product_id AND s.seller_id = sp.seller_id and seller_quantity  > 0
ORDER BY price, product_id) as T
where seller_id = 'S_2';


SELECT table_sellers.seller_id, seller_name, description, seller_quantity, base_cost, discount, (base_cost * (1 - discount/100)) AS price
FROM table_sellers, table_sellers_products
WHERE table_sellers.seller_id = table_sellers_products.seller_id and product_id = 'P1'
ORDER BY price, table_sellers.seller_id;

select distinct(brand_name) from table_products