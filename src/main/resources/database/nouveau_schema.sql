-- ============================================
-- SCHÉMA FINAL - BD_ELECTRO
-- Application de gestion des paramètres médicaux
-- ============================================

-- ============================================
-- TABLE 1 : MEDECIN
-- ============================================
CREATE TABLE Medecin (
    ID_Medecin SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL, -- Hash BCrypt
    tel VARCHAR(20),
    statut VARCHAR(20) DEFAULT 'actif' CHECK (statut IN ('actif', 'inactif', 'suspendu')),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_connexion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index Medecin
CREATE INDEX idx_medecin_email ON Medecin(email);
CREATE INDEX idx_medecin_statut ON Medecin(statut) WHERE statut = 'actif';

-- ============================================
-- TABLE 2 : DISPOSITIF
-- ============================================
CREATE TABLE Dispositif (
    ID_Dispositif SERIAL PRIMARY KEY,
    code_dispositif VARCHAR(50) UNIQUE NOT NULL,
    nom_centre_de_sante VARCHAR(150) NOT NULL,
    localisation VARCHAR(200),
    contact VARCHAR(50),
    statut VARCHAR(20) DEFAULT 'actif' CHECK (statut IN ('actif', 'inactif', 'maintenance')),
    date_installation DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index Dispositif
CREATE INDEX idx_dispositif_code ON Dispositif(code_dispositif);
CREATE INDEX idx_dispositif_statut ON Dispositif(statut) WHERE statut = 'actif';
CREATE INDEX idx_dispositif_centre ON Dispositif(nom_centre_de_sante);

-- ============================================
-- TABLE 3 : PARAMETRES
-- ============================================
CREATE TABLE Parametres (
    ID_Parametres SERIAL PRIMARY KEY,
    identifiant_patient VARCHAR(50) NOT NULL,
    ID_Dispositif INT NOT NULL,
    
    -- Paramètres vitaux
    poids_patient DECIMAL(5,2) CHECK (poids_patient > 0 AND poids_patient < 300),
    temperature DECIMAL(4,2) CHECK (temperature >= 30 AND temperature <= 45),
    pression_arterielle_systolique INT CHECK (pression_arterielle_systolique BETWEEN 40 AND 300),
    pression_arterielle_diastolique INT CHECK (pression_arterielle_diastolique BETWEEN 20 AND 200),
    frequence_foetale INT CHECK (frequence_foetale BETWEEN 50 AND 220),
    
    -- Métadonnées
    date_mesure TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'en_attente' CHECK (statut IN ('en_attente', 'diagnostique', 'archive')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clé étrangère
    CONSTRAINT fk_param_dispositif
        FOREIGN KEY (ID_Dispositif)
        REFERENCES Dispositif(ID_Dispositif)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Index Parametres
CREATE INDEX idx_param_dispositif ON Parametres(ID_Dispositif);
CREATE INDEX idx_param_patient ON Parametres(identifiant_patient);
CREATE INDEX idx_param_date_mesure ON Parametres(date_mesure DESC);
CREATE INDEX idx_param_statut ON Parametres(statut) WHERE statut = 'en_attente';

-- ============================================
-- TABLE 4 : DIAGNOSTIC
-- ============================================
CREATE TABLE Diagnostic (
    ID_Diagnostic SERIAL PRIMARY KEY,
    ID_Medecin INT NOT NULL,
    ID_Parametres INT NOT NULL,
    
    contenu TEXT NOT NULL,
    
    date_diagnostic TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clés étrangères
    CONSTRAINT fk_diag_medecin
        FOREIGN KEY (ID_Medecin)
        REFERENCES Medecin(ID_Medecin)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
        
    CONSTRAINT fk_diag_param
        FOREIGN KEY (ID_Parametres)
        REFERENCES Parametres(ID_Parametres)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Un seul diagnostic par paramètre
    CONSTRAINT unique_diagnostic_param UNIQUE (ID_Parametres)
);

-- Index Diagnostic
CREATE INDEX idx_diag_medecin ON Diagnostic(ID_Medecin);
CREATE INDEX idx_diag_param ON Diagnostic(ID_Parametres);
CREATE INDEX idx_diag_date ON Diagnostic(date_diagnostic DESC);

-- ============================================
-- TRIGGERS POUR AUTO-UPDATE DES TIMESTAMPS
-- ============================================

-- Fonction générique pour updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Appliquer aux tables concernées
CREATE TRIGGER update_medecin_updated_at 
    BEFORE UPDATE ON Medecin
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_dispositif_updated_at 
    BEFORE UPDATE ON Dispositif
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_diagnostic_updated_at 
    BEFORE UPDATE ON Diagnostic
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- TRIGGER : CHANGEMENT AUTOMATIQUE DU STATUT DES PARAMETRES
-- ============================================

-- Quand un diagnostic est créé, passer les paramètres à 'diagnostique'
CREATE OR REPLACE FUNCTION update_parametres_statut()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Parametres 
    SET statut = 'diagnostique' 
    WHERE ID_Parametres = NEW.ID_Parametres 
      AND statut = 'en_attente';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_parametres_statut
    AFTER INSERT ON Diagnostic
    FOR EACH ROW
    EXECUTE FUNCTION update_parametres_statut();

-- ============================================
-- VUES UTILES
-- ============================================

-- Vue : Tableau de bord médecin (paramètres en attente)
CREATE VIEW vue_parametres_en_attente AS
SELECT 
    p.ID_Parametres,
    p.identifiant_patient,
    p.poids_patient,
    p.temperature,
    p.pression_arterielle_systolique || '/' || p.pression_arterielle_diastolique as tension,
    p.frequence_foetale,
    p.date_mesure,
    d.nom_centre_de_sante,
    d.localisation
FROM Parametres p
JOIN Dispositif d ON p.ID_Dispositif = d.ID_Dispositif
WHERE p.statut = 'en_attente'
ORDER BY p.date_mesure DESC;

-- Vue : Historique complet avec diagnostics
CREATE VIEW vue_historique_complet AS
SELECT 
    p.ID_Parametres,
    p.identifiant_patient,
    p.date_mesure,
    p.temperature,
    p.pression_arterielle_systolique,
    p.pression_arterielle_diastolique,
    p.frequence_foetale,
    p.poids_patient,
    p.statut as statut_parametres,
    d.nom_centre_de_sante,
    diag.contenu as diagnostic,
    diag.date_diagnostic,
    m.nom || ' ' || m.prenom as medecin
FROM Parametres p
JOIN Dispositif d ON p.ID_Dispositif = d.ID_Dispositif
LEFT JOIN Diagnostic diag ON p.ID_Parametres = diag.ID_Parametres
LEFT JOIN Medecin m ON diag.ID_Medecin = m.ID_Medecin
ORDER BY p.date_mesure DESC;

-- ============================================
-- COMMENTAIRES SUR LES TABLES
-- ============================================

COMMENT ON TABLE Medecin IS 'Médecins inscrits dans le système';
COMMENT ON TABLE Dispositif IS 'Dispositifs de mesure installés dans les centres';
COMMENT ON TABLE Parametres IS 'Mesures des paramètres vitaux des patients';
COMMENT ON TABLE Diagnostic IS 'Diagnostics établis par les médecins (un seul par paramètre)';

COMMENT ON COLUMN Medecin.mot_de_passe IS 'Hash BCrypt du mot de passe (jamais en clair)';
COMMENT ON COLUMN Parametres.statut IS 'en_attente: pas de diagnostic | diagnostique: a un diagnostic | archive: archivé';
COMMENT ON COLUMN Diagnostic.ID_Parametres IS 'UNIQUE: un seul diagnostic par paramètre';