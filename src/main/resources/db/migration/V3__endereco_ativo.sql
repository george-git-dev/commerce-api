-- Endereço removido pelo cliente some da conta, mas a linha fica: pedidos antigos apontam para ela
-- (até o pedido guardar a própria cópia do endereço — B5/B6).
ALTER TABLE addresses ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
