-- ============================================================
-- AV CAR AUTO CENTER — Schema SQLite
-- Projeto Integrador 2026/1 — SENAI FATESG ADS
-- ============================================================

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS marca (
    idMarca   INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS modelo (
    idModelo   INTEGER PRIMARY KEY AUTOINCREMENT,
    nomeModelo VARCHAR(200) NOT NULL,
    anoModelo  INTEGER NOT NULL,
    idMarca    INTEGER NOT NULL,
    FOREIGN KEY (idMarca) REFERENCES marca (idMarca)
);

CREATE TABLE IF NOT EXISTS pessoa (
    idPessoa      INTEGER PRIMARY KEY AUTOINCREMENT,
    nomeCompleto  VARCHAR(150) NOT NULL,
    telefone      VARCHAR(20)  NOT NULL UNIQUE,
    email         VARCHAR(150) NOT NULL UNIQUE,
    endereco      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente (
    idCliente    INTEGER PRIMARY KEY AUTOINCREMENT,
    dataCadastro DATE NOT NULL,
    idPessoa     INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (idPessoa) REFERENCES pessoa (idPessoa)
);

CREATE TABLE IF NOT EXISTS pessoaFisica (
    cpf             VARCHAR(11) PRIMARY KEY,
    rg              VARCHAR(20) NOT NULL UNIQUE,
    dataNascimento  DATE NOT NULL,
    idCliente       INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente)
);

CREATE TABLE IF NOT EXISTS pessoaJuridica (
    cnpj         VARCHAR(14) PRIMARY KEY,
    razaoSocial  VARCHAR(150) NOT NULL,
    nomeFantasia VARCHAR(150),
    dataAbertura DATE NOT NULL,
    idCliente    INTEGER NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente)
);

CREATE TABLE IF NOT EXISTS veiculo (
    idVeiculo INTEGER PRIMARY KEY AUTOINCREMENT,
    placa     VARCHAR(8)  NOT NULL UNIQUE,
    cor       VARCHAR(50) NOT NULL,
    chassi    VARCHAR(17) NOT NULL UNIQUE,
    idModelo  INTEGER NOT NULL,
    idCliente INTEGER NOT NULL,
    FOREIGN KEY (idModelo)  REFERENCES modelo  (idModelo),
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente)
);

CREATE TABLE IF NOT EXISTS historicoVeiculo (
    idPessoa   INTEGER NOT NULL,
    idVeiculo  INTEGER NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim    DATE,
    PRIMARY KEY (idPessoa, idVeiculo, dataInicio),
    FOREIGN KEY (idPessoa)  REFERENCES pessoa  (idPessoa),
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (idVeiculo)
);

CREATE TABLE IF NOT EXISTS funcaoColaborador (
    idFuncao INTEGER PRIMARY KEY AUTOINCREMENT,
    funcao   VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS colaborador (
    idColaborador INTEGER PRIMARY KEY AUTOINCREMENT,
    dataAdmissao  DATE NOT NULL,
    salario       REAL NOT NULL,
    idPessoa      INTEGER NOT NULL,
    idFuncao      INTEGER NOT NULL,
    FOREIGN KEY (idPessoa)  REFERENCES pessoa             (idPessoa),
    FOREIGN KEY (idFuncao)  REFERENCES funcaoColaborador  (idFuncao)
);

-- status: 'orcamento' | 'execucao' | 'pagamento' | 'finalizado'
CREATE TABLE IF NOT EXISTS ordemDeServico (
    idOS           INTEGER PRIMARY KEY AUTOINCREMENT,
    dataAbertura   DATE NOT NULL,
    dataFechamento DATE,
    status         TEXT NOT NULL CHECK (status IN ('orcamento','execucao','pagamento','finalizado')),
    valorTotal     REAL,
    observacoes    VARCHAR(500),
    idVeiculo      INTEGER NOT NULL,
    FOREIGN KEY (idVeiculo) REFERENCES veiculo (idVeiculo)
);

CREATE TABLE IF NOT EXISTS servicosInternos (
    idServicoInterno INTEGER PRIMARY KEY AUTOINCREMENT,
    descricao        VARCHAR(500) NOT NULL,
    valorCobrado     REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS itemServicoInterno (
    idItemServicoInterno INTEGER PRIMARY KEY AUTOINCREMENT,
    valorItem            REAL NOT NULL,
    garantia             INTEGER NOT NULL,
    observacoes          VARCHAR(500),
    idOS                 INTEGER NOT NULL,
    FOREIGN KEY (idOS) REFERENCES ordemDeServico (idOS)
);

CREATE TABLE IF NOT EXISTS servicosDoColaborador (
    idColaborador    INTEGER NOT NULL,
    idServicoInterno INTEGER NOT NULL,
    dataServico      DATE NOT NULL,
    PRIMARY KEY (idColaborador, idServicoInterno, dataServico),
    FOREIGN KEY (idColaborador)    REFERENCES colaborador      (idColaborador),
    FOREIGN KEY (idServicoInterno) REFERENCES servicosInternos (idServicoInterno)
);

CREATE TABLE IF NOT EXISTS servicosItens (
    idServicoInterno     INTEGER NOT NULL,
    idItemServicoInterno INTEGER NOT NULL,
    dataExecucao         DATE NOT NULL,
    PRIMARY KEY (idServicoInterno, idItemServicoInterno),
    FOREIGN KEY (idServicoInterno)     REFERENCES servicosInternos  (idServicoInterno),
    FOREIGN KEY (idItemServicoInterno) REFERENCES itemServicoInterno (idItemServicoInterno)
);

CREATE TABLE IF NOT EXISTS fornecedor (
    idFornecedor   INTEGER PRIMARY KEY AUTOINCREMENT,
    nomeFornecedor VARCHAR(200) NOT NULL,
    telefone       VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS servicoExterno (
    idServicoExterno INTEGER PRIMARY KEY AUTOINCREMENT,
    descricao        VARCHAR(255) NOT NULL,
    valorCobrado     REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS itemPedidoServicoExterno (
    idItemPedidoServicoExterno INTEGER PRIMARY KEY AUTOINCREMENT,
    valorItem        REAL NOT NULL,
    garantia         INTEGER NOT NULL,
    observacoes      VARCHAR(500),
    idServicoExterno INTEGER NOT NULL,
    FOREIGN KEY (idServicoExterno) REFERENCES servicoExterno (idServicoExterno)
);

CREATE TABLE IF NOT EXISTS itemFornecedor (
    idFornecedor               INTEGER NOT NULL,
    idItemPedidoServicoExterno INTEGER NOT NULL,
    dataExecucao               DATE NOT NULL,
    PRIMARY KEY (idFornecedor, idItemPedidoServicoExterno, dataExecucao),
    FOREIGN KEY (idFornecedor)               REFERENCES fornecedor                (idFornecedor),
    FOREIGN KEY (idItemPedidoServicoExterno) REFERENCES itemPedidoServicoExterno  (idItemPedidoServicoExterno)
);

CREATE TABLE IF NOT EXISTS peca (
    codigoNacional INTEGER PRIMARY KEY,
    modelo         VARCHAR(50)  NOT NULL,
    marca          VARCHAR(100) NOT NULL,
    anoVeiculo     INTEGER NOT NULL,
    anoModelo      INTEGER NOT NULL,
    precoUnitario  REAL NOT NULL,
    garantia       INTEGER NOT NULL,
    idFornecedor   INTEGER NOT NULL,
    FOREIGN KEY (idFornecedor) REFERENCES fornecedor (idFornecedor)
);

CREATE TABLE IF NOT EXISTS itemPedidoPeca (
    idItemPedidoPeca INTEGER PRIMARY KEY AUTOINCREMENT,
    quantidade       INTEGER NOT NULL,
    dataEntrega      DATE,
    codigoNacional   INTEGER NOT NULL,
    idFornecedor     INTEGER NOT NULL,
    idOS             INTEGER NOT NULL,
    FOREIGN KEY (codigoNacional) REFERENCES peca          (codigoNacional),
    FOREIGN KEY (idFornecedor)   REFERENCES fornecedor     (idFornecedor),
    FOREIGN KEY (idOS)           REFERENCES ordemDeServico (idOS)
);
