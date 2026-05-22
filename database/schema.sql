-- ============================================================
-- PI 2026/1 — SENAI FATESG — ADS 3º Período
-- Sistema de Controle de Oficina Mecânica — AV CAR AUTO CENTER
-- Script de criação do banco de dados SQLite
-- ============================================================

PRAGMA foreign_keys = ON;

-- ─── CLIENTE ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente   INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo         TEXT    NOT NULL CHECK(tipo IN ('PF', 'PJ')), -- Pessoa Física ou Jurídica
    nome         TEXT    NOT NULL,
    documento    TEXT    NOT NULL UNIQUE, -- CPF ou CNPJ
    telefone     TEXT,
    email        TEXT,
    endereco     TEXT,
    criado_em    TEXT    DEFAULT (datetime('now', 'localtime'))
);

-- ─── VEÍCULO ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS veiculo (
    id_veiculo      INTEGER PRIMARY KEY AUTOINCREMENT,
    placa           TEXT    NOT NULL UNIQUE,
    marca           TEXT    NOT NULL,
    modelo          TEXT    NOT NULL,
    ano_fabricacao  INTEGER NOT NULL,
    ano_modelo      INTEGER NOT NULL,
    cor             TEXT
);

-- ─── HISTÓRICO DE PROPRIETÁRIOS ──────────────────────────────
-- Um veículo pode ter múltiplos donos ao longo do tempo
CREATE TABLE IF NOT EXISTS historico_proprietario (
    id_historico  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_veiculo    INTEGER NOT NULL REFERENCES veiculo(id_veiculo),
    id_cliente    INTEGER NOT NULL REFERENCES cliente(id_cliente),
    data_inicio   TEXT    NOT NULL,
    data_fim      TEXT    -- NULL indica proprietário atual
);

-- ─── FORNECEDOR ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS fornecedor (
    id_fornecedor INTEGER PRIMARY KEY AUTOINCREMENT,
    razao_social  TEXT NOT NULL,
    cnpj          TEXT NOT NULL UNIQUE,
    telefone      TEXT,
    email         TEXT
);

-- ─── PEÇA ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS peca (
    id_peca           INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo_nacional   TEXT    NOT NULL UNIQUE, -- Código de identificação nacional
    descricao         TEXT    NOT NULL,
    id_fornecedor     INTEGER NOT NULL REFERENCES fornecedor(id_fornecedor),
    prazo_garantia_dias INTEGER NOT NULL DEFAULT 180 -- Prazo de garantia em dias
);

-- ─── COLABORADOR ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS colaborador (
    id_colaborador INTEGER PRIMARY KEY AUTOINCREMENT,
    nome           TEXT NOT NULL,
    cpf            TEXT NOT NULL UNIQUE,
    telefone       TEXT,
    ativo          INTEGER NOT NULL DEFAULT 1 -- 1 = ativo, 0 = inativo
);

-- ─── FUNÇÃO DO COLABORADOR ───────────────────────────────────
-- Um colaborador pode ter uma ou mais funções
CREATE TABLE IF NOT EXISTS funcao_colaborador (
    id_funcao      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_colaborador INTEGER NOT NULL REFERENCES colaborador(id_colaborador),
    funcao         TEXT    NOT NULL -- ex: Mecânico, Atendente, Secretária
);

-- ─── PARCEIRO EXTERNO (TERCEIRIZADO) ─────────────────────────
CREATE TABLE IF NOT EXISTS parceiro_externo (
    id_parceiro  INTEGER PRIMARY KEY AUTOINCREMENT,
    razao_social TEXT NOT NULL,
    cnpj         TEXT NOT NULL UNIQUE,
    especialidade TEXT, -- ex: Retífica, Guincho, Funilaria
    telefone     TEXT
);

-- ─── ORDEM DE SERVIÇO ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ordem_servico (
    id_os         INTEGER PRIMARY KEY AUTOINCREMENT,
    id_veiculo    INTEGER NOT NULL REFERENCES veiculo(id_veiculo),
    id_cliente    INTEGER NOT NULL REFERENCES cliente(id_cliente),
    status        TEXT    NOT NULL DEFAULT 'ORCAMENTO'
                          CHECK(status IN ('ORCAMENTO', 'EXECUCAO', 'AGUARDANDO_PAGAMENTO', 'FINALIZADA')),
    data_abertura TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    data_conclusao TEXT,  -- preenchido quando status = FINALIZADA
    observacoes   TEXT
);

-- ─── SERVIÇO DA OS ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS servico_os (
    id_servico_os  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_os          INTEGER NOT NULL REFERENCES ordem_servico(id_os),
    id_colaborador INTEGER NOT NULL REFERENCES colaborador(id_colaborador),
    descricao      TEXT    NOT NULL,
    tipo           TEXT    NOT NULL CHECK(tipo IN ('INTERNO', 'TERCEIRIZADO')),
    id_parceiro    INTEGER REFERENCES parceiro_externo(id_parceiro), -- preenchido se TERCEIRIZADO
    valor          REAL,
    prazo_garantia_dias INTEGER NOT NULL DEFAULT 90
);

-- ─── PEÇA USADA NA OS ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS peca_os (
    id_peca_os    INTEGER PRIMARY KEY AUTOINCREMENT,
    id_os         INTEGER NOT NULL REFERENCES ordem_servico(id_os),
    id_peca       INTEGER NOT NULL REFERENCES peca(id_peca),
    quantidade    INTEGER NOT NULL DEFAULT 1,
    valor_unitario REAL
);

-- ─── GARANTIA DA PEÇA ────────────────────────────────────────
-- Gerada automaticamente ao finalizar a OS
CREATE TABLE IF NOT EXISTS garantia_peca (
    id_garantia   INTEGER PRIMARY KEY AUTOINCREMENT,
    id_peca_os    INTEGER NOT NULL REFERENCES peca_os(id_peca_os),
    data_inicio   TEXT    NOT NULL, -- data de finalização da OS
    data_fim      TEXT    NOT NULL  -- data_inicio + prazo_garantia_dias da peça
);

-- ─── GARANTIA DO SERVIÇO ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS garantia_servico (
    id_garantia    INTEGER PRIMARY KEY AUTOINCREMENT,
    id_servico_os  INTEGER NOT NULL REFERENCES servico_os(id_servico_os),
    data_inicio    TEXT    NOT NULL,
    data_fim       TEXT    NOT NULL
);

-- ─── PAGAMENTO ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pagamento (
    id_pagamento  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_os         INTEGER NOT NULL REFERENCES ordem_servico(id_os),
    valor_total   REAL    NOT NULL,
    forma_pagamento TEXT,
    data_pagamento TEXT   NOT NULL DEFAULT (datetime('now', 'localtime'))
);
