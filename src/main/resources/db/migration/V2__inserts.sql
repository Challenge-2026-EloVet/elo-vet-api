INSERT INTO elo_pet (nome, especie, raca, sexo, data_nascimento, idade_aproximada, flag_castrado, foto)
VALUES
    ('Thor', 'Cachorro', 'Labrador', 'M', '2020-05-10', 6, 1, NULL),
    ('Mel', 'Cachorro', 'Vira-lata', 'F', '2019-08-20', 7, 1, NULL),
    ('Luna', 'Gato', 'Siames', 'F', '2021-02-14', 5, 0, NULL),
    ('Bob', 'Cachorro', 'Poodle', 'M', '2018-11-03', 8, 1, NULL),
    ('Nina', 'Gato', 'Persa', 'F', '2022-01-25', 4, 0, NULL),
    ('Max', 'Cachorro', 'Bulldog', 'M', '2017-06-18', 9, 1, NULL),
    ('Amora', 'Cachorro', 'Shih Tzu', 'F', '2020-09-09', 6, 1, NULL),
    ('Zeus', 'Cachorro', 'Pastor Alemão', 'M', '2016-12-04', 10, 1, NULL),
    ('Floquinho', 'Gato', 'Maine Coon', 'M', '2023-04-07', 3, 0, NULL),
    ('Kiara', 'Cachorro', 'Golden Retriever', 'F', '2021-07-12', 5, 1, NULL);

INSERT INTO elo_endereco (logradouro, numero, complemento, bairro, cidade, estado, cep)
VALUES
    ('Avenida Paulista', '1000', 'Cj 501', 'Bela Vista', 'São Paulo', 'SP', '01310100'),
    ('Rua das Flores', '250', NULL, 'Jardim América', 'Campinas', 'SP', '13010000'),
    ('Avenida Brasil', '500', 'Bloco B', 'Funcionários', 'Belo Horizonte', 'MG', '30140000'),
    ('Rua Copacabana', '120', 'Apto 302', 'Copacabana', 'Rio de Janeiro', 'RJ', '22020001'),
    ('Rua Sete de Setembro', '45', NULL, 'Centro', 'Curitiba', 'PR', '80010070');

INSERT INTO elo_login (nome_usuario, email, senha_hash, tipo_usuario)
VALUES
    ('dra.ana', 'ana.silva@elovet.com.br', '$2a$12$e8N3...hash1', 'VETERINARIO'),
    ('dr.brunos', 'bruno.souza@elovet.com.br', '$2a$12$e8N3...hash2', 'VETERINARIO'),
    ('dra.carla', 'carla.lima@elovet.com.br', '$2a$12$e8N3...hash3', 'VETERINARIO'),
    ('dr.diego', 'diego.santos@elovet.com.br', '$2a$12$e8N3...hash4', 'VETERINARIO'),
    ('dra.elisa', 'elisa.costa@elovet.com.br', '$2a$12$e8N3...hash5', 'VETERINARIO'),
    ('dr.felipe', 'felipe.rocha@elovet.com.br', '$2a$12$e8N3...hash6', 'VETERINARIO'),
    ('dra.gabriela', 'gabriela.alves@elovet.com.br', '$2a$12$e8N3...hash7', 'VETERINARIO'),
    ('dr.henrique', 'henrique.martins@elovet.com.br', '$2a$12$e8N3...hash8', 'VETERINARIO'),
    ('dra.isabela', 'isabela.pereira@elovet.com.br', '$2a$12$e8N3...hash9', 'VETERINARIO'),
    ('dr.joao', 'joao.oliveira@elovet.com.br', '$2a$12$e8N3...hash10', 'VETERINARIO'),
    ('admin.clyvo', 'admin@clyvo.com.br', '$2a$12$e8N3...hash11', 'ADMIN'),
    ('tutor.carlos', 'carlos.tutor@gmail.com', '$2a$12$e8N3...hash12', 'RESPONSAVEL');

INSERT INTO elo_veterinario (nome_completo, cpf, rg, data_nascimento, crmv, telefone, id_usuario, id_endereco)
VALUES
    ('Dra. Ana Silva', '11122233344', 'MG-1234567', '1985-03-15', '1001', '(11) 91234-5678', 1, 1),
    ('Dr. Bruno Souza', '22233344455', 'SP-7654321', '1980-07-08', '1002', '(21) 99876-5432', 2, 1),
    ('Dra. Carla Lima', '33344455566', 'RJ-9876543', '1990-12-21', '1003', '(31) 93456-7890', 3, 1),
    ('Dr. Diego Santos', '44455566677', 'PR-4567890', '1978-05-30', '1004', '(41) 97654-3210', 4, 1),
    ('Dra. Elisa Costa', '55566677788', 'BA-3210987', '1988-09-11', '1005', '(71) 98877-6655', 5, 1),
    ('Dr. Felipe Rocha', '66677788899', 'CE-6543210', '1982-02-25', '1006', '(85) 99123-4567', 6, 1),
    ('Dra. Gabriela Alves', '77788899900', 'PE-1122334', '1992-11-05', '1007', '(81) 96543-2109', 7, 1),
    ('Dr. Henrique Martins', '88899900011', 'RS-2233445', '1976-08-17', '1008', '(51) 97890-1234', 8, 1),
    ('Dra. Isabela Pereira', '99900011122', 'SC-3344556', '1987-01-28', '1009', '(47) 98765-4321', 9, 1),
    ('Dr. João Oliveira', '00011122233', 'GO-4455667', '1983-06-14', '1010', '(62) 99654-3210', 10, 1);