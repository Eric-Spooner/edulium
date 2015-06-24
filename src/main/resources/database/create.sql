CREATE TABLE IF NOT EXISTS RestaurantUser (
    ID VARCHAR(25) PRIMARY KEY,
    name VARCHAR(100),
    userRole VARCHAR(100),
    tip DECIMAL(20,2),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS RestaurantUserHistory (
    ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    name VARCHAR(100),
    userRole VARCHAR(100),
    tip DECIMAL(20,2),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS Invoice (
    ID IDENTITY,
    invoiceTime TIMESTAMP,
    brutto DECIMAL(20, 2),
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    canceled BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS InvoiceHistory (
    ID BIGINT REFERENCES Invoice(ID),
    invoiceTime TIMESTAMP,
    brutto DECIMAL(20, 2),
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    canceled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS Instalment (
    ID BIGINT AUTO_INCREMENT,
    instalmentTime TIMESTAMP,
    paymentInfo TEXT,
    type VARCHAR(100),
    amount DECIMAL(20, 2),
    invoice_ID BIGINT REFERENCES Invoice(ID),
    PRIMARY KEY(ID, invoice_ID)
);

CREATE VIEW IF NOT EXISTS InvoiceExtended AS
    SELECT *, ((SELECT ISNULL(SUM(inst.amount), 0)
                FROM Instalment AS inst JOIN Invoice AS inv ON inst.invoice_ID = inv.ID) >= brutto) AS closed
    FROM Invoice;

CREATE TABLE IF NOT EXISTS RestaurantSection (
    ID IDENTITY,
    name VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS RestaurantSectionHistory (
    ID BIGINT REFERENCES RestaurantSection(ID),
    name VARCHAR(100),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS RestaurantTable (
    section_ID BIGINT REFERENCES RestaurantSection(ID),
    number BIGINT,
    seats INT,
    tableRow INT,
    tableColumn INT,
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    disabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(section_ID, number)
);

CREATE TABLE IF NOT EXISTS TableHistory (
    section_ID BIGINT,
    number BIGINT,
    seats INT,
    tableRow INT,
    tableColumn INT,
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    FOREIGN KEY(section_ID, number) REFERENCES RestaurantTable(section_ID, number),
    PRIMARY KEY(section_ID, number, changeNr)
);

CREATE TABLE IF NOT EXISTS Reservation (
    ID IDENTITY,
    reservationTime TIMESTAMP,
    name VARCHAR(100),
    quantity INT,
    duration BIGINT,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ReservationHistory (
    ID BIGINT REFERENCES Reservation(ID),
    reservationTime TIMESTAMP,
    name VARCHAR(100),
    quantity INT,
    duration BIGINT,
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS ReservationAssoc (
    reservation_ID BIGINT REFERENCES Reservation(ID),
    table_section BIGINT,
    table_number BIGINT,
    disabled BOOLEAN DEFAULT FALSE,
    FOREIGN KEY(table_section, table_number) REFERENCES RestaurantTable(section_ID, number),
    PRIMARY KEY(table_section, table_number, reservation_ID)
);

CREATE TABLE IF NOT EXISTS ReservationAssocHistory (
    reservation_ID BIGINT,
    table_section BIGINT,
    table_number BIGINT,
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    FOREIGN KEY(table_section, table_number, reservation_ID) REFERENCES ReservationAssoc(table_section, table_number, reservation_ID),
    PRIMARY KEY(table_section, table_number, reservation_ID, changeNr)
);

CREATE TABLE IF NOT EXISTS MenuCategory (
    ID IDENTITY,
    name VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS MenuCategoryHistory (
    ID BIGINT REFERENCES MenuCategory(ID),
    name VARCHAR(100),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS TaxRate (
    ID IDENTITY,
    taxRateValue DECIMAL(5, 4),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS TaxRateHistory (
    ID BIGINT REFERENCES TaxRate(ID),
    taxRateValue DECIMAL(5, 4),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS MenuEntry (
    ID IDENTITY,
    name VARCHAR(100),
    price DECIMAL(20, 2),
    available BOOLEAN,
    description TEXT,
    taxRate_ID BIGINT REFERENCES TaxRate(ID),
    category_ID BIGINT REFERENCES MenuCategory(ID),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS MenuEntryHistory (
    ID BIGINT REFERENCES MenuEntry(ID),
    name VARCHAR(100),
    price DECIMAL(20, 2),
    available BOOLEAN,
    description TEXT,
    taxRate_ID BIGINT REFERENCES TaxRate(ID),
    category_ID BIGINT REFERENCES MenuCategory(ID),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS Sale (
    ID IDENTITY,
    name VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS SaleHistory (
    ID BIGINT REFERENCES Sale(ID),
    name VARCHAR(100),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS SaleAssoc (
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    sale_ID BIGINT REFERENCES Sale(ID),
    salePrice DECIMAL(20, 2),
    disabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(menuEntry_ID, sale_ID)
);

CREATE TABLE IF NOT EXISTS SaleAssocHistory (
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    sale_ID BIGINT REFERENCES Sale(ID),
    salePrice DECIMAL(20, 2),
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(menuEntry_ID, sale_ID, changeNr)
);

CREATE TABLE IF NOT EXISTS OnetimeSale (
    ID BIGINT PRIMARY KEY REFERENCES Sale(ID),
    fromTime TIMESTAMP,
    toTime TIMESTAMP
);

CREATE TABLE IF NOT EXISTS OnetimeSaleHistory (
    ID BIGINT REFERENCES Sale(ID),
    fromTime TIMESTAMP,
    toTime TIMESTAMP,
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS IntermittentSale (
    ID BIGINT PRIMARY KEY REFERENCES Sale(ID),
    monday BOOLEAN,
    tuesday BOOLEAN,
    wednesday BOOLEAN,
    thursday BOOLEAN,
    friday BOOLEAN,
    saturday BOOLEAN,
    sunday BOOLEAN,
    fromDayTime TIME,
    duration BIGINT,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS IntermittentSaleHistory (
    ID BIGINT REFERENCES Sale(ID),
    monday BOOLEAN,
    tuesday BOOLEAN,
    wednesday BOOLEAN,
    thursday BOOLEAN,
    friday BOOLEAN,
    saturday BOOLEAN,
    sunday BOOLEAN,
    fromDayTime TIME,
    duration BIGINT,
    enabled BOOLEAN,
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS Menu (
    ID IDENTITY,
    name VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS MenuHistory (
    ID BIGINT REFERENCES Menu(ID),
    name VARCHAR(100),
    deleted BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS MenuAssoc (
    menu_ID BIGINT REFERENCES Menu(ID),
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    menuPrice DECIMAL(20, 2),
    disabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(menu_ID, menuEntry_ID)
);

CREATE TABLE IF NOT EXISTS MenuAssocHistory (
    menu_ID BIGINT REFERENCES Menu(ID),
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    menuPrice DECIMAL(20, 2),
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(menu_ID, menuEntry_ID, changeNr)
);

CREATE TABLE IF NOT EXISTS RestaurantOrder (
    ID IDENTITY,
    invoice_ID BIGINT REFERENCES Invoice(ID),
    table_section BIGINT,
    table_number BIGINT,
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    orderTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    brutto DECIMAL(20, 2),
    tax DECIMAL(3, 2),
    info TEXT,
    state VARCHAR(20),
    canceled BOOLEAN DEFAULT FALSE,
    FOREIGN KEY(table_section, table_number) REFERENCES RestaurantTable(section_ID, number),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS RestaurantOrderHistory (
    ID BIGINT REFERENCES RestaurantOrder(ID),
    invoice_ID BIGINT REFERENCES Invoice(ID),
    table_section BIGINT,
    table_number BIGINT,
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    orderTime TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    brutto DECIMAL(20, 2),
    tax DECIMAL(3, 2),
    info TEXT,
    state VARCHAR(20),
    canceled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    FOREIGN KEY(table_section, table_number) REFERENCES RestaurantTable(section_ID, number),
    PRIMARY KEY(ID, changeNr)
);

MERGE INTO RestaurantUser (ID, name, userRole, tip) KEY(ID) VALUES ('manager', 'Maverick Manager', 'ROLE_MANAGER', 0);