INSERT INTO RestaurantUser (ID, name, userRole) VALUES ('daotester', 'DAO Testuser', 'admin');
INSERT INTO RestaurantUser (ID, name, userRole) VALUES ('servicetester', 'Service Testuser', 'manager');

INSERT INTO MenuCategory (ID, name) VALUES (1, 'starter');
INSERT INTO MenuCategory (ID, name) VALUES (2, 'main dish');
INSERT INTO MenuCategory (ID, name) VALUES (3, 'dessert');
INSERT INTO MenuCategory (ID, name) VALUES (4, 'drinks');

INSERT INTO TaxRate (ID, taxRateValue) VALUES (1, 0.02);
INSERT INTO TaxRate (ID,taxRateValue) VALUES (2, 0.03);
INSERT INTO TaxRate (ID,taxRateValue) VALUES (3, 0.01);

INSERT INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       Values(1, 'Schnitzel', 10, true, 'Wiener Schnitzel', 1, 2);
INSERT INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       Values(2, 'Gemüse-Suppe', 3, true, 'Frische Gemüsesuppe', 3, 1);
INSERT INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       Values(3, 'Bananen-Split', 4, true, 'Bananen mit Vanille Eiscreme und Schokosauce', 2, 3);
INSERT INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       Values(4, 'Leitungswasser', 1, true, 'Frisches Wiener Leitungswasser', 3, 4);


INSERT INTO Menu(ID,name) VALUES (1, 'Spring Menu');

INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (1,1,8);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (1,2,2.5);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (1,3,3);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (1,4,0.5);

INSERT INTO Menu(ID,name) VALUES (2, 'Schnitzel Menu');

INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (2,1,8);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (2,4,0.5);

INSERT INTO Menu(ID,name) VALUES (3, 'Gemüse Menu');

INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (3,2,2.5);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (3,4,0.5);

INSERT INTO Menu(ID,name) VALUES (4, 'Bananen Menu');

INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (4,3,3);
INSERT INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) VALUES (4,4,0.5);