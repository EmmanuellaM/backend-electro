-- Création de la table diagnostic_ia
CREATE TABLE IF NOT EXISTS diagnostic_ia (
    id_diagnostic_ia SERIAL PRIMARY KEY,
    classe_predite VARCHAR(50) NOT NULL,
    score_confiance DECIMAL(3,2) NOT NULL CHECK (score_confiance >= 0 AND score_confiance <= 1),
    explication_json TEXT,
    recommandations TEXT,
    probabilites TEXT,
    valide_par_medecin BOOLEAN,
    commentaire_medecin TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_parametres INTEGER NOT NULL UNIQUE REFERENCES parametres(id_parametres) ON DELETE CASCADE,
    id_medecin_validateur INTEGER REFERENCES medecin(id_medecin) ON DELETE SET NULL
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_diagnostic_ia_parametres ON diagnostic_ia(id_parametres);
CREATE INDEX IF NOT EXISTS idx_diagnostic_ia_patient ON diagnostic_ia(id_parametres);
CREATE INDEX IF NOT EXISTS idx_diagnostic_ia_classe ON diagnostic_ia(classe_predite);
CREATE INDEX IF NOT EXISTS idx_diagnostic_ia_validation ON diagnostic_ia(valide_par_medecin);
CREATE INDEX IF NOT EXISTS idx_diagnostic_ia_created ON diagnostic_ia(created_at DESC);

-- Trigger pour mettre à jour updated_at automatiquement
CREATE OR REPLACE FUNCTION update_diagnostic_ia_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_diagnostic_ia_timestamp
BEFORE UPDATE ON diagnostic_ia
FOR EACH ROW
EXECUTE FUNCTION update_diagnostic_ia_timestamp();

-- Commentaires sur la table et les colonnes
COMMENT ON TABLE diagnostic_ia IS 'Diagnostics générés par l''IA avec explications SHAP';
COMMENT ON COLUMN diagnostic_ia.classe_predite IS 'Classe diagnostiquée: normal, pre_eclampsie, diabete_gestationnel, infection, souffrance_foetale, travail_premature';
COMMENT ON COLUMN diagnostic_ia.score_confiance IS 'Score de confiance de la prédiction (0.0 à 1.0)';
COMMENT ON COLUMN diagnostic_ia.explication_json IS 'Explication SHAP au format JSON avec paramètres influents';
COMMENT ON COLUMN diagnostic_ia.recommandations IS 'Recommandations cliniques au format JSON';
COMMENT ON COLUMN diagnostic_ia.probabilites IS 'Probabilités pour toutes les classes au format JSON';
COMMENT ON COLUMN diagnostic_ia.valide_par_medecin IS 'null=en attente, true=validé, false=rejeté';
