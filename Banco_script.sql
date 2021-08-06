CREATE DATABASE correio;

CREATE TABLE IF NOT EXISTS servidor
(
	dominio VARCHAR(30) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS usuario
(
	dominio VARCHAR(30) NOT NULL,
	email VARCHAR(40) NOT NULL,
	nome VARCHAR(50) NOT NULL,
	senha VARCHAR(20) NOT NULL,
	PRIMARY KEY(dominio,email),
	FOREIGN KEY (dominio) REFERENCES servidor(dominio) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mensagem(
	id INTEGER NOT NULL AUTO_INCREMENT,
	remetente_dominio VARCHAR(30) NOT NULL,
	remetente_email VARCHAR(40) NOT NULL,
	assunto VARCHAR(20) NOT NULL,
	corpo VARCHAR(300) NOT NULL,
	destinatario_dominio VARCHAR(30) NOT NULL,
	destinatario_email VARCHAR(40) NOT NULL,
	PRIMARY KEY(id,remetente_dominio,remetente_email),
	FOREIGN KEY (destinatario_dominio,destinatario_email) REFERENCES usuario(dominio,email) ON DELETE CASCADE,
	FOREIGN KEY (remetente_dominio,remetente_email) REFERENCES usuario(dominio,email) ON DELETE CASCADE
);
