/**
 * Package contenant les entités JPA de l'application médicale.
 * <p>
 * Ces entités représentent le modèle de données et mappent les tables PostgreSQL
 * vers des objets Java manipulables dans l'application Spring Boot.
 * 
 * <h2>Entités disponibles:</h2>
 * <ul>
 *   <li>{@link com.electro.bdelectro.entity.Medecin} - Représente un médecin</li>
 *   <li>{@link com.electro.bdelectro.entity.Dispositif} - Représente un dispositif de collecte</li>
 *   <li>{@link com.electro.bdelectro.entity.Parametres} - Représente les paramètres vitaux d'un patient</li>
 *   <li>{@link com.electro.bdelectro.entity.Diagnostic} - Représente un diagnostic médical</li>
 * </ul>
 * 
 * <h2>Relations:</h2>
 * <pre>
 * Medecin (1) ──< établit >── (N) Diagnostic
 * Diagnostic (N) ──< se base sur >── (1) Parametres
 * Parametres (N) ──< collecté par >── (1) Dispositif
 * </pre>
 * 
 * <h2>Technologies utilisées:</h2>
 * <ul>
 *   <li>JPA/Hibernate - Mapping Objet-Relationnel</li>
 *   <li>Lombok - Réduction du code boilerplate</li>
 *   <li>Jakarta Bean Validation - Validation des données</li>
 *   <li>PostgreSQL - Base de données relationnelle</li>
 * </ul>
 * 
 * <h2>Conventions de nommage:</h2>
 * <ul>
 *   <li>Noms de tables: snake_case (ex: nom_centre_de_sante)</li>
 *   <li>Noms d'attributs Java: camelCase (ex: nomCentreDeSante)</li>
 *   <li>Clés primaires: id_[nom_table] (ex: id_medecin)</li>
 *   <li>Clés étrangères: [nom_table]_id (ex: medecin_id)</li>
 * </ul>
 * 
 * @author Kant
 * @version 1.0
 * @since 2024-12-03
 */
package com.polytechnique.backend.entity;