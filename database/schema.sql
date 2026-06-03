-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Sistema de Controle de Oficina Mecânica — AV CAR AUTO CENTER
-- Banco de dados: PostgreSQL
-- ============================================================

-- ─── DROP (ordem inversa das dependências) ───────────────────
DROP TABLE IF EXISTS itemPedidoPeca         CASCADE;
DROP TABLE IF EXISTS peca                   CASCADE;
DROP TABLE IF EXISTS itemFornecedor         CASCADE;
DROP TABLE IF EXISTS itemPedidoServicoExterno CASCADE;
DROP TABLE IF EXISTS servicoExterno         CASCADE;
DROP TABLE IF EXISTS servicosItens          CASCADE;
DROP TABLE IF EXISTS servicosDoColaborador  CASCADE;
DROP TABLE IF EXISTS itemServicoInterno     CASCADE;
DROP TABLE IF EXISTS servicosInternos       CASCADE;
DROP TABLE IF EXISTS ordemDeServico         CASCADE;
DROP TABLE IF EXISTS historicoVeiculo       CASCADE;
DROP TABLE IF EXISTS veiculo                CASCADE;
DROP TABLE IF EXISTS modelo                 CASCADE;
DROP TABLE IF EXISTS marca                  CASCADE;
DROP TABLE IF EXISTS colaborador            CASCADE;
DROP TABLE IF EXISTS funcaoColaborador      CASCADE;
DROP TABLE IF EXISTS pessoaFisica           CASCADE;
DROP TABLE IF EXISTS pessoaJuridica         CASCADE;
DROP TABLE IF EXISTS cliente                CASCADE;
DROP TABLE IF EXISTS pessoa                 CASCADE;
DROP TABLE IF EXISTS fornecedor             CASCADE;
DROP TYPE  IF EXISTS status_os;

-- ─── MARCA ───────────────────────────────────────────────────
CREATE TABLE marca (
    idMarca SERIAL,
    nome    VARCHAR(200) NOT NULL,
    PRIMARY KEY (idMarca)
);

-- ─── MODELO ──────────────────────────────────────────────────
CREATE TABLE modelo (
    idModelo    SERIAL,
    nomeModelo  VARCHAR(200) NOT NULL,
    anoModelo   DATE         NOT NULL,
    idMarca     INT          NOT NULL,
    FOREIGN KEY (idMarca) REFERENCES marca (idMarca),
    PRIMARY KEY (idModelo)
);

-- ─── PESSOA ──────────────────────────────────────────────────
-- Entidade genérica: centraliza dados comuns a clientes e colaboradores
CREATE TABLE pessoa (
    idPessoa      SERIAL,
    nomeCompleto  VARCHAR(150) NOT NULL,
    telefone      VARCHAR(20)  NOT NULL UNIQUE,
    email         VARCHAR(150) NOT NULL UNIQUE,
    endereco      VARCHAR(255) NOT NULL,
    PRIMARY KEY (idPessoa)
);

-- ─── CLIENTE ─────────────────────────────────────────────────
-- Especialização de Pessoa
CREATE TABLE cliente (
    idCliente    SERIAL,
    dataCadastro DATE NOT NULL,
    idPessoa     INT  NOT NULL UNIQUE,
    FOREIGN KEY (idPessoa) REFERENCES pessoa (idPessoa),
    PRIMARY KEY (idCliente)
);

-- ─── PESSOA FÍSICA ───────────────────────────────────────────
-- Especialização de Cliente
CREATE TABLE pessoaFisica (
    cpf             VARCHAR(11) NOT NULL,
    rg              VARCHAR(20) NOT NULL UNIQUE,
    dataNascimento  DATE        NOT NULL,
    idCliente       INT         NOT NULL UNIQUE,
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente),
    PRIMARY KEY (cpf)
);

-- ─── PESSOA JURÍDICA ─────────────────────────────────────────
-- Especialização de Cliente
CREATE TABLE pessoaJuridica (
    cnpj         VARCHAR(14)  NOT NULL,
    razaoSocial  VARCHAR(150) NOT NULL,
    nomeFantasia VARCHAR(150),
    dataAbertura DATE         NOT NULL,
    idCliente    INT          NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente),
    PRIMARY KEY (cnpj)
);

-- ─── VEÍCULO ─────────────────────────────────────────────────
CREATE TABLE veiculo (
    idVeiculo  SERIAL,
    placa      VARCHAR(8)  NOT NULL UNIQUE,
    cor        VARCHAR(50) NOT NULL,
    chassi     VARCHAR(17) NOT NULL UNIQUE,
    idModelo   INT         NOT NULL,
    idCliente  INT         NOT NULL,
    FOREIGN KEY (idModelo)  REFERENCES modelo  (idModelo),
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente),
    PRIMARY KEY (idVeiculo)
);

-- ─── HISTÓRICO DE PROPRIETÁRIOS ──────────────────────────────
-- Rastreia todos os donos de um veículo ao longo do tempo
CREATE TABLE historicoVeiculo (
    idPessoa    INT  NOT NULL,
    idVeiculo   INT  NOT NULL,
    dataInicio  DATE NOT NULL,
    dataFim     DATE,           -- NULL indica proprietário atual
    PRIMARY KEY (idPessoa, idVeiculo, dataInicio),
    FOREIGN KEY (idPessoa)  REFERENCES pessoa  (idPessoa),
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (idVeiculo)
);

-- ─── FUNÇÃO DO COLABORADOR ───────────────────────────────────
CREATE TABLE funcaoColaborador (
    idFuncao  SERIAL,
    funcao    VARCHAR(100) NOT NULL,
    PRIMARY KEY (idFuncao)
);

-- ─── COLABORADOR ─────────────────────────────────────────────
-- Especialização de Pessoa
CREATE TABLE colaborador (
    idColaborador  SERIAL,
    dataAdmissao   DATE NOT NULL,
    salario        REAL NOT NULL,
    idPessoa       INT  NOT NULL UNIQUE,
    idFuncao       INT  NOT NULL,
    FOREIGN KEY (idPessoa)  REFERENCES pessoa            (idPessoa),
    FOREIGN KEY (idFuncao)  REFERENCES funcaoColaborador (idFuncao),
    PRIMARY KEY (idColaborador)
);

-- ─── STATUS DA ORDEM DE SERVIÇO ──────────────────────────────
CREATE TYPE status_os AS ENUM ('orcamento', 'execucao', 'pagamento', 'finalizado');

-- ─── ORDEM DE SERVIÇO ────────────────────────────────────────
CREATE TABLE ordemDeServico (
    idOS           SERIAL,
    dataAbertura   DATE      NOT NULL,
    dataFechamento DATE,                        -- NULL até encerramento
    status         status_os NOT NULL,
    valorTotal     REAL,
    observacoes    VARCHAR(500),
    idVeiculo      INT       NOT NULL,
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (idVeiculo),
    PRIMARY KEY (idOS)
);

-- ─── SERVIÇOS INTERNOS (catálogo) ────────────────────────────
CREATE TABLE servicosInternos (
    idServicoInterno  SERIAL,
    descricao         VARCHAR(500) NOT NULL,
    valorCobrado      REAL         NOT NULL,
    PRIMARY KEY (idServicoInterno)
);

-- ─── ITEM SERVIÇO INTERNO ────────────────────────────────────
-- Execução de um serviço interno dentro de uma OS
CREATE TABLE itemServicoInterno (
    idItemServicoInterno  SERIAL,
    valorItem             REAL         NOT NULL,
    garantia              INT          NOT NULL,
    observacoes           VARCHAR(500) NOT NULL,
    idOS                  INT          NOT NULL,
    FOREIGN KEY (idOS) REFERENCES ordemDeServico (idOS),
    PRIMARY KEY (idItemServicoInterno)
);

-- ─── SERVIÇOS DO COLABORADOR ─────────────────────────────────
-- Serviços que um colaborador está executando
CREATE TABLE servicosDoColaborador (
    idColaborador    INT  NOT NULL,
    idServicoInterno INT  NOT NULL,
    dataServico      DATE NOT NULL,
    PRIMARY KEY (idColaborador, idServicoInterno, dataServico),
    FOREIGN KEY (idColaborador)    REFERENCES colaborador     (idColaborador),
    FOREIGN KEY (idServicoInterno) REFERENCES servicosInternos (idServicoInterno)
);

-- ─── SERVIÇOS ITENS ──────────────────────────────────────────
-- Vincula um item de serviço interno ao seu respectivo serviço interno
CREATE TABLE servicosItens (
    idServicoInterno      INT  NOT NULL,
    idItemServicoInterno  INT  NOT NULL,
    dataExecucao          DATE NOT NULL,
    PRIMARY KEY (idServicoInterno, idItemServicoInterno),
    FOREIGN KEY (idServicoInterno)     REFERENCES servicosInternos  (idServicoInterno),
    FOREIGN KEY (idItemServicoInterno) REFERENCES itemServicoInterno (idItemServicoInterno)
);

-- ─── FORNECEDOR ──────────────────────────────────────────────
CREATE TABLE fornecedor (
    idFornecedor   SERIAL,
    nomeFornecedor VARCHAR(200) NOT NULL,
    telefone       VARCHAR(20)  NOT NULL UNIQUE,
    PRIMARY KEY (idFornecedor)
);

-- ─── SERVIÇO EXTERNO (catálogo) ──────────────────────────────
CREATE TABLE servicoExterno (
    idServicoExterno  SERIAL,
    descricao         VARCHAR(255) NOT NULL,
    valorCobrado      REAL         NOT NULL,
    PRIMARY KEY (idServicoExterno)
);

-- ─── ITEM PEDIDO SERVIÇO EXTERNO ─────────────────────────────
-- Execução de um serviço externo (terceirizado) vinculado a uma OS
CREATE TABLE itemPedidoServicoExterno (
    idItemPedidoServicoExterno  SERIAL,
    valorItem                   REAL         NOT NULL,
    garantia                    INT          NOT NULL,
    observacoes                 VARCHAR(500),
    idServicoExterno            INT          NOT NULL,
    FOREIGN KEY (idServicoExterno) REFERENCES servicoExterno (idServicoExterno),
    PRIMARY KEY (idItemPedidoServicoExterno)
);

-- ─── ITEM FORNECEDOR ─────────────────────────────────────────
-- Vincula fornecedor ao serviço externo prestado
CREATE TABLE itemFornecedor (
    idFornecedor               INT  NOT NULL,
    idItemPedidoServicoExterno INT  NOT NULL,
    dataExecucao               DATE NOT NULL,
    PRIMARY KEY (idFornecedor, idItemPedidoServicoExterno, dataExecucao),
    FOREIGN KEY (idFornecedor)               REFERENCES fornecedor              (idFornecedor),
    FOREIGN KEY (idItemPedidoServicoExterno) REFERENCES itemPedidoServicoExterno (idItemPedidoServicoExterno)
);

-- ─── PEÇA ────────────────────────────────────────────────────
CREATE TABLE peca (
    codigoNacional  INT          NOT NULL,
    modelo          VARCHAR(50)  NOT NULL,
    marca           VARCHAR(100) NOT NULL,
    anoVeiculo      INT          NOT NULL CHECK (anoVeiculo >= 1900),
    anoModelo       INT          NOT NULL CHECK (anoModelo  >= 1900),
    precoUnitario   REAL         NOT NULL CHECK (precoUnitario > 0),
    garantia        INT          NOT NULL CHECK (garantia >= 0),
    idFornecedor    INT          NOT NULL,
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor (idFornecedor),
    PRIMARY KEY (codigoNacional)
);

-- ─── ITEM PEDIDO PEÇA ────────────────────────────────────────
-- Cada peça dentro de uma OS, com rastreabilidade de fornecedor
CREATE TABLE itemPedidoPeca (
    idItemPedidoPeca  SERIAL,
    quantidade        INT  NOT NULL CHECK (quantidade > 0),
    dataEntrega       DATE,           -- NULL até entrega confirmada
    codigoNacional    INT  NOT NULL,
    idFornecedor      INT  NOT NULL,
    idOS              INT  NOT NULL,
    FOREIGN KEY (codigoNacional) REFERENCES peca          (codigoNacional),
    FOREIGN KEY (idFornecedor)   REFERENCES fornecedor    (idFornecedor),
    FOREIGN KEY (idOS)           REFERENCES ordemDeServico (idOS),
    PRIMARY KEY (idItemPedidoPeca)
);