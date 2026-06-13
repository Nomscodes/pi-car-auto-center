-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Sistema de Controle de Oficina Mecânica — AV CAR AUTO CENTER
-- Banco de dados: PostgreSQL
-- Schema corrigido para bater com as entidades Java
-- ============================================================

-- ─── DROP (ordem inversa das dependências) ───────────────────
DROP TABLE IF EXISTS itemPedidoPeca                CASCADE;
DROP TABLE IF EXISTS peca                          CASCADE;
DROP TABLE IF EXISTS itemFornecedor                CASCADE;
DROP TABLE IF EXISTS itemPedidoServicoExterno      CASCADE;
DROP TABLE IF EXISTS servicoExterno                CASCADE;
DROP TABLE IF EXISTS servicosItens                 CASCADE;
DROP TABLE IF EXISTS servicosDoColaborador         CASCADE;
DROP TABLE IF EXISTS itemServicoInterno            CASCADE;
DROP TABLE IF EXISTS servicosInternos              CASCADE;
DROP TABLE IF EXISTS ordemDeServico                CASCADE;
DROP TABLE IF EXISTS historicoVeiculo              CASCADE;
DROP TABLE IF EXISTS veiculo                       CASCADE;
DROP TABLE IF EXISTS modelo                        CASCADE;
DROP TABLE IF EXISTS marca                         CASCADE;
DROP TABLE IF EXISTS colaborador                   CASCADE;
DROP TABLE IF EXISTS funcaoColaborador             CASCADE;
DROP TABLE IF EXISTS pessoaFisica                  CASCADE;
DROP TABLE IF EXISTS pessoaJuridica                CASCADE;
DROP TABLE IF EXISTS cliente                       CASCADE;
DROP TABLE IF EXISTS pessoa                        CASCADE;
DROP TABLE IF EXISTS fornecedor                    CASCADE;
DROP TYPE  IF EXISTS status_os;

-- ─── MARCA ───────────────────────────────────────────────────
-- BaseModel: id=BIGINT, ativo=BOOLEAN, data_hora_criacao=TIMESTAMP
-- MarcaModel: nome=VARCHAR(200)
CREATE TABLE marca (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nome              VARCHAR(200) NOT NULL
);

-- ─── MODELO ──────────────────────────────────────────────────
-- ModeloModel: nomeModelo=VARCHAR, anoModelo=Integer(→INTEGER), idMarca=Long(→BIGINT)
CREATE TABLE modelo (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeModelo        VARCHAR(200) NOT NULL,
    anoModelo         INTEGER      NOT NULL,
    idMarca           BIGINT       NOT NULL,
    FOREIGN KEY (idMarca) REFERENCES marca (id)
);

-- ─── PESSOA ──────────────────────────────────────────────────
-- PessoaModel: nomeCompleto, telefone, email, endereco todos STRING
CREATE TABLE pessoa (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeCompleto      VARCHAR(150) NOT NULL,
    telefone          VARCHAR(20)  NOT NULL UNIQUE,
    email             VARCHAR(150) NOT NULL UNIQUE,
    endereco          VARCHAR(255) NOT NULL
);

-- ─── CLIENTE ─────────────────────────────────────────────────
-- ClienteModel: dataCadastro=LocalDate(→DATE)
CREATE TABLE cliente (
    idPessoa          BIGINT    PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    dataCadastro      DATE      NOT NULL,
    FOREIGN KEY (idPessoa) REFERENCES pessoa (id)
);

-- ─── PESSOA FÍSICA ───────────────────────────────────────────
-- PessoaFisicaModel: cpf, rg=STRING, dataNascimento=LocalDate
CREATE TABLE pessoaFisica (
    idCliente         BIGINT      PRIMARY KEY,
    data_hora_criacao TIMESTAMP   DEFAULT NOW(),
    ativo             BOOLEAN     NOT NULL DEFAULT TRUE,
    cpf               VARCHAR(11) NOT NULL UNIQUE,
    rg                VARCHAR(20) NOT NULL UNIQUE,
    dataNascimento    DATE        NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── PESSOA JURÍDICA ─────────────────────────────────────────
-- PessoaJuridicaModel: cnpj, razaoSocial, nomeFantasia, dataAbertura, inscricaoEstadual
CREATE TABLE pessoaJuridica (
    idCliente         BIGINT       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    cnpj              VARCHAR(14)  NOT NULL UNIQUE,
    razaoSocial       VARCHAR(150) NOT NULL,
    nomeFantasia      VARCHAR(150),
    dataAbertura      DATE         NOT NULL,
    inscricaoEstadual VARCHAR(30),
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── VEÍCULO ─────────────────────────────────────────────────
-- VeiculoModel: placa, cor, chassi=STRING; idModelo, idCliente=Long(→BIGINT)
CREATE TABLE veiculo (
    id                BIGSERIAL   PRIMARY KEY,
    data_hora_criacao TIMESTAMP   DEFAULT NOW(),
    ativo             BOOLEAN     NOT NULL DEFAULT TRUE,
    placa             VARCHAR(8)  NOT NULL UNIQUE,
    cor               VARCHAR(50) NOT NULL,
    chassi            VARCHAR(17) NOT NULL UNIQUE,
    idModelo          BIGINT      NOT NULL,
    idCliente         BIGINT      NOT NULL,
    FOREIGN KEY (idModelo)  REFERENCES modelo  (id),
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── HISTÓRICO DE PROPRIETÁRIOS ──────────────────────────────
-- HistoricoVeiculoModel: idPessoa, idVeiculo=Long(→BIGINT), dataInicio, dataFim=LocalDate
CREATE TABLE historicoVeiculo (
    idPessoa   BIGINT NOT NULL,
    idVeiculo  BIGINT NOT NULL,
    dataInicio DATE   NOT NULL,
    dataFim    DATE,
    PRIMARY KEY (idPessoa, idVeiculo, dataInicio),
    FOREIGN KEY (idPessoa)  REFERENCES pessoa  (id),
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (id)
);

-- ─── FUNÇÃO DO COLABORADOR ───────────────────────────────────
-- FuncaoColaboradorModel: funcao=STRING
CREATE TABLE funcaoColaborador (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    funcao            VARCHAR(100) NOT NULL
);

-- ─── COLABORADOR ─────────────────────────────────────────────
-- ColaboradorModel: cpf=STRING, dataAdmissao=LocalDate, salario=double(→FLOAT8), idFuncao via @JoinColumn=BIGINT
CREATE TABLE colaborador (
    idPessoa          BIGINT           PRIMARY KEY,
    data_hora_criacao TIMESTAMP        DEFAULT NOW(),
    ativo             BOOLEAN          NOT NULL DEFAULT TRUE,
    cpf               VARCHAR(11)      NOT NULL UNIQUE,
    dataAdmissao      DATE             NOT NULL,
    salario           DOUBLE PRECISION NOT NULL,
    idFuncao          BIGINT           NOT NULL,
    FOREIGN KEY (idPessoa) REFERENCES pessoa            (id),
    FOREIGN KEY (idFuncao) REFERENCES funcaoColaborador (id)
);

-- ─── STATUS DA ORDEM DE SERVIÇO ──────────────────────────────
CREATE TYPE status_os AS ENUM ('ORCAMENTO', 'EXECUCAO', 'PAGAMENTO', 'FINALIZADO');

-- ─── ORDEM DE SERVIÇO ────────────────────────────────────────
-- OrdemServicoModel: dataAbertura, dataFechamento=LocalDate; status=enum STRING;
--   valorTotal=Double(→FLOAT8); observacoes=STRING; idVeiculo=Long(→BIGINT)
CREATE TABLE ordemDeServico (
    id                BIGSERIAL PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    dataAbertura      DATE      NOT NULL,
    dataFechamento    DATE,
    status            VARCHAR(20) NOT NULL,
    valorTotal        DOUBLE PRECISION,
    observacoes       VARCHAR(500),
    idVeiculo         BIGINT    NOT NULL,
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (id)
);

-- ─── SERVIÇOS INTERNOS (catálogo) ────────────────────────────
-- ServicoInternoModel: descricao=STRING, valorCobrado=double(→FLOAT8)
CREATE TABLE servicosInternos (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    descricao         VARCHAR(500) NOT NULL,
    valorCobrado      DOUBLE PRECISION NOT NULL
);

-- ─── ITEM SERVIÇO INTERNO ────────────────────────────────────
-- ItemServicoInternoModel: valorItem=double(→FLOAT8), garantia=int(→INTEGER),
--   observacoes=STRING, idOS=Long(→BIGINT)
CREATE TABLE itemServicoInterno (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    valorItem         DOUBLE PRECISION NOT NULL,
    garantia          INTEGER      NOT NULL,
    observacoes       VARCHAR(500) NOT NULL,
    idOS              BIGINT       NOT NULL,
    FOREIGN KEY (idOS) REFERENCES ordemDeServico (id)
);

-- ─── SERVIÇOS DO COLABORADOR ─────────────────────────────────
-- ServicoDoColaboradorModel: idColaborador, idServicoInterno=Long(→BIGINT), dataServico=LocalDate
CREATE TABLE servicosDoColaborador (
    idColaborador    BIGINT NOT NULL,
    idServicoInterno BIGINT NOT NULL,
    dataServico      DATE   NOT NULL,
    PRIMARY KEY (idColaborador, idServicoInterno, dataServico),
    FOREIGN KEY (idColaborador)    REFERENCES colaborador     (idPessoa),
    FOREIGN KEY (idServicoInterno) REFERENCES servicosInternos (id)
);

-- ─── SERVIÇOS ITENS ──────────────────────────────────────────
CREATE TABLE servicosItens (
    idServicoInterno     BIGINT NOT NULL,
    idItemServicoInterno BIGINT NOT NULL,
    dataExecucao         DATE   NOT NULL,
    PRIMARY KEY (idServicoInterno, idItemServicoInterno),
    FOREIGN KEY (idServicoInterno)     REFERENCES servicosInternos   (id),
    FOREIGN KEY (idItemServicoInterno) REFERENCES itemServicoInterno (id)
);

-- ─── FORNECEDOR ──────────────────────────────────────────────
-- FornecedorModel: nomeFornecedor, cnpj, telefone, email=STRING
CREATE TABLE fornecedor (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeFornecedor    VARCHAR(200) NOT NULL,
    cnpj              VARCHAR(14)  UNIQUE,
    telefone          VARCHAR(20)  NOT NULL,
    email             VARCHAR(150) NOT NULL
);

-- ─── SERVIÇO EXTERNO (catálogo) ──────────────────────────────
-- ServicoExternoModel: descricao=STRING, valorCobrado=double(→FLOAT8)
CREATE TABLE servicoExterno (
    id                BIGSERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    descricao         VARCHAR(255) NOT NULL,
    valorCobrado      DOUBLE PRECISION NOT NULL
);

-- ─── ITEM PEDIDO SERVIÇO EXTERNO ─────────────────────────────
-- ItemPedidoServicoExternoModel: valorItem=Double(→FLOAT8), garantia=Integer(→INTEGER),
--   observacoes=STRING, idServicoExterno, idOS=Long(→BIGINT)
CREATE TABLE itemPedidoServicoExterno (
    id                BIGSERIAL        PRIMARY KEY,
    data_hora_criacao TIMESTAMP        DEFAULT NOW(),
    ativo             BOOLEAN          NOT NULL DEFAULT TRUE,
    valorItem         DOUBLE PRECISION NOT NULL,
    garantia          INTEGER          NOT NULL,
    observacoes       VARCHAR(500),
    idServicoExterno  BIGINT           NOT NULL,
    idOS              BIGINT           NOT NULL,
    FOREIGN KEY (idServicoExterno) REFERENCES servicoExterno  (id),
    FOREIGN KEY (idOS)             REFERENCES ordemDeServico  (id)
);

-- ─── ITEM FORNECEDOR ─────────────────────────────────────────
-- ItemFornecedorModel: idFornecedor, idItemPedidoServicoExterno=Long(→BIGINT), dataExecucao=LocalDate
CREATE TABLE itemFornecedor (
    idFornecedor               BIGINT NOT NULL,
    idItemPedidoServicoExterno BIGINT NOT NULL,
    dataExecucao               DATE   NOT NULL,
    PRIMARY KEY (idFornecedor, idItemPedidoServicoExterno, dataExecucao),
    FOREIGN KEY (idFornecedor)               REFERENCES fornecedor              (id),
    FOREIGN KEY (idItemPedidoServicoExterno) REFERENCES itemPedidoServicoExterno (id)
);

-- ─── PEÇA ────────────────────────────────────────────────────
-- PecaModel: codigoNacional=Integer(→INTEGER), modelo, marca=STRING,
--   anoVeiculo, anoModelo, garantia=Integer(→INTEGER),
--   precoUnitario=double(→FLOAT8), idFornecedor=Long(→BIGINT)
CREATE TABLE peca (
    id                BIGSERIAL        PRIMARY KEY,
    data_hora_criacao TIMESTAMP        DEFAULT NOW(),
    ativo             BOOLEAN          NOT NULL DEFAULT TRUE,
    codigoNacional    BIGINT           NOT NULL UNIQUE,
    modelo            VARCHAR(50)      NOT NULL,
    marca             VARCHAR(100)     NOT NULL,
    anoVeiculo        INTEGER          NOT NULL CHECK (anoVeiculo >= 1900),
    anoModelo         INTEGER          NOT NULL CHECK (anoModelo  >= 1900),
    precoUnitario     DOUBLE PRECISION NOT NULL CHECK (precoUnitario > 0),
    garantia          INTEGER          NOT NULL CHECK (garantia >= 0),
    idFornecedor      BIGINT           NOT NULL,
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor (id)
);

-- ─── ITEM PEDIDO PEÇA ────────────────────────────────────────
-- ItemPedidoPecaModel: quantidade=int(→INTEGER), dataEntrega=LocalDate,
--   codigoNacional=Long(→BIGINT), idFornecedor, idOS=Long(→BIGINT)
CREATE TABLE itemPedidoPeca (
    id                BIGSERIAL PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    quantidade        INTEGER   NOT NULL CHECK (quantidade > 0),
    dataEntrega       DATE,
    codigoNacional    BIGINT    NOT NULL,
    idFornecedor      BIGINT    NOT NULL,
    idOS              BIGINT    NOT NULL,
    FOREIGN KEY (codigoNacional) REFERENCES peca          (codigoNacional),
    FOREIGN KEY (idFornecedor)   REFERENCES fornecedor    (id),
    FOREIGN KEY (idOS)           REFERENCES ordemDeServico (id)
);


ALTER TABLE itemPedidoPeca 
    DROP CONSTRAINT IF EXISTS itempedidopeca_codigonacional_fkey;

ALTER TABLE itemPedidoPeca 
    ALTER COLUMN codigoNacional TYPE INTEGER;

ALTER TABLE itemPedidoPeca 
    ADD CONSTRAINT itempedidopeca_codigonacional_fkey 
    FOREIGN KEY (codigoNacional) REFERENCES peca(codigoNacional);


	-- Corrige peca.codigoNacional: BIGINT → INTEGER
ALTER TABLE itemPedidoPeca DROP CONSTRAINT IF EXISTS itempedidopeca_codigonacional_fkey;

ALTER TABLE peca ALTER COLUMN codigoNacional TYPE INTEGER;

ALTER TABLE itemPedidoPeca ALTER COLUMN codigoNacional TYPE INTEGER;

ALTER TABLE itemPedidoPeca 
    ADD CONSTRAINT itempedidopeca_codigonacional_fkey 
    FOREIGN KEY (codigoNacional) REFERENCES peca(codigoNacional);