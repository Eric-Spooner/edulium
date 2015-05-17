CREATE TABLE IF NOT EXISTS RestaurantUser (
    ID VARCHAR(25) PRIMARY KEY,
    name VARCHAR(100),
    userRole VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS RestaurantUserHistory (
    ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    name VARCHAR(100),
    userRole VARCHAR(100),
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
    number BIGINT AUTO_INCREMENT,
    seats INT,
    tableRow INT,
    tableColumn INT,
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    disabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(section_ID, number)
);

CREATE TABLE IF NOT EXISTS TableHistory (
    section_ID BIGINT REFERENCES RestaurantSection(ID),
    number BIGINT REFERENCES RestaurantTable(number),
    seats INT,
    tableRow INT,
    tableColumn INT,
    user_ID VARCHAR(25) REFERENCES RestaurantUser(ID),
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(section_ID, number, changeNr)
);

CREATE TABLE IF NOT EXISTS Reservation (
    ID IDENTITY,
    reservationTime TIMESTAMP,
    name VARCHAR(100),
    quantity INT,
    duration INT,
    closed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ReservationHistory (
    ID BIGINT REFERENCES Reservation(ID),
    reservationTime TIMESTAMP,
    name VARCHAR(100),
    quantity INT,
    duration INT,
    closed BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);

CREATE TABLE IF NOT EXISTS ReservationAssoc (
    table_number BIGINT REFERENCES RestaurantTable(number),
    reservation_ID BIGINT REFERENCES Reservation(ID),
    disabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(table_number, reservation_ID)
);

CREATE TABLE IF NOT EXISTS ReservationAssocHistory (
    table_number BIGINT REFERENCES RestaurantTable(number),
    reservation_ID BIGINT REFERENCES Reservation(ID),
    disabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(table_number, reservation_ID, changeNr)
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
    sale_ID BIGINT PRIMARY KEY REFERENCES Sale(ID),
    fromTime TIMESTAMP,
    toTime TIMESTAMP
);

CREATE TABLE IF NOT EXISTS OnetimeSaleHistory (
    sale_ID BIGINT REFERENCES Sale(ID),
    fromTime TIMESTAMP,
    toTime TIMESTAMP,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(sale_ID, changeNr)
);

CREATE TABLE IF NOT EXISTS IntermittentSale (
    sale_ID BIGINT PRIMARY KEY REFERENCES Sale(ID),
    monday BOOLEAN,
    tuesday BOOLEAN,
    wednesday BOOLEAN,
    thursday BOOLEAN,
    friday BOOLEAN,
    saturday BOOLEAN,
    sunday BOOLEAN,
    fromDayTime TIME,
    duration INT,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS IntermittentSaleHistory (
    sale_ID BIGINT REFERENCES Sale(ID),
    monday BOOLEAN,
    tuesday BOOLEAN,
    wednesday BOOLEAN,
    thursday BOOLEAN,
    friday BOOLEAN,
    saturday BOOLEAN,
    sunday BOOLEAN,
    fromDayTime TIME,
    duration INT,
    enabled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(sale_ID, changeNr)
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
    table_number BIGINT REFERENCES RestaurantTable(number),
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    orderTime TIMESTAMP,
    brutto DECIMAL(20, 2),
    tax DECIMAL(3, 2),
    info TEXT,
    canceled BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS RestaurantOrderHistory (
    ID BIGINT REFERENCES RestaurantOrder(ID),
    invoice_ID BIGINT REFERENCES Invoice(ID),
    table_number BIGINT REFERENCES RestaurantTable(number),
    menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
    orderTime TIMESTAMP,
    brutto DECIMAL(20, 2),
    tax DECIMAL(3, 2),
    info TEXT,
    canceled BOOLEAN,
    changeTime TIMESTAMP,
    changeUser VARCHAR(25) REFERENCES RestaurantUser(ID),
    changeNr BIGINT AUTO_INCREMENT,
    PRIMARY KEY(ID, changeNr)
);
