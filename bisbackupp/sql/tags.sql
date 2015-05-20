SELECT table_tags.tag_id, tag_name
FROM table_tags, table_products_tags
WHERE table_tags.tag_id = table_products_tags.tag_id AND product_id = 'P1';

SELECT product_name
FROM table_tags, table_products, table_products_tags
WHERE table_products.product_id = table_products_tags.product_id AND table_tags.tag_id = table_products_tags.tag_id and table_tags.tag_id = 'T1';