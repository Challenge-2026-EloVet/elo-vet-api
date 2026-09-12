-- ===============================================================================
-- ELO VET - SCRIPT DDL POSTGRESQL
-- ===============================================================================

-- ------------------------------------------------------------------------------
-- 1. DROP TABLES (Limpeza de tabelas existentes)
-- ------------------------------------------------------------------------------

DROP TABLE IF EXISTS elo_pet_responsavel CASCADE;
DROP TABLE IF EXISTS elo_responsavel CASCADE;
DROP TABLE IF EXISTS elo_pet CASCADE;
DROP TABLE IF EXISTS elo_login CASCADE;

-- ------------------------------------------------------------------------------
-- 2. CRIAÇÃO DAS TABELAS E RESTRIÇÕES
-- ------------------------------------------------------------------------------

-- Tabela: ELO_LOGIN
CREATE TABLE elo_login (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    nome_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(256) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL,
    CONSTRAINT pk_elo_login PRIMARY KEY (id_usuario),
    CONSTRAINT uk_elo_login_nome_usuario UNIQUE (nome_usuario),
    CONSTRAINT uk_elo_login_email UNIQUE (email),
    CONSTRAINT ck_elo_login_tipo_usuario CHECK (
        tipo_usuario IN ('ADMIN', 'USER', 'VETERINARIO', 'RESPONSAVEL')
    )
);

-- Tabela: ELO_RESPONSAVEL
CREATE TABLE elo_responsavel (
    id_responsavel BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_usuario BIGINT NOT NULL,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    rg VARCHAR(20),
    data_nascimento DATE,
    telefone VARCHAR(20),
    CONSTRAINT pk_elo_responsavel PRIMARY KEY (id_responsavel),
    CONSTRAINT uk_elo_responsavel_cpf UNIQUE (cpf),
    CONSTRAINT uk_elo_responsavel_usuario UNIQUE (id_usuario),
    CONSTRAINT ck_elo_responsavel_cpf CHECK (cpf ~ '^[0-9]{11}$'),
    CONSTRAINT fk_elo_responsavel_usuario FOREIGN KEY (id_usuario) REFERENCES elo_login (id_usuario)
);

-- Tabela: ELO_PET
CREATE TABLE elo_pet (
    id_pet BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    nome VARCHAR(150) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raca VARCHAR(100),
    sexo CHAR(1),
    data_nascimento DATE,
    idade_aproximada SMALLINT,
    flag_castrado SMALLINT,
    foto BYTEA,
    CONSTRAINT pk_elo_pet PRIMARY KEY (id_pet),
    CONSTRAINT ck_elo_pet_sexo CHECK (sexo IN ('M', 'F', 'N')),
    CONSTRAINT ck_elo_pet_castrado CHECK (flag_castrado IN (0, 1)),
    CONSTRAINT ck_elo_pet_idade CHECK (idade_aproximada >= 0),
    CONSTRAINT ck_elo_pet_data_idade CHECK (data_nascimento IS NOT NULL OR idade_aproximada IS NOT NULL)
);

-- Tabela: ELO_PET_RESPONSAVEL
CREATE TABLE elo_pet_responsavel (
    id_pet_responsavel BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_pet BIGINT NOT NULL,
    id_responsavel BIGINT NOT NULL,
    data_elo TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_elo_pet_responsavel PRIMARY KEY (id_pet_responsavel),
    CONSTRAINT uk_elo_pet_responsavel UNIQUE (id_pet, id_responsavel),
    CONSTRAINT fk_elo_pet_responsavel_pet FOREIGN KEY (id_pet) REFERENCES elo_pet (id_pet),
    CONSTRAINT fk_elo_pet_responsavel_responsavel FOREIGN KEY (id_responsavel) REFERENCES elo_responsavel (id_responsavel)
);

CREATE INDEX ix_elo_pet_responsavel_pet ON elo_pet_responsavel (id_pet);
CREATE INDEX ix_elo_pet_responsavel_responsavel ON elo_pet_responsavel (id_responsavel);


