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
  ('95980', 'Jarkko', 'Nieminen', 'Lataajanpolku 2 A', 'nieminen.jarkko@gmail.com', '+3584946856'),
  ('83960', 'Saana', 'Kuisma', 'Hallikatu 44', 'kuisma.saana@gmail.com', '+3585009442955'),
  ('89920', 'Ismo', 'Laanila', 'Urheilukatu 10', 'laanila.ismo@gmail.com', '+3585034837179'),
  ('34130', 'Paavo', 'Turunen', 'Ruotsinkatu 7 B 6', 'paavo.turunen@gmail.com', '+46722728452'),
  ('83960', 'Maria', 'Nieminen', 'Tehdaskatu 56', 'marianieminen@outlook.com', '+79243510915'),
  ('19700', 'Helmi', 'Koskinen', 'Nurmitie 1 D', 'koskhelmi@hotmail.com', '+358508737755'),
  ('98530', 'Matias', 'Mäkinen', 'Poikkikatu 18', 'matias.makinen@gmail.com', '+5577984265222'),
  ('73200', 'Lauri', 'Korhonen', 'Vitostie 6551', 'lauri1293@gmail.com', '+358494652246'),
  ('88610', 'Olavi', 'Virtanen', 'Koukkukatu 21', 'virtanen.olavi@gmail.com', '+358426859');

INSERT INTO palvelu(alue_id, nimi, tyyppi, kuvaus, hinta, alv) VALUES
  (1, 'Siivouspalvelu', 1, 'Mökin siivous', 170, 10),
  (1, 'Liinavaatevuokraus', 2, 'Liinavaatteiden vuokraaminen ', 30, 10),
  (2, 'Moottorikelkkavuokraus', 3, 'Voit vuokrata moottorikelkan käyttöön', 150, 10),
  (2, 'Lasketteluvälinevuokraus', 1, 'Valittavana mikä tahansa lasketteluväline', 59.8, 10),
  (4, 'Kalastusvälinevuokraus', 4, 'Kalastus setti jolla voi heittokalastaa ', 20, 10),
  (6, 'Hiihtovälinevuokraus', 3, 'Hiihtosukset, sekä sauvat joilla voi hiihtää', 30, 10),
  (7, 'Venevuokraus', 3, 'Vene jossa on pieni moottori jolla voi ajella', 90, 10),
  (8, 'Lumikenkävuokraus', 2, 'Lumikenkäsetti yhdelle sisältäen sauvat', 19.99, 10),
  (5, 'Ohjelmapalvelu', 1, 'Voit tilata palvelun, joka tarjoaa erilaisia aktiviteetteja', 9.99, 10),
  (7, 'Hierontapalvelu', 4, 'Voit valita vapaamuotoisen hieronnan', 34.20, 10),
  (5, 'Kylpyläpalvelu', 2, 'Varaa rentouttava hetki mukavassa kylpylässä', 50, 10);

INSERT INTO mokki(alue_id, postinro, mokkinimi, katuosoite, hinta, kuvaus, henkilomaara, varustelu) VALUES
  (1, '83960', 'Kultaloma', 'Kultahietikko-katu 2', 215, 'Viihtyisä, juuri valmistunut mökki upealla paikalla', 6, 'Poreallas, pihasauna'),
  (3, '93830', 'Villa Valkea', 'Lomaharjuntie 10', 400, 'Luksustason mökki kaikilla varusteilla', 10, 'Iso keittiö, palju, sauna'),
  (5, '98530', 'Ukko-Paavon pirtti', 'Ukontie 4D', 199, 'Perinteinen kelohirsimökki', 4, 'Savusauna, hyvät keittiövälineet'),
  (7, '99130', 'Villa Marina B', 'Saunakuja 7B', 220, 'Järven äärellä josta pääset yksityisesti nauttimaan', 2, 'Laatu sängyt, tunnelmavalaistus'),
  (8, '89920', 'Rentoloma', 'Järvenrantatie 5A', 220, 'Rentouttava sohva jossa nautit lomasta', 2, 'Hierontatuoli, lihashuoltovälineet, sauna'),
  (4, '95980', 'Hovilantie 9A', 'Hovilantie 9A', 300, 'Upea mökki viimeisen päälle sijainnilla', 3, 'Sauna, laadukas keittiö '),
  (6, '88610', 'Villa Järvenraito', 'Pihkapolku 10', 350.99, 'Sisältää kaikki pelit ja pensselit', 5, 'Virveli, soutuvene, sauna'),
  (2, '73310', 'Rinnetupa', 'Mökkikuja 33H', 221, 'Mökiltä pääset laskemaan suoraan rinteeseen', 4, 'Laskettelusukset, pulkka, palju'),
  (6, '88610', 'Veetintupa', 'Kalastajantie 16', 170, 'Autenttinen ja tunnelmallinen mökki', 2, 'Pihasauna, poreallas'),
  (2, '73310', 'Talvipirtti', 'Järvenrantatie 22D', 219, 'Hyvin varusteltu talviharrastukseen', 2, 'Pulkka, sukset, lauta');


INSERT INTO varaus(asiakas_id, mokki_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) VALUES
  (1, 4, '2023-04-30 17:20:01', '2023-04-30 17:20:50', '2023-05-01 16:00:00', '2023-05-16 12:00:00'),
  (2, 7, '2023-04-15 20:20:20', '2023-04-26 16:32:56', '2023-04-28 16:00:00', '2023-05-02 12:00:00'),
  (3, 2, '2023-04-26 14:52:53', '2023-04-26 14:52:53', '2023-04-30 16:00:00', '2023-05-16 12:00:00'),
  (4, 8, '2023-03-26 06:05:04', '2023-04-01 12:00:00', '2023-04-10 16:00:00', '2023-04-19 12:00:00'),
  (5, 4, '2023-04-28 16:30:00', '2023-04-28 16:33:00', '2023-04-30 16:00:00', '2023-05-09 12:00:00'),
  (6, 9, '2023-04-12 14:42:30', '2023-04-16 15:38:01', '2023-04-20 16:00:00', '2023-04-30 12:00:00'),
  (3, 3, '2023-04-28 19:00:05', '2023-04-28 19:00:05', '2023-05-24 16:00:00', '2023-05-26 12:00:00'),
  (8, 5, '2023-04-10 12:24:36', '2023-04-10 12:24:36', '2023-04-11 16:00:00', '2023-04-18 12:00:00'),
  (2, 10, '2023-03-19 18:30:11', '2023-03-19 18:32:11', '2023-03-31 16:00:00', '2023-04-04 12:00:00'),
  (4, 1, '2023-04-17 19:59:22', '2023-04-17 19:59:22', '2023-05-04 16:00:00', '2023-05-10 12:00:00');


INSERT INTO lasku(varaus_id, summa, alv, status) VALUES
  (1, 1000, 14, 0),
  (2, 1990, 14, 1),
  (3, 750, 14, 2),
  (4, 998, 14, 2),
  (5, 1210, 14, 2),
  (6, 2450, 14, 1),
  (7, 579, 14, 0),
  (8, 799, 14, 0),
  (9, 1199, 14, 2),
  (10, 1660, 14, 1);

INSERT INTO varauksen_palvelut(varaus_id, palvelu_id, lkm) VALUES
  (1, 6, 2),
  (2, 1, 1),
  (3, 10, 1),
  (4, 4, 1),
  (5, 3, 1),
  (6, 2, 4),
  (7, 2, 2),
  (8, 1, 1),
  (9, 5, 3),
  (10, 7, 2);
