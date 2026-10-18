INSERT INTO USER (id, name, email, password, active, token, created_on, updated_on)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Josue Reyes', 'josue@example.com', 'Password123!', true, 'mock-jwt-token-1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO USER (id, name, email, password, active, token, created_on, updated_on)
VALUES ('b1ffe100-0d1c-4fe9-cc7e-7cc0ce491b22', 'Ana Gomez', 'ana@example.com', 'Password123!', true, 'mock-jwt-token-2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO PHONE (id, number, citycode, countryCode, user_id, created_on, updated_on)
VALUES ('p1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '5551234', '1', '505', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO PHONE (id, number, citycode, countryCode, user_id, created_on, updated_on)
VALUES ('p2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '88887777', '2', '505', 'b1ffe100-0d1c-4fe9-cc7e-7cc0ce491b22', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);