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

-- JWT login function for PostgREST
CREATE OR REPLACE FUNCTION login(p_badge VARCHAR, p_password VARCHAR)
RETURNS JSON AS $$
DECLARE
    officer policemen%ROWTYPE;
    token TEXT;
BEGIN
    SELECT * INTO officer FROM policemen WHERE badge_number = p_badge;
    IF officer IS NULL OR officer.password_hash != crypt(p_password, officer.password_hash) THEN
        RAISE EXCEPTION 'Invalid credentials';
    END IF;
    token := sign(
        json_build_object(
            'role', 'authenticator',
            'policeman_id', officer.id,
            'policeman_name', officer.name,
            'exp', extract(epoch from now()) + 3600
        ),
        current_setting('app.jwt_secret')
    );
    RETURN json_build_object('token', token);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- PostgREST role
CREATE ROLE authenticator NOINHERIT LOGIN PASSWORD 'your_password_here';
GRANT USAGE ON SCHEMA public TO authenticator;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO authenticator;
GRANT EXECUTE ON FUNCTION login(VARCHAR, VARCHAR) TO authenticator;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO authenticator;
