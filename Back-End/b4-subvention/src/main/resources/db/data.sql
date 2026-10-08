-- Supprime toutes les données
-------
DELETE FROM compte;
DELETE FROM depense;
DELETE FROM comptable;
DELETE FrOM subvention;

-- Ajoute  une extension à PostgreSQL
-------
CREATE SCHEMA IF NOT EXISTS ext;
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA ext;


-- subvention
-------

INSERT INTO subvention (nom, organisme, id, annee, montant, disponible)
VALUES
    ('Soutien au développement associatif',
     'Région Nouvelle-Aquitaine',
     1, 2026, 25000, 25000),

    ('Fonds d''initiative territoriale',
     'Conseil départemental de la Haute-Vienne',
     2, 2026, 15000, 15000),

    ('Soutien aux projets d''intérêt public local',
     'Ville de Limoges',
     3, 2026, 12000, 12000),

    ('Fonds de développement de la vie associative',
     'Préfecture de la Haute-Vienne',
     4,  2026, 18000, 18000),

    ('Programme d''accompagnement des initiatives locales',
     'Communauté urbaine Limoges Métropole',
     5, 2026, 10000, 10000),

    ('Fonds de cohésion et d''inclusion sociale',
     'Fonds social européen+ (Union européenne)',
     6, 2026, 50000, 50000),

    ('Programme européen pour l''engagement citoyen',
     'Commission européenne',
     7, 2026, 30000, 30000),

    ('Fonds pour les initiatives solidaires',
     'Fondation de France',
     8, 2026, 7500, 7500),

    ('Programme d''engagement territorial',
     'Fondation SNCF',
     9, 2026, 20000, 20000),

    ('Fonds de soutien aux projets d''intérêt général',
     'Fondation Crédit Agricole',
     10, 2026, 12500, 12500);

ALTER TABLE subvention     ALTER COLUMN id     RESTART WITH 11;
    

-- comptable
-------

INSERT INTO comptable (id, nom, prenom, email)
VALUES
    ( 1, 'Anderson', 'Alex', 'alex.anderson@gmail.com'),
    ( 2, 'Bianchi', 'Bianca', 'bianca.bianchi@yahoo.com'),
    ( 3, 'Cortez', 'Carlos', 'carlos.cortez@outlook.com'),
    ( 4, 'Dimitrov', 'Daria', 'daria.dimitrov@proton.me'),
    ( 5, 'Evans', 'Ethan', 'ethan.evans@gmail.com'),
    ( 6, 'Fahri', 'Fatima', 'fatima.fahri@yahoo.fr'),
    ( 7, 'Gallo', 'Giorgio', 'giorgio.gallo@outlook.com'),
    ( 8, 'Hassan', 'Hana', 'hana.hassan@icloud.com'),
    ( 9, 'Ivanov', 'Ivan', 'ivan.ivanov@gmail.com'),
    (10, 'Jones', 'Julia', 'julia.jones@yahoo.com');

ALTER TABLE comptable    ALTER COLUMN id     RESTART WITH 11;
    

-- depense
-------

INSERT INTO depense (id, objet, date_depense , montant, reliquat, comptable_id)
VALUES
    ( 1, 'Achat véhicule','2026-08-18', 45000, 45000,  1),
    ( 2, 'Serveur','2026-04-14', 12000, 12000,  1),
    ( 3, 'Travaux d''isolation','2026-07-17', 33000, 33000,  1),
    ( 4, 'Impirmantes','2026-03-13', 12000, 12000,  2),
    ( 5, 'Campagne de communication','2026-06-16', 21000, 21000,  2),
    ( 6, 'Vidéoprojecteurs','2026-02-12', 4200, 4200,  3),
    ( 7, 'Animations estivales','2026-05-15', 14000, 14000,  1),
    ( 8, 'Assurances','2026-01-11', 11000, 11000,  2),
    ( 9, 'Voyage d''études','2026-10-20', 2500, 2500,  NULL),
    ( 10, 'Audit sécurité','2026-09-19', 8000, 8000,  NULL);

ALTER TABLE depense     ALTER COLUMN id     RESTART WITH 11;


-- compte
-------

INSERT INTO compte (id, nom, empreinte_mdp, role_admin, comptable_id ) VALUES
( 1, 'admin', ext.crypt( 'admin', ext.gen_salt('bf')), TRUE, NULL ),
( 2, 'guest', ext.crypt('guest', ext.gen_salt('bf')), FALSE, NULL ),
( 3, 'alex', ext.crypt('alex', ext.gen_salt('bf')), FALSE, 1 ),
( 4, 'bianca', ext.crypt('bianca', ext.gen_salt('bf')), FALSE,2 ),
( 5, 'carlos', ext.crypt('carlos', ext.gen_salt('bf')), FALSE,3  );

ALTER TABLE compte      ALTER COLUMN id     RESTART WITH 10;
