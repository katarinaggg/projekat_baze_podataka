-- =====================================================================
-- Savetovaliste "Novi pocetak" - kompletna skripta baze podataka
-- Autorke: Katarina Gordic (RN 41/2023) i Anja Stankovic (RN 58/2023)
--
-- Skripta kreira bazu savetovaliste, sve tabele, strane kljuceve,
-- inicijalne (test) podatke i sve stored procedure.
-- Pokrenuti je JEDNOM, u celini, nad MySQL serverom na kome baza
-- savetovaliste jos ne postoji (CREATE TABLE nema IF NOT EXISTS).
-- =====================================================================

CREATE DATABASE IF NOT EXISTS savetovaliste;
USE savetovaliste;

-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-04-28 14:53:48.422

-- tables
-- Table: cena_seanse
CREATE TABLE cena_seanse (
    cena_id int  NOT NULL AUTO_INCREMENT,
    cena decimal(7,2)  NOT NULL,
    datum_od date NOT NULL,
    CONSTRAINT cena_seanse_pk PRIMARY KEY (cena_id)
);

-- Table: centar_za_obuku
CREATE TABLE centar_za_obuku (
    centar_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(100)  NOT NULL,
    email varchar(90)  NOT NULL,
    telefon varchar(20)  NOT NULL,
    ulica varchar(70)  NOT NULL,
    broj varchar(10)  NOT NULL,
    opstina nvarchar(30)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    UNIQUE INDEX email_ak_2 (email),
    CONSTRAINT centar_za_obuku_pk PRIMARY KEY (centar_id)
);

-- Table: fakultet
CREATE TABLE fakultet (
    fakultet_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(60)  NOT NULL,
    univerzitet_id int  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT fakultet_pk PRIMARY KEY (fakultet_id)
);

-- Table: fakultet_oblast
CREATE TABLE fakultet_oblast (
    fakultet_id int  NOT NULL,
    oblast_id int  NOT NULL,
    CONSTRAINT fakultet_oblast_pk PRIMARY KEY (fakultet_id,oblast_id)
);


-- Table: kurs
CREATE TABLE kurs (
    kurs_id int  NOT NULL AUTO_INCREMENT,
    datum datetime  NOT NULL,
    kurs_prema_dinaru decimal(6,2)  NOT NULL,
    valuta_id char(3)  NOT NULL,
    CONSTRAINT kurs_pk PRIMARY KEY (kurs_id)
);

-- Table: nacin_placanja
CREATE TABLE nacin_placanja (
    nacin_id char(2)  NOT NULL,
    naziv nvarchar(20)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT nacin_placanja_pk PRIMARY KEY (nacin_id)
);

-- Table: objava_seanse
CREATE TABLE objava_seanse (
	objava_id INT NOT NULL AUTO_INCREMENT,
    seansa_id INT NOT NULL,
    datum_objave DATE NOT NULL,
    primalac NVARCHAR(256),
    razlog TEXT NOT NULL,
    CONSTRAINT objava_seanse_pk PRIMARY KEY (objava_id)
);


-- Table: oblast
CREATE TABLE oblast (
    oblast_id int  NOT NULL,
    naziv nvarchar(40) NOT NULL,
    usmerenje_id int  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT oblast_pk PRIMARY KEY (oblast_id)
);

-- Table: oblast_psihoterapije
CREATE TABLE oblast_psihoterapije (
    sert_oblast_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(70)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT oblast_psihoterapije_pk PRIMARY KEY (sert_oblast_id)
);

-- Table: oblast_testa
CREATE TABLE oblast_testa (
    test_oblast_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(70)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT oblast_testa_pk PRIMARY KEY (test_oblast_id)
);

-- Table: placanje
CREATE TABLE placanje (
    placanje_id int  NOT NULL AUTO_INCREMENT,
    datum_uplate date  NOT NULL,
    iznos decimal(7,2)  NOT NULL,
    svrha ENUM('SEANSA - CELA', 'SEANSA - PRVA RATA', 'SEANSA - DRUGA RATA', 'TEST') NOT NULL,
    napomena NVARCHAR(100),   -- da se unese ime testa ako se placa test
    rata int  NULL,
    provizija_5_posto boolean  NULL,
    seansa_id int  NOT NULL,
    valuta_id char(3)  NOT NULL,
    nacin_id char(2)  NOT NULL,
    CONSTRAINT placanje_pk PRIMARY KEY (placanje_id)
);

-- Table: pol
CREATE TABLE pol (
    pol_id char(1)  NOT NULL,
    pol nvarchar(10)  NOT NULL,
    CONSTRAINT pol_pk PRIMARY KEY (pol_id)
);

-- Table: psihoterapeut_kandidat
CREATE TABLE psihoterapeut_kandidat (
    psihoterapeut_id int  NOT NULL AUTO_INCREMENT,
    jmbg bigint NOT NULL,
    ime nvarchar(25)  NOT NULL,
    prezime nvarchar(50)  NOT NULL,
    datum_rodjenja date  NOT NULL,
    telefon varchar(20)  NOT NULL,
    prebivaliste nvarchar(50)  NOT NULL,
    email varchar(90)  NOT NULL,
    lozinka varchar(32)  NULL,
    fakultet_id int  NOT NULL,
    stepen_id char(3)  NOT NULL,
    registrovan boolean NOT NULL DEFAULT FALSE,
    UNIQUE INDEX jmbg_ak_1 (jmbg),
    UNIQUE INDEX email_ak_2 (email),
    CONSTRAINT psihoterapeut_kandidat_pk PRIMARY KEY (psihoterapeut_id)
);

-- Table: rad_pod_supervizijom
CREATE TABLE rad_pod_supervizijom (
    rad_id int  NOT NULL AUTO_INCREMENT,
    datum_pocetka date  NOT NULL,
    datum_zavrsetka date  NULL,
    opis longtext  NULL,
    psihoterapeut_id int  NOT NULL,
    kandidat_id int  NOT NULL COMMENT 'isto FK',
    centar_id int  NOT NULL,
    CONSTRAINT rad_pod_supervizijom_pk PRIMARY KEY (rad_id)
);

-- Table: klijent
CREATE TABLE klijent (
    klijent_id int  NOT NULL AUTO_INCREMENT,
    ime nvarchar(20)  NOT NULL,
    prezime nvarchar(20)  NOT NULL,
    datum_rodjenja date  NOT NULL,
    email varchar(90)  NOT NULL,
    telefon varchar(20)  NOT NULL,
    prvi_put boolean  NOT NULL,
    opis_problema longtext  NOT NULL,
    pol_pol_id char(1)  NOT NULL,
    UNIQUE INDEX email_ak_1 (email),
    CONSTRAINT klijent_pk PRIMARY KEY (klijent_id)
);

CREATE INDEX ime_idx_1 ON klijent (ime);

CREATE INDEX prezime_idx_2 ON klijent (prezime);

-- Table: rezultat_testiranja
CREATE TABLE rezultat_testiranja (
    rezultat longtext  NOT NULL,
    test_id int  NOT NULL,
    seansa_id int  NOT NULL,
    CONSTRAINT rezultat_testiranja_pk PRIMARY KEY (test_id,seansa_id)
);

-- Table: seansa
CREATE TABLE seansa (
    seansa_id int  NOT NULL AUTO_INCREMENT,
    dan date  NOT NULL,
    vreme time  NOT NULL,
    beleske longtext  NULL,
    trajanje decimal(5,2)  NULL,
    placanje boolean  NULL,
    zlocin boolean  NULL,
    na_sudu boolean  NULL,
    klijent_id int  NOT NULL,
    cena_id int  NULL,
    psihoterapeut_kandidat_id int  NOT NULL,
    odrzana boolean NOT NULL DEFAULT FALSE,
    CONSTRAINT seansa_pk PRIMARY KEY (seansa_id)
);

CREATE INDEX dan_idx_1 ON seansa (dan);

-- Table: sertifikat
CREATE TABLE sertifikat (
    datum_sertifikacije date  NOT NULL,
    psihoterapeut_kandidat_id int  NOT NULL,
    sert_oblast_id int  NOT NULL,
    CONSTRAINT sertifikat_pk PRIMARY KEY (psihoterapeut_kandidat_id)
);

-- Table: stepen
CREATE TABLE stepen (
    stepen_id char(3)  NOT NULL,
    stepen_studija varchar(15)  NOT NULL,
    UNIQUE INDEX stepen_studija_ak_1 (stepen_studija),
    CONSTRAINT stepen_pk PRIMARY KEY (stepen_id)
);

-- Table: test
CREATE TABLE test (
    test_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(30)  NOT NULL,
    cena decimal(10,2)  NOT NULL,
    test_oblast_id int  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT test_pk PRIMARY KEY (test_id)
);

-- Table: univerzitet
CREATE TABLE univerzitet (
    univerzitet_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(60)  NOT NULL,
    usmerenje_id int  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT univerzitet_pk PRIMARY KEY (univerzitet_id)
);

-- Table: usmerenje
CREATE TABLE usmerenje (
    usmerenje_id int  NOT NULL AUTO_INCREMENT,
    naziv nvarchar(50)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT usmerenje_pk PRIMARY KEY (usmerenje_id)
);

-- Table: valuta
CREATE TABLE valuta (
    valuta_id char(3)  NOT NULL,
    naziv nvarchar(20)  NOT NULL,
    UNIQUE INDEX naziv_ak_1 (naziv),
    CONSTRAINT valuta_pk PRIMARY KEY (valuta_id)
);


-- foreign keys
-- Reference: fakultet_kandidat (table: psihoterapeut_kandidat)
ALTER TABLE psihoterapeut_kandidat ADD CONSTRAINT fakultet_kandidat FOREIGN KEY fakultet_kandidat (fakultet_id)
    REFERENCES fakultet (fakultet_id);

-- Reference: fakultet_oblast_fakultet (table: fakultet_oblast)
ALTER TABLE fakultet_oblast ADD CONSTRAINT fakultet_oblast_fakultet FOREIGN KEY fakultet_oblast_fakultet (fakultet_id)
    REFERENCES fakultet (fakultet_id);

-- Reference: fakultet_oblast_oblast (table: fakultet_oblast)
ALTER TABLE fakultet_oblast ADD CONSTRAINT fakultet_oblast_oblast FOREIGN KEY fakultet_oblast_oblast (oblast_id)
    REFERENCES oblast (oblast_id);

-- Reference: fakultet_univerzitet (table: fakultet)
ALTER TABLE fakultet ADD CONSTRAINT fakultet_univerzitet FOREIGN KEY fakultet_univerzitet (univerzitet_id)
    REFERENCES univerzitet (univerzitet_id);

-- Reference: kandidat_stepen (table: psihoterapeut_kandidat)
ALTER TABLE psihoterapeut_kandidat ADD CONSTRAINT kandidat_stepen FOREIGN KEY kandidat_stepen (stepen_id)
    REFERENCES stepen (stepen_id);

-- Reference: klijent_pol (table: klijent)
ALTER TABLE klijent ADD CONSTRAINT klijent_pol FOREIGN KEY klijent_pol (pol_pol_id)
    REFERENCES pol (pol_id);

-- Reference: kurs_valuta (table: kurs)
ALTER TABLE kurs ADD CONSTRAINT kurs_valuta FOREIGN KEY kurs_valuta (valuta_id)
    REFERENCES valuta (valuta_id);

-- Reference: oblast_usmerenje (table: oblast)
ALTER TABLE oblast ADD CONSTRAINT oblast_usmerenje FOREIGN KEY oblast_usmerenje (usmerenje_id)
    REFERENCES usmerenje (usmerenje_id);

-- Reference: placanje_nacin_placanja (table: placanje)
ALTER TABLE placanje ADD CONSTRAINT placanje_nacin_placanja FOREIGN KEY placanje_nacin_placanja (nacin_id)
    REFERENCES nacin_placanja (nacin_id);

-- Reference: placanje_seansa (table: placanje)
ALTER TABLE placanje ADD CONSTRAINT placanje_seansa FOREIGN KEY placanje_seansa (seansa_id)
    REFERENCES seansa (seansa_id);

-- Reference: placanje_valuta (table: placanje)
ALTER TABLE placanje ADD CONSTRAINT placanje_valuta FOREIGN KEY placanje_valuta (valuta_id)
    REFERENCES valuta (valuta_id);

-- Reference: psihoterapeut_seansa (table: seansa)
ALTER TABLE seansa ADD CONSTRAINT psihoterapeut_seansa FOREIGN KEY psihoterapeut_seansa (psihoterapeut_kandidat_id)
    REFERENCES psihoterapeut_kandidat (psihoterapeut_id);

-- Reference: rad_pod_supervizijom_centar_za_obuku (table: rad_pod_supervizijom)
ALTER TABLE rad_pod_supervizijom ADD CONSTRAINT rad_pod_supervizijom_centar_za_obuku FOREIGN KEY rad_pod_supervizijom_centar_za_obuku (centar_id)
    REFERENCES centar_za_obuku (centar_id);

-- Reference: rad_pod_supervizijom_psihoterapeut_kandidat (table: rad_pod_supervizijom) // ovo je kandidat
-- ISPRAVKA: ranije je ovaj FK pokazivao na kolonu psihoterapeut_id (isto kao FK ispod),
-- pa kolona kandidat_id nije imala strani kljuc. Sada je FK na kandidat_id, kao u fizickom modelu.
ALTER TABLE rad_pod_supervizijom ADD CONSTRAINT rad_pod_supervizijom_psihoterapeut_kandidat FOREIGN KEY rad_pod_supervizijom_psihoterapeut_kandidat (kandidat_id)
    REFERENCES psihoterapeut_kandidat (psihoterapeut_id);

-- Reference: rad_pod_supervizijom_psihoterapeut_kandidat_1 (table: rad_pod_supervizijom) // ovo je psihoterapeut (supervizor)
ALTER TABLE rad_pod_supervizijom ADD CONSTRAINT rad_pod_supervizijom_psihoterapeut_kandidat_1 FOREIGN KEY rad_pod_supervizijom_psihoterapeut_kandidat_1 (psihoterapeut_id)
    REFERENCES psihoterapeut_kandidat (psihoterapeut_id);

-- Reference: rezultat_testiranja_seansa (table: rezultat_testiranja)
ALTER TABLE rezultat_testiranja ADD CONSTRAINT rezultat_testiranja_seansa FOREIGN KEY rezultat_testiranja_seansa (seansa_id)
    REFERENCES seansa (seansa_id);

-- Reference: rezultat_testiranja_test (table: rezultat_testiranja)
ALTER TABLE rezultat_testiranja ADD CONSTRAINT rezultat_testiranja_test FOREIGN KEY rezultat_testiranja_test (test_id)
    REFERENCES test (test_id);

-- Reference: seansa_cena_seanse (table: seansa)
ALTER TABLE seansa ADD CONSTRAINT seansa_cena_seanse FOREIGN KEY seansa_cena_seanse (cena_id)
    REFERENCES cena_seanse (cena_id);

-- Reference: seansa_klijent (table: seansa)
ALTER TABLE seansa ADD CONSTRAINT seansa_klijent FOREIGN KEY seansa_klijent (klijent_id)
    REFERENCES klijent (klijent_id);

-- Reference: sertifikat_kandidat (table: sertifikat)
ALTER TABLE sertifikat ADD CONSTRAINT sertifikat_kandidat FOREIGN KEY sertifikat_kandidat (psihoterapeut_kandidat_id)
    REFERENCES psihoterapeut_kandidat (psihoterapeut_id);

-- Reference: sertifikat_oblast_psihoterapije (table: sertifikat)
ALTER TABLE sertifikat ADD CONSTRAINT sertifikat_oblast_psihoterapije FOREIGN KEY sertifikat_oblast_psihoterapije (sert_oblast_id)
    REFERENCES oblast_psihoterapije (sert_oblast_id);

-- Reference: test_test_oblast (table: test)
ALTER TABLE test ADD CONSTRAINT test_test_oblast FOREIGN KEY test_test_oblast (test_oblast_id)
    REFERENCES oblast_testa (test_oblast_id);

-- Reference: univerzitet_usmerenje (table: univerzitet)
ALTER TABLE univerzitet ADD CONSTRAINT univerzitet_usmerenje FOREIGN KEY univerzitet_usmerenje (usmerenje_id)
    REFERENCES usmerenje (usmerenje_id);
    
-- Reference: objava_seanse_seansa (table: objava_seanse)
ALTER TABLE objava_seanse ADD CONSTRAINT objava_seanse_seansa FOREIGN KEY (seansa_id)
    REFERENCES seansa (seansa_id);


INSERT INTO usmerenje (usmerenje_id, naziv) VALUES 
(1, 'Humanističke nauke'),
(2, 'Društvene nauke'),
(3, 'Primenjene nauke'),
(4, 'Prirodne nauke'),
(5, 'Tehničke nauke'),
(6, 'Informacione tehnologije'),
(7, 'Medicinske nauke'),
(8, 'Umetničke nauke'),
(9, 'Poljoprivredne nauke'),
(10, 'Sportske nauke');
    
INSERT INTO univerzitet (univerzitet_id, naziv, usmerenje_id) VALUES
(1, 'Univerzitet za humanističke nauke Beograd', 1),
(2, 'Univerzitet za psihologiju Novi Sad', 1),
(3, 'Internacionalni univerzitet društvenih i humanističkih nauka', 1),
(4, 'Univerzitet za primenjene nauke Kragujevac', 3),
(5, 'Univerzitet informatičkih nauka', 6),
(6, 'Univerzitet umetnosti Niš', 8),
(7, 'Univerzitet filozofskih nauka Subotica', 1),
(8, 'Univerzitet humanističkih i društvenih nauka', 1),
(9, 'Univerzitet tehničkih nauka', 5),
(10, 'Univerzitet za sportske nauke', 10);

-- Unos oblasti (10 redova)
INSERT INTO oblast (oblast_id, naziv, usmerenje_id) VALUES
(1, 'Psihologija', 1),
(2, 'Pedagogija', 1),
(3, 'Filozofija', 1),
(4, 'Sociologija', 1),
(5, 'Komunikologija', 1),
(6, 'Menadžment', 3),
(7, 'Informatika', 6),
(8, 'Likovna umetnost', 8),
(9, 'Mašinstvo', 5),
(10, 'Sport', 10);

INSERT INTO fakultet (fakultet_id, naziv, univerzitet_id) VALUES
(1, 'Fakultet za psihologiju', 1),
(2, 'Fakultet za pedagogiju', 2),
(3, 'Fakultet za društvene nauke', 3),
(4, 'Fakultet za primenjenu psihologiju', 1),
(5, 'Fakultet za informacione tehnologije', 5),
(6, 'Fakultet za filozofiju', 7),
(7, 'Fakultet za komunikaciju i medije', 8),
(8, 'Fakultet za menadžment', 4),
(9, 'Fakultet tehničkih nauka', 9),
(10, 'Fakultet za sport i fizičku kulturu', 10);

-- Povezivanje fakulteta sa oblastima (najmanje po jedna, neki više)
INSERT INTO fakultet_oblast (fakultet_id, oblast_id) VALUES
(1, 1),
(2, 2),
(3, 4),
(4, 1),
(4, 2),
(5, 7),
(6, 3),
(7, 5),
(8, 6),
(9, 9),
(10, 10);


INSERT INTO stepen (stepen_id, stepen_studija) VALUES
('BSc', 'Osnovne'),
('MSc', 'Master'),
('PhD', 'Doktorske');

-- Psihoterapeuti kandidati (24 redova, prvih 12 su psihoterapeuti)
INSERT INTO psihoterapeut_kandidat 
(jmbg, ime, prezime, datum_rodjenja, telefon, prebivaliste, email, lozinka, fakultet_id, stepen_id, registrovan) VALUES
(1205990711017, 'Marko', 'Marković', STR_TO_DATE('12.05.1990', '%d.%m.%Y'), '0611234567', 'Beograd', 'marko.markovic@gmail.com', '1234Marko', 1, 'MSc', TRUE),
(230799745015, 'Ana', 'Jovanović', STR_TO_DATE('23.07.1992', '%d.%m.%Y'), '0622345678', 'Vranje', 'ana.jovanovic@gmail.com', '2307.misko', 2, 'MSc', TRUE),
(140399715029, 'Ivana', 'Petrović', STR_TO_DATE('14.03.1993', '%d.%m.%Y'), '0633456789', 'Beograd', 'ivana.petrovic@yahoo.com', 'lozinkaIvana', 4, 'BSc', TRUE),
(051099731021, 'Stefan', 'Milinković', STR_TO_DATE('05.10.1991', '%d.%m.%Y'), '0619876543', 'Niš', 'stefan.milinkovic@hotmail.com', 'volimFudbal123', 4, 'MSc', TRUE),
(111199721030, 'Milan', 'Đorđević', STR_TO_DATE('11.11.1994', '%d.%m.%Y'), '0625566778', 'Kragujevac', 'milan.djordjevic@gmail.com', '1234321', 6, 'MSc', TRUE),
(060699735053, 'Kristina', 'Vasić', STR_TO_DATE('06.06.1990', '%d.%m.%Y'), '0647788990', 'Niš', 'kristina.vasic@gmail.com', 'kris96', 1, 'PhD', TRUE),
(1101990701017, 'Petar', 'Simić', STR_TO_DATE('11.01.1989', '%d.%m.%Y'), '0611122334', 'Beograd', 'petar.simic@gmail.com', 'mojaLozinka', 1, 'MSc', TRUE),
(2202991701028, 'Milica', 'Živković', STR_TO_DATE('22.02.1990', '%d.%m.%Y'), '0622233445', 'Novi Sad', 'milica.zivkovic@gmail.com', 'lozinkaMilica', 3, 'BSc', TRUE),
(1303992701039, 'Stefan', 'Pavlović', STR_TO_DATE('13.03.1991', '%d.%m.%Y'), '0633344556', 'Niš', 'stefan.pavlovic@gmail.com', '130392stefan', 2, 'MSc', TRUE),
(1707996701073, 'Nikola', 'Đorđević', STR_TO_DATE('17.07.1993', '%d.%m.%Y'), '0677788990', 'Čačak', 'nikola.djordjevic@gmail.com', 'jaVolimCacak', 4, 'PhD', TRUE),
(1909998701095, 'Marko', 'Kovačević', STR_TO_DATE('19.09.1992', '%d.%m.%Y'), '0699900112', 'Valjevo', 'marko.kovacevic@gmail.com', 'mare98', 3, 'MSc', TRUE),
(1212000701117, 'Luka', 'Mihajlović', STR_TO_DATE('12.12.1994', '%d.%m.%Y'), '0611222334', 'Kraljevo', 'luka.mihajlovic@gmail.com', 'lozinkaLuka', 10, 'MSc', TRUE),
-- Preostalih 12 kandidata (registrovan = FALSE)
-- kandidati 13,14,15,16 su zavrsili rad sa supervizijom tako da su i oni sada postali psihoterapeuti (ali nisu registrovani)
(3009997150361, 'Jelena', 'Pavlović', STR_TO_DATE('30.09.1995', '%d.%m.%Y'), '0657654321', 'Beograd', 'jelena.pavlovic@gmail.com', NULL, 5, 'PhD', FALSE),  
(2501998050442, 'Tamara', 'Savić', STR_TO_DATE('25.01.1995', '%d.%m.%Y'), '0635566778', 'Novi Sad', 'tamara.savic@gmail.com', NULL, 7, 'BSc', FALSE),  
(1704988210483, 'Nikola', 'Ilić', STR_TO_DATE('17.04.1995', '%d.%m.%Y'), '0656677889', 'Subotica', 'nikola.ilic@yahoo.com', NULL, 1, 'MSc', FALSE),
(2112987110504, 'Vladimir', 'Radović', STR_TO_DATE('21.12.1995', '%d.%m.%Y'), '0618899001', 'Beograd', 'vladimir.radovic@gmail.com', NULL, 10, 'BSc', FALSE),
(0404993701040, 'Jelena', 'Vuković', STR_TO_DATE('04.04.1995', '%d.%m.%Y'), '0644455667', 'Kragujevac', 'jelena.vukovic@gmail.com', NULL, 2, 'PhD', FALSE),
(1505995701040, 'Nemanja', 'Nikolić', STR_TO_DATE('15.05.1995', '%d.%m.%Y'), '0655566778', 'Subotica', 'nemanja.nikolic@gmail.com', NULL, 6, 'MSc', FALSE),
(2808997701084, 'Ana', 'Janković', STR_TO_DATE('28.08.1995', '%d.%m.%Y'), '0688899001', 'Sombor', 'ana.jankovic@gmail.com', NULL, 1, 'MSc', FALSE),
(0110999701106, 'Teodora', 'Petrović', STR_TO_DATE('01.10.1995', '%d.%m.%Y'), '0600111223', 'Pirot', 'teodora.petrovic@gmail.com', NULL, 5, 'BSc', FALSE),
(2301011701128, 'Mina', 'Savić', STR_TO_DATE('23.01.1995', '%d.%m.%Y'), '0622333445', 'Kruševac', 'mina.savic@gmail.com', NULL, 6, 'PhD', FALSE),
(1402022701139, 'Uroš', 'Ristić', STR_TO_DATE('14.02.1995', '%d.%m.%Y'), '0633444556', 'Požarevac', 'uros.ristic@gmail.com', NULL, 7, 'MSc', FALSE),
(0503033701140, 'Katarina', 'Jovanović', STR_TO_DATE('05.03.1995', '%d.%m.%Y'), '0644555667', 'Pančevo', 'katarina.jovanovic@gmail.com', NULL, 8, 'BSc', FALSE),
(1604044701151, 'Miloš', 'Stanković', STR_TO_DATE('16.04.1995', '%d.%m.%Y'), '0655666778', 'Zaječar', 'milos.stankovic@gmail.com', NULL, 9, 'MSc', FALSE);


-- 1. Centar za obuku psihoterapeuta (10 redova)
INSERT INTO centar_za_obuku (naziv, email, telefon, ulica, broj, opstina) VALUES
('Centar za psihoterapiju Beograd', 'kontakt@psihobeograd.rs', '0111234567', 'Kneza Miloša', '16', 'Savski venac'),
('Psihološki centar Novi Sad', 'info@psihons.rs', '0212345678', 'Bulevar Oslobođenja', '45/1', 'Novi Sad'),
('Humanistički centar', 'kontakt@humanistika.rs', '0114567890', 'Takovska', '23', 'Palilula'),
('Centar za mentalno zdravlje', 'mentalnozdravlje@gmail.com', '0116543210', 'Nemanjina', '12/a', 'Savski venac'),
('Centar terapija Plus', 'terapijaplus@outlook.com', '+381641122334', 'Vojvode Stepe', '112', 'Voždovac'),
('Psihološki studio Sofia', 'sofia@studio.rs', '+381602233445', 'Cara Dušana', '85/1A', 'Zemun'),
('Centar za razvoj ličnosti', 'info@razvojlicnosti.rs', '+381633455566', 'Vojvode Mišića', '14', 'Savski venac'),
('Institut za psihoterapiju', 'kontakt@institutpsihoterapija.rs', '0117654321', 'Dečanska', '7', 'Stari grad'),
('Psihoterapijski centar Niš', 'psihonissupport@yahoo.com', '018123456', 'Obrenovićeva', '29/c', 'Niš'),
('Psihološko savetovalište Subotica', 'suboticapsih@gmail.com', '024987654', 'Korzo', '10/a', 'Subotica');

-- 2. Oblasti psihoterapije
INSERT INTO oblast_psihoterapije (naziv) VALUES 
('Kognitivno-bihevioralna terapija'),
('Terapija prihvatanja i posvećenosti'),
('Humanistička terapija'),
('Geštalt terapija'),
('Porodična terapija'),
('Sistemska terapija'),
('Psihodinamska terapija'),
('Mindfulness terapija'),
('Eksperimentalna terapija'),
('Somatska terapija');

-- 3. Sertifikati (12 redova) + 4 nova koji su zavrsili rad i dobili sertifikat
INSERT INTO sertifikat (datum_sertifikacije, psihoterapeut_kandidat_id, sert_oblast_id) VALUES
(STR_TO_DATE('12.05.2015', '%d.%m.%Y'), 1, 1),
(STR_TO_DATE('15.07.2017', '%d.%m.%Y'), 2, 2),
(STR_TO_DATE('01.09.2018', '%d.%m.%Y'), 3, 3),
(STR_TO_DATE('10.11.2017', '%d.%m.%Y'), 4, 4),
(STR_TO_DATE('28.01.2020', '%d.%m.%Y'), 5, 5),
(STR_TO_DATE('14.03.2016', '%d.%m.%Y'), 6, 6),
(STR_TO_DATE('22.05.2014', '%d.%m.%Y'), 7, 7),
(STR_TO_DATE('19.07.2015', '%d.%m.%Y'), 8, 8),
(STR_TO_DATE('03.10.2018', '%d.%m.%Y'), 9, 9),
(STR_TO_DATE('15.12.2019', '%d.%m.%Y'), 10, 10),
(STR_TO_DATE('25.02.2018', '%d.%m.%Y'), 11, 1),
(STR_TO_DATE('12.04.2020', '%d.%m.%Y'), 12, 2);


INSERT INTO rad_pod_supervizijom (datum_pocetka, datum_zavrsetka, opis, psihoterapeut_id, kandidat_id, centar_id) VALUES
(STR_TO_DATE('01.02.2017', '%d.%m.%Y'), STR_TO_DATE('15.01.2021', '%d.%m.%Y'), 'Posttraumatski stres', 1, 13, 3),
(STR_TO_DATE('01.03.2018', '%d.%m.%Y'), STR_TO_DATE('22.01.2022', '%d.%m.%Y'), 'Socijalna anksioznost', 2, 14, 4),
(STR_TO_DATE('01.04.2019', '%d.%m.%Y'), STR_TO_DATE('29.01.2023', '%d.%m.%Y'), 'Samopouzdanje kod mladih', 3, 15, 5),
(STR_TO_DATE('01.12.2017', '%d.%m.%Y'), STR_TO_DATE('05.12.2021', '%d.%m.%Y'), 'Rad sa stresom', 4, 16, 6),
(STR_TO_DATE('01.06.2023', '%d.%m.%Y'), NULL, 'Anksioznost i njeno prevazilaženje', 5, 17, 7),
(STR_TO_DATE('01.07.2024', '%d.%m.%Y'), NULL, 'Ispitna anksioznost', 6, 18, 8),
(STR_TO_DATE('01.08.2022', '%d.%m.%Y'), NULL, 'Poremećaji pažnje', 7, 19, 9),
(STR_TO_DATE('01.09.2022', '%d.%m.%Y'), NULL, 'Problemi sa spavanjem', 8, 20, 10),
(STR_TO_DATE('01.10.2023', '%d.%m.%Y'), NULL, 'Ljubavni izazovi kod mladih', 9, 21, 2),
(STR_TO_DATE('01.12.2021', '%d.%m.%Y'), NULL, 'Traume iz detinjstva', 10, 22, 4),
(STR_TO_DATE('09.12.2021', '%d.%m.%Y'), NULL, 'Rani znaci depresije', 11, 23, 6),
(STR_TO_DATE('01.02.2022', '%d.%m.%Y'), NULL, 'Psihodinamski pristupi', 1, 24, 10);

INSERT INTO pol (pol_id, pol) VALUES
('M', 'Muško'),
('Ž', 'Žensko');

INSERT INTO klijent (ime, prezime, datum_rodjenja, email, telefon, prvi_put, opis_problema, pol_pol_id) VALUES
('Anja', 'Stanković', STR_TO_DATE('09.05.2004', '%d.%m.%Y'), 'anja.stankovic@gmail.com', '0641234567', true, 'Poremećaj pažnje', 'Ž'),
('Katarina', 'Gordić', STR_TO_DATE('12.05.2004', '%d.%m.%Y'), 'katarina.gordic@gmail.com', '0619876543', true, 'Ljubavni problemi', 'Ž'),
('Maja', 'Stanković', STR_TO_DATE('04.12.1998', '%d.%m.%Y'), 'maja.stankovic@yahoo.com', '0622345678', true, 'Problemi sa spavanjem', 'Ž'),
('Aleksandar', 'Jovanović', STR_TO_DATE('21.09.1992', '%d.%m.%Y'), 'aleksandar.jovanovic@hotmail.com', '0633456789', false, 'Problemi u vezi', 'M'),
('Milica', 'Đukić', STR_TO_DATE('11.11.1996', '%d.%m.%Y'), 'milica.djukić@gmail.com', '0654567890', true, 'Posttraumatski stres', 'Ž'),
('Lazar', 'Pavlović', STR_TO_DATE('30.05.1994', '%d.%m.%Y'), 'lazar.pavlovic@gmail.com', '0645678901', true, 'Socijalna anksioznost', 'M'),
('Teodora', 'Simić', STR_TO_DATE('14.06.1999', '%d.%m.%Y'), 'teodora.simic@gmail.com', '0611239874', false, 'Problemi sa samopouzdanjem', 'Ž'),
('Nikola', 'Milošević', STR_TO_DATE('08.01.1991', '%d.%m.%Y'), 'nikola.milosevic@gmail.com', '0639876543', true, 'Problemi sa stresom', 'M'),
('Nina', 'Ristić', STR_TO_DATE('31.05.2005', '%d.%m.%Y'), 'nina.ristic@gmail.com', '0623457890', true, 'Anksioznost', 'Ž'),
('Stefan', 'Lukić', STR_TO_DATE('03.07.1993', '%d.%m.%Y'), 'stefan.lukic@gmail.com', '0656789012', false, 'Anksioznost pred ispite', 'M');

INSERT INTO oblast_testa (naziv) VALUES 
('Test ličnosti'),
('Test emocionalne inteligencije'),
('Skala depresije'),
('Test anksioznosti'),
('Kognitivne sposobnosti test'),
('Test pažnje i koncentracije'),
('Test profesionalne orijentacije'),
('Skala stresa'),
('Test verbalne sposobnosti'),
('Test socijalne inteligencije');

INSERT INTO test (naziv, cena, test_oblast_id) VALUES
('Test ličnosti NEO-PI-R', 3500.00, 1),
('Skala depresije BDI-II', 2500.00, 3),
('Test anksioznosti STAI', 2600.00, 4),
('Test emocionalne inteligencije', 4000.00, 2),
('Test kognitivnih sposobnosti', 4500.00, 5),
('Test pažnje i koncentracije', 3000.00, 6),
('Profesionalna orijentacija', 3200.00, 7),
('Skala stresa PSS', 2700.00, 8),
('Test verbalne sposobnosti', 2800.00, 9),
('Test socijalne inteligencije', 3900.00, 10);

INSERT INTO cena_seanse (cena, datum_od) VALUES
(3000.00, STR_TO_DATE('01.01.2021', '%d.%m.%Y %H:%i')),
(3200.00, STR_TO_DATE('01.06.2021', '%d.%m.%Y %H:%i')),
(3500.00, STR_TO_DATE('01.01.2022', '%d.%m.%Y %H:%i')),
(3700.00, STR_TO_DATE('01.09.2022', '%d.%m.%Y %H:%i')),
(4000.00, STR_TO_DATE('01.01.2023', '%d.%m.%Y %H:%i')),
(4200.00, STR_TO_DATE('01.07.2023', '%d.%m.%Y %H:%i')),
(4500.00, STR_TO_DATE('01.01.2024', '%d.%m.%Y %H:%i')),
(4700.00, STR_TO_DATE('01.06.2024', '%d.%m.%Y %H:%i')),
(5000.00, STR_TO_DATE('01.01.2025', '%d.%m.%Y %H:%i')),
(5200.00, STR_TO_DATE('01.04.2025', '%d.%m.%Y %H:%i'));

INSERT INTO nacin_placanja (nacin_id, naziv) VALUES
('G', 'Gotovina'),
('K', 'Platna kartica');


INSERT INTO seansa (dan, vreme, beleske, trajanje, placanje, zlocin, na_sudu, klijent_id, cena_id, psihoterapeut_kandidat_id, odrzana) VALUES

-- Anja Stanković (1 seansa) - 2021
(STR_TO_DATE('15.01.2021', '%d.%m.%Y'), '10:00:00', 'Prva konsultacija – Poremećaj pažnje', 1.00, true, false, false, 1, 8, 1, true), -- test 

-- Katarina Gordić (4 seanse) - 2021
(STR_TO_DATE('05.06.2021', '%d.%m.%Y'), '12:30:00', 'Prva konsultacija – Ljubavni problemi', 1.00, true, true, false, 2, 8, 2, true),
(STR_TO_DATE('12.06.2021', '%d.%m.%Y'), '13:00:00', 'Priznala nasilje nad partnerom - Rad na emocionalnoj stabilnosti', 1.00, true, false, true, 2, 8, 2, true), 
(STR_TO_DATE('19.06.2021', '%d.%m.%Y'), '14:00:00', 'Komunikacione veštine', 1.00, true, false, true, 2, 8, 2, true), 
(STR_TO_DATE('26.06.2021', '%d.%m.%Y'), '15:00:00', 'Samokontrola', 1.00, true, false, false, 2, 8, 2, true), 

-- Maja Stanković (3 seanse) - 2021
(STR_TO_DATE('01.02.2021', '%d.%m.%Y'), '09:00:00', 'Prva konsultacija – Problemi sa spavanjem - Sumnja na anksioznost, uradila test', 1.00, true, false, false, 3, 9, 3, true), -- ovde imas radjen test
(STR_TO_DATE('08.02.2021', '%d.%m.%Y'), '09:30:00', 'Tehnike za lakše uspavljivanje', 1.00, true, false, false, 3, 9, 3, true), -- test
(STR_TO_DATE('15.02.2021', '%d.%m.%Y'), '10:00:00', 'Rad na stresu i spavanju', 1.00, true, false, false, 3, 9, 3, true),  

-- Aleksandar Jovanović (5 seansi) - 2021
(STR_TO_DATE('01.03.2021', '%d.%m.%Y'), '10:00:00', 'Prva konsultacija – Problemi u vezi', 1.00, true, false, false, 4, 9, 4, true),
(STR_TO_DATE('08.03.2021', '%d.%m.%Y'), '11:00:00', 'Rad na partnerskim odnosima', 1.00, true, false, false, 4, 9, 4, true),
(STR_TO_DATE('15.03.2021', '%d.%m.%Y'), '12:00:00', 'Tehnike rešavanja konflikata', 1.00, true, false, false, 4, 9, 4, true),
(STR_TO_DATE('22.03.2021', '%d.%m.%Y'), '13:00:00', 'Komunikacija u paru', 1.00, true, false, false, 4, 9, 4, true),
(STR_TO_DATE('29.03.2021', '%d.%m.%Y'), '14:00:00', 'Zajedničko planiranje', 1.00, true, false, false, 4, 9, 4, true),

-- Milica Đukić (14 seansi) - 2022
(STR_TO_DATE('10.05.2022', '%d.%m.%Y'), '15:00:00', 'Prva konsultacija – Posttraumatski stres', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('17.05.2022', '%d.%m.%Y'), '10:30:00', 'Rad na traumatskim iskustvima', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('24.05.2022', '%d.%m.%Y'), '11:00:00', 'Tehnike smanjenja PTSP simptoma', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('31.05.2022', '%d.%m.%Y'), '12:00:00', 'Kognitivna restrukturacija', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('07.06.2022', '%d.%m.%Y'), '13:00:00', 'Vežbe opuštanja', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('14.06.2022', '%d.%m.%Y'), '14:00:00', 'Rad na samopouzdanju', 1.00, true, false, false, 5, 9, 1, true), -- test emocionalne inteligencije
(STR_TO_DATE('21.06.2022', '%d.%m.%Y'), '15:00:00', 'Integracija iskustava', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('28.06.2022', '%d.%m.%Y'), '09:30:00', 'Prevencija relapsa', 1.00, true, false, false, 5, 9, 1, true), -- skala stresa 
(STR_TO_DATE('05.07.2022', '%d.%m.%Y'), '10:30:00', 'Samopomoćne strategije', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('12.07.2022', '%d.%m.%Y'), '11:30:00', 'Evaluacija napretka', 1.00, true, false, false, 5, 9, 1, true), -- test emocionalne inteligencije 
(STR_TO_DATE('19.07.2022', '%d.%m.%Y'), '12:30:00', 'Završna sesija', 1.00, true, false, false, 5, 9, 1, true),
(STR_TO_DATE('01.08.2022', '%d.%m.%Y'), '10:00:00', 'Seansa kod kandidata', 1.00, true, false, false, 5, 9, 13, true),
(STR_TO_DATE('08.08.2022', '%d.%m.%Y'), '11:00:00', 'Nastavak rada na traumama', 1.00, true, false, false, 5, 9, 13, true), 
(STR_TO_DATE('15.08.2022', '%d.%m.%Y'), '12:00:00', 'Kandidat radi vežbe opuštanja', 1.00, true, false, false, 5, 9, 13, true),

-- Lazar Pavlović (6 seanse) - 2023
(STR_TO_DATE('01.06.2023', '%d.%m.%Y'), '09:30:00', 'Prva konsultacija – Socijalna anksioznost - Skala anksioznosti', 1.00, true, false, false, 6, 9, 6, true), -- test
(STR_TO_DATE('08.06.2023', '%d.%m.%Y'), '10:00:00', 'Rad na socijalnim veštinama', 1.00, true, false, false, 6, 9, 6, true), -- test verbalne spsoobnosyi
(STR_TO_DATE('15.06.2023', '%d.%m.%Y'), '11:00:00', 'Vežbe izlaganja', 1.00, true, false, false, 6, 9, 6, true),
(STR_TO_DATE('22.06.2023', '%d.%m.%Y'), '12:00:00', 'Samopouzdanje u grupi', 1.00, true, false, false, 6, 9, 6, true), -- test socijalne inteligencije 
(STR_TO_DATE('22.08.2023', '%d.%m.%Y'), '10:30:00', 'Seansa sa kandidatkinjom Milicom', 1.00, true, false, false, 6, 9, 14, true),
(STR_TO_DATE('29.08.2023', '%d.%m.%Y'), '11:30:00', 'Rad na anksioznosti kod kandidata', 1.00, true, false, false, 6, 9, 14, true),

-- Teodora Simić (5 seanse) - 2023
(STR_TO_DATE('01.07.2023', '%d.%m.%Y'), '11:00:00', 'Prva konsultacija – Problemi sa samopouzdanjem', 1.00, true, false, false, 7, 9, 7, true),
(STR_TO_DATE('08.07.2023', '%d.%m.%Y'), '12:00:00', 'Jačanje samopoštovanja', 1.00, true, false, false, 7, 9, 7, true),
(STR_TO_DATE('15.07.2023', '%d.%m.%Y'), '13:00:00', 'Postavljanje ciljeva', 1.00, true, false, false, 7, 9, 7, true),
(STR_TO_DATE('05.09.2023', '%d.%m.%Y'), '12:30:00', 'Ispitna trema – kandidat pod nadzorom', 1.00, true, false, false, 7, 10, 15, true),
(STR_TO_DATE('12.09.2023', '%d.%m.%Y'), '10:00:00', 'Druga seansa – kandidat i klijent', 1.00, true, false, false, 7, 10, 15, true),

-- Nikola Milošević (7 seansi) - 2024
(STR_TO_DATE('01.08.2024', '%d.%m.%Y'), '14:00:00', 'Prva konsultacija – Problemi sa stresom - 	Priznao krađu poslodavcu', 1.00, true, false, true, 8, 10, 8, true), -- priznao kradju kod poslodavca
(STR_TO_DATE('08.08.2024', '%d.%m.%Y'), '15:00:00', 'Tehnike kontrole stresa', 1.00, true, false, false, 8, 10, 8, true),
(STR_TO_DATE('15.08.2024', '%d.%m.%Y'), '09:30:00', 'Mindfulness vežbe', 1.00, true, false, false, 8, 10, 8, true),
(STR_TO_DATE('22.08.2024', '%d.%m.%Y'), '10:30:00', 'Kognitivno restrukturiranje', 1.00, true, true, false, 8, 10, 8, true),
(STR_TO_DATE('29.08.2024', '%d.%m.%Y'), '11:30:00', 'Evaluacija napretka', 1.00, true, false, false, 8, 10, 8, true),
(STR_TO_DATE('19.09.2024', '%d.%m.%Y'), '09:30:00', 'Seansa sa fokusom na pažnju', 1.00, true, false, false, 8, 10, 16, true),  -- test
(STR_TO_DATE('26.09.2024', '%d.%m.%Y'), '11:00:00', 'Problemi sa spavanjem – nastavak', 1.00, true, false, false, 8, 10, 16, true),

-- Nina Ristić (4 seanse) - 2025
(STR_TO_DATE('01.04.2025', '%d.%m.%Y'), '10:00:00', 'Prva konsultacija – Anksioznost', 1.00, true, false, false, 9, 10, 9, true),
(STR_TO_DATE('08.04.2025', '%d.%m.%Y'), '11:00:00', 'Rad na kontroli anksioznosti', 1.00, true, false, false, 9, 10, 9, true),
(STR_TO_DATE('21.04.2025', '%d.%m.%Y'), '12:00:00', 'Ljubavni izazovi u adolescenciji', 1.00, false, false, false, 9, 10, 9, true),
(STR_TO_DATE('28.04.2025', '%d.%m.%Y'), '13:00:00', 'Završna faza rada sa kandidatom', 1.00, false, false, false, 9, 10, 9, true),

-- Stefan Lukić (4 seanse) - 2025
(STR_TO_DATE('01.02.2025', '%d.%m.%Y'), '09:30:00', 'Prva konsultacija – Anksioznost pred ispit - 	Sudski proces zbog lažnog svedočenja na drugom suđenju', 1.00, true, false, true, 10, 10, 10, true),
-- 2 zakazane seanse
(STR_TO_DATE('08.06.2025', '%d.%m.%Y'), '10:30:00', NULL, NULL, false, false, false, 10, NULL, 10, false),
(STR_TO_DATE('15.06.2025', '%d.%m.%Y'), '11:30:00', NULL, NULL, NULL, false, false, 10, NULL, 10, false),

-- Anja Sankovic zakazana kod Marka Markovica
(STR_TO_DATE('01.06.2025', '%d.%m.%Y'), '10:30:00', NULL, NULL, false, false, false, 1, NULL, 1, false),
-- Katarina Gordic zakazana kod Marka Markovica
(STR_TO_DATE('08.06.2025', '%d.%m.%Y'), '10:30:00', NULL, NULL, false, false, false, 2, NULL, 1, false);


INSERT INTO rezultat_testiranja (rezultat, test_id, seansa_id) VALUES
('Granične vrednosti na skali pažnje', 6, 1), -- Anja Stanković
('Visok nivo stresa', 8, 6), -- Maja Stanković
('Blaga depresija', 2, 7), -- Maja Stanković
('Prosečna emocionalna inteligencija', 4, 19), -- Milica Đukić
('Srednji nivo stresa', 8, 21), -- Milica Đukić
('Izražena emocionalna inteligencija', 4, 23), -- Milica Đukić
('Visoka anksioznost', 3, 28), -- Lazar Pavlović
('Prosek za verbalne sposobnosti', 9, 29), -- Lazar Pavlović
('Visoka socijalna inteligencija', 10, 31), -- Lazar Pavlović
('Prosek za pažnju', 6, 44); -- Nikola Milosevic 


INSERT INTO objava_seanse (seansa_id, datum_objave, primalac, razlog) VALUES
-- seansa_id = 2 (Katarina Gordić)
(2, STR_TO_DATE('13.06.2021', '%d.%m.%Y'), 'Policija', 'Klijentkinja je tokom seanse priznala nasilje nad partnerom.'),
(2, STR_TO_DATE('16.06.2021', '%d.%m.%Y'), 'Centar za socijalni rad', 'Seansa prosleđena nadležnim službama u okviru zaštite porodice.'),
-- seansa_id = 3 i 4 (Katarina Gordić - sud)
(3, STR_TO_DATE('20.06.2021', '%d.%m.%Y'), 'Sud', 'Klijentkinja se nalazi na sudskom saslušanju, podaci su relevantni za postupak.'),
(4, STR_TO_DATE('27.06.2021', '%d.%m.%Y'), 'Sud', 'Nastavak sudskog procesa, dopuna dokumentacije.'),
-- seansa_id = 12 (Aleksandar Jovanović)
(12, STR_TO_DATE('29.03.2021', '%d.%m.%Y'), 'Klijent: Aleksandar Jovanović', 'Lični uvid u završnu seansu iz partnerske terapije.'),
-- seansa_id = 22 (Milica Djukić)
(22, STR_TO_DATE('22.06.2023', '%d.%m.%Y'), 'Školski psiholog', 'Napredak u prevazilaženju socijalne anksioznosti.'),
-- seansa_id = 28 (Lazar Pavlović)
(28, STR_TO_DATE('05.09.2023', '%d.%m.%Y'), 'Mentor psihoterapeuta', 'Seansa pod supervizijom – procena kandidata.'),
-- seansa_id = 39 (Nikola Milošević)
(39, STR_TO_DATE('01.08.2024', '%d.%m.%Y'), 'Sud', 'Priznao krađu poslodavcu.'),
-- seansa_id = 40 (Nikola Milošević)
(40, STR_TO_DATE('08.08.2024', '%d.%m.%Y'), 'Poslodavac', 'Nastavak rada na stresu usled posledica incidenta na poslu.'),
-- seansa_id = 41 (Nikola Milošević)
(41, STR_TO_DATE('15.08.2024', '%d.%m.%Y'), 'Porodica', 'Pomoć u stabilizaciji klijenta kod kuće nakon traumatskih situacija.'),
-- seansa_id = 42 (Nikola Milošević)
(42, STR_TO_DATE('22.08.2024', '%d.%m.%Y'), 'Policija', 'Razmatrana prijava klijenta o opasnosti u okruženju.'),
-- seansa_id = 43 (Nikola Milošević)
(43, STR_TO_DATE('29.08.2024', '%d.%m.%Y'), 'Centar za socijalni rad', 'Evaluacija napretka u funkcionalnom ponašanju.'),
-- seansa_id = 50 (Stefan Lukić)
(50, STR_TO_DATE('01.02.2025', '%d.%m.%Y'), 'Sud', 'Sudski proces zbog lažnog svedočenja, izveštaj sa seanse.');


INSERT INTO valuta (valuta_id, naziv) VALUES
('RSD', 'Srpski dinar'),
('EUR', 'Evro'),
('USD', 'Američki dolar'),
('CHF', 'Švajcarski franak'),
('GBP', 'Britanska funta'),
('AUD', 'Australijski dolar'),
('CAD', 'Kanadski dolar'),
('JPY', 'Japanski jen'),
('SEK', 'Švedska kruna'),
('CZK', 'Češka kruna');

INSERT INTO kurs (datum, kurs_prema_dinaru, valuta_id) VALUES
-- EUR
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 117.80, 'EUR'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 118.00, 'EUR'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 117.40, 'EUR'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 117.90, 'EUR'),

-- USD
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 99.20, 'USD'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 101.00, 'USD'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 97.50, 'USD'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 95.80, 'USD'),

-- CHF
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 108.00, 'CHF'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 109.50, 'CHF'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 107.20, 'CHF'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 108.30, 'CHF'),

-- GBP
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 135.80, 'GBP'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 133.20, 'GBP'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 136.50, 'GBP'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 134.00, 'GBP'),

-- AUD
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 74.80, 'AUD'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 75.30, 'AUD'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 77.00, 'AUD'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 76.50, 'AUD'),

-- CAD
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 79.50, 'CAD'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 80.70, 'CAD'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 81.10, 'CAD'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 80.30, 'CAD'),

-- JPY
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 0.96, 'JPY'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 0.95, 'JPY'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 0.93, 'JPY'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 0.92, 'JPY'),

-- SEK
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 10.90, 'SEK'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 11.10, 'SEK'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 10.80, 'SEK'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 11.00, 'SEK'),

-- CZK
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 4.65, 'CZK'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 4.75, 'CZK'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 4.60, 'CZK'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 4.70, 'CZK'),

-- RSD (uvek 1.00)
(STR_TO_DATE('01.01.2022 09:00', '%d.%m.%Y %H:%i'), 1.00, 'RSD'),
(STR_TO_DATE('01.01.2023 09:00', '%d.%m.%Y %H:%i'), 1.00, 'RSD'),
(STR_TO_DATE('01.01.2024 09:00', '%d.%m.%Y %H:%i'), 1.00, 'RSD'),
(STR_TO_DATE('01.01.2025 09:00', '%d.%m.%Y %H:%i'), 1.00, 'RSD');


-- promeni negde valute, provizije i nacin placanja

INSERT INTO placanje (datum_uplate, iznos, svrha, napomena, rata, provizija_5_posto, seansa_id, valuta_id, nacin_id) VALUES

-- Seansa 1 - Anja Stanković (plaćanje seanse + test)
('2021-01-15', 1500.00, 'SEANSA - PRVA RATA', NULL, 1, false, 1, 'RSD', 'G'),
('2021-01-20', 1500.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 1, 'RSD', 'G'),
-- nije platila test
-- ('2021-01-15', 3000.00, 'TEST', 'Test pažnje i koncentracije', NULL, false, 1, 'RSD', 'K'),

-- Seansa 2 - Katarina Gordić
('2021-06-05', 3200.00, 'SEANSA - CELA', NULL, NULL, false, 2, 'RSD', 'G'),
('2021-06-12', 3200.00, 'SEANSA - CELA', NULL, NULL, false, 3, 'RSD', 'G'),
('2021-06-19', 32.00, 'SEANSA - CELA', NULL, NULL, false, 4, 'EUR', 'G'),
('2021-06-26', 3200.00, 'SEANSA - CELA', NULL, NULL, false, 5, 'RSD', 'G'),

-- Maja Stanković
('2021-02-01', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 6, 'RSD', 'K'),
('2021-02-01', 2700.00, 'TEST', 'Skala stresa PSS', NULL, false, 6, 'RSD', 'K'),
('2021-02-08', 30.00, 'SEANSA - CELA', NULL, NULL, true, 7, 'USD', 'G'),
('2021-02-08', 2500.00, 'TEST', 'Skala depresije BDI-II', NULL, false, 7, 'RSD', 'G'),
-- ('2021-02-15', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 8, 'RSD', 'K'),

-- Aleksandar Jovanović
('2021-03-01', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 9, 'RSD', 'G'),
('2021-03-08', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 10, 'RSD', 'G'),
('2021-03-15', 30.00, 'SEANSA - CELA', NULL, NULL, false, 11, 'EUR', 'G'),
('2021-03-22', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 12, 'RSD', 'K'),
('2021-03-29', 3000.00, 'SEANSA - CELA', NULL, NULL, false, 13, 'RSD', 'K'),


-- Milica Đukić
-- zakomentarisane uplate nedostaju - prikazace se u dugovanjima
('2022-05-10', 2000.00, 'SEANSA - PRVA RATA', NULL, 1, false, 14, 'RSD', 'K'),
-- ('2022-05-12', 1500.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 14, 'RSD', 'K'),
('2022-05-17', 35.00, 'SEANSA - CELA', NULL, NULL, false, 15, 'EUR', 'G'),
('2022-05-24', 33.00, 'SEANSA - CELA', NULL, NULL, true, 16, 'USD', 'G'),
('2022-05-31', 3500.00, 'SEANSA - CELA', NULL, NULL, false, 17, 'RSD', 'K'),
-- ('2022-06-07', 3500.00, 'SEANSA - CELA', NULL, NULL, false, 18, 'RSD', 'G'),
('2022-06-14', 2000.00, 'SEANSA - PRVA RATA', NULL, 1, false, 19, 'RSD', 'G'),
('2022-06-15', 1500.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 19, 'RSD', 'G'),
('2022-06-14', 4000.00, 'TEST', 'Test emocionalne inteligencije', NULL, false, 19, 'RSD', 'G'),
('2022-06-21', 32.00, 'SEANSA - CELA', NULL, NULL, true, 20, 'GBP', 'G'),
('2022-06-28', 3500.00, 'SEANSA - CELA', NULL, NULL, false, 21, 'RSD', 'G'),
('2022-06-28', 2700.00, 'TEST', 'Skala stresa PSS', NULL, false, 21, 'RSD', 'K'),
('2022-07-05', 35.00, 'SEANSA - CELA', NULL, NULL, false, 22, 'EUR', 'G'),
('2022-07-12', 3500.00, 'SEANSA - CELA', NULL, NULL, false, 23, 'RSD', 'G'),
-- ('2022-07-12', 4000.00, 'TEST', 'Test emocionalne inteligencije', NULL, false, 23, 'RSD', 'G'),
('2022-07-19', 3500.00, 'SEANSA - CELA', NULL, NULL, false, 24, 'RSD', 'G'),
('2022-08-01', 1800.00, 'SEANSA - PRVA RATA', NULL, 1, false, 25, 'RSD', 'K'),
('2022-08-02', 1700.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 25, 'RSD', 'K'),
('2022-08-08', 35.00, 'SEANSA - CELA', NULL, NULL, true, 26, 'CHF', 'G'),
('2022-08-15', 2000.00, 'SEANSA - PRVA RATA', NULL, 1, false, 27, 'RSD', 'G'),
('2022-08-20', 1500.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 27, 'RSD', 'G'),

-- Lazar Pavlović
('2023-06-01', 37.00, 'SEANSA - CELA', NULL, NULL, true, 28, 'USD', 'G'),
('2023-06-01', 2600.00, 'TEST', 'Test anksioznosti STAI', NULL, false, 28, 'RSD', 'G'),
('2023-06-08', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 29, 'RSD', 'G'),
('2023-06-08', 2800.00, 'TEST', 'Test verbalne sposobnosti', NULL, false, 29, 'RSD', 'G'),
('2023-06-15', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 30, 'RSD', 'G'),
('2023-06-22', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 31, 'RSD', 'G'),
('2023-06-22', 3900.00, 'TEST', 'Test socijalne inteligencije', NULL, false, 31, 'RSD', 'G'),
('2023-08-22', 37.00, 'SEANSA - CELA', NULL, NULL, false, 32, 'EUR', 'K'),
('2023-08-29', 2700.00, 'SEANSA - PRVA RATA', NULL, 1, false, 33, 'RSD', 'K'),
-- ('2023-08-29', 1000.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 33, 'RSD', 'K'),

-- Teodora Simić
('2023-07-01', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 34, 'RSD', 'K'),
('2023-07-08', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 35, 'RSD', 'K'),
('2023-07-15', 3700.00, 'SEANSA - CELA', NULL, NULL, false, 36, 'RSD', 'K'),
('2023-09-05', 4000.00, 'SEANSA - CELA', NULL, NULL, false, 37, 'RSD', 'K'),
-- ('2023-09-12', 4000.00, 'SEANSA - CELA', NULL, NULL, false, 38, 'RSD', 'K'),

-- Nikola Milošević
('2024-08-01', 2500.00, 'SEANSA - PRVA RATA', NULL, 1, false, 39, 'RSD', 'G'),
('2024-08-05', 2000.00, 'SEANSA - DRUGA RATA', NULL, 2, false, 39, 'RSD', 'G'),
('2024-08-08', 4500.00, 'SEANSA - CELA', NULL, NULL, false, 40, 'RSD', 'G'),
('2024-08-15', 45.00, 'SEANSA - CELA', NULL, NULL, true, 41, 'CHF', 'G'),
('2024-08-22', 4500.00, 'SEANSA - CELA', NULL, NULL, false, 42, 'RSD', 'K'),
('2024-08-29', 4500.00, 'SEANSA - CELA', NULL, NULL, false, 43, 'RSD', 'K'),
('2024-09-19', 4500.00, 'SEANSA - CELA', NULL, NULL, false, 44, 'RSD', 'K'),
('2024-09-19', 272.00, 'TEST', 'Test pažnje i koncentracije ', NULL, true, 44, 'SEK', 'K'),
-- ('2024-09-26', 4500.00, 'SEANSA - CELA', NULL, NULL, false, 45, 'RSD', 'G'),

-- Nina Ristić
('2025-04-01', 5000.00, 'SEANSA - CELA', NULL, NULL, false, 46, 'RSD', 'G'),
('2025-04-08', 5000.00, 'SEANSA - CELA', NULL, NULL, false, 47, 'RSD', 'G'),
-- nije platila 2 seanse

-- Stefan Lukić
('2025-02-01', 5000.00, 'SEANSA - CELA', NULL, NULL, false, 50, 'RSD', 'G');


DROP PROCEDURE IF EXISTS registruj_kandidata;
DROP PROCEDURE IF EXISTS get_sertifikovani_psihoterapeuti;
DROP PROCEDURE IF EXISTS get_kandidati_na_superviziji;
DROP PROCEDURE IF EXISTS dodaj_sertifikat;
DROP PROCEDURE IF EXISTS postavi_registrovan;
DROP PROCEDURE IF EXISTS obrisi_kandidata_po_emailu;
DROP PROCEDURE IF EXISTS proveri_kandidata_sa_zavrsenim_radom;
DROP PROCEDURE IF EXISTS zakazi_seansu;
DROP PROCEDURE IF EXISTS oznaci_seansu_kao_odrzanu;
DROP PROCEDURE IF EXISTS dodaj_klijenta;
DROP PROCEDURE IF EXISTS dodaj_seansu;
DROP PROCEDURE IF EXISTS vrati_testove_za_seansu;
DROP PROCEDURE IF EXISTS objavi_seansu;
DROP PROCEDURE IF EXISTS dodaj_rezultat_za_test;
DROP PROCEDURE IF EXISTS unesi_placanja_za_celu_seansu;
DROP PROCEDURE IF EXISTS unesi_placanje_testa;

DELIMITER $$
CREATE PROCEDURE registruj_kandidata (
    IN p_jmbg BIGINT,
    IN p_ime VARCHAR(50),
    IN p_prezime VARCHAR(50),
    IN p_datum_rodjenja DATE,
    IN p_telefon VARCHAR(20),
    IN p_prebivaliste VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_lozinka VARCHAR(100),
    IN p_fakultet_id INT,
    IN p_stepen_id VARCHAR(10)
)
BEGIN
    DECLARE broj INT;

    SELECT COUNT(*) INTO broj
    FROM psihoterapeut_kandidat
    WHERE jmbg = p_jmbg;

    IF broj = 0 THEN
        INSERT INTO psihoterapeut_kandidat (
            jmbg, ime, prezime, datum_rodjenja, telefon,
            prebivaliste, email, lozinka, fakultet_id,
            stepen_id, registrovan
        )
        VALUES (
            p_jmbg, p_ime, p_prezime, p_datum_rodjenja, p_telefon,
            p_prebivaliste, p_email, p_lozinka, p_fakultet_id,
            p_stepen_id, TRUE
        );
    ELSE
        UPDATE psihoterapeut_kandidat
        SET
            ime = p_ime,
            prezime = p_prezime,
            datum_rodjenja = p_datum_rodjenja,
            telefon = p_telefon,
            prebivaliste = p_prebivaliste,
            email = p_email,
            lozinka = p_lozinka,
            fakultet_id = p_fakultet_id,
            stepen_id = p_stepen_id,
            registrovan = TRUE
        WHERE jmbg = p_jmbg;
    END IF;
END$$
DELIMITER ;


-- 1. Prikaz svih sertifikovanih psihoterapeuta (imaju red u sertifikat)
DELIMITER //
CREATE PROCEDURE get_sertifikovani_psihoterapeuti()
BEGIN
    SELECT pk.*,
           s.datum_sertifikacije,
           op.naziv AS oblast_terapije
    FROM psihoterapeut_kandidat pk
    JOIN sertifikat s ON pk.psihoterapeut_id = s.psihoterapeut_kandidat_id
    JOIN oblast_psihoterapije op ON s.sert_oblast_id = op.sert_oblast_id;
END //
DELIMITER ;

-- 2. Prikaz kandidata koji su trenutno na superviziji (nemaju datum_zavrsetka)
DELIMITER //
CREATE PROCEDURE get_kandidati_na_superviziji()
BEGIN
    SELECT pk.*,
           rps.datum_pocetka,
           rps.opis,
           czo.naziv AS centar_obuke
    FROM rad_pod_supervizijom rps
    JOIN psihoterapeut_kandidat pk ON rps.kandidat_id = pk.psihoterapeut_id
    JOIN centar_za_obuku czo ON rps.centar_id = czo.centar_id
    WHERE rps.datum_zavrsetka IS NULL;
END //
DELIMITER ;

-- 3. Dodavanje novog sertifikata (nakon završetka supervizije)
DELIMITER //
CREATE PROCEDURE dodaj_sertifikat (
    IN p_psihoterapeut_id INT,
    IN p_oblast_id INT,
    IN p_datum_sert DATE
)
BEGIN
    INSERT INTO sertifikat (datum_sertifikacije, psihoterapeut_kandidat_id, sert_oblast_id)
    VALUES (p_datum_sert, p_psihoterapeut_id, p_oblast_id);
END//

DELIMITER ;

-- 4. Postavljanje da je korisnik registrovan
DELIMITER //
CREATE PROCEDURE postavi_registrovan(
    IN p_jmbg VARCHAR(13)
)
BEGIN
    UPDATE psihoterapeut_kandidat
    SET registrovan = TRUE
    WHERE jmbg = p_jmbg;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE obrisi_kandidata_po_emailu(IN p_email VARCHAR(90))
BEGIN
    DELETE FROM psihoterapeut_kandidat WHERE email = p_email;
END;
//
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE proveri_kandidata_sa_zavrsenim_radom (
    IN jmbg_unos BIGINT
)
BEGIN
    SELECT pk.*
    FROM psihoterapeut_kandidat pk
    JOIN rad_pod_supervizijom r ON pk.psihoterapeut_id = r.kandidat_id
    WHERE pk.jmbg = jmbg_unos
      AND r.datum_zavrsetka IS NOT NULL;
END $$

DELIMITER ;


DELIMITER $$
CREATE PROCEDURE zakazi_seansu(
    IN p_dan DATE,
    IN p_vreme TIME,
    IN p_klijent_id INT,
    IN p_psihoterapeut_id INT
)
BEGIN
    INSERT INTO seansa (dan, vreme, klijent_id, psihoterapeut_kandidat_id, odrzana)
    VALUES (p_dan, p_vreme, p_klijent_id, p_psihoterapeut_id, FALSE);
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE oznaci_seansu_kao_odrzanu(
    IN p_seansa_id INT,
    IN p_beleske TEXT,
    IN p_trajanje DECIMAL(5,2),
    IN p_placanje BOOLEAN,
    IN p_cena_id INT
)
BEGIN
    UPDATE seansa
    SET
        beleske = p_beleske,
        trajanje = p_trajanje,
        placanje = p_placanje,
        cena_id = p_cena_id,
        odrzana = TRUE
    WHERE seansa_id = p_seansa_id;
END$$
DELIMITER ;



DELIMITER $$
CREATE PROCEDURE dodaj_klijenta(
    IN p_ime NVARCHAR(20),
    IN p_prezime NVARCHAR(20),
    IN p_datum DATE,
    IN p_email VARCHAR(90),
    IN p_telefon VARCHAR(20),
    IN p_prvi_put BOOLEAN,
    IN p_opis LONGTEXT,
    IN p_pol CHAR(1)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    INSERT INTO klijent (
        ime, prezime, datum_rodjenja, email, telefon, prvi_put, opis_problema, pol_pol_id
    ) VALUES (
        p_ime, p_prezime, p_datum, p_email, p_telefon, p_prvi_put, p_opis, p_pol
    );

    COMMIT;
END$$
DELIMITER ;



DELIMITER $$
-- procedura koja radi
CREATE PROCEDURE dodaj_seansu (
    IN p_dan DATE,
    IN p_vreme TIME,
    IN p_trajanje DECIMAL(5,2),
    IN p_placanje BOOLEAN,
    IN p_klijent_id INT,
    IN p_psihoterapeut_id INT,
    IN p_beleske LONGTEXT,
    IN p_zlocin BOOLEAN,
    IN p_na_sudu BOOLEAN,
    IN p_cena_id INT,
    IN p_odrzana BOOLEAN
)
BEGIN
    START TRANSACTION;

    INSERT INTO seansa (
        dan, vreme, trajanje, placanje, klijent_id,
        psihoterapeut_kandidat_id, beleske, zlocin,
        na_sudu, cena_id, odrzana
    )
    VALUES (
        p_dan, p_vreme, p_trajanje, p_placanje, p_klijent_id,
        p_psihoterapeut_id, p_beleske, p_zlocin,
        p_na_sudu, p_cena_id, p_odrzana
    );

    COMMIT;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE vrati_testove_za_seansu (
    IN p_seansa_id INT
)
BEGIN
    SELECT t.naziv, t.cena, rt.rezultat
    FROM rezultat_testiranja rt
    JOIN test t ON rt.test_id = t.test_id
    WHERE rt.seansa_id = p_seansa_id;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE objavi_seansu (
    IN p_seansa_id INT,
    IN p_datum DATE,
    IN p_primalac NVARCHAR(256),
    IN p_razlog TEXT,
    IN p_zlocin BOOLEAN,
    IN p_na_sudu BOOLEAN
)
BEGIN
    --  Upis objave
    INSERT INTO objava_seanse (seansa_id, datum_objave, primalac, razlog)
    VALUES (p_seansa_id, p_datum, p_primalac, p_razlog);

    --  Ažuriranje vrednosti u seansi
    UPDATE seansa
    SET zlocin = p_zlocin,
        na_sudu = p_na_sudu
    WHERE seansa_id = p_seansa_id;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE dodaj_rezultat_za_test (
    IN p_seansa_id INT,
    IN p_test_id INT,
    IN p_rezultat TEXT
)
BEGIN
    INSERT INTO rezultat_testiranja (seansa_id, test_id, rezultat)
    VALUES (p_seansa_id, p_test_id, p_rezultat);
END$$
DELIMITER ;


DELIMITER //
CREATE PROCEDURE unesi_placanja_za_celu_seansu (
    IN p_seansa_id INT,
    IN p_valuta_id CHAR(3),
    IN p_nacin_id CHAR(2)
)
BEGIN
    DECLARE cena DECIMAL(10,2);

    -- Uzmi cenu seanse
    SELECT cs.cena
    INTO cena
    FROM seansa s
    JOIN cena_seanse cs ON s.cena_id = cs.cena_id
    WHERE s.seansa_id = p_seansa_id;

    -- Ubaci plaćanje za celu seansu
    INSERT INTO placanje (datum_uplate, iznos, svrha, napomena, rata, provizija_5_posto, seansa_id, valuta_id, nacin_id)
    VALUES (CURDATE(), cena, 'SEANSA - CELA', NULL, NULL, FALSE, p_seansa_id, p_valuta_id, p_nacin_id);
END //
DELIMITER ;



DELIMITER //
CREATE PROCEDURE unesi_placanje_testa (
    IN p_datum DATE,
    IN p_iznos DECIMAL(10,2),
    IN p_svrha ENUM('SEANSA - CELA', 'SEANSA - PRVA RATA', 'SEANSA - DRUGA RATA', 'TEST'),
    IN p_napomena NVARCHAR(100),
    IN p_seansa_id INT
)
BEGIN
    INSERT INTO placanje (datum_uplate, iznos, svrha, napomena, rata, provizija_5_posto, seansa_id, valuta_id, nacin_id)
    VALUES (p_datum, p_iznos, p_svrha, p_napomena, NULL, FALSE, p_seansa_id, 'RSD', 'G');
END //
DELIMITER ;




 