-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Sistema de Controle de Oficina Mecânica — AV CAR AUTO CENTER
-- Banco de dados: PostgreSQL
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
CREATE TABLE marca (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nome              VARCHAR(200) NOT NULL
);

-- ─── MODELO ──────────────────────────────────────────────────
CREATE TABLE modelo (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeModelo        VARCHAR(200) NOT NULL,
    anoModelo         INT          NOT NULL,
    idMarca           INT          NOT NULL,
    FOREIGN KEY (idMarca) REFERENCES marca (id)
);

-- ─── PESSOA ──────────────────────────────────────────────────
-- Entidade genérica: centraliza dados comuns a clientes e colaboradores
CREATE TABLE pessoa (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeCompleto      VARCHAR(150) NOT NULL,
    telefone          VARCHAR(20)  NOT NULL UNIQUE,
    email             VARCHAR(150) NOT NULL UNIQUE,
    endereco          VARCHAR(255) NOT NULL
);

-- ─── CLIENTE ─────────────────────────────────────────────────
-- Especialização de Pessoa (herança JOINED)
CREATE TABLE cliente (
    idPessoa          INT       PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    dataCadastro      DATE      NOT NULL,
    FOREIGN KEY (idPessoa) REFERENCES pessoa (id)
);

-- ─── PESSOA FÍSICA ───────────────────────────────────────────
-- Especialização de Cliente
CREATE TABLE pessoaFisica (
    idCliente         INT         PRIMARY KEY,
    data_hora_criacao TIMESTAMP   DEFAULT NOW(),
    ativo             BOOLEAN     NOT NULL DEFAULT TRUE,
    cpf               VARCHAR(11) NOT NULL UNIQUE,
    rg                VARCHAR(20) NOT NULL UNIQUE,
    dataNascimento    DATE        NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── PESSOA JURÍDICA ─────────────────────────────────────────
-- Especialização de Cliente
CREATE TABLE pessoaJuridica (
    idCliente         INT          PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    cnpj              VARCHAR(14)  NOT NULL UNIQUE,
    razaoSocial       VARCHAR(150) NOT NULL,
    nomeFantasia      VARCHAR(150),
    dataAbertura      DATE         NOT NULL,
    inscricaoEstadual VARCHAR(30)
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── VEÍCULO ─────────────────────────────────────────────────
CREATE TABLE veiculo (
    id                SERIAL      PRIMARY KEY,
    data_hora_criacao TIMESTAMP   DEFAULT NOW(),
    ativo             BOOLEAN     NOT NULL DEFAULT TRUE,
    placa             VARCHAR(8)  NOT NULL UNIQUE,
    cor               VARCHAR(50) NOT NULL,
    chassi            VARCHAR(17) NOT NULL UNIQUE,
    idModelo          INT         NOT NULL,
    idCliente         INT         NOT NULL,
    FOREIGN KEY (idModelo)  REFERENCES modelo  (id),
    FOREIGN KEY (idCliente) REFERENCES cliente (idPessoa)
);

-- ─── HISTÓRICO DE PROPRIETÁRIOS ──────────────────────────────
-- Rastreia todos os donos de um veículo ao longo do tempo
CREATE TABLE historicoVeiculo (
    idPessoa   INT  NOT NULL,
    idVeiculo  INT  NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim    DATE,
    PRIMARY KEY (idPessoa, idVeiculo, dataInicio),
    FOREIGN KEY (idPessoa)  REFERENCES pessoa  (id),
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (id)
);

-- ─── FUNÇÃO DO COLABORADOR ───────────────────────────────────
CREATE TABLE funcaoColaborador (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    funcao            VARCHAR(100) NOT NULL
);

-- ─── COLABORADOR ─────────────────────────────────────────────
-- Especialização de Pessoa (herança JOINED)
CREATE TABLE colaborador (
    idPessoa          INT              PRIMARY KEY,
    data_hora_criacao TIMESTAMP        DEFAULT NOW(),
    ativo             BOOLEAN          NOT NULL DEFAULT TRUE,
    dataAdmissao      DATE             NOT NULL,
    salario           DOUBLE PRECISION NOT NULL,
    cpf               VARCHAR(11)  NOT NULL UNIQUE,
    idFuncao          INT              NOT NULL,
    FOREIGN KEY (idPessoa) REFERENCES pessoa            (id),
    FOREIGN KEY (idFuncao) REFERENCES funcaoColaborador (id)
);

-- ─── STATUS DA ORDEM DE SERVIÇO ──────────────────────────────
CREATE TYPE status_os AS ENUM ('ORCAMENTO', 'EXECUCAO', 'PAGAMENTO', 'FINALIZADO');

-- ─── ORDEM DE SERVIÇO ────────────────────────────────────────
CREATE TABLE ordemDeServico (
    id                SERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    dataAbertura      DATE      NOT NULL,
    dataFechamento    DATE,
    status            status_os NOT NULL,
    valorTotal        REAL,
    observacoes       VARCHAR(500),
    idVeiculo         INT       NOT NULL,
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (id)
);

-- ─── SERVIÇOS INTERNOS (catálogo) ────────────────────────────
CREATE TABLE servicosInternos (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    descricao         VARCHAR(500) NOT NULL,
    valorCobrado      REAL         NOT NULL
);

-- ─── ITEM SERVIÇO INTERNO ────────────────────────────────────
-- Execução de um serviço interno dentro de uma OS
CREATE TABLE itemServicoInterno (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    valorItem         REAL         NOT NULL,
    garantia          INT          NOT NULL,
    observacoes       VARCHAR(500) NOT NULL,
    idOS              INT          NOT NULL,
    FOREIGN KEY (idOS) REFERENCES ordemDeServico (id)
);

-- ─── SERVIÇOS DO COLABORADOR ─────────────────────────────────
-- Serviços que um colaborador está executando
CREATE TABLE servicosDoColaborador (
    idColaborador    INT  NOT NULL,
    idServicoInterno INT  NOT NULL,
    dataServico      DATE NOT NULL,
    PRIMARY KEY (idColaborador, idServicoInterno, dataServico),
    FOREIGN KEY (idColaborador)    REFERENCES colaborador     (idPessoa),
    FOREIGN KEY (idServicoInterno) REFERENCES servicosInternos (id)
);

-- ─── SERVIÇOS ITENS ──────────────────────────────────────────
-- Vincula um item de serviço interno ao seu respectivo serviço interno
CREATE TABLE servicosItens (
    idServicoInterno     INT  NOT NULL,
    idItemServicoInterno INT  NOT NULL,
    dataExecucao         DATE NOT NULL,
    PRIMARY KEY (idServicoInterno, idItemServicoInterno),
    FOREIGN KEY (idServicoInterno)     REFERENCES servicosInternos   (id),
    FOREIGN KEY (idItemServicoInterno) REFERENCES itemServicoInterno (id)
);

-- ─── FORNECEDOR ──────────────────────────────────────────────
CREATE TABLE fornecedor (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    nomeFornecedor    VARCHAR(200) NOT NULL,
    telefone          VARCHAR(20)  NOT NULL,
    cnpj              VARCHAR(14)  UNIQUE,
    email             VARCHAR(150) NOT NULL
);

-- ─── SERVIÇO EXTERNO (catálogo) ──────────────────────────────
CREATE TABLE servicoExterno (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    descricao         VARCHAR(255) NOT NULL,
    valorCobrado      REAL         NOT NULL
);

-- ─── ITEM PEDIDO SERVIÇO EXTERNO ─────────────────────────────
-- Execução de um serviço externo (terceirizado) vinculado a uma OS
CREATE TABLE itemPedidoServicoExterno (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    valorItem         REAL         NOT NULL,
    garantia          INT          NOT NULL,
    observacoes       VARCHAR(500),
    idServicoExterno  INT          NOT NULL,
    idOS              INT          NOT NULL,
    FOREIGN KEY (idServicoExterno) REFERENCES servicoExterno  (id),
    FOREIGN KEY (idOS)             REFERENCES ordemDeServico  (id)
);

-- ─── ITEM FORNECEDOR ─────────────────────────────────────────
-- Vincula fornecedor ao serviço externo prestado
CREATE TABLE itemFornecedor (
    idFornecedor               INT  NOT NULL,
    idItemPedidoServicoExterno INT  NOT NULL,
    dataExecucao               DATE NOT NULL,
    PRIMARY KEY (idFornecedor, idItemPedidoServicoExterno, dataExecucao),
    FOREIGN KEY (idFornecedor)               REFERENCES fornecedor              (id),
    FOREIGN KEY (idItemPedidoServicoExterno) REFERENCES itemPedidoServicoExterno (id)
);

-- ─── PEÇA ────────────────────────────────────────────────────
CREATE TABLE peca (
    id                SERIAL       PRIMARY KEY,
    data_hora_criacao TIMESTAMP    DEFAULT NOW(),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    codigoNacional    INT          NOT NULL UNIQUE,
    modelo            VARCHAR(50)  NOT NULL,
    marca             VARCHAR(100) NOT NULL,
    anoVeiculo        INT          NOT NULL CHECK (anoVeiculo >= 1900),
    anoModelo         INT          NOT NULL CHECK (anoModelo  >= 1900),
    precoUnitario     REAL         NOT NULL CHECK (precoUnitario > 0),
    garantia          INT          NOT NULL CHECK (garantia >= 0),
    idFornecedor      INT          NOT NULL,
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor (id)
);

-- ─── ITEM PEDIDO PEÇA ────────────────────────────────────────
-- Cada peça dentro de uma OS, com rastreabilidade de fornecedor
CREATE TABLE itemPedidoPeca (
    id                SERIAL    PRIMARY KEY,
    data_hora_criacao TIMESTAMP DEFAULT NOW(),
    ativo             BOOLEAN   NOT NULL DEFAULT TRUE,
    quantidade        INT       NOT NULL CHECK (quantidade > 0),
    dataEntrega       DATE,
    codigoNacional    INT       NOT NULL,
    idFornecedor      INT       NOT NULL,
    idOS              INT       NOT NULL,
    FOREIGN KEY (codigoNacional) REFERENCES peca          (codigoNacional),
    FOREIGN KEY (idFornecedor)   REFERENCES fornecedor    (id),
    FOREIGN KEY (idOS)           REFERENCES ordemDeServico (id)
);