CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    photo VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    jersey_number INT
);

CREATE TABLE IF NOT EXISTS teams (
    id BIGSERIAL PRIMARY KEY,
    team_name VARCHAR(255) NOT NULL UNIQUE,
    escudo VARCHAR(255),
    colores_uniforme VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS team_players (
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, user_id),
    CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES teams(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS tournaments (
    id VARCHAR(255) PRIMARY KEY,
    tournament_name VARCHAR(255) NOT NULL UNIQUE,
    fecha_inicio VARCHAR(255),
    fecha_fin VARCHAR(255),
    numero_equipos INT,
    costo_inscripcion DOUBLE PRECISION,
    status VARCHAR(50),
    rules TEXT,
    fecha_cierre_inscripciones VARCHAR(255),
    fecha_inicio_fase_grupos VARCHAR(255),
    sanctions TEXT,
    campeon VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tournament_horarios (
    tournament_id VARCHAR(255) NOT NULL,
    horario VARCHAR(255),
    CONSTRAINT fk_tournament_horarios FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

CREATE TABLE IF NOT EXISTS tournament_canchas (
    tournament_id VARCHAR(255) NOT NULL,
    cancha VARCHAR(255),
    CONSTRAINT fk_tournament_canchas FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

CREATE TABLE IF NOT EXISTS matches (
    id VARCHAR(255) PRIMARY KEY,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    match_date VARCHAR(255) NOT NULL,
    home_score INT,
    away_score INT,
    status VARCHAR(50),
    tournament_name VARCHAR(255),
    referee_email VARCHAR(255),
    phase VARCHAR(50),
    next_match_id VARCHAR(255),
    alineaciones_json TEXT,
    goals_json TEXT,
    yellow_cards_json TEXT,
    red_cards_json TEXT,
    CONSTRAINT fk_next_match FOREIGN KEY (next_match_id) REFERENCES matches(id)
);

CREATE TABLE IF NOT EXISTS registrations (
    id VARCHAR(255) PRIMARY KEY,
    team_name VARCHAR(255) NOT NULL,
    tournament_name VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    comprobante_pago_url VARCHAR(255),
    fecha_inscripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS invitations (
    id VARCHAR(255) PRIMARY KEY,
    captain_email VARCHAR(255),
    player_email VARCHAR(255),
    team_name VARCHAR(255),
    status VARCHAR(50),
    message TEXT
);

-- Índices B2-2.3
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_teams_name ON teams(team_name);
CREATE INDEX IF NOT EXISTS idx_tournaments_name ON tournaments(tournament_name);
CREATE INDEX IF NOT EXISTS idx_matches_date ON matches(match_date);
CREATE INDEX IF NOT EXISTS idx_matches_tournament ON matches(tournament_name);
