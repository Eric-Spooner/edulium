MERGE INTO RestaurantUser (ID, name, userRole, tip) KEY(ID) VALUES ('daotester', 'DAO Testuser', 'admin', 0);
MERGE INTO RestaurantUser (ID, name, userRole, tip) KEY(ID) VALUES ('servicetester', 'Service Testuser', 'manager', 0);

MERGE INTO RestaurantSection (ID, name) KEY(ID) VALUES(1, 'Garden');
MERGE INTO RestaurantSection (ID, name) KEY(ID) VALUES(2, 'Main');

MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(1, 1, 5, 1, 2, 'daotester');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(2, 2, 5, 1, 2, 'servicetester');


MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (1, 'starter');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (2, 'main dish');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (3, 'dessert');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (4, 'drinks');

MERGE INTO TaxRate (ID, taxRateValue) KEY(ID) VALUES (1, 0.02);
MERGE INTO TaxRate (ID,taxRateValue) KEY(ID) VALUES (2, 0.03);
MERGE INTO TaxRate (ID,taxRateValue) KEY(ID) VALUES (3, 0.01);

MERGE INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       KEY(ID) Values(1, 'Schnitzel', 10, true, 'Wiener Schnitzel', 1, 2);
MERGE INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       KEY(ID) Values(2, 'Gemüse-Suppe', 3, true, 'Frische Gemüsesuppe', 3, 1);
MERGE INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       KEY(ID) Values(3, 'Bananen-Split', 4, true, 'Bananen mit Vanille Eiscreme und Schokosauce', 2, 3);
MERGE INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       KEY(ID) Values(4, 'Leitungswasser', 1, true, 'Frisches Wiener Leitungswasser', 3, 4);


MERGE INTO Menu(ID,name) KEY(ID) VALUES (1, 'Spring Menu');

MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (1,1,8);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (1,2,2.5);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (1,3,3);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (1,4,0.5);

MERGE INTO Menu(ID,name) KEY(ID) VALUES (2, 'Schnitzel Menu');

MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (2,1,8);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (2,4,0.5);

MERGE INTO Menu(ID,name) KEY(ID) VALUES (3, 'Gemüse Menu');

MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (3,2,2.5);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (3,4,0.5);

MERGE INTO Menu(ID,name) KEY(ID) VALUES (4, 'Bananen Menu');

MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (4,3,3);
MERGE INTO MenuAssoc(menu_ID, menuEntry_ID, menuPrice) KEY(menu_ID, menuEntry_ID) VALUES (4,4,0.5);
