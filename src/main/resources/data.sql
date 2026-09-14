INSERT INTO part (id, name, number, active, created_at, updated_at)
VALUES (1, 'Cartucho de toner preto', 'HP-56A', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO part (id, name, number, active, created_at, updated_at)
VALUES (2, 'Rolo de transferencia', 'RM2-6454', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO equipament (id, name, brand, category, type, active, created_at, updated_at)
VALUES (1, 'HP LaserJet Pro M404dn', 'HP', 'PRINTER', 'LASER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE part ALTER COLUMN id RESTART WITH 3;
ALTER TABLE equipament ALTER COLUMN id RESTART WITH 2;

INSERT INTO equipament_part (equipament_id, part_id)
VALUES (1, 1);

INSERT INTO equipament_part (equipament_id, part_id)
VALUES (1, 2);