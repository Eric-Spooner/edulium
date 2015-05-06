#TODO:
#ReservationAssocHistory?
#PaymentInfo und Type was für ein typ?

CREATE TABLE RestaurantUser (
	ID IDENTITY PRIMARY KEY,
	name VARCHAR(100),
	userRole VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE UserHistory (
	name VARCHAR(100),
	userRole VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	changeNr IDENTITY,
	user_ID FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(changeNr, user_ID)
);

CREATE TABLE Invoice (
	ID IDENTITY PRIMARY KEY,
	invoiceTime INT,
	brutto DOUBLE,
	paid DOUBLE DEFAULT FALSE,
	canceled BOOLEAN DEFAULT FALSE,
	cashedWaiter FOREIGN KEY REFERENCES RestaurantUser(ID)
);

CREATE TABLE InvoiceHistory (
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	changeNr IDENTITY,
	paid DOUBLE,
	canceled BOOLEAN DEFAULT FALSE,
	invoice_ID FOREIGN KEY REFERENCES Invoice(ID),
	PRIMARY KEY(changeNr, invoice_ID)
);

CREATE TABLE Instalment (
	ID IDENTITY,
	instalmentTime TIME,
	paymentInfo VARCHAR(200),
	type VARCHAR(100),
	amount DOUBLE,
	invoice_ID FOREIGN KEY REFERENCES Invoice(ID),
	PRIMARY KEY(ID, invoice_ID)
);

CREATE TABLE RestaurantSection (
	ID IDENTITY PRIMARY KEY,
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE SectionHistory (
	section_ID FOREIGN KEY REFERENCES RestaurantSection(ID),
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(section_ID, changeNr)
);

CREATE TABLE RestaurantTable (
	waiter_ID FOREIGN KEY REFERENCES RestaurantUser(ID),
	section_ID FOREIGN KEY REFERENCES RestaurantSection(ID),
	seats INT,
	number IDENTITY,
	tableRow INT,
	tableCol INT,
	disabled BOOLEAN DEFAULT FALSE,
	PRIMARY KEY(section_ID, number)
);

CREATE TABLE TableHistory (
	section_ID FOREIGN KEY REFERENCES RestaurantSection(ID),
	table_Number FOREIGN KEY REFERENCES RestaurantTable(number),
	seats INT,
	number INT,
	tableRow INT,
	tableCol INT,
	disabled BOOLEAN DEFAULT FALSE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	waiter_ID FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(changeNr, section_ID, table_Number)
);

CREATE TABLE Reservation (
	ID IDENTITY PRIMARY KEY,
	reservationTime TIME,
	name VARCHAR(100),
	quantity INT,
	closed BOOLEAN DEFAULT FALSE,
	duration INT
);

CREATE TABLE ReservationHistory (
	reservation_ID FOREIGN KEY REFERENCES Reservation(ID),
	reservationTime TIME,
	name VARCHAR(100),
	quantity INT,
	closed BOOLEAN DEFAULT FALSE,
	duration INT,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(reservation_ID, changeNr)
);

CREATE TABLE ReservationAssoc (
	table_ID FOREIGN KEY REFERENCES RestaurantTable(ID),
	reservation_ID FOREIGN KEY REFERENCES Reservation(ID),
	PRIMARY KEY(table_ID, reservation_ID)
);

CREATE TABLE MenuCategory (
	ID IDENTITY PRIMARY KEY,
	deleted BOOLEAN DEFAULT FALSE,
	name VARCHAR(100)
);

CREATE TABLE MenuCategoryHistory (
	menuCategory_ID FOREIGN KEY REFERENCES MenuCategory(ID),
	deleted BOOLEAN DEFAULT FALSE,
	name VARCHAR(100),
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(menuCategory_ID, changeNr)
);

CREATE TABLE MenuEntry (
	ID IDENTITY PRIMARY KEY,
	name VARCHAR(100),
	price DOUBLE,
	available BOOLEAN DEFAULT FALSE,
	description VARCHAR(300),
	deleted BOOLEAN
);

CREATE TABLE MenuEntryHistory (
	menuEntry_ID FOREIGN KEY REFERENCES MenuEntry(ID),
	name VARCHAR(100),
	price DOUBLE,
	available BOOLEAN DEFAULT FALSE,
	description VARCHAR(300),
	deleted BOOLEAN,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	taxRate DOUBLE,
	menuCategory FOREIGN KEY REFERENCES MenuCategory(ID),
	PRIMARY KEY(menuEntry_ID, changeNr)
);

CREATE TABLE TaxRate (
	ID IDENTITY PRIMARY KEY,
	taxRateValue DOUBLE,
	deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE TaxRateHistory (
	taxRate_ID FOREIGN KEY REFERENCES TaxRate(ID),
	taxRateValue DOUBLE,
	deleted BOOLEAN DEFAULT FALSE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(taxRate_ID, changeNr)
);

CREATE TABLE Sale (
	ID IDENTITY PRIMARY KEY,
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE SaleHistory ()
	sale_ID FOREIGN KEY REFERENCES Sale(ID),
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(sale_ID, changeNr)
;

CREATE TABLE SaleAssoc (
	menuEntry_ID FOREIGN KEY REFERENCES MenuEntry(ID),
	sale_ID FOREIGN KEY REFERENCES Sale(ID),
	disabled BOOLEAN DEFAULT FALSE,
	salePrice DOUBLE,
	PRIMARY KEY(menuEntry_ID, sale_ID)
);

CREATE TABLE SaleAssocHistory (
	saleAssoc_menuEntry_ID FOREIGN KEY REFERENCES SaleAssoc(menuEntry_ID),
	saleAssoc_sale_ID FOREIGN KEY REFERENCES SaleAssoc(sale_ID),
	disabled BOOLEAN DEFAULT FALSE,
	salePrice DOUBLE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(saleAssoc_menuEntry_ID, saleAssoc_sale_ID, changeNr)
);

CREATE TABLE OnetimeSale (
	sale_ID FOREIGN KEY REFERENCES Sale(ID),
	fromTime TIME,
	toTime TIME,
	PRIMARY KEY(sale_ID)
);

CREATE TABLE OnetimeSaleHistory (
	sale_ID FOREIGN KEY REFERENCES OnetimeSale(sale_ID),
	fromTime TIME,
	toTime TIME,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(sale_ID, changeNr)
);

CREATE TABLE IntermittentSale (
	sale_ID FOREIGN KEY REFERENCES Sale(ID),
	enabled BOOLEAN,
	monday BOOLEAN,
	tuesday BOOLEAN,
	wednesday BOOLEAN,
	thursday BOOLEAN,
	friday BOOLEAN,
	saturday BOOLEAN,
	sunday BOOLEAN,
	fromDayTime TIME,
	duration INT
);

CREATE TABLE IntermittentSaleHistory (
	sale_ID FOREIGN KEY REFERENCES IntermittentSale(ID),
	enabled BOOLEAN,
	monday BOOLEAN,
	tuesday BOOLEAN,
	wednesday BOOLEAN,
	thursday BOOLEAN,
	friday BOOLEAN,
	saturday BOOLEAN,
	sunday BOOLEAN,
	fromDayTime TIME,
	duration INT,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(sale_ID, changeNr)
);

CREATE TABLE Menu (
	ID IDENTITY PRIMARY KEY,
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE MenuHistory (
	menu_ID FOREIGN KEY REFERENCES Menu(ID),
	name VARCHAR(100),
	deleted BOOLEAN DEFAULT FALSE,
	changeNr IDENTITY,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(menu_ID, changeNr)
);

CREATE TABLE MenuAssoc (
	menu_ID FOREIGN KEY REFERENCES Menu(ID),
	menuEntry_ID FOREIGN KEY REFERENCES MenuEntry(ID),
	menuPrice DOUBLE,
	disabled BOOLEAN DEFAULT FALSE,
	PRIMARY KEY(menu_ID, menuEntry_ID)
);

CREATE TABLE MenuAssocHistory (
	menu_ID FOREIGN KEY REFERENCES MenuAssoc(menu_ID),
	menuEntry_ID FOREIGN KEY REFERENCES MenuAssoc(menuEntry_ID),
	menuPrice DOUBLE,
	disabled BOOLEAN DEFAULT FALSE,
	changeNr INT,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	PRIMARY KEY(menu_ID, menuEntry_ID, changeNr)
);

CREATE TABLE RestaurantOrder (
	ID INT PRIMARY KEY,
	invoice_ID FOREIGN KEY REFERENCES Invoice(ID),
	table_ID FOREIGN KEY REFERENCES RestaurantTable(ID),
	menuEntry_ID FOREIGN KEY REFERENCES MenuEntry(ID),
	orderTime TIME,
	canceled BOOLEAN DEFAULT FALSE,
	brutto DOUBLE,
	tax DOUBLE,
	info VARCHAR(100)
);

CREATE TABLE OrderHistory (
	order_ID FOREIGN KEY REFERENCES RestaurantOrder,
	changeNr INT,
	changeTime TIME,
	changeUser FOREIGN KEY REFERENCES RestaurantUser(ID),
	canceled BOOLEAN DEFAULT FALSE,
	info VARCHAR(100),
	invoice_ID FOREIGN KEY REFERENCES Invoice(ID),
	table_ID FOREIGN KEY REFERENCES RestaurantTable(ID),
	PRIMARY KEY(order_ID, changeNr)
);
