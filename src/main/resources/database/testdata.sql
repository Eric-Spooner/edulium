MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('daotester', 'DAO Testuser', 'COOK');
MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('servicetester', 'Service Testuser', 'ROLE_MANAGER');

MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (1, 'starter');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (2, 'main dish');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (3, 'dessert');
MERGE INTO MenuCategory (ID, name) KEY(ID) VALUES (4, 'drinks');

MERGE INTO TaxRate (ID, taxRateValue) KEY(ID) VALUES (1, 0.02);
MERGE INTO TaxRate (ID,taxRateValue) KEY(ID) VALUES (2, 0.03);
MERGE INTO TaxRate (ID,taxRateValue) KEY(ID) VALUES (3, 0.01);

MERGE INTO RestaurantSection (ID, name) KEY(ID) VALUES(1, 'Garden');
MERGE INTO RestaurantSection (ID, name) KEY(ID) VALUES(2, 'Main');
MERGE INTO RestaurantSection (ID, name) KEY(ID) VALUES(3, 'Wintergarden');

MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(1, 1, 5, 1, 2, 'daotester');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(2, 2, 5, 1, 2, 'servicetester');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 3, 5, 1, 2, 'daotester');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 4, 5, 1, 2, 'servicetester');

MERGE INTO MenuEntry (ID, name, price, available, description, taxRate_ID, category_ID)
       KEY(ID) Values(1, 'Schnitzel', 9, true, 'Wiener Schnitzel', 1, 2);
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


MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info,  state)
KEY (id, table_section, table_number) VALUES (1, 1, 1, 1, 10, 0.01, '', 'QUEUED');
MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (2, 1, 1, 2, 10, 0.01, '','QUEUED');
MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (3, 2, 2, 3, 10, 0.01, 'Info' , 'IN_PROGRESS');
MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (4, 2, 2, 4, 10, 0.01, '','QUEUED');
MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (5, 3, 3, 3, 10, 0.01, 'Without salt' , 'QUEUED');
MERGE INTO RestaurantOrder (id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (6, 3, 4, 2, 10, 0.01, '' , 'QUEUED');