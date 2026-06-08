-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Seed: marcas e modelos do mercado brasileiro
-- Execute após o schema.sql
-- ============================================================

-- ─── MARCAS ──────────────────────────────────────────────────
INSERT INTO marca (nome) VALUES ('Chevrolet');   -- id 1
INSERT INTO marca (nome) VALUES ('Volkswagen');  -- id 2
INSERT INTO marca (nome) VALUES ('Fiat');        -- id 3
INSERT INTO marca (nome) VALUES ('Ford');        -- id 4
INSERT INTO marca (nome) VALUES ('Toyota');      -- id 5
INSERT INTO marca (nome) VALUES ('Honda');       -- id 6
INSERT INTO marca (nome) VALUES ('Hyundai');     -- id 7
INSERT INTO marca (nome) VALUES ('Renault');     -- id 8
INSERT INTO marca (nome) VALUES ('Nissan');      -- id 9
INSERT INTO marca (nome) VALUES ('Jeep');        -- id 10
INSERT INTO marca (nome) VALUES ('Kia');         -- id 11
INSERT INTO marca (nome) VALUES ('Mitsubishi');  -- id 12
INSERT INTO marca (nome) VALUES ('Peugeot');     -- id 13
INSERT INTO marca (nome) VALUES ('Citroën');     -- id 14
INSERT INTO marca (nome) VALUES ('BMW');         -- id 15
INSERT INTO marca (nome) VALUES ('Mercedes-Benz'); -- id 16
INSERT INTO marca (nome) VALUES ('Audi');        -- id 17
INSERT INTO marca (nome) VALUES ('Volvo');       -- id 18
INSERT INTO marca (nome) VALUES ('Land Rover');  -- id 19
INSERT INTO marca (nome) VALUES ('Subaru');      -- id 20

-- ─── MODELOS — CHEVROLET (id 1) ──────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Onix',        '2019-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Onix Plus',   '2020-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Tracker',     '2021-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cruze',       '2017-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('S10',         '2016-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Spin',        '2015-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Montana',     '2023-01-01', 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Equinox',     '2018-01-01', 1);

-- ─── MODELOS — VOLKSWAGEN (id 2) ─────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Gol',         '2016-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Polo',        '2018-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Virtus',      '2019-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('T-Cross',     '2020-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Nivus',       '2021-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Taos',        '2022-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Amarok',      '2017-01-01', 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Saveiro',     '2015-01-01', 2);

-- ─── MODELOS — FIAT (id 3) ───────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Argo',        '2018-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cronos',      '2019-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Pulse',       '2022-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Fastback',    '2023-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Toro',        '2017-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Strada',      '2021-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Mobi',        '2016-01-01', 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Titano',      '2023-01-01', 3);

-- ─── MODELOS — FORD (id 4) ───────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Ka',          '2015-01-01', 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('EcoSport',    '2018-01-01', 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Ranger',      '2020-01-01', 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Territory',   '2021-01-01', 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Bronco',      '2022-01-01', 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Maverick',    '2022-01-01', 4);

-- ─── MODELOS — TOYOTA (id 5) ─────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Corolla',     '2020-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Corolla Cross','2022-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Yaris',       '2019-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Hilux',       '2021-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('SW4',         '2020-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('RAV4',        '2019-01-01', 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Prius',       '2018-01-01', 5);

-- ─── MODELOS — HONDA (id 6) ──────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Civic',       '2020-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('City',        '2021-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('City Hatch',  '2022-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HR-V',        '2022-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('WR-V',        '2023-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('CR-V',        '2019-01-01', 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Fit',         '2015-01-01', 6);

-- ─── MODELOS — HYUNDAI (id 7) ────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HB20',        '2020-01-01', 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HB20S',       '2020-01-01', 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Creta',       '2022-01-01', 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Tucson',      '2021-01-01', 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Santa Fe',    '2019-01-01', 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Elantra',     '2022-01-01', 7);

-- ─── MODELOS — RENAULT (id 8) ────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Kwid',        '2017-01-01', 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sandero',     '2016-01-01', 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Logan',       '2016-01-01', 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Duster',      '2020-01-01', 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Captur',      '2019-01-01', 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Oroch',       '2016-01-01', 8);

-- ─── MODELOS — NISSAN (id 9) ─────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Kicks',       '2017-01-01', 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Versa',       '2021-01-01', 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Frontier',    '2022-01-01', 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sentra',      '2021-01-01', 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X-Trail',     '2023-01-01', 9);

-- ─── MODELOS — JEEP (id 10) ──────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Renegade',    '2016-01-01', 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Compass',     '2017-01-01', 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Commander',   '2022-01-01', 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Wrangler',    '2020-01-01', 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Gladiator',   '2022-01-01', 10);

-- ─── MODELOS — KIA (id 11) ───────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sportage',    '2022-01-01', 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Stinger',     '2020-01-01', 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sorento',     '2021-01-01', 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('EV6',         '2023-01-01', 11);

-- ─── MODELOS — MITSUBISHI (id 12) ────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Eclipse Cross','2021-01-01', 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Outlander',   '2022-01-01', 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('L200 Triton', '2020-01-01', 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('ASX',         '2023-01-01', 12);

-- ─── MODELOS — PEUGEOT (id 13) ───────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('208',         '2021-01-01', 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('2008',        '2022-01-01', 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('3008',        '2021-01-01', 13);

-- ─── MODELOS — CITROËN (id 14) ───────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C3',          '2023-01-01', 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C4 Cactus',   '2020-01-01', 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Aircross',    '2021-01-01', 14);

-- ─── MODELOS — BMW (id 15) ───────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Serie 3',     '2020-01-01', 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Serie 5',     '2021-01-01', 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X1',          '2023-01-01', 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X3',          '2021-01-01', 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X5',          '2022-01-01', 15);

-- ─── MODELOS — MERCEDES-BENZ (id 16) ─────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Classe A',    '2020-01-01', 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Classe C',    '2022-01-01', 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('GLA',         '2021-01-01', 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('GLC',         '2022-01-01', 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sprinter',    '2020-01-01', 16);

-- ─── MODELOS — AUDI (id 17) ──────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('A3',          '2022-01-01', 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('A4',          '2021-01-01', 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Q3',          '2020-01-01', 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Q5',          '2021-01-01', 17);

-- ─── MODELOS — VOLVO (id 18) ─────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC40',        '2021-01-01', 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC60',        '2022-01-01', 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC90',        '2020-01-01', 18);

-- ─── MODELOS — LAND ROVER (id 19) ────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Defender',    '2021-01-01', 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Discovery',   '2020-01-01', 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Range Rover', '2022-01-01', 19);

-- ─── MODELOS — SUBARU (id 20) ────────────────────────────────
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Forester',    '2021-01-01', 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Outback',     '2022-01-01', 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Impreza',     '2020-01-01', 20);
