CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE policemen (
    id SERIAL PRIMARY KEY,
    badge_number VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE drivers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    plate_number VARCHAR(20) NOT NULL UNIQUE,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INTEGER NOT NULL
);

CREATE TABLE infractions (
    id SERIAL PRIMARY KEY,
    driver_id INTEGER NOT NULL REFERENCES drivers(id),
    vehicle_id INTEGER NOT NULL REFERENCES vehicles(id),
    officer_id INTEGER REFERENCES policemen(id),
    type VARCHAR(100) NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    description TEXT,
    location VARCHAR(255)
);

CREATE OR REPLACE FUNCTION login(p_badge VARCHAR, p_password VARCHAR)
RETURNS JSON AS $$
DECLARE
    officer policemen%ROWTYPE;
BEGIN
    SELECT * INTO officer FROM policemen WHERE badge_number = p_badge;
    IF officer IS NULL OR officer.password_hash != crypt(p_password, officer.password_hash) THEN
        RAISE EXCEPTION 'Invalid credentials';
    END IF;
    RETURN json_build_object('id', officer.id, 'name', officer.name, 'badge_number', officer.badge_number);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE ROLE authenticator NOINHERIT LOGIN PASSWORD 'postgrest_password';
GRANT USAGE ON SCHEMA public TO authenticator;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO authenticator;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO authenticator;
GRANT EXECUTE ON FUNCTION login(VARCHAR, VARCHAR) TO authenticator;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO authenticator;

INSERT INTO policemen (badge_number, name, password_hash) VALUES
    ('B001', 'Officer Andria', crypt('password123', gen_salt('bf'))),
    ('B002', 'Officer Rajoelina', crypt('password456', gen_salt('bf')));
