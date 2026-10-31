-- Dados de teste SÓ DE DESENVOLVIMENTO (profile dev; nunca em produção).
-- Migração "repetível" do Flyway: roda de novo sempre que este arquivo muda.
-- Por isso começa zerando tudo — editar aqui recria os dados de teste do zero.

-- 1) Limpeza (TRUNCATE ... CASCADE já respeita as FKs e reinicia os ids)
TRUNCATE TABLE affiliate_sales, affiliates, payments, order_items, orders, cart_items, carts,
    product_promotions, promotions, product_attributes, products, brands, categories,
    addresses, expenses, users RESTART IDENTITY CASCADE;

-- 2) Catálogo
INSERT INTO categories (name, active)
VALUES ('Masculinos', true),
       ('Femininos', true),
       ('Árabes', true);

INSERT INTO brands (name, active)
VALUES ('ÂMBRA', true),
       ('Deserto Dourado', true);

INSERT INTO products (name, description, price, stock, active, category_id, brand_id, created_at, updated_at)
VALUES ('Noite de Oud', 'Eau de Parfum 100ml, notas amadeiradas e especiadas.', 389.90, 25, true,
        (SELECT id FROM categories WHERE name = 'Árabes'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(), NOW()),
       ('Âmbar Real', 'Eau de Parfum 100ml, âmbar e baunilha.', 449.90, 15, true,
        (SELECT id FROM categories WHERE name = 'Árabes'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(), NOW()),
       ('Oud Supremo', 'Eau de Parfum 100ml, oud intenso e resinoso.', 599.90, 8, true,
        (SELECT id FROM categories WHERE name = 'Árabes'), (SELECT id FROM brands WHERE name = 'Deserto Dourado'),
        NOW(), NOW()),
       ('Edição Limitada Âmbar', 'Eau de Parfum 100ml, edição limitada — descontinuado.', 699.90, 5, false,
        (SELECT id FROM categories WHERE name = 'Árabes'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(), NOW()),
       ('Flor de Damasco', 'Eau de Parfum 75ml, floral frutado.', 329.90, 30, true,
        (SELECT id FROM categories WHERE name = 'Femininos'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(),
        NOW()),
       ('Jardim Secreto', 'Eau de Parfum 75ml, floral verde.', 279.90, 20, true,
        (SELECT id FROM categories WHERE name = 'Femininos'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(),
        NOW()),
       ('Rosa do Deserto', 'Eau de Parfum 100ml, floral amadeirado.', 399.90, 0, true,
        (SELECT id FROM categories WHERE name = 'Femininos'), (SELECT id FROM brands WHERE name = 'Deserto Dourado'),
        NOW(), NOW()),
       ('Madeira do Deserto', 'Eau de Parfum 100ml, amadeirado seco.', 519.90, 12, true,
        (SELECT id FROM categories WHERE name = 'Masculinos'), (SELECT id FROM brands WHERE name = 'Deserto Dourado'),
        NOW(), NOW()),
       ('Couro Nobre', 'Eau de Parfum 100ml, couro e especiarias.', 459.90, 18, true,
        (SELECT id FROM categories WHERE name = 'Masculinos'), (SELECT id FROM brands WHERE name = 'ÂMBRA'), NOW(),
        NOW()),
       ('Vetiver Intenso', 'Eau de Parfum 100ml, vetiver e cedro.', 349.90, 22, true,
        (SELECT id FROM categories WHERE name = 'Masculinos'), (SELECT id FROM brands WHERE name = 'Deserto Dourado'),
        NOW(), NOW());

-- 3) Usuários de teste — os MESMOS do Keycloak de desenvolvimento (commerce-auth,
-- scripts/seed-dev-users.sh). Sem senha nem papel: login e papéis são do Keycloak.
-- No primeiro acesso com token, a API liga a conta pelo e-mail confirmado e grava o
-- keycloak_id (sub). Os dados de teste de cliente (endereços, pedidos) ficam com Ana e Bia.
INSERT INTO users (cpf, name, email, active)
VALUES ('11111111111', 'Super Admin', 'superadmin@email.com', true),
       ('22222222222', 'Admin Loja', 'admin@email.com', true),
       ('33333333333', 'Visualizador Loja', 'viewer@email.com', true),
       ('44444444444', 'Ana Souza', 'ana@email.com', true),
       ('55555555555', 'Bia Lima', 'bia@email.com', true);

-- 4) Endereços (um por cliente)
INSERT INTO addresses (street, number, complement, neighborhood, city, state, zip_code, primary_address, user_id)
VALUES ('Rua das Flores', '123', 'Apto 45', 'Centro', 'São Paulo', 'SP', '01000-000', true,
        (SELECT id FROM users WHERE email = 'ana@email.com')),
       ('Av. Brasil', '456', NULL, 'Jardim América', 'Rio de Janeiro', 'RJ', '20000-000', true,
        (SELECT id FROM users WHERE email = 'bia@email.com'));

-- 5) Pedidos + itens + pagamentos
-- Pedido 1 — Ana, ENTREGUE, pago via PIX
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'ana@email.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'ana@email.com')),
        1049.70, 'ENTREGUE', NOW() - INTERVAL '20 days');
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Noite de Oud', 389.90, 1, 389.90),
       ((SELECT MAX(id) FROM orders), 'Flor de Damasco', 329.90, 2, 659.80);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 1049.70, 'PIX', 'APROVADO', NOW() - INTERVAL '20 days');

-- Pedido 2 — Ana, PAGO, aguardando envio
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'ana@email.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'ana@email.com')),
        459.90, 'PAGO', NOW() - INTERVAL '2 days');
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Couro Nobre', 459.90, 1, 459.90);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 459.90, 'CARTAO_CREDITO', 'APROVADO', NOW() - INTERVAL '2 days');

-- Pedido 3 — Bia, PENDENTE, sem pagamento ainda (testar fluxo "aguardando pagamento")
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'bia@email.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'bia@email.com')),
        349.90, 'PENDENTE', NOW());
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Vetiver Intenso', 349.90, 1, 349.90);

-- Pedido 4 — Bia, CANCELADO, pagamento recusado via boleto
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'bia@email.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'bia@email.com')),
        399.90, 'CANCELADO', NOW() - INTERVAL '10 days');
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Rosa do Deserto', 399.90, 1, 399.90);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 399.90, 'BOLETO', 'RECUSADO', NOW() - INTERVAL '10 days');