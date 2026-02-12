-- Address
INSERT INTO addresses (address_id, recipient_name, phone_number, zip_code, address1, address2) VALUES (1, 'Test Recipient', '010-1234-5678', '12345', 'Test Address 1', 'Test Address 2');

-- Product
INSERT INTO products (product_id, name, original_price, discount_price, discount_rate, thumbnail_image, category, stock, description, brand, specs, created_at)
VALUES (1, 'Test Product', 50000.00, 40000.00, 20, '/images/test_product.png', 'CategoryA', 100, 'Description for test product', 'Test Brand', '{}', NOW());

-- Order
INSERT INTO orders (order_id, order_number, status, created_at, order_type, address_id)
VALUES (1, 'ORD-1', 'PENDING', NOW(), 'DIRECT', 1);

-- OrderItem
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price_at_order)
VALUES (1, 1, 1, 1, 40000.00);