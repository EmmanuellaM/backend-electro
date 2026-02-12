-- ============================================================================
-- SCRIPT DE CRÉATION DE LA BASE DE DONNÉES MATERNICARE
-- PostgreSQL
-- Généré le 22 janvier 2026
-- ============================================================================

-- Création de la base de données (à exécuter une seule fois avec un superuser)
-- CREATE DATABASE bd_electro;
-- \c bd_electro;

-- ============================================================================
-- DROP DES TABLES ET TYPES (Ordre respectant les contraintes)
-- ============================================================================
DROP TABLE IF EXISTS seuil_maintenance CASCADE;
DROP TABLE IF EXISTS notification_sms CASCADE;
DROP TABLE IF EXISTS sms_messages CASCADE;
DROP TABLE IF EXISTS feedback_ia CASCADE;
DROP TABLE IF EXISTS diagnostic_ia CASCADE;
DROP TABLE IF EXISTS diagnostic CASCADE;
DROP TABLE IF EXISTS parametres CASCADE;
DROP TABLE IF EXISTS dispositif CASCADE;
DROP TABLE IF EXISTS medecin CASCADE;
DROP TABLE IF EXISTS infirmier_local CASCADE;
DROP TABLE IF EXISTS administrateur CASCADE;
DROP TABLE IF EXISTS uplink_messages CASCADE;
DROP TABLE IF EXISTS password_reset_token CASCADE;

DROP TYPE IF EXISTS role_type CASCADE;
DROP TYPE IF EXISTS statut_administrateur_type CASCADE;
DROP TYPE IF EXISTS statut_medecin_type CASCADE;
DROP TYPE IF EXISTS statut_dispositif_type CASCADE;
DROP TYPE IF EXISTS genre_type CASCADE;
DROP TYPE IF EXISTS specialite_medecin_type CASCADE;
DROP TYPE IF EXISTS sms_status_type CASCADE;

-- ============================================================================
-- TYPES ENUM
-- ============================================================================

-- Role des utilisateurs
CREATE TYPE role_type AS ENUM ('ADMIN', 'SUPER_ADMIN', 'MEDECIN');

-- Statut des administrateurs
CREATE TYPE statut_administrateur_type AS ENUM ('ACTIF', 'SUSPENDU', 'INACTIF');

-- Statut des médecins
CREATE TYPE statut_medecin_type AS ENUM ('ACTIF', 'INACTIF', 'SUSPENDU');

-- Statut des dispositifs
CREATE TYPE statut_dispositif_type AS ENUM ('ACTIF', 'INACTIF', 'MAINTENANCE', 'EN_ATTENTE', 'NON_ATTRIBUE');

-- Genre
CREATE TYPE genre_type AS ENUM ('MASCULIN', 'FEMININ');

-- Spécialité du médecin
CREATE TYPE specialite_medecin_type AS ENUM (
    'GYNECOLOGIE_OBSTETRIQUE',
    'MEDECINE_GENERALE',
    'CARDIOLOGIE',
    'PEDIATRIE',
    'DERMATOLOGIE',
    'NEUROLOGIE',
    'OPHTALMOLOGIE',
    'ORL',
    'CHIRURGIE_GENERALE',
    'SAGE_FEMME',
    'ANESTHESIE_REANIMATION',
    'RADIOLOGIE',
    'PSYCHIATRIE',
    'AUTRE'
);

-- Statut des SMS
CREATE TYPE sms_status_type AS ENUM ('PENDING', 'SENT', 'FAILED', 'SIMULATED');

-- ============================================================================
-- TABLE : administrateur
-- Responsable de la création des médecins, infirmiers et dispositifs
-- ============================================================================
CREATE TABLE administrateur (
    id_admin SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    statut VARCHAR(20) DEFAULT 'ACTIF',
    numero_cni VARCHAR(50),
    tel VARCHAR(20),
    tel2 VARCHAR(20),
    genre VARCHAR(10),
    doit_changer_mot_de_passe BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_admin_role CHECK (role IN ('ADMIN', 'SUPER_ADMIN')),
    CONSTRAINT chk_admin_statut CHECK (statut IN ('ACTIF', 'SUSPENDU', 'INACTIF')),
    CONSTRAINT chk_admin_genre CHECK (genre IN ('MASCULIN', 'FEMININ'))
);

-- Index
CREATE INDEX idx_admin_email ON administrateur(email);
CREATE INDEX idx_admin_statut ON administrateur(statut);

-- ============================================================================
-- TABLE : infirmier_local
-- Infirmiers responsables de la collecte des données sur le terrain
-- ============================================================================
CREATE TABLE infirmier_local (
    id_infirmier SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone1 VARCHAR(20) NOT NULL,
    telephone2 VARCHAR(20),
    zone_affectation VARCHAR(200) NOT NULL,
    statut VARCHAR(20) DEFAULT 'actif',
    genre VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_administrateur INTEGER,
    
    CONSTRAINT fk_infirmier_admin
        FOREIGN KEY (id_administrateur)
        REFERENCES administrateur(id_admin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_infirmier_genre CHECK (genre IN ('MASCULIN', 'FEMININ'))
);

-- Index
CREATE INDEX idx_infirmier_zone ON infirmier_local(zone_affectation);
CREATE INDEX idx_infirmier_statut ON infirmier_local(statut);
CREATE INDEX idx_infirmier_admin ON infirmier_local(id_administrateur);

-- ============================================================================
-- TABLE : medecin
-- Médecins responsables des diagnostics
-- ============================================================================
CREATE TABLE medecin (
    id_medecin SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    tel VARCHAR(20),
    mot_de_passe VARCHAR(255),
    numero_carte_identite VARCHAR(50) NOT NULL UNIQUE,
    statut VARCHAR(20) DEFAULT 'ACTIF',
    genre VARCHAR(10),
    specialite VARCHAR(50),
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_connexion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_administrateur INTEGER,
    
    CONSTRAINT fk_medecin_admin
        FOREIGN KEY (id_administrateur)
        REFERENCES administrateur(id_admin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_medecin_statut CHECK (statut IN ('ACTIF', 'INACTIF', 'SUSPENDU')),
    CONSTRAINT chk_medecin_genre CHECK (genre IN ('MASCULIN', 'FEMININ')),
    CONSTRAINT chk_medecin_specialite CHECK (specialite IN (
        'GYNECOLOGIE_OBSTETRIQUE',
        'MEDECINE_GENERALE',
        'CARDIOLOGIE',
        'PEDIATRIE',
        'DERMATOLOGIE',
        'NEUROLOGIE',
        'OPHTALMOLOGIE',
        'ORL',
        'CHIRURGIE_GENERALE',
        'SAGE_FEMME',
        'ANESTHESIE_REANIMATION',
        'RADIOLOGIE',
        'PSYCHIATRIE',
        'AUTRE'
    ))
);

-- Index
CREATE INDEX idx_medecin_email ON medecin(email);
CREATE INDEX idx_medecin_statut ON medecin(statut);
CREATE INDEX idx_medecin_admin ON medecin(id_administrateur);
CREATE INDEX idx_medecin_cni ON medecin(numero_carte_identite);

-- ============================================================================
-- TABLE : dispositif
-- Boîtiers LoRaWAN de collecte des données médicales
-- ============================================================================
CREATE TABLE dispositif (
    id_dispositif SERIAL PRIMARY KEY,
    deveui VARCHAR(50) NOT NULL UNIQUE,
    appeui VARCHAR(50) NOT NULL DEFAULT '0000000000000000',
    appkey VARCHAR(50) NOT NULL,
    nom_centre_de_sante VARCHAR(150) NOT NULL,
    localisation VARCHAR(200),
    contact VARCHAR(50),
    statut VARCHAR(50) DEFAULT 'NON_ATTRIBUE',
    date_installation DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_infirmier_local INTEGER,
    id_administrateur INTEGER,
    
    CONSTRAINT fk_dispositif_infirmier
        FOREIGN KEY (id_infirmier_local)
        REFERENCES infirmier_local(id_infirmier)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_dispositif_admin
        FOREIGN KEY (id_administrateur)
        REFERENCES administrateur(id_admin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_dispositif_statut CHECK (statut IN ('ACTIF', 'INACTIF', 'MAINTENANCE', 'EN_ATTENTE', 'NON_ATTRIBUE'))
);

-- Index
CREATE INDEX idx_dispositif_deveui ON dispositif(deveui);
CREATE INDEX idx_dispositif_statut ON dispositif(statut);
CREATE INDEX idx_dispositif_infirmier ON dispositif(id_infirmier_local);
CREATE INDEX idx_dispositif_admin ON dispositif(id_administrateur);

-- ============================================================================
-- TABLE : parametres
-- Paramètres médicaux des patientes (données collectées par les dispositifs)
-- ============================================================================
CREATE TABLE parametres (
    id_parametres SERIAL PRIMARY KEY,
    identifiant_patient VARCHAR(50) NOT NULL,
    poids_patient NUMERIC(5,2) NOT NULL,
    age_patient INTEGER,
    temperature NUMERIC(4,2) NOT NULL,
    pression_arterielle_systolique INTEGER,
    pression_arterielle_diastolique INTEGER,
    frequence_foetale INTEGER,
    glycemie NUMERIC(4,2),
    saturation_oxygene INTEGER,
    date_dernieres_regles DATE,
    date_mesure TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'en_attente',
    verrouille_par_medecin_id INTEGER,
    verrouille_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_dispositif INTEGER NOT NULL,
    
    CONSTRAINT fk_param_dispositif
        FOREIGN KEY (id_dispositif)
        REFERENCES dispositif(id_dispositif)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_poids CHECK (poids_patient > 0 AND poids_patient < 300),
    CONSTRAINT chk_age CHECK (age_patient IS NULL OR (age_patient >= 10 AND age_patient <= 100)),
    CONSTRAINT chk_temperature CHECK (temperature >= 30 AND temperature <= 45),
    CONSTRAINT chk_pression_sys CHECK (pression_arterielle_systolique IS NULL OR (pression_arterielle_systolique BETWEEN 40 AND 300)),
    CONSTRAINT chk_pression_dia CHECK (pression_arterielle_diastolique IS NULL OR (pression_arterielle_diastolique BETWEEN 20 AND 200)),
    CONSTRAINT chk_freq_foetale CHECK (frequence_foetale IS NULL OR (frequence_foetale BETWEEN 50 AND 220)),
    CONSTRAINT chk_saturation_oxygene CHECK (saturation_oxygene IS NULL OR (saturation_oxygene BETWEEN 50 AND 100)),
    CONSTRAINT chk_statut_param CHECK (statut IN ('en_attente', 'diagnostique', 'archive'))
);

-- Index
CREATE INDEX idx_param_dispositif ON parametres(id_dispositif);
CREATE INDEX idx_param_patient ON parametres(identifiant_patient);
CREATE INDEX idx_param_statut ON parametres(statut);
CREATE INDEX idx_param_date_mesure ON parametres(date_mesure);
CREATE INDEX idx_param_verrouille ON parametres(verrouille_par_medecin_id);

-- ============================================================================
-- TABLE : diagnostic
-- Diagnostics établis par les médecins
-- ============================================================================
CREATE TABLE diagnostic (
    id_diagnostic SERIAL PRIMARY KEY,
    contenu TEXT NOT NULL,
    date_diagnostic TIMESTAMP,
    date_validation TIMESTAMP,
    recommandations TEXT,
    niveau_urgence VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_medecin INTEGER NOT NULL,
    id_parametres INTEGER NOT NULL UNIQUE,
    
    CONSTRAINT fk_diag_medecin
        FOREIGN KEY (id_medecin)
        REFERENCES medecin(id_medecin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_diag_parametres
        FOREIGN KEY (id_parametres)
        REFERENCES parametres(id_parametres)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_niveau_urgence CHECK (niveau_urgence IS NULL OR niveau_urgence IN ('NORMAL', 'CRITIQUE'))
);

-- Index
CREATE INDEX idx_diag_medecin ON diagnostic(id_medecin);
CREATE INDEX idx_diag_parametres ON diagnostic(id_parametres);
CREATE INDEX idx_diag_date ON diagnostic(date_diagnostic);
CREATE INDEX idx_diag_urgence ON diagnostic(niveau_urgence);

-- ============================================================================
-- TABLE : diagnostic_ia
-- Diagnostics générés par l'intelligence artificielle
-- ============================================================================
CREATE TABLE diagnostic_ia (
    id_diagnostic_ia SERIAL PRIMARY KEY,
    classe_predite VARCHAR(50) NOT NULL,
    score_confiance DOUBLE PRECISION NOT NULL,
    explication_json TEXT,
    recommandations TEXT,
    probabilites TEXT,
    valide_par_medecin BOOLEAN,
    commentaire_medecin TEXT,
    note_ia INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_parametres INTEGER NOT NULL,
    id_medecin_validateur INTEGER,
    
    CONSTRAINT fk_diag_ia_parametres
        FOREIGN KEY (id_parametres)
        REFERENCES parametres(id_parametres)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_diag_ia_medecin
        FOREIGN KEY (id_medecin_validateur)
        REFERENCES medecin(id_medecin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_score_confiance CHECK (score_confiance >= 0 AND score_confiance <= 1),
    CONSTRAINT chk_note_ia CHECK (note_ia IS NULL OR (note_ia >= 1 AND note_ia <= 5)),
    CONSTRAINT chk_classe_predite CHECK (classe_predite IN (
        'normal', 
        'pre_eclampsie', 
        'diabete_gestationnel', 
        'infection', 
        'souffrance_foetale', 
        'travail_premature'
    ))
);

-- Index
CREATE INDEX idx_diag_ia_parametres ON diagnostic_ia(id_parametres);
CREATE INDEX idx_diag_ia_classe ON diagnostic_ia(classe_predite);
CREATE INDEX idx_diag_ia_validateur ON diagnostic_ia(id_medecin_validateur);
CREATE INDEX idx_diag_ia_valide ON diagnostic_ia(valide_par_medecin);

-- ============================================================================
-- TABLE : notification_sms
-- Notifications SMS envoyées aux infirmiers
-- ============================================================================
CREATE TABLE notification_sms (
    id_notification SERIAL PRIMARY KEY,
    contenu_message TEXT NOT NULL,
    numero_destinataire VARCHAR(20) NOT NULL,
    date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    succes BOOLEAN DEFAULT FALSE,
    id_infirmier_local INTEGER,
    id_diagnostic INTEGER,
    
    CONSTRAINT fk_notif_infirmier
        FOREIGN KEY (id_infirmier_local)
        REFERENCES infirmier_local(id_infirmier)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_notif_diagnostic
        FOREIGN KEY (id_diagnostic)
        REFERENCES diagnostic(id_diagnostic)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Index
CREATE INDEX idx_notif_infirmier ON notification_sms(id_infirmier_local);
CREATE INDEX idx_notif_diagnostic ON notification_sms(id_diagnostic);
CREATE INDEX idx_notif_date ON notification_sms(date_envoi);

-- ============================================================================
-- TABLE : sms_messages
-- Historique complet des SMS envoyés
-- ============================================================================
CREATE TABLE sms_messages (
    id SERIAL PRIMARY KEY,
    infirmier_local_id INTEGER NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_by VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SIMULATED',
    
    CONSTRAINT fk_sms_infirmier
        FOREIGN KEY (infirmier_local_id)
        REFERENCES infirmier_local(id_infirmier)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_sms_status CHECK (status IN ('PENDING', 'SENT', 'FAILED', 'SIMULATED'))
);

-- Index
CREATE INDEX idx_sms_infirmier ON sms_messages(infirmier_local_id);
CREATE INDEX idx_sms_status ON sms_messages(status);
CREATE INDEX idx_sms_sent_at ON sms_messages(sent_at);

-- ============================================================================
-- TABLE : feedback_ia
-- Feedbacks des médecins sur les prédictions IA (amélioration continue)
-- ============================================================================
CREATE TABLE feedback_ia (
    id_feedback SERIAL PRIMARY KEY,
    age_patient INTEGER NOT NULL,
    poids_patient DOUBLE PRECISION NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    pression_systolique INTEGER NOT NULL,
    pression_diastolique INTEGER NOT NULL,
    frequence_foetale INTEGER NOT NULL,
    glycemie DOUBLE PRECISION,
    classe_predite VARCHAR(100) NOT NULL,
    score_confiance DOUBLE PRECISION NOT NULL,
    explication_medecin TEXT,
    note_medecin INTEGER NOT NULL,
    commentaire_medecin TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_medecin INTEGER NOT NULL,
    id_parametres INTEGER,
    
    CONSTRAINT fk_feedback_medecin
        FOREIGN KEY (id_medecin)
        REFERENCES medecin(id_medecin)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_feedback_parametres
        FOREIGN KEY (id_parametres)
        REFERENCES parametres(id_parametres)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    CONSTRAINT chk_note_medecin CHECK (note_medecin >= 1 AND note_medecin <= 5)
);

-- Index
CREATE INDEX idx_feedback_medecin ON feedback_ia(id_medecin);
CREATE INDEX idx_feedback_parametres ON feedback_ia(id_parametres);
CREATE INDEX idx_feedback_classe ON feedback_ia(classe_predite);

-- ============================================================================
-- TABLE : uplink_messages
-- Messages LoRaWAN reçus de ChirpStack (LECTURE SEULE)
-- ============================================================================
CREATE TABLE uplink_messages (
    id SERIAL PRIMARY KEY,
    application_id VARCHAR(100),
    application_name VARCHAR(100),
    device_name VARCHAR(100),
    device_profile_name VARCHAR(100),
    device_profile_id UUID,
    dev_eui VARCHAR(50),
    frequency BIGINT,
    dr INTEGER,
    adr BOOLEAN,
    f_cnt INTEGER,
    f_port INTEGER,
    data_base64 TEXT,
    text_payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN DEFAULT FALSE
);

-- Index
CREATE INDEX idx_uplink_deveui ON uplink_messages(dev_eui);
CREATE INDEX idx_uplink_processed ON uplink_messages(processed);
CREATE INDEX idx_uplink_created ON uplink_messages(created_at);

-- ============================================================================
-- TABLE : password_reset_token
-- Tokens pour la réinitialisation de mot de passe
-- ============================================================================
CREATE TABLE password_reset_token (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    token VARCHAR(10) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index
CREATE INDEX idx_reset_email ON password_reset_token(email);
CREATE INDEX idx_reset_token ON password_reset_token(token);
CREATE INDEX idx_reset_expiry ON password_reset_token(expiry_date);

-- ============================================================================
-- TABLE : seuil_maintenance
-- Seuils configurables pour la détection de maintenance des dispositifs
-- Un seul enregistrement (singleton, id=1)
-- ============================================================================
CREATE TABLE seuil_maintenance (
    id INTEGER PRIMARY KEY DEFAULT 1,
    temperature_min NUMERIC(4,1) NOT NULL DEFAULT 35.5,
    temperature_max NUMERIC(4,1) NOT NULL DEFAULT 38.5,
    frequence_foetale_min INTEGER NOT NULL DEFAULT 110,
    frequence_foetale_max INTEGER NOT NULL DEFAULT 160,
    pression_systolique_max INTEGER NOT NULL DEFAULT 140,
    pression_diastolique_max INTEGER NOT NULL DEFAULT 90,
    glycemie_max NUMERIC(4,1) NOT NULL DEFAULT 7.0,
    saturation_oxygene_min INTEGER NOT NULL DEFAULT 95,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_seuil_singleton CHECK (id = 1),
    CONSTRAINT chk_seuil_temp_min CHECK (temperature_min >= 30.0 AND temperature_min <= 42.0),
    CONSTRAINT chk_seuil_temp_max CHECK (temperature_max >= 30.0 AND temperature_max <= 42.0),
    CONSTRAINT chk_seuil_fcf_min CHECK (frequence_foetale_min >= 50 AND frequence_foetale_min <= 220),
    CONSTRAINT chk_seuil_fcf_max CHECK (frequence_foetale_max >= 50 AND frequence_foetale_max <= 220),
    CONSTRAINT chk_seuil_sys_max CHECK (pression_systolique_max >= 40 AND pression_systolique_max <= 300),
    CONSTRAINT chk_seuil_dia_max CHECK (pression_diastolique_max >= 20 AND pression_diastolique_max <= 200),
    CONSTRAINT chk_seuil_glyc_max CHECK (glycemie_max >= 1.0 AND glycemie_max <= 30.0),
    CONSTRAINT chk_seuil_spo2_min CHECK (saturation_oxygene_min >= 50 AND saturation_oxygene_min <= 100)
);

-- Insérer l'enregistrement par défaut
INSERT INTO seuil_maintenance (id) VALUES (1) ON CONFLICT DO NOTHING;

-- Trigger updated_at pour seuil_maintenance
CREATE TRIGGER update_seuil_maintenance_updated_at
    BEFORE UPDATE ON seuil_maintenance
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================================================
-- TRIGGERS
-- ============================================================================

-- Trigger pour mettre à jour updated_at automatiquement
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Appliquer le trigger sur les tables concernées
CREATE TRIGGER update_administrateur_updated_at
    BEFORE UPDATE ON administrateur
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_infirmier_updated_at
    BEFORE UPDATE ON infirmier_local
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_medecin_updated_at
    BEFORE UPDATE ON medecin
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_dispositif_updated_at
    BEFORE UPDATE ON dispositif
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_diagnostic_updated_at
    BEFORE UPDATE ON diagnostic
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_diagnostic_ia_updated_at
    BEFORE UPDATE ON diagnostic_ia
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour changer le statut des paramètres après création d'un diagnostic
CREATE OR REPLACE FUNCTION update_parametres_statut_on_diagnostic()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE parametres 
    SET statut = 'diagnostique' 
    WHERE id_parametres = NEW.id_parametres;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_update_param_on_diag
    AFTER INSERT ON diagnostic
    FOR EACH ROW EXECUTE FUNCTION update_parametres_statut_on_diagnostic();

-- ============================================================================
-- TRIGGER : Traitement automatique des uplink_messages vers parametres
-- Ce trigger parse le payload LoRaWAN et crée automatiquement les paramètres
-- ============================================================================

-- Fonction trigger pour traiter les messages uplink
CREATE OR REPLACE FUNCTION fn_process_uplink_message()
RETURNS TRIGGER AS $$
DECLARE
    v_id_local TEXT;
    v_poids DECIMAL(10,2);
    v_temperature DECIMAL(5,2);
    v_sys INTEGER;
    v_dia INTEGER;
    v_fcf INTEGER;
    v_glycemie DECIMAL(10,2);
    v_age INTEGER;
    v_spo2 INTEGER;
    v_ddr DATE;
    v_code_patient TEXT;
    v_dispositif_id INTEGER;
    v_parts TEXT[];
    v_nb_parts INTEGER;
BEGIN
    -- Vérifier que le payload n'est pas vide
    IF NEW.text_payload IS NULL OR NEW.text_payload = '' THEN
        RAISE NOTICE 'Payload vide pour uplink_message id=%', NEW.id;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- Séparer le payload par le délimiteur ';'
    -- Format attendu: ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE[;SPO2;DDR]
    v_parts := string_to_array(NEW.text_payload, ';');
    v_nb_parts := array_length(v_parts, 1);

    -- Vérifier le nombre de champs (minimum 8, max 10)
    IF v_nb_parts < 8 THEN
        RAISE NOTICE 'Format payload invalide. Attendu >= 8 champs, reçu %. Payload: %', 
                     v_nb_parts, NEW.text_payload;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- Extraire les valeurs
    BEGIN
        v_id_local := TRIM(v_parts[1]);
        v_poids := CAST(TRIM(v_parts[2]) AS DECIMAL(10,2));
        v_temperature := CAST(TRIM(v_parts[3]) AS DECIMAL(5,2));
        v_sys := CAST(TRIM(v_parts[4]) AS INTEGER);
        v_dia := CAST(TRIM(v_parts[5]) AS INTEGER);
        v_fcf := CAST(TRIM(v_parts[6]) AS INTEGER);
        v_glycemie := CAST(TRIM(v_parts[7]) AS DECIMAL(10,2));
        v_age := CAST(TRIM(v_parts[8]) AS INTEGER);

        -- Champs optionnels (SpO2 et DDR)
        IF v_nb_parts >= 9 AND TRIM(v_parts[9]) != '' THEN
            v_spo2 := CAST(TRIM(v_parts[9]) AS INTEGER);
        END IF;
        IF v_nb_parts >= 10 AND TRIM(v_parts[10]) != '' THEN
            v_ddr := CAST(TRIM(v_parts[10]) AS DATE);
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'Erreur de parsing du payload: %. Payload: %', SQLERRM, NEW.text_payload;
        NEW.processed := TRUE;
        RETURN NEW;
    END;

    -- Générer le code patient unique: DevEUI-ID_LOCAL
    v_code_patient := NEW.dev_eui || '-' || v_id_local;

    -- Trouver le dispositif correspondant au DevEUI
    SELECT id_dispositif INTO v_dispositif_id 
    FROM dispositif 
    WHERE deveui = NEW.dev_eui;

    IF v_dispositif_id IS NULL THEN
        RAISE NOTICE 'Dispositif non trouvé pour DevEUI: %', NEW.dev_eui;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- ARCHIVAGE AUTOMATIQUE : Passer les anciennes mesures "en_attente" en "archive"
    -- pour ce patient spécifique avant d'insérer la nouvelle
    UPDATE parametres 
    SET statut = 'archive' 
    WHERE identifiant_patient = v_code_patient 
    AND statut = 'en_attente';

    -- Insérer dans la table parametres
    INSERT INTO parametres (
        identifiant_patient,
        id_dispositif,
        poids_patient,
        temperature,
        pression_arterielle_systolique,
        pression_arterielle_diastolique,
        frequence_foetale,
        glycemie,
        age_patient,
        saturation_oxygene,
        date_dernieres_regles,
        date_mesure,
        statut,
        created_at
    ) VALUES (
        v_code_patient,
        v_dispositif_id,
        v_poids,
        v_temperature,
        v_sys,
        v_dia,
        v_fcf,
        v_glycemie,
        v_age,
        v_spo2,
        v_ddr,
        COALESCE(NEW.created_at, NOW()),
        'en_attente',
        NOW()
    );

    RAISE NOTICE 'Parametres créé pour patient: % depuis uplink_message id=%', v_code_patient, NEW.id;

    -- Marquer le message comme traité
    NEW.processed := TRUE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger déclenché à chaque insertion dans uplink_messages
CREATE TRIGGER trg_process_uplink_message
    BEFORE INSERT ON uplink_messages
    FOR EACH ROW
    EXECUTE FUNCTION fn_process_uplink_message();

-- Commentaires descriptifs
COMMENT ON FUNCTION fn_process_uplink_message() IS 
'Fonction trigger qui parse automatiquement les messages uplink LoRaWAN et crée les entrées Parametres correspondantes. Format payload: ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE[;SPO2;DDR]';

COMMENT ON TRIGGER trg_process_uplink_message ON uplink_messages IS 
'Trigger déclenché à chaque insertion dans uplink_messages pour créer automatiquement les Parametres';
