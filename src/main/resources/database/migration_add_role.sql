-- Migration script to add 'role' column to 'medecin' table
ALTER TABLE medecin ADD COLUMN role VARCHAR(255) DEFAULT 'medecin';
