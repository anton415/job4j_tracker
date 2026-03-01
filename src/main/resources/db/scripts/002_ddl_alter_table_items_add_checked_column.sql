-- Добавление нового столбца checked
ALTER TABLE items ADD COLUMN checked BOOLEAN NOT NULL DEFAULT false;