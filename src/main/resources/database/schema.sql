-- Création de la base (à exécuter une fois)
-- Tu peux supprimer cette partie si tu exécutes directement dans ma_base
-- CREATE DATABASE bd_electro;
-- \c bd_electro;

-- TABLE : Medecin
CREATE TABLE Medecin (
    ID_Medecin SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    tel VARCHAR(20)
);

-- TABLE : Dispositif
CREATE TABLE Dispositif (
    ID_Dispositif SERIAL PRIMARY KEY,
    nom_centre_de_sante VARCHAR(150) NOT NULL,
    contact VARCHAR(50)
);

-- TABLE : Parametres
CREATE TABLE Parametres (
    ID_Parametres SERIAL PRIMARY KEY,
    identifiant_patient VARCHAR(50) NOT NULL,
    poids_patient DECIMAL(5,2),
    temperature DECIMAL(4,2),
    pression_arterielle VARCHAR(20),
    frequence_foetale INT,
    ID_Dispositif INT NOT NULL,

    CONSTRAINT fk_param_dispo
        FOREIGN KEY (ID_Dispositif)
        REFERENCES Dispositif(ID_Dispositif)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Création d’un index manuel (optionnel mais recommandé)
CREATE INDEX idx_param_dispo ON Parametres(ID_Dispositif);

-- TABLE : Diagnostic
CREATE TABLE Diagnostic (
    ID_Diagnostic SERIAL PRIMARY KEY,
    contenu TEXT NOT NULL,
    ID_Medecin INT NOT NULL,
    ID_Parametres INT NOT NULL,

    CONSTRAINT fk_diag_medecin
        FOREIGN KEY (ID_Medecin)
        REFERENCES Medecin(ID_Medecin)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    CONSTRAINT fk_diag_param
        FOREIGN KEY (ID_Parametres)
        REFERENCES Parametres(ID_Parametres)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Index
CREATE INDEX idx_diag_medecin ON Diagnostic(ID_Medecin);
CREATE INDEX idx_diag_param ON Diagnostic(ID_Parametres);

