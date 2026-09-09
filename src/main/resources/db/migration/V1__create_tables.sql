-- ==============================================================================
-- ELO VET - SCRIPT DDL POSTGRESQL
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. DROP TABLES (Limpeza de tabelas existentes)
-- ------------------------------------------------------------------------------

DROP TABLE IF EXISTS elo_agendamento CASCADE;
DROP TABLE IF EXISTS elo_clinica_veterinario CASCADE;
DROP TABLE IF EXISTS elo_veterinario_especialidade CASCADE;
DROP TABLE IF EXISTS elo_pet_responsavel CASCADE;
DROP TABLE IF EXISTS elo_veterinario CASCADE;
DROP TABLE IF EXISTS elo_responsavel CASCADE;
DROP TABLE IF EXISTS elo_pet CASCADE;
DROP TABLE IF EXISTS elo_especialidade CASCADE;
DROP TABLE IF EXISTS elo_clinica CASCADE;
DROP TABLE IF EXISTS elo_endereco CASCADE;
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

-- Tabela: ELO_ENDERECO
CREATE TABLE elo_endereco (
    id_endereco BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    CONSTRAINT pk_elo_endereco PRIMARY KEY (id_endereco),
    CONSTRAINT ck_elo_endereco_estado CHECK (LENGTH(estado) = 2),
    CONSTRAINT ck_elo_endereco_cep CHECK (cep ~ '^[0-9]{8}$')
);

-- Tabela: ELO_CLINICA
CREATE TABLE elo_clinica (
    id_clinica BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    nome VARCHAR(150) NOT NULL,
    cnpj VARCHAR(14) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(150),
    id_endereco BIGINT NOT NULL,
    CONSTRAINT pk_elo_clinica PRIMARY KEY (id_clinica),
    CONSTRAINT uk_elo_clinica_cnpj UNIQUE (cnpj),
    CONSTRAINT ck_elo_clinica_cnpj CHECK (cnpj ~ '^[0-9]{14}$'),
    CONSTRAINT fk_elo_clinica_endereco FOREIGN KEY (id_endereco) REFERENCES elo_endereco (id_endereco)
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
    id_endereco BIGINT NOT NULL,
    CONSTRAINT pk_elo_responsavel PRIMARY KEY (id_responsavel),
    CONSTRAINT uk_elo_responsavel_cpf UNIQUE (cpf),
    CONSTRAINT uk_elo_responsavel_usuario UNIQUE (id_usuario),
    CONSTRAINT ck_elo_responsavel_cpf CHECK (cpf ~ '^[0-9]{11}$'),
    CONSTRAINT fk_elo_responsavel_endereco FOREIGN KEY (id_endereco) REFERENCES elo_endereco (id_endereco),
    CONSTRAINT fk_elo_responsavel_usuario FOREIGN KEY (id_usuario) REFERENCES elo_login (id_usuario)
);

-- Tabela: ELO_VETERINARIO
CREATE TABLE elo_veterinario (
    id_veterinario BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_usuario BIGINT NOT NULL,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    rg VARCHAR(20),
    data_nascimento DATE,
    crmv VARCHAR(30) NOT NULL,
    telefone VARCHAR(20),
    id_endereco BIGINT NOT NULL,
    CONSTRAINT pk_elo_veterinario PRIMARY KEY (id_veterinario),
    CONSTRAINT uk_elo_veterinario_cpf UNIQUE (cpf),
    CONSTRAINT uk_elo_veterinario_crmv UNIQUE (crmv),
    CONSTRAINT uk_elo_veterinario_usuario UNIQUE (id_usuario),
    CONSTRAINT ck_elo_veterinario_cpf CHECK (cpf ~ '^[0-9]{11}$'),
    CONSTRAINT fk_elo_veterinario_endereco FOREIGN KEY (id_endereco) REFERENCES elo_endereco (id_endereco),
    CONSTRAINT fk_elo_veterinario_usuario FOREIGN KEY (id_usuario) REFERENCES elo_login (id_usuario)
);

-- Tabela: ELO_ESPECIALIDADE
CREATE TABLE elo_especialidade (
    id_especialidade BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    CONSTRAINT pk_elo_especialidade PRIMARY KEY (id_especialidade),
    CONSTRAINT uk_elo_especialidade_nome UNIQUE (nome)
);

-- Tabela: ELO_VETERINARIO_ESPECIALIDADE
CREATE TABLE elo_veterinario_especialidade (
    id_veterinario_especialidade BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_veterinario BIGINT NOT NULL,
    id_especialidade BIGINT NOT NULL,
    CONSTRAINT pk_elo_vet_especialidade PRIMARY KEY (id_veterinario_especialidade),
    CONSTRAINT uk_ev_es_veterinario_especialidade UNIQUE (id_veterinario, id_especialidade),
    CONSTRAINT fk_ev_es_veterinario FOREIGN KEY (id_veterinario) REFERENCES elo_veterinario (id_veterinario),
    CONSTRAINT fk_ev_es_especialidade FOREIGN KEY (id_especialidade) REFERENCES elo_especialidade (id_especialidade)
);

CREATE INDEX ix_elo_veterinario_especialidade_vet ON elo_veterinario_especialidade (id_veterinario);
CREATE INDEX ix_elo_veterinario_especialidade_esp ON elo_veterinario_especialidade (id_especialidade);

-- Tabela: ELO_CLINICA_VETERINARIO
CREATE TABLE elo_clinica_veterinario (
    id_clinica_veterinario BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_clinica BIGINT NOT NULL,
    id_veterinario BIGINT NOT NULL,
    CONSTRAINT pk_elo_clinica_veterinario PRIMARY KEY (id_clinica_veterinario),
    CONSTRAINT uk_elo_clinica_veterinario UNIQUE (id_clinica, id_veterinario),
    CONSTRAINT fk_ecv_clinica FOREIGN KEY (id_clinica) REFERENCES elo_clinica (id_clinica),
    CONSTRAINT fk_ecv_veterinario FOREIGN KEY (id_veterinario) REFERENCES elo_veterinario (id_veterinario)
);

CREATE INDEX ix_elo_clinica_veterinario_clinica ON elo_clinica_veterinario (id_clinica);
CREATE INDEX ix_elo_clinica_veterinario_vet ON elo_clinica_veterinario (id_veterinario);

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

-- Tabela: ELO_AGENDAMENTO
CREATE TABLE elo_agendamento (
    id_agendamento BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    id_pet BIGINT NOT NULL,
    id_responsavel BIGINT NOT NULL,
    id_veterinario BIGINT NOT NULL,
    id_clinica BIGINT NOT NULL,
    data_hora_agendamento TIMESTAMP NOT NULL,
    status VARCHAR(30) DEFAULT 'AGENDADO' NOT NULL,
    observacao VARCHAR(500),
    CONSTRAINT pk_elo_agendamento PRIMARY KEY (id_agendamento),
    CONSTRAINT ck_elo_agendamento_status CHECK (
        status IN ('AGENDADO', 'CONFIRMADO', 'EM_ATENDIMENTO', 'CONCLUIDO', 'CANCELADO', 'NAO_COMPARECEU')
    ),
    CONSTRAINT fk_elo_agendamento_clinica FOREIGN KEY (id_clinica) REFERENCES elo_clinica (id_clinica),
    CONSTRAINT fk_elo_agendamento_pet FOREIGN KEY (id_pet) REFERENCES elo_pet (id_pet),
    CONSTRAINT fk_elo_agendamento_responsavel FOREIGN KEY (id_responsavel) REFERENCES elo_responsavel (id_responsavel),
    CONSTRAINT fk_elo_agendamento_veterinario FOREIGN KEY (id_veterinario) REFERENCES elo_veterinario (id_veterinario)
);

CREATE INDEX ix_elo_agendamento_pet ON elo_agendamento (id_pet);
CREATE INDEX ix_elo_agendamento_responsavel ON elo_agendamento (id_responsavel);
CREATE INDEX ix_elo_agendamento_veterinario ON elo_agendamento (id_veterinario);
CREATE INDEX ix_elo_agendamento_clinica ON elo_agendamento (id_clinica);

