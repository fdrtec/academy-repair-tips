INSERT INTO part (id, name, number)
VALUES (1, 'Cartucho de toner preto', 'HP-56A');

INSERT INTO part (id, name, number)
VALUES (2, 'Rolo de transferencia', 'RM2-6454');

INSERT INTO equipament (id, name, brand, category, type)
VALUES (1, 'HP LaserJet Pro M404dn', 'HP', 'PRINTER', 'LASER');

INSERT INTO equipament_part (equipament_id, part_id)
VALUES (1, 1);

INSERT INTO equipament_part (equipament_id, part_id)
VALUES (1, 2);