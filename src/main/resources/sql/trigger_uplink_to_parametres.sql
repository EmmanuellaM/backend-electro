-- ============================================
-- TRIGGER POSTGRESQL : Traitement automatique des uplink_messages
-- ============================================
-- Ce script crée une fonction trigger qui :
-- 1. Parse le text_payload d'un nouveau message uplink
-- 2. Génère le code patient unique (deveui + id_local)
-- 3. Insère automatiquement dans la table parametres
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
    v_id_local TEXT;
    v_poids DECIMAL(10,2);
    v_temperature DECIMAL(5,2);
    v_sys INTEGER;
    v_dia INTEGER;
    v_fcf INTEGER;
    v_glycemie DECIMAL(10,2);
    v_age INTEGER;
    v_code_patient TEXT;
    v_dispositif_id INTEGER;
    v_parts TEXT[];
BEGIN
    -- Vérifier que le payload n'est pas vide
    IF NEW.text_payload IS NULL OR NEW.text_payload = '' THEN
        RAISE NOTICE 'Payload vide pour uplink_message id=%', NEW.id;
        NEW.processed := TRUE;
        RETURN NEW;
    END IF;

    -- Séparer le payload par le délimiteur ';'
    -- Format attendu: ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE
    v_parts := string_to_array(NEW.text_payload, ';');

    -- Vérifier le nombre de champs
    IF array_length(v_parts, 1) < 8 THEN
        RAISE NOTICE 'Format payload invalide. Attendu 8 champs, reçu %. Payload: %', 
                     array_length(v_parts, 1), NEW.text_payload;
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
'Fonction trigger qui parse automatiquement les messages uplink LoRaWAN et crée les entrées Parametres correspondantes. Format payload: ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE';

COMMENT ON TRIGGER trg_process_uplink_message ON uplink_messages IS 
'Trigger déclenché à chaque insertion dans uplink_messages pour créer automatiquement les Parametres';

-- ============================================
-- TEST (décommentez pour tester)
-- ============================================
-- INSERT INTO uplink_messages (dev_eui, text_payload, created_at, processed) 
-- VALUES ('A840410001819123', 'P05;65.5;36.8;120;80;142;5.2;28', NOW(), false);
-- 
-- SELECT * FROM parametres ORDER BY id DESC LIMIT 1;
