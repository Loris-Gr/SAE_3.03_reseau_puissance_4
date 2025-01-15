IF EXISTS DROP TABLE PARTIE;
IF EXISTS DROP TABLE EQUIPE;


CREATE TABLE EQUIPE (
    idEqu INT PRIMARY KEY,
    nomEqu VARCHAR(50) UNIQUE NOT NULL,
    couleurEqu CHAR(1) NOT NULL,
    scoreEqu INT DEFAULT 0
);

INSERT INTO Equipe (id, nom, symbole) VALUES
(0, 'AUCUNE', ' '),
(1, 'JAUNE', 'J'),
(2, 'ROUGE', 'R');

CREATE TABLE PARTIE (
    idPart INT PRIMARY KEY,
    datePart DATE NOT NULL,
    idEquGagn INT,
    FOREIGN KEY (idEquGagn) REFERENCES Equipe(idEqu)
);
