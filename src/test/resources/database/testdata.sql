
INSERT INTO MenuCategory (name) VALUES ('starter');
INSERT INTO MenuCategory (name) VALUES ('main dish');
INSERT INTO MenuCategory (name) VALUES ('dessert');

MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('daotester', 'DAO Testuser', 'admin');
MERGE INTO RestaurantUser (ID, name, userRole) KEY(ID) VALUES ('servicetester', 'Service Testuser', 'manager');