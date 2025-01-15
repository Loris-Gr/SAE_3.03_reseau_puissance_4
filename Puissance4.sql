CREATE TABLE Equipe (
    id INT PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL,
    symbole CHAR(1) NOT NULL,
    score INT DEFAULT 0
);

INSERT INTO Equipe (id, nom, symbole) VALUES
(0, 'AUCUNE', ' '),
(1, 'JAUNE', 'J'),
(2, 'ROUGE', 'R');

