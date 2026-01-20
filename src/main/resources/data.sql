-- Initialisation des données pour les administrateurs
-- Ce fichier est exécuté automatiquement par Spring Boot au démarrage

-- Insertion du Super Admin par défaut (si n'existe pas)
INSERT INTO administrateur (nom, email, mot_de_passe, role, statut, created_at, updated_at)
SELECT 'Super Admin', 'superadmin@maternicare.com', 'SuperAdminPass1!', 'SUPER_ADMIN', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM administrateur WHERE email = 'superadmin@maternicare.com'
);

-- Mettre à jour les administrateurs existants qui n'ont pas de statut
UPDATE administrateur SET statut = 'ACTIF' WHERE statut IS NULL;
