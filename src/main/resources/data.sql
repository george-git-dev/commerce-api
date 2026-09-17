-- Seed de desenvolvimento local (H2). Roda em TODO restart da aplicação
-- (spring.sql.init.mode=always) — por isso começa limpando as tabelas antes
-- de inserir, pra nunca duplicar dado.
--
-- Sem dados de PROMOÇÃO: essa entidade ainda não existe no backend (só na
-- spec do OpenAPI) — nada pra semear aqui até isso ser implementado.

-- 1) Limpeza (filhos antes dos pais, por causa das FKs)
DELETE
FROM payments;
DELETE
FROM order_items;
DELETE
FROM orders;
DELETE
FROM addresses;
DELETE
FROM product_attributes;
DELETE
FROM products;
DELETE
FROM brands;
DELETE
FROM categories;
DELETE
FROM users;

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

-- 3) Usuários de teste
-- Senha de TODOS: senha123 (hash BCrypt abaixo já é dessa senha em texto puro)
INSERT INTO users (cpf, name, email, password, active, role)
VALUES ('11111111111', 'George Admin', 'admin@commerce.com',
        '$2b$10$4iVc9yK/7zO70LCt/6uhfOUroilQJgpULcwT2Cw5x.SbSbACHyD1i', true, 'ROLE_SUPER_ADMIN'),
       ('22222222222', 'Ana Operadora', 'ana.admin@commerce.com',
        '$2b$10$4iVc9yK/7zO70LCt/6uhfOUroilQJgpULcwT2Cw5x.SbSbACHyD1i', true, 'ROLE_ADMIN'),
       ('33333333333', 'Paulo Visualizador', 'paulo.viewer@commerce.com',
        '$2b$10$4iVc9yK/7zO70LCt/6uhfOUroilQJgpULcwT2Cw5x.SbSbACHyD1i', true, 'ROLE_VIEWER'),
       ('44444444444', 'Maria Cliente', 'maria.cliente@commerce.com',
        '$2b$10$4iVc9yK/7zO70LCt/6uhfOUroilQJgpULcwT2Cw5x.SbSbACHyD1i', true, 'ROLE_CUSTOMER'),
       ('55555555555', 'João Cliente', 'joao.cliente@commerce.com',
        '$2b$10$4iVc9yK/7zO70LCt/6uhfOUroilQJgpULcwT2Cw5x.SbSbACHyD1i', true, 'ROLE_CUSTOMER');

-- 4) Endereços (um por cliente)
INSERT INTO addresses (street, number, complement, neighborhood, city, state, zip_code, primary_address, user_id)
VALUES ('Rua das Flores', '123', 'Apto 45', 'Centro', 'São Paulo', 'SP', '01000-000', true,
        (SELECT id FROM users WHERE email = 'maria.cliente@commerce.com')),
       ('Av. Brasil', '456', NULL, 'Jardim América', 'Rio de Janeiro', 'RJ', '20000-000', true,
        (SELECT id FROM users WHERE email = 'joao.cliente@commerce.com'));

-- 5) Pedidos + itens + pagamentos
-- Pedido 1 — Maria, ENTREGUE, pago via PIX
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'maria.cliente@commerce.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'maria.cliente@commerce.com')),
        1049.70, 'ENTREGUE', DATEADD('DAY', -20, NOW()));
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Noite de Oud', 389.90, 1, 389.90),
       ((SELECT MAX(id) FROM orders), 'Flor de Damasco', 329.90, 2, 659.80);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 1049.70, 'PIX', 'APROVADO', DATEADD('DAY', -20, NOW()));

-- Pedido 2 — Maria, PAGO, aguardando envio
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'maria.cliente@commerce.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'maria.cliente@commerce.com')),
        459.90, 'PAGO', DATEADD('DAY', -2, NOW()));
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Couro Nobre', 459.90, 1, 459.90);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 459.90, 'CARTAO_CREDITO', 'APROVADO', DATEADD('DAY', -2, NOW()));

-- Pedido 3 — João, PENDENTE, sem pagamento ainda (testar fluxo "aguardando pagamento")
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'joao.cliente@commerce.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'joao.cliente@commerce.com')),
        349.90, 'PENDENTE', NOW());
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Vetiver Intenso', 349.90, 1, 349.90);

-- Pedido 4 — João, CANCELADO, pagamento recusado via boleto
INSERT INTO orders (user_id, address_id, total, status, created_at)
VALUES ((SELECT id FROM users WHERE email = 'joao.cliente@commerce.com'),
        (SELECT id FROM addresses WHERE user_id = (SELECT id FROM users WHERE email = 'joao.cliente@commerce.com')),
        399.90, 'CANCELADO', DATEADD('DAY', -10, NOW()));
INSERT INTO order_items (order_id, product_name, product_price, quantity, subtotal)
VALUES ((SELECT MAX(id) FROM orders), 'Rosa do Deserto', 399.90, 1, 399.90);
INSERT INTO payments (order_id, amount, method, status, created_at)
VALUES ((SELECT MAX(id) FROM orders), 399.90, 'BOLETO', 'RECUSADO', DATEADD('DAY', -10, NOW()));