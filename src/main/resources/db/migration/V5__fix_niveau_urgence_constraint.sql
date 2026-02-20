-- Migration pour autoriser le niveau d'urgence 'URGENT' dans les diagnostics
-- Supprimer l'ancienne contrainte
ALTER TABLE diagnostic DROP CONSTRAINT IF EXISTS chk_niveau_urgence;

-- Ajouter la nouvelle contrainte avec 'URGENT' inclus
ALTER TABLE diagnostic ADD CONSTRAINT chk_niveau_urgence 
    CHECK (niveau_urgence IS NULL OR niveau_urgence IN ('NORMAL', 'URGENT', 'CRITIQUE'));
