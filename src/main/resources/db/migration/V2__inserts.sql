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
