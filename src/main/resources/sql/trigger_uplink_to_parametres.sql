-- ============================================
-- TRIGGER POSTGRESQL : Traitement automatique des uplink_messages
-- ============================================
-- Ce script crée une fonction trigger qui :
-- 1. Lit les colonnes médicales individuelles d'un nouveau message uplink
-- 2. Génère le code patient unique (dev_eui + id_patient)
-- 3. Insère automatiquement dans la table parametres
-- ============================================
-- NOUVELLE STRUCTURE: les données médicales sont maintenant dans des
-- colonnes individuelles (age, bpm_moyen, ddr, fcf, glycemie, id_patient,
-- poids, taille, temperature, tension, tension_dia, tension_sys)
-- ============================================

-- Supprimer le trigger et la fonction s'ils existent déjà
DROP TRIGGER IF EXISTS trg_process_uplink_message ON uplink_messages;
DROP FUNCTION IF EXISTS fn_process_uplink_message();

-- ============================================
-- FONCTION TRIGGER
-- ============================================
CREATE OR REPLACE FUNCTION fn_process_uplink_message()
RETURNS TRIGGER AS $$
DECLARE
    v_code_patient TEXT;
    v_dispositif_id INTEGER;
    v_ddr DATE;
BEGIN
    -- Vérifier que l'id_patient est présent
    IF NEW.id_patient IS NULL THEN
        RAISE NOTICE 'id_patient manquant pour uplink_message id=%', NEW.id;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- Générer le code patient unique: dev_eui-id_patient
    v_code_patient := NEW.dev_eui || '-' || NEW.id_patient;

    -- Trouver le dispositif correspondant au DevEUI
    SELECT id_dispositif INTO v_dispositif_id 
    FROM dispositif 
    WHERE deveui = NEW.dev_eui;

    IF v_dispositif_id IS NULL THEN
        RAISE NOTICE 'Dispositif non trouvé pour DevEUI: %', NEW.dev_eui;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- Convertir la DDR si présente
    v_ddr := NULL;
    IF NEW.ddr IS NOT NULL AND NEW.ddr != '' THEN
        BEGIN
            v_ddr := CAST(NEW.ddr AS DATE);
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE 'Format DDR invalide: %. Erreur: %', NEW.ddr, SQLERRM;
        END;
    END IF;

    -- Archiver les mesures existantes en_attente pour ce patient
    UPDATE parametres 
    SET statut = 'archive' 
    WHERE identifiant_patient = v_code_patient 
      AND statut = 'en_attente';

    -- Insérer dans la table parametres (lecture directe des colonnes)
    INSERT INTO parametres (
        identifiant_patient,
        id_dispositif,
        poids_patient,
        taille_patient,
        temperature,
        pression_arterielle_systolique,
        pression_arterielle_diastolique,
        frequence_foetale,
        glycemie,
        age_patient,
        frequence_cardiaque_mere,
        date_dernieres_regles,
        date_mesure,
        statut,
        created_at
    ) VALUES (
        v_code_patient,
        v_dispositif_id,
        NEW.poids,
        NEW.taille,
        NEW.temperature,
        NEW.tension_sys,
        NEW.tension_dia,
        NEW.fcf,
        NEW.glycemie,
        NEW.age,
        NEW.bpm_moyen,
        v_ddr,
        COALESCE(NEW.published_at, NOW()),
        'en_attente',
        NOW()
    );

    RAISE NOTICE 'Parametres créé pour patient: % depuis uplink_message id=%', v_code_patient, NEW.id;

    -- Marquer le message comme traité
    NEW.processed := TRUE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- TRIGGER
-- ============================================
CREATE TRIGGER trg_process_uplink_message
    BEFORE INSERT ON uplink_messages
    FOR EACH ROW
    EXECUTE FUNCTION fn_process_uplink_message();

-- ============================================
-- COMMENTAIRES
-- ============================================
COMMENT ON FUNCTION fn_process_uplink_message() IS 
'Fonction trigger qui lit les colonnes médicales individuelles des messages uplink LoRaWAN et crée les entrées Parametres correspondantes. Colonnes: id_patient, poids, taille, temperature, tension_sys, tension_dia, fcf, glycemie, age, bpm_moyen, ddr';

COMMENT ON TRIGGER trg_process_uplink_message ON uplink_messages IS 
'Trigger déclenché à chaque insertion dans uplink_messages pour créer automatiquement les Parametres';

-- ============================================
-- TEST (décommentez pour tester)
-- ============================================
-- INSERT INTO uplink_messages (
--     dev_eui, id_patient, poids, taille, temperature, 
--     tension_sys, tension_dia, fcf, glycemie, age, bpm_moyen, ddr,
--     published_at, processed
-- ) VALUES (
--     'A840410001819123', 5, 65.50, 165.00, 36.80, 
--     120, 80, 142, 5.20, 28, 75, '2025-06-15',
--     NOW(), false
-- );
-- 
-- SELECT * FROM parametres ORDER BY id_parametres DESC LIMIT 1;
