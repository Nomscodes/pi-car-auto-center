<div align="center">

# 🔧 AV CAR AUTO CENTER — Sistema de Controle de Oficina Mecânica

**Projeto Integrador 2026/1 · SENAI FATESG · ADS 3º Período**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Swing](https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)
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
- ✅ Importação de planilhas Excel (`.xlsx`) para cadastro de peças via **Adapter**
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
├── src/main/
│   ├── java/br/com/picarauto/
│   │   ├── adapter/        # Padrão Adapter — importação de planilhas Excel
│   │   ├── controller/     # Controladores (MVC)
│   │   ├── dao/            # Placeholder de rastreabilidade Git
│   │   ├── decorator/      # Padrão Decorator — resumo de OS
│   │   ├── factory/        # Padrão Factory — criação de itens de serviço
│   │   ├── model/          # Entidades JPA e DTOs
│   │   ├── repository/     # Interfaces Spring Data JPA
│   │   ├── service/        # Lógica de negócio
│   │   ├── util/           # Utilitários (FilaOS, Ordenadores, ContextoAplicacao)
│   │   ├── validation/     # Validações de campos e regras de negócio
│   │   ├── view/           # Telas Java Swing
│   │   └── Main.java       # Ponto de entrada Spring Boot
│   └── resources/
│       └── application.properties  # Configuração do banco e JPA
├── database/
│   └── schema.sql          # Script de referência do banco
├── .github/
│   └── pull_request_template.md
├── .gitignore
├── CONTRIBUTING.md
└── README.md
```

---

## 🛠️ Tecnologias

| Tecnologia        | Versão  | Uso                                        |
|-------------------|---------|--------------------------------------------|
| Java              | 17+     | Linguagem principal                        |
| Spring Boot       | 3.3.0   | Inicialização e configuração da aplicação  |
| Spring Data JPA   | 3.3.0   | Repositórios e acesso ao banco de dados    |
| Hibernate         | 6.x     | ORM — mapeamento objeto-relacional         |
| PostgreSQL        | 42.7.3  | Banco de dados relacional                  |
| Apache POI        | 5.2.5   | Leitura de planilhas `.xlsx`               |
| Java Swing        | —       | Interface gráfica desktop                  |
| NetBeans IDE      | —       | Ambiente de desenvolvimento                |
| Maven             | —       | Gerenciamento de dependências e build      |

---

## ⚙️ Pré-requisitos

- Java 17+
- PostgreSQL instalado e rodando
- Banco de dados criado com o nome `pi_car_auto_center`
- Usuário `postgres` com senha `postgres` (padrão — ajuste no `application.properties` se necessário)

---

## ▶️ Como Executar

```bash
# Clone o repositório
git clone https://github.com/Nomscodes/pi-car-auto-center.git

# Acesse o diretório
cd pi-car-auto-center

# Compile e execute via Maven
mvn spring-boot:run
```

> O Hibernate cria as tabelas automaticamente na primeira execução (`ddl-auto=update`).

---

## 🧩 Padrões de Projeto Aplicados

| Padrão          | Classe principal          | Pacote              |
|-----------------|---------------------------|---------------------|
| Singleton       | `ContextoAplicacao`       | `util`              |
| Iterator        | `FilaOS`                  | `util`              |
| Template Method | `OrdenadorOS`             | `util`              |
| Factory Method  | `IServicoItemFactory`     | `factory`           |
| Decorator       | `ResumoOSDecorator`       | `decorator`         |
| Adapter         | `PecaExcelAdapter`        | `adapter`           |

---

## 📊 Estrutura de Dados

| Estrutura / Algoritmo     | Classe              | Justificativa                                              |
|---------------------------|---------------------|------------------------------------------------------------|
| Fila encadeada (FIFO)     | `FilaOS`            | Respeita a ordem de chegada dos veículos à oficina         |
| Insertion Sort manual     | `OrdenadorOS`       | Eficiente para listas pequenas e parcialmente ordenadas    |
| Busca linear via Iterator | `FilaOS`            | Adequada ao volume de OS de uma oficina de médio porte     |

---

## 📝 Regras de Commit

> ⚠️ **Regra principal: um commit = um escopo. Nunca misture escopos em um único commit.**

Seguimos o padrão **Conventional Commits**. Todo commit deve ter a estrutura:

```
<tipo>(<escopo>): descrição curta em português
```

### Tipos

| Tipo       | Quando usar                         |
|------------|-------------------------------------|
| `feat`     | Nova funcionalidade                 |
| `fix`      | Correção de bug                     |
| `docs`     | Alteração em documentação           |
| `refactor` | Refatoração sem nova funcionalidade |
| `test`     | Adição ou ajuste de testes          |
| `chore`    | Configuração, build, dependências   |
| `style`    | Formatação sem mudança de lógica    |

### Escopos válidos

| Escopo        | O que cobre                               |
|---------------|-------------------------------------------|
| `cliente`     | Tudo relacionado ao cadastro de clientes  |
| `veiculo`     | Cadastro e histórico de veículos          |
| `os`          | Ordem de Serviço e seu ciclo de vida      |
| `peca`        | Cadastro e rastreabilidade de peças       |
| `fornecedor`  | Cadastro de fornecedores                  |
| `colaborador` | Cadastro de colaboradores e funções       |
| `parceiro`    | Empresas terceirizadas                    |
| `garantia`    | Controle de garantias de peças e serviços |
| `pagamento`   | Registro e controle de pagamentos         |
| `db`          | Schema, migrations, banco de dados        |
| `ui`          | Componentes visuais sem escopo específico |
| `util`        | Classes utilitárias                       |
| `config`      | Arquivos de configuração do projeto       |
| `adapter`     | Importação de dados externos              |

### Exemplos corretos ✅

```bash
git commit -m "feat(peca): adiciona importação de planilha Excel via adapter"
git commit -m "feat(cliente): adiciona cadastro de pessoa jurídica"
git commit -m "fix(os): corrige transição de status para Finalizada"
git commit -m "refactor(config): migra persistência para Spring Data JPA"
git commit -m "chore(config): adiciona dependência Apache POI ao pom.xml"
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

| Entidade         | Descrição                                    |
|------------------|----------------------------------------------|
| `Cliente`        | PF ou PJ — vinculado ao histórico do veículo |
| `Veiculo`        | Identificado por marca, modelo e ano         |
| `OrdemDeServico` | Núcleo do sistema — controla todo o fluxo    |
| `Servico`        | Interno ou terceirizado, com garantia        |
| `Peca`           | Com código nacional e fornecedor rastreável  |
| `Fornecedor`     | Responsável pela garantia das peças          |
| `Colaborador`    | Com uma ou mais funções                      |
| `Parceiro`       | Empresa terceirizada para serviços externos  |

---

## 📅 Cronograma

| Marco                           | Data       |
|---------------------------------|------------|
| Início do projeto               | 01/06/2026 |
| Entrega de Requisitos e MER/DER | 08/06/2026 |
| Entrega de todos os artefatos   | 15/06/2026 |
| Apresentação final              | 16/06/2026 |

---

## 👥 Equipe

| Nome                      | GitHub                                                  |
|---------------------------|---------------------------------------------------------|
| Caio Nunes de Abreu       | [@Caio4breu](https://github.com/Caio4breu)              |
| Cassiano Nunes de Abreu   | [@Nomscodes](https://github.com/Nomscodes)              |
| Gabriel Naoki Uto Turigoe | [@GabrielNaokiUT](https://github.com/GabrielNaokiUT)   |
| Wyllian Mariano           | [@wyllianmn](https://github.com/wyllianmn)              |

---

## 🏫 Informações Acadêmicas

| Item                   | Detalhe                                           |
|------------------------|---------------------------------------------------|
| Instituição            | SENAI FATESG — Goiânia, GO                        |
| Curso                  | Superior de Análise e Desenvolvimento de Sistemas |
| Período                | 3º Semestre                                       |
| Semestre               | 2026/1                                            |
| Professor Líder        | Eugênio Júlio Messala C. Carvalho                 |
| Coordenação Técnica    | Fabrícia Neres Borges                             |
| Coordenação Pedagógica | Eduardo Costa Jil                                 |

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos — SENAI FATESG 2026/1.
