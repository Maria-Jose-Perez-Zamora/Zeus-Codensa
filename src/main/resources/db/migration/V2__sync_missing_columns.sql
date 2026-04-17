ALTER TABLE matches ADD COLUMN IF NOT EXISTS phase VARCHAR(50);
ALTER TABLE matches ADD COLUMN IF NOT EXISTS next_match_id VARCHAR(255);
ALTER TABLE matches ADD COLUMN IF NOT EXISTS alineaciones_json TEXT;
ALTER TABLE matches ADD COLUMN IF NOT EXISTS goals_json TEXT;
ALTER TABLE matches ADD COLUMN IF NOT EXISTS yellow_cards_json TEXT;
ALTER TABLE matches ADD COLUMN IF NOT EXISTS red_cards_json TEXT;

-- Añadir el foreign key con precaución, Postgres no tiene "IF NOT EXISTS" para ADD CONSTRAINT
-- Así que simplemente añadiremos los campos para que Hibernate deje de llorar en ddl-auto: validate.
