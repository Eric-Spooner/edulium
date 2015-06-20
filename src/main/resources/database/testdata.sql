MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('cook', 'Crusty Cook', 'ROLE_COOK');
MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('waiter', 'Wily Waiter', 'ROLE_SERVICE');
MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('manager', 'Maverick Manager', 'ROLE_MANAGER');

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
Values(1, 1, 5, 1, 2, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(2, 2, 5, 1, 2, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 3, 5, 1, 2, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 4, 15, 3, 1, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 5, 4, 3, 3, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 6, 4, 1, 4, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 7, 4, 1, 6, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 8, 4, 1, 8, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 9, 4, 1, 9, 'waiter');
MERGE INTO RestaurantTable(section_ID, number, seats, tableRow, tableColumn, user_ID) KEY(section_ID, number)
Values(3, 10, 4, 1, 12, 'waiter');


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

MERGE INTO Invoice (id, invoiceTime, brutto, user_ID, canceled) KEY(id)
VALUES(1, TIMESTAMP '2015-01-01 12:00:00', 18.50, 'waiter', false);
MERGE INTO Invoice (id, invoiceTime, brutto, user_ID, canceled) KEY(id)
VALUES(2, TIMESTAMP '2015-01-01 12:00:00', 12.75, 'waiter', false);
MERGE INTO Invoice (id, invoiceTime, brutto, user_ID, canceled) KEY(id)
VALUES(3, TIMESTAMP '2015-01-02 12:00:00', 48.00, 'waiter', false);
MERGE INTO Invoice (id, invoiceTime, brutto, user_ID, canceled) KEY(id)
VALUES(4, TIMESTAMP '2015-01-03 12:00:00', 118.00, 'waiter', false);
MERGE INTO Invoice (id, invoiceTime, brutto, user_ID, canceled) KEY(id)
VALUES(5, TIMESTAMP '2015-01-05 12:00:00', 3.50, 'waiter', false);

MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info,  state)
KEY (id, table_section, table_number) VALUES (1, 1, 1, 1, 1, 10, 0.01, '', 'QUEUED');
MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (2, 2, 1, 1, 2, 10, 0.01, '','QUEUED');
MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (3, 3, 1, 1, 3, 10, 0.01, 'Info' , 'IN_PROGRESS');
MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (4, 4, 1, 1, 4, 10, 0.01, '','QUEUED');
MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (5, 5, 1, 1, 3, 10, 0.01, 'Without salt' , 'READY_FOR_DELIVERY');
MERGE INTO RestaurantOrder (id, invoice_id, table_section, table_number , menuEntry_ID, brutto, tax, info, state)
KEY (id, table_section, table_number) VALUES (6, 4, 1, 1, 2, 10, 0.01, '' , 'DELIVERED');

MERGE INTO Reservation(id, reservationTime, name, quantity, duration) KEY(id)
VALUES(1, TIMESTAMP '2015-01-01 12:00:00', 'peter', 4, 12000000);
MERGE INTO Reservation(id, reservationTime, name, quantity, duration) KEY(id)
VALUES(2, TIMESTAMP '2015-01-02 12:00:00', 'paul', 6, 20000000);
MERGE INTO Reservation(id, reservationTime, name, quantity, duration) KEY(id)
VALUES(3, TIMESTAMP '2015-01-01 12:45:00', 'evan', 8, 30000000);

MERGE INTO ReservationAssoc (reservation_id, table_section, table_number) KEY(reservation_id, table_section, table_number)
VALUES(1, 3, 6);

MERGE INTO ReservationAssoc (reservation_id, table_section, table_number) KEY(reservation_id, table_section, table_number)
VALUES(2, 3, 9);
MERGE INTO ReservationAssoc (reservation_id, table_section, table_number) KEY(reservation_id, table_section, table_number)
VALUES(2, 3, 10);

MERGE INTO ReservationAssoc (reservation_id, table_section, table_number) KEY(reservation_id, table_section, table_number)
VALUES(3, 3, 7);
MERGE INTO ReservationAssoc (reservation_id, table_section, table_number) KEY(reservation_id, table_section, table_number)
VALUES(3, 2, 2);
