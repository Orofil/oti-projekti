INSERT INTO alue(nimi) VALUES
  ('Koli'),
  ('Tahko'),
  ('Ruka'),
  ('Ylläs'),
  ('Pyhä'),
  ('Vuokatti'),
  ('Levi'),
  ('Hossa');

INSERT INTO posti(postinro, toimipaikka) VALUES
  ('96500', 'ROVANIEMI'),
  ('88610', 'VUOKATTI'),
  ('73200', 'VARPAISJÄRVI'),
  ('34130', 'YLINEN'),
  ('19700', 'SYSMÄ'),
  ('95700', 'PELLO'),
  ('83960', 'KOLI'),
  ('73310', 'TAHKOVUORI'),
  ('93830', 'RUKATUNTURI'),
  ('95980', 'YLLÄSJÄRVI'),
  ('98530', 'PYHÄTUNTURI'),
  ('99130', 'SIRKKA'), -- Levi
  ('89920', 'RUHTINANSALMI'); -- Hossa

INSERT INTO asiakas(postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro) VALUES
  ('95980', 'Jaakko', 'Saano', 'Lataajanpolku 2 A', 'saano.jaakko@gmail.com', '+3584946856'), -- TODO Meidän nimet voi poistaa näistä jos nämä tulee lopulliseen ohjelmaan
  ('83960', 'Veikka', 'Kukko', 'Hallikatu 44', 'kukko.veikka@gmail.com', '+3585009442955'),
  ('89920', 'Veeti', 'Rajatammi', 'Urheilukatu 10', 'rajatammi.veeti@gmail.com', '+3585034837179'),
  ('34130', 'Oliver', 'Lehto', 'Ruotsinkatu 7 B 6', 'lehto.oliver@gmail.com', '+46722728452'),
  ('83960', 'Maria', 'Nieminen', 'Tehdaskatu 56', 'marianieminen@outlook.com', '+79243510915'),
  ('19700', 'Helmi', 'Koskinen', 'Nurmitie 1 D', 'koskhelmi@hotmail.com', '+358508737755'),
  ('98530', 'Matias', 'Mäkinen', 'Poikkikatu 18', 'matias.makinen@gmail.com', '+5577984265222'),
  ('73200', 'Lauri', 'Korhonen', 'Vitostie 6551', 'lauri1293@gmail.com', '+358494652246'),
  ('88610', 'Olavi', 'Virtanen', 'Koukkukatu 21', 'virtanen.olavi@gmail.com', '+358426859');

-- TODO palveluiden nimet ja kuvaukset
-- TODO joku merkitys tyypille
INSERT INTO palvelu(alue_id, nimi, tyyppi, kuvaus, hinta, alv) VALUES
  (1, '', 1, '', 45.9, 10),
  (1, '', 2, '', 30, 10),
  (2, '', 3, '', 10.99, 10),
  (2, '', 1, '', 8.90, 10),
  (4, '', 4, '', 20, 10),
  (6, '', 3, '', 15, 10),
  (7, '', 3, '', 20, 10),
  (8, '', 2, '', 19.99, 10),
  (5, '', 1, '', 9.99, 10),
  (7, '', 4, '', 34.20, 10),
  (5, '', 2, '', 10, 10);

INSERT INTO mokki(alue_id, postinro, mokkinimi, katuosoite, hinta, kuvaus, henkilomaara, varustelu) VALUES
  (1, '83960', 'Kultaloma', 'Kultahietikko-katu 2', 215, 'Viihtyisä, juuri valmistunut mökki upealla paikalla', 6, 'Kiitettävä'),
  (3, '93830', 'Villa Valkea', 'Lomaharjuntie 10', 400, 'Luksustason mökki kaikilla varusteilla', 10, 'Erinomainen'),
  (5, '98530', 'Ukko-Paavon pirtti', 'Ukontie 4D', 199, 'Perinteinen kelohirsimökki', 4, 'Hyvä'),
  (7, '99130', 'Villa Marina B', 'Saunakuja 7B', 220, 'Järven äärellä josta pääset yksityisesti nauttimaan', 2, 'Hyvä),
  (8, '89920', 'Rentoloma', 'Järvenrantatie 5A', 220, 'Rentouttava sohva jossa nautit lomasta', 2, 'Hyvä'),
  (4, '95980', 'Hovilantie 9A', 'Hovilantie 9A', 300, 'Upea mökki viimeisen päälle sijainnilla', 3, 'Kiitettävä'),
  (6, '88610', 'Villa Järvenraito', 'Pihkapolku 10', 350.99, 'Sisältää kaikki pelit ja pensselit', 5, 'Kiitettävä'),
  (2, '73310', 'Rinnetupa', 'Mökkikuja 33H', 221, 'Mökiltä pääset laskemaan suoraan rinteeseen', 4, 'Hyvä'),
  (6, '88610', 'Veetintupa', 'Kalastajantie 16', 170, 'Autenttinen ja tunnelmallinen mökki', 2, 'Hyvä'),
  (2, '73310', 'Talvipirtti', 'Järvenrantatie 22D', 219, 'Hyvin varusteltu talviharrastukseen', 2, 'Kiitettävä');

-- TODO päivämäärät, osa vahvistuspäivämääristä on tarkoituksella tyhjiä, sellaisia voi tehdä enemmänkin maksumuistutuksen testausta varten
INSERT INTO varaus(asiakas_id, mokki_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) VALUES
  (1, 4, '2023-04-30 17:20:01', '2023-- ::', '2023-- ::', '2023-- ::'),
  (2, 7, '2023-04-15 20:20:20', '2023-- ::', '2023-- ::', '2023-- ::'),
  (3, 2, '2023-04-26 14:52:53', '', '2023-- ::', '2023-- ::'),
  (4, 8, '2023-03-26 06:05:04', '2023-- ::', '2023-- ::', '2023-- ::'),
  (5, 4, '2023-04-28 16:30:00', '2023-- ::', '2023-- ::', '2023-- ::'),
  (6, 9, '2023-04-12 14:42:30', '2023-- ::', '2023-- ::', '2023-- ::'),
  (3, 3, '2023-04-28 19:00:05', '', '2023-- ::', '2023-- ::'),
  (8, 5, '2023-04-10 12:24:36', '', '2023-- ::', '2023-- ::'),
  (2, 10, '2023-03-19 18:30:11', '2023-- ::', '2023-- ::', '2023-- ::'),
  (4, 1, '2023-04-17 19:59:22', '2023-- ::', '2023-- ::', '2023-- ::');

-- TODO summa ja alvista sama juttu kuin palvelussa
INSERT INTO lasku(varaus_id, summa, alv) VALUES
  (1, 0, 14),
  (2, 0, 14),
  (3, 0, 14),
  (4, 0, 14),
  (5, 0, 14),
  (6, 0, 14),
  (7, 0, 14),
  (8, 0, 14),
  (9, 0, 14),
  (10, 0, 14);

-- TODO kaikki
INSERT INTO varauksen_palvelut(varaus_id, palvelu_id, lkm) VALUES
  (1,6,2),
  (2,1,1),
  (3,10,1),
  (4,4,0),
  (5,3,1),
  (6,2,4),
  (7,2,2),
  (8,1,1),
  (9,5,3),
  (10,7,2);
