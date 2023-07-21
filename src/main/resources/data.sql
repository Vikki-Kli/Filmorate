INSERT INTO genres (id, name)
VALUES (1, 'COMEDY'),
       (2, 'DRAMA'),
       (3, 'ANIMATION'),
       (4, 'THRILLER'),
       (5, 'DOCUMENTARY'),
       (6, 'ACTION'),
       (7, 'ROMCOM'),
       (8, 'SCIFI'),
       (9, 'HORROR'),
       (10, 'ADVENTURE'),
       (11, 'SHORT'),
       (12, 'CRIME'),
       (13, 'FANTASY'),
       (14, 'ROMANCE'),
       (15, 'FAMILY'),
       (16, 'WAR'),
       (17, 'MUSICAL'),
       (18, 'BIOGRAPHY'),
       (19, 'WESTERN'),
       (20, 'DETECTIVE'),
       (21, 'HISTORY')
ON CONFLICT DO NOTHING;

INSERT INTO mpa (id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG13'),
       (4, 'R'),
       (5, 'NC17')
ON CONFLICT DO NOTHING;