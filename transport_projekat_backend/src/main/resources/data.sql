INSERT INTO VEHICLE_TYPES (NAME, PRICE_PER_KM) VALUES
                                                   ('STANDARD', 2),
                                                   ('LUKSUZNO', 5),
                                                   ('KOMBI', 7);

INSERT INTO USER_ACTIVATIONS(DATE_CREATED,MINUTES_VALID) VALUES
    (CURRENT_TIMESTAMP,25);

INSERT INTO USERS (ADDRESS,EMAIL,IS_ACTIVATED,IS_BLOCKED,NAME,PASSWORD,SURNAME,TELEPHONE_NUMBER,USER_ACTIVATION_ID) VALUES
    ('Bulevar oslobođenja 74', 'imenko@mail.com', false, false, 'Radomir', 'test2Test', 'Radomirović', '065338295504',1);

INSERT INTO PASSENGERS VALUES 1;

INSERT INTO USERS (ADDRESS,EMAIL,IS_ACTIVATED,IS_BLOCKED,NAME,PASSWORD,SURNAME,TELEPHONE_NUMBER) VALUES
    ('Bulevar cara lazara 23','mail_inactive@mail.com',false,false,'Vujadin','Test2test','Vujadinović','06388217512');


INSERT INTO LOCATIONS (ADDRESS, LATITUDE, LONGITUDE) VALUES
                                                         ('Bulevar oslobodjenja 46', 45.267136, 19.833549),
                                                         ('Bulevar oslobodjenja 47', 46.267136, 20.833549);

INSERT INTO VEHICLES (BABY_TRANSPORT, LICENSE_NUMBER, MODEL, PASSENGER_SEATS, PET_TRANSPORT, CURRENT_LOCATION, VEHICLE_TYPE) VALUES
    (true, 'NS 123-AB', 'VS Golf 3', 4, true, 1, 1);

-- INSERT INTO VEHICLES(VEHICLE_TYPE) VALUES(1);

INSERT INTO DRIVERS(ID, VEHICLE_ID) VALUES (2, 1);

INSERT INTO DOCUMENTS(DOCUMENT_IMAGE, NAME, DRIVER_ID) VALUES
                                                               ('U3dhZ2dlciByb2Nrcw=', 'VOZACKA_DOZVOLA', 2);

INSERT INTO WORKING_HOURS(SHIFT_START, SHIFT_END, DRIVER_ID) VALUES
                                                                     ('2019-08-24 14:15:22', '2019-08-24 14:21:22', 2);

--
-- INSERT INTO USERS (ADDRESS,EMAIL,IS_ACTIVATED,IS_BLOCKED,NAME,PASSWORD,SURNAME,TELEPHONE_NUMBER) VALUES
--     ('Bulevar Oslobođenja 23','driver@mail.com',true, false, 'Jovan', 'neprobojna_lozinka', 'Test2Test', '06533829');
--
-- INSERT INTO DRIVERS VALUES (3, 1);
