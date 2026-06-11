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
pi-car-auto-center/
├── src/br/com/picarauto/
│   ├── model/          # Entidades e regras de domínio
│   ├── dao/            # Acesso ao banco de dados (DAO Pattern)
│   ├── service/        # Lógica de negócio
│   ├── view/           # Telas Java Swing
│   ├── controller/     # Controladores (MVC)
│   └── util/           # Utilitários (conexão, helpers)
├── database/
│   └── schema.sql      # Script de criação do banco
├── docs/
│   ├── atas/           # Atas de reunião
│   ├── ers/            # Especificação de Requisitos do Sistema
│   └── diagramas/      # DER, Casos de Uso, Diagrama de Classes
├── lib/                # Dependências externas (.jar)
├── .github/
│   └── pull_request_template.md
├── .gitignore
├── CONTRIBUTING.md
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
| NetBeans IDE | Ambiente de desenvolvimento |

---

## ▶️ Como Executar

> **Pré-requisitos:** Java 17+ instalado · Compatível com Windows e Linux

```bash
# Clone o repositório
git clone https://github.com/Nomscodes/pi-car-auto-center.git

# Acesse o diretório
cd pi-car-auto-center

# Compile o projeto
javac -cp "lib/*" -d out src/**/*.java

# Execute
java -cp "out:lib/*" br.com.picarauto.Main
```

---

## 📝 Regras de Commit

> ⚠️ **Regra principal: um commit = um escopo. Nunca misture escopos em um único commit.**

Seguimos o padrão **Conventional Commits**. Todo commit deve ter a estrutura:

```
<tipo>(<escopo>): descrição curta em português
```

### Tipos

| Tipo | Quando usar |
|------|-------------|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Alteração em documentação |
| `refactor` | Refatoração sem nova funcionalidade |
| `test` | Adição ou ajuste de testes |
| `chore` | Configuração, build, dependências |
| `style` | Formatação sem mudança de lógica |

### Escopos válidos

| Escopo | O que cobre |
|--------|-------------|
| `cliente` | Tudo relacionado ao cadastro de clientes |
| `veiculo` | Cadastro e histórico de veículos |
| `os` | Ordem de Serviço e seu ciclo de vida |
| `peca` | Cadastro e rastreabilidade de peças |
| `fornecedor` | Cadastro de fornecedores |
| `colaborador` | Cadastro de colaboradores e funções |
| `parceiro` | Empresas terceirizadas |
| `garantia` | Controle de garantias de peças e serviços |
| `pagamento` | Registro e controle de pagamentos |
| `db` | Schema, migrations, banco de dados |
| `ui` | Componentes visuais sem escopo específico |
| `util` | Classes utilitárias (conexão, helpers) |
| `config` | Arquivos de configuração do projeto |

### Exemplos corretos ✅

```bash
git commit -m "feat(cliente): adiciona cadastro de pessoa jurídica"
git commit -m "fix(os): corrige transição de status para Finalizada"
git commit -m "refactor(db): separa criação de tabelas em métodos distintos"
git commit -m "docs: adiciona ata de reunião de 01/06/2026"
git commit -m "chore(config): adiciona sqlite-jdbc ao lib"
```

### Exemplos incorretos ❌

```bash
# ❌ Mistura dois escopos no mesmo commit
git commit -m "feat: cadastro de cliente e veículo"

# ❌ Sem escopo e descrição vaga
git commit -m "ajustes"

# ❌ Em inglês (este projeto usa português)
git commit -m "feat(client): add new form"
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
| Caio Nunes de Abreu | [@Caio4breu](https://github.com/Caio4breu) |
| Cassiano Nunes de Abreu | [@Nomscodes](https://github.com/Nomscodes) |
| Gabriel Naoki Uto Turigoe | [@GabrielNaokiUT](https://github.com/GabrielNaokiUT)) | 
| Wyllian Mariano | — | [@wyllianmn](https://github.com/wyllianmn)) | 

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
----------------------