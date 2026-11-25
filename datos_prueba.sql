USE gestor_reservas_db;

INSERT INTO clientes (nombre, email, telefono)
VALUES
    ('Ana Demo', 'ana.demo@example.com', '+34600111222'),
    ('Luis Demo', 'luis.demo@example.com', '+34600333444');

INSERT INTO servicios (nombre, descripcion, precio)
VALUES
    ('DEMO - Limpieza', 'Servicio de limpieza de ejemplo', 35.00),
    ('DEMO - Jardinería', 'Servicio de jardinería de ejemplo', 50.00);

SET @ana_id = (
    SELECT id FROM clientes
    WHERE email = 'ana.demo@example.com'
    LIMIT 1
);

SET @luis_id = (
    SELECT id FROM clientes
    WHERE email = 'luis.demo@example.com'
    LIMIT 1
);

SET @limpieza_id = (
    SELECT id FROM servicios
    WHERE nombre = 'DEMO - Limpieza'
    LIMIT 1
);

SET @jardineria_id = (
    SELECT id FROM servicios
    WHERE nombre = 'DEMO - Jardinería'
    LIMIT 1
);

INSERT INTO reservas (fecha_reserva, estado, cliente_id, servicio_id)
VALUES
    (DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), 'Pendiente', @ana_id, @limpieza_id),
    (DATE_ADD(CURRENT_DATE, INTERVAL 8 DAY), 'Confirmada', @luis_id, @jardineria_id);