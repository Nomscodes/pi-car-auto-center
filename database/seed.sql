-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Seed: marcas e modelos do mercado brasileiro
-- Execute após o schema.sql
-- ============================================================

-- --- MARCAS ---
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

-- --- MODELOS — CHEVROLET (id 1) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Onix',        2019, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Onix Plus',   2020, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Tracker',     2021, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cruze',       2017, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('S10',         2016, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Spin',        2015, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Montana',     2023, 1);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Equinox',     2018, 1);

-- --- MODELOS — VOLKSWAGEN (id 2) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Gol',         2016, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Polo',        2018, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Virtus',      2019, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('T-Cross',     2020, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Nivus',       2021, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Taos',        2022, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Amarok',      2017, 2);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Saveiro',     2015, 2);

-- --- MODELOS — FIAT (id 3) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Argo',        2018, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cronos',      2019, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Pulse',       2022, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Fastback',    2023, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Toro',        2017, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Strada',      2021, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Mobi',        2016, 3);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Titano',      2023, 3);

-- --- MODELOS — FORD (id 4) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Ka',          2015, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('EcoSport',    2018, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Ranger',      2020, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Territory',   2021, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Bronco',      2022, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Maverick',    2022, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Edge',        2019, 4);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Fusion',      2017, 4);

-- --- MODELOS — TOYOTA (id 5) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Corolla',     2020, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Corolla Cross',2022, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Yaris',       2019, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Hilux',       2021, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('SW4',         2020, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('RAV4',        2019, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Prius',       2018, 5);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Camry',       2020, 5);

-- --- MODELOS — HONDA (id 6) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Civic',       2020, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('City',        2021, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('City Hatch',  2022, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HR-V',        2022, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('WR-V',        2023, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('CR-V',        2019, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Fit',         2015, 6);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Accord',      2018, 6);

-- --- MODELOS — HYUNDAI (id 7) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HB20',        2020, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('HB20S',       2020, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Creta',       2022, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Tucson',      2021, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Santa Fe',    2019, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Elantra',     2022, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Azera',       2018, 7);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Venue',       2021, 7);

-- --- MODELOS — RENAULT (id 8) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Kwid',        2017, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sandero',     2016, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Logan',       2016, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Duster',      2020, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Captur',      2019, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Oroch',       2016, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Stepway',     2019, 8);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Zoe',         2021, 8);

-- --- MODELOS — NISSAN (id 9) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Kicks',       2017, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Versa',       2021, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Frontier',    2022, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sentra',      2021, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X-Trail',     2023, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('March',       2018, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Leaf',        2020, 9);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Murano',      2019, 9);

-- --- MODELOS — JEEP (id 10) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Renegade',    2016, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Compass',     2017, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Commander',   2022, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Wrangler',    2020, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Gladiator',   2022, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Grand Cherokee', 2021, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cherokee',    2019, 10);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Avenger',     2023, 10);

-- --- MODELOS — KIA (id 11) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sportage',    2022, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Stinger',     2020, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sorento',     2021, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('EV6',         2023, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Carnival',    2022, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Cerato',      2019, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Picanto',     2018, 11);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Soul',        2020, 11);

-- --- MODELOS — MITSUBISHI (id 12) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Eclipse Cross', 2021, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Outlander',   2022, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('L200 Triton', 2020, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('ASX',         2023, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Pajero',      2019, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Galant',      2016, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Lancer',      2017, 12);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Xpander',     2022, 12);

-- --- MODELOS — PEUGEOT (id 13) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('208',         2021, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('2008',        2022, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('3008',        2021, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('408',         2023, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('308',         2020, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Expert',      2019, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Landtrek',    2021, 13);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('e-208',       2022, 13);

-- --- MODELOS — CITROËN (id 14) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C3',          2023, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C4 Cactus',   2020, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Aircross',    2021, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Berlingo',    2019, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Jumper',      2018, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C5',          2020, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C4',          2022, 14);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Spacetourer', 2021, 14);

-- --- MODELOS — BMW (id 15) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Serie 3',     2020, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Serie 5',     2021, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X1',          2023, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X3',          2021, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X5',          2022, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Serie 1',     2019, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('X6',          2021, 15);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('M3',          2022, 15);

-- --- MODELOS — MERCEDES-BENZ (id 16) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Classe A',    2020, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Classe C',    2022, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('GLA',         2021, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('GLC',         2022, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sprinter',    2020, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('CLA',         2021, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('EQC',         2022, 16);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Actros',      2019, 16);

-- --- MODELOS — AUDI (id 17) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('A3',          2022, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('A4',          2021, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Q3',          2020, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Q5',          2021, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Q7',          2022, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('A5',          2021, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('e-tron',      2022, 17);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('RS3',         2022, 17);

-- --- MODELOS — VOLVO (id 18) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC40',        2021, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC60',        2022, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XC90',        2020, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('S60',         2021, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('V60',         2020, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('C40',         2022, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('S90',         2021, 18);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('V90',         2020, 18);

-- --- MODELOS — LAND ROVER (id 19) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Defender',    2021, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Discovery',   2020, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Range Rover', 2022, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Freelander',  2018, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Evoque',      2021, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Velar',       2022, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Sport',       2023, 19);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Discovery Sport', 2021, 19);

-- --- MODELOS — SUBARU (id 20) ---
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Forester',    2021, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Outback',     2022, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Impreza',     2020, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('XV',          2021, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Legacy',      2019, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('BRZ',         2022, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('WRX',         2021, 20);
INSERT INTO modelo (nomeModelo, anoModelo, idMarca) VALUES ('Ascent',      2020, 20);
