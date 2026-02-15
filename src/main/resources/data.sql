-- ============================================================================
-- JEU DE DONNÉES DE TEST - MATERNICARE
-- ============================================================================

-- Nettoyage des données existantes (Ordre respectant les contraintes)
TRUNCATE TABLE notification_sms, sms_messages, feedback_ia, diagnostic_ia, diagnostic, parametres, dispositif, medecin, infirmier_local, administrateur CASCADE;

-- Réinitialisation des séquences
ALTER SEQUENCE administrateur_id_admin_seq RESTART WITH 1;
ALTER SEQUENCE infirmier_local_id_infirmier_seq RESTART WITH 1;
ALTER SEQUENCE medecin_id_medecin_seq RESTART WITH 1;
ALTER SEQUENCE dispositif_id_dispositif_seq RESTART WITH 1;
ALTER SEQUENCE parametres_id_parametres_seq RESTART WITH 1;
ALTER SEQUENCE diagnostic_id_diagnostic_seq RESTART WITH 1;
ALTER SEQUENCE diagnostic_ia_id_diagnostic_ia_seq RESTART WITH 1;
ALTER SEQUENCE notification_sms_id_notification_seq RESTART WITH 1;

-- 1. ADMINISTRATEURS
-- Mot de passe : password1!
INSERT INTO administrateur (nom, email, mot_de_passe, role, statut, numero_cni, tel, tel2, genre, doit_changer_mot_de_passe) VALUES
('Super Administrateur', 'superadmin@maternicare.com', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'SUPER_ADMIN', 'ACTIF', '1092837465', '+237600000000', NULL, 'MASCULIN', FALSE),
('Dr. Sadi Junior', 'admin@maternicare.com', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'ADMIN', 'ACTIF', '1122334455', '+237611111111', '+237622222222', 'MASCULIN', FALSE),
('Admin Régional Nord', 'admin.nord@maternicare.com', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'ADMIN', 'ACTIF', '9988776655', '+237633333333', NULL, 'FEMININ', FALSE);

-- 2. INFIRMIERS LOCAUX
INSERT INTO infirmier_local (nom, prenom, telephone1, telephone2, zone_affectation, statut, genre, id_administrateur) VALUES
('Eboa', 'Samuel', '+237699001122', '+237677001122', 'Centre de Santé Biyem-Assi', 'ACTIF', 'MASCULIN', 2),
('Mekoulou', 'Thérèse', '+237699003344', NULL, 'Hôpital de District d''Efoulan', 'ACTIF', 'FEMININ', 2),
('Abena', 'Cécile', '+237699005566', '+237677005566', 'Centre de Santé de Mvolyé', 'ACTIF', 'FEMININ', 3),
('Zambo', 'Patrick', '+237699112233', NULL, 'Poste de Santé Akwa', 'ACTIF', 'MASCULIN', 3);

-- 3. MÉDECINS
INSERT INTO medecin (nom, prenom, email, tel, mot_de_passe, numero_carte_identite, statut, genre, specialite, id_administrateur) VALUES
('Mbarga', 'Paul', 'paul.mbarga@hopital.cm', '+237655001122', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'CNI-100200300', 'ACTIF', 'MASCULIN', 'GYNECOLOGIE_OBSTETRIQUE', 2),
('Ngo Ngué', 'Esther', 'esther.ngo@hopital.cm', '+237655112233', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'CNI-400500600', 'ACTIF', 'FEMININ', 'SAGE_FEMME', 2),
('Talla', 'Rodrigue', 'rodrigue.talla@hopital.cm', '+237655445566', '$2a$10$0i1ud/czxHnaakCXcBnAb.VFJO4Lo4WjAt17u4zwmCdNPyZ.ic05G', 'CNI-700800900', 'ACTIF', 'MASCULIN', 'MEDECINE_GENERALE', 3);

-- 4. DISPOSITIFS
INSERT INTO dispositif (deveui, appeui, appkey, nom_centre_de_sante, localisation, contact, statut, date_installation, id_infirmier_local, id_administrateur) VALUES
('A840411AB22C33D1', '0000000000000000', '2B7E151628AED2A6ABF7158809CF4F3C', 'Hôpital de District de Biyem-Assi', 'Yaoundé, Biyem-Assi', '+237222001122', 'ACTIF', '2023-11-15', 1, 2),
('A840411AB22C33D2', '0000000000000000', '2B7E151628AED2A6ABF7158809CF4F3C', 'Centre Médical d''Arrondissement d''Efoulan', 'Yaoundé, Efoulan', '+237222113344', 'ACTIF', '2023-12-01', 2, 2),
('A840411AB22C33D3', '0000000000000000', '2B7E151628AED2A6ABF7158809CF4F3C', 'Poste de Santé de Mvolyé', 'Yaoundé, Mvolyé', '+237222556677', 'EN_ATTENTE', NULL, 2, 3),
('B840411AB22C33E4', '0000000000000000', '2B7E151628AED2A6ABF7158809CF4F3C', 'Non défini', 'Non défini', 'Non défini', 'NON_ATTRIBUE', NULL, NULL, NULL),
('B840411AB22C33E5', '0000000000000000', '2B7E151628AED2A6ABF7158809CF4F3C', 'Non défini', 'Non défini', 'Non défini', 'NON_ATTRIBUE', NULL, NULL, NULL);

-- 5. PARAMETRES
INSERT INTO parametres (identifiant_patient, poids_patient, taille_patient, age_patient, temperature, pression_arterielle_systolique, pression_arterielle_diastolique, frequence_foetale, glycemie, saturation_oxygene, date_dernieres_regles, date_mesure, statut, id_dispositif) VALUES
-- PAT-YDE-001 (Patient 1) - Profil Stable / Normal
('A840411AB22C33D1-PAT-YDE-001', 72.5, 165.0, 26, 37.1, 118, 78, 142, 0.92, 98, '2025-08-10', NOW() - INTERVAL '30 minutes', 'en_attente', 1), -- ID 1
('A840411AB22C33D1-PAT-YDE-001', 72.3, 165.0, 26, 36.8, 115, 75, 140, 0.90, 97, '2025-08-10', NOW() - INTERVAL '1 day', 'archive', 1),        -- ID 2
('A840411AB22C33D1-PAT-YDE-001', 72.0, 165.0, 26, 37.0, 112, 72, 138, 0.88, 98, '2025-08-10', NOW() - INTERVAL '2 days', 'archive', 1),       -- ID 3
('A840411AB22C33D1-PAT-YDE-001', 71.8, 165.0, 26, 36.9, 114, 74, 139, 0.87, 97, '2025-08-10', NOW() - INTERVAL '3 days', 'archive', 1),       -- ID 4
('A840411AB22C33D1-PAT-YDE-001', 71.5, 165.0, 26, 37.0, 110, 70, 137, 0.85, 98, '2025-08-10', NOW() - INTERVAL '4 days', 'archive', 1),       -- ID 5

-- PAT-YDE-002 (Patient 2) - Profil Dégradation (Pré-éclampsie + Fièvre)
('A840411AB22C33D1-PAT-YDE-002', 65.0, 160.0, 22, 38.6, 150, 98, 165, 1.15, 94, '2025-09-15', NOW() - INTERVAL '2 hours', 'en_attente', 1),    -- ID 6
('A840411AB22C33D1-PAT-YDE-002', 64.8, 160.0, 22, 37.9, 142, 92, 158, 1.10, 95, '2025-09-15', NOW() - INTERVAL '6 hours', 'archive', 1),       -- ID 7
('A840411AB22C33D1-PAT-YDE-002', 64.5, 160.0, 22, 37.2, 135, 90, 150, 1.05, 96, '2025-09-15', NOW() - INTERVAL '12 hours', 'archive', 1),      -- ID 8
('A840411AB22C33D1-PAT-YDE-002', 64.3, 160.0, 22, 37.0, 125, 82, 145, 0.98, 97, '2025-09-15', NOW() - INTERVAL '1 day', 'archive', 1),         -- ID 9
('A840411AB22C33D1-PAT-YDE-002', 64.0, 160.0, 22, 36.8, 120, 80, 142, 0.95, 98, '2025-09-15', NOW() - INTERVAL '2 days', 'archive', 1),        -- ID 10

-- PAT-YDE-003 (Patient 3) - Profil Fluctuant léger
('A840411AB22C33D1-PAT-YDE-003', 80.2, 170.0, 31, 36.9, 120, 80, 140, 0.89, 97, '2025-07-20', NOW() - INTERVAL '3 hours', 'diagnostique', 1),  -- ID 11
('A840411AB22C33D1-PAT-YDE-003', 80.0, 170.0, 31, 37.1, 125, 82, 145, 0.95, 96, '2025-07-20', NOW() - INTERVAL '1 day', 'archive', 1),         -- ID 12
('A840411AB22C33D1-PAT-YDE-003', 79.8, 170.0, 31, 36.7, 118, 78, 138, 0.91, 98, '2025-07-20', NOW() - INTERVAL '2 days', 'archive', 1),        -- ID 13
('A840411AB22C33D1-PAT-YDE-003', 79.5, 170.0, 31, 37.0, 122, 80, 142, 0.93, 97, '2025-07-20', NOW() - INTERVAL '3 days', 'archive', 1),        -- ID 14

('A840411AB22C33D2-PAT-EFL-001', 68.4, 162.0, 25, 37.0, 110, 70, 138, 0.85, 99, '2025-10-05', NOW() - INTERVAL '5 hours', 'diagnostique', 2),  -- ID 15
('A840411AB22C33D2-PAT-EFL-002', 76.0, 168.0, 29, 37.5, 135, 90, 155, 1.05, 96, '2025-08-25', NOW() - INTERVAL '1 day', 'diagnostique', 2),     -- ID 16
('A840411AB22C33D1-PAT-YDE-004', 62.1, 158.0, 23, 37.2, 115, 75, 145, 0.90, 98, '2025-11-12', NOW() - INTERVAL '2 days', 'archive', 1),        -- ID 17
('A840411AB22C33D2-PAT-EFL-003', 85.5, 175.0, 34, 39.1, 160, 105, 172, 1.30, 93, '2025-09-01', NOW() - INTERVAL '1 day', 'en_attente', 2),     -- ID 18
('A840411AB22C33D1-PAT-YDE-005', 70.0, 164.0, 27, 36.8, 122, 82, 148, 0.98, 97, '2025-10-20', NOW() - INTERVAL '4 days', 'diagnostique', 1);   -- ID 19

-- 6. DIAGNOSTICS
INSERT INTO diagnostic (contenu, date_diagnostic, date_validation, recommandations, niveau_urgence, id_medecin, id_parametres) VALUES
('Constantes vitales dans les normes. Poursuivre le suivi prénatal mensuel.', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours', 'Alimentation équilibrée et repos modéré.', 'NORMAL', 1, 11),
('Tension artérielle stable. Rythme foetal régulier.', NOW() - INTERVAL '4 hours', NOW() - INTERVAL '4 hours', 'Suivi de la tension au centre de santé dans 1 semaine.', 'NORMAL', 1, 15),
('Légère hypertension gestationnelle détectée. Surveillance accrue nécessaire.', NOW() - INTERVAL '18 hours', NOW() - INTERVAL '18 hours', 'Suivi quotidien de la tension pendant 3 jours. Réduction du sel.', 'NORMAL', 2, 16),
('Cas de pré-éclampsie sévère. Hospitalisation immédiate requise.', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', 'Transfert urgent vers l''unité de soins intensifs obstétriques.', 'CRITIQUE', 1, 19);

-- 7. DIAGNOSTICS IA
INSERT INTO diagnostic_ia (classe_predite, score_confiance, explication_json, recommandations, probabilites, valide_par_medecin, id_parametres) VALUES
('normal', 0.95, '{"facteurs": "tous normaux", "systeme": "ok"}', 'RAS. Suivi normal.', '{"normal": 0.95, "infection": 0.03}', TRUE, 11),
('pre_eclampsie', 0.89, '{"pression": "élevée", "fcf": "limite haute"}', 'Alerte : Risque de pré-éclampsie détecté.', '{"pre_eclampsie": 0.89, "normal": 0.08}', FALSE, 6),
('souffrance_foetale', 0.91, '{"fcf": "172 bpm", "temperature": "39.1°C"}', 'Urgence médicale : Rythme cardiaque foetal alarmant.', '{"souffrance_foetale": 0.91, "infection": 0.06}', NULL, 18),
('infection', 0.78, '{"temperature": "38.6°C"}', 'Suspection d''infection. Vérifier les autres signes cliniques.', '{"infection": 0.78, "normal": 0.15}', NULL, 6);

-- 8. NOTIFICATIONS SMS
INSERT INTO notification_sms (contenu_message, numero_destinataire, date_envoi, succes, id_infirmier_local, id_diagnostic) VALUES
('Alerte MaterniCare : Un diagnostic CRITIQUE a été établi pour la patiente PAT-YDE-005. Hospitalisation immédiate requise.', '+237699001122', NOW() - INTERVAL '3 days', TRUE, 1, 4),
('Notification MaterniCare : Diagnostic disponible pour PAT-YDE-003. Statut : NORMAL.', '+237699001122', NOW() - INTERVAL '2 hours', TRUE, 1, 1);

-- 9. FEEDBACK IA
INSERT INTO feedback_ia (age_patient, poids_patient, taille_patient, temperature, pression_systolique, pression_diastolique, frequence_foetale, glycemie, classe_predite, score_confiance, note_medecin, commentaire_medecin, id_medecin, id_parametres) VALUES
(31, 80.2, 170.0, 36.9, 120, 80, 140, 0.89, 'normal', 0.95, 5, 'Prédiction IA parfaitement en accord avec mon diagnostic.', 1, 11);
