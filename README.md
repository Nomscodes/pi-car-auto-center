<div align="center">

# 🔧 AV CAR AUTO CENTER — Sistema de Controle de Oficina Mecânica

**Projeto Integrador 2026/1 · SENAI FATESG · ADS 3º Período**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=for-the-badge)
![License](https://img.shields.io/badge/Licença-Acadêmica-blue?style=for-the-badge)

</div>

---

## 📋 Sobre o Projeto

Sistema desktop de gerenciamento de **Ordens de Serviço (OS)** desenvolvido para a oficina mecânica **AV CAR AUTO CENTER**, de propriedade do Sr. Godofredo Silva.

O sistema visa substituir o controle manual atual, que não registra adequadamente as OS executadas, implementando rastreabilidade completa de peças, fornecedores, garantias, serviços internos e terceirizados.

> 📌 **Disciplinas envolvidas:** Modelagem de Banco de Dados · Arquitetura e Projeto de Software · Estrutura de Dados I · Gestão de Projetos · Padrões de Projeto

---

## 🎯 Funcionalidades Previstas

- ✅ Cadastro de clientes **Pessoa Física** (CPF) e **Pessoa Jurídica** (CNPJ)
- ✅ Cadastro e histórico de proprietários de veículos
- ✅ Gerenciamento de **Ordens de Serviço** com ciclo de vida completo
- ✅ Controle de **status da OS**: Orçamento → Execução → Aguardando Pagamento → Finalizada
- ✅ Cadastro de peças com **código nacional** e rastreabilidade por fornecedor
- ✅ Controle de **garantia de peças e serviços**
- ✅ Cadastro de colaboradores com múltiplas funções
- ✅ Registro de **serviços internos e terceirizados**
- ✅ Funcionamento **100% local** — sem dependência de internet

---

## 🗂️ Fluxo da Ordem de Serviço

```
┌─────────────┐    aprovação    ┌─────────────┐    conclusão    ┌──────────────────────┐    pagamento    ┌─────────────┐
│  ORÇAMENTO  │ ─────────────► │   EXECUÇÃO  │ ─────────────► │ AGUARDANDO PAGAMENTO │ ─────────────► │  FINALIZADA │
└─────────────┘                 └─────────────┘                 └──────────────────────┘                 └─────────────┘
                                                                                                                  │
                                                                                                      início da garantia
```

---

## 🏗️ Arquitetura

```
av-car-auto-center/
├── src/
│   ├── model/          # Entidades e regras de domínio
│   ├── dao/            # Acesso ao banco de dados (DAO Pattern)
│   ├── service/        # Lógica de negócio
│   ├── view/           # Telas Java Swing
│   └── controller/     # Controladores (MVC)
├── database/
│   └── schema.sql      # Script de criação do banco
├── docs/
│   ├── ERS.pdf         # Especificação de Requisitos do Sistema
│   ├── DER.png         # Diagrama Entidade-Relacionamento
│   ├── casos-de-uso/   # Diagramas UML
│   └── atas/           # Atas de reunião
├── .github/
│   └── CONTRIBUTING.md
└── README.md
```

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Java 17+ | Linguagem principal |
| Java Swing | Interface gráfica desktop |
| SQLite | Banco de dados local |
| JDBC | Conexão Java ↔ Banco |

---

## ▶️ Como Executar

> **Pré-requisitos:** Java 17+ instalado · Compatível com Windows e Linux

```bash
# Clone o repositório
git clone https://github.com/Nomscodes/av-car-auto-center.git

# Acesse o diretório
cd av-car-auto-center

# Compile o projeto
javac -cp "lib/*" -d out src/**/*.java

# Execute
java -cp "out:lib/*" Main
```

---

## 🗄️ Modelo de Dados — Entidades Principais

| Entidade | Descrição |
|----------|-----------|
| `Cliente` | PF ou PJ — vinculado ao histórico do veículo |
| `Veiculo` | Identificado por marca, modelo e ano |
| `OrdemDeServico` | Núcleo do sistema — controla todo o fluxo |
| `Servico` | Interno ou terceirizado, com garantia |
| `Peca` | Com código nacional e fornecedor rastreável |
| `Fornecedor` | Responsável pela garantia das peças |
| `Colaborador` | Com uma ou mais funções |
| `Parceiro` | Empresa terceirizada para serviços externos |

---

## 📅 Cronograma

| Marco | Data |
|-------|------|
| Início do projeto | 01/06/2026 |
| Entrega de Requisitos e MER/DER | 08/06/2026 |
| Entrega de todos os artefatos | 15/06/2026 |
| Apresentação final | 16/06/2026 |

---

## 👥 Equipe

| Nome | GitHub |
|------|--------|
| Caio Nunes de Abreu | — |
| Cassiano Nunes de Abreu | [@Nomscodes](https://github.com/Nomscodes) |
| Gabriel Naoki Uto Turigoe | — |
| Wyllian Mariano | — |

---

## 🏫 Informações Acadêmicas

| Item | Detalhe |
|------|---------|
| Instituição | SENAI FATESG — Goiânia, GO |
| Curso | Superior de Análise e Desenvolvimento de Sistemas |
| Período | 3º Semestre |
| Semestre | 2026/1 |
| Professor Líder | Eugênio Júlio Messala C. Carvalho |
| Coordenação Técnica | Fabrícia Neres Borges |
| Coordenação Pedagógica | Eduardo Costa Jil |

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos — SENAI FATESG 2026/1.
