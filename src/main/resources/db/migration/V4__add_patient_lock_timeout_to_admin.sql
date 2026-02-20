-- Migration pour ajouter le délai de verrouillage configurable par administrateur
ALTER TABLE administrateur ADD COLUMN IF NOT EXISTS patient_lock_timeout INTEGER NOT NULL DEFAULT 30;
