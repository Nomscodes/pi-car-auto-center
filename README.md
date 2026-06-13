<div align="center">

# 🔧 AV CAR AUTO CENTER
## Sistema de Controle de Oficina Mecânica

**Projeto Integrador 2026/1 · SENAI FATESG · ADS 3º Período**

[![Java](https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL%2017-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Swing](https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)]()
[![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=for-the-badge)]()

</div>

---

## 📋 Sobre o Projeto

Sistema desktop de gerenciamento de **Ordens de Serviço (OS)** desenvolvido para a oficina mecânica **AV CAR AUTO CENTER**, de propriedade do Sr. Godofredo Silva.

O sistema substitui o controle manual atual, implementando rastreabilidade completa do ciclo de vida das OS — desde o orçamento até o pagamento — com controle de peças, fornecedores, garantias, serviços internos e terceirizados.

> 📌 **Disciplinas envolvidas:** Modelagem de Banco de Dados · Arquitetura e Projeto de Software · Estrutura de Dados I · Gestão de Projetos · Padrões de Projeto

---

## ✅ Funcionalidades Implementadas

| Módulo | Status |
|--------|--------|
| Cadastro de clientes PF e PJ (com máscaras CPF/CNPJ) | ✅ Integrado ao banco |
| Histórico de proprietários de veículos | ✅ Integrado ao banco |
| Gerenciamento de Ordens de Serviço | ✅ Integrado ao banco |
| Controle de status da OS (Orçamento → Execução → Pagamento → Finalizada) | ✅ Implementado |
| Cadastro de peças com código nacional e rastreabilidade por fornecedor | ✅ Em integração |
| Controle de garantia de peças e serviços | ✅ Implementado |
| Cadastro de colaboradores com funções | ✅ Em integração |
| Registro de serviços internos e terceirizados | ✅ Em integração |
| Seleção de marca e modelo de veículo com logos | ✅ Implementado |
| Importação de peças via Excel (.xlsx) | ✅ Implementado (Apache POI) |
| Funcionamento 100% local — sem dependência de internet | ✅ Garantido |

---

## 🗂️ Fluxo da Ordem de Serviço

```
┌─────────────┐   aprovação   ┌─────────────┐   conclusão   ┌─────────────┐   pagamento   ┌─────────────┐
│  ORÇAMENTO  │ ────────────► │   EXECUÇÃO  │ ────────────► │  PAGAMENTO  │ ────────────► │  FINALIZADO │
└─────────────┘               └─────────────┘               └─────────────┘               └─────────────┘
                                                                                                   │
                                                                                       início da garantia
```

> ⚠️ O fluxo é **unidirecional** — não há retrocesso de status.

---

## 🏗️ Arquitetura

O sistema adota o estilo arquitetural **Monolito em Camadas**, com separação clara de responsabilidades:

```
pi-car-auto-center/
├── src/main/java/br/com/picarauto/
│   ├── adapter/        # Padrão Adapter — importação Excel via Apache POI
│   ├── controller/     # Controladores — ponte entre View e Service
│   ├── decorator/      # Padrão Decorator — geração de resumo de OS
│   ├── factory/        # Padrão Factory Method — criação de itens de serviço
│   ├── model/          # Entidades JPA, DTOs e Exceptions
│   │   ├── base/       # BaseModel (id, ativo, dataHoraCriacao)
│   │   ├── dto/        # Data Transfer Objects
│   │   └── exception/  # FieldValidationException, RuleValidationException
│   ├── repository/     # Interfaces JPA (Spring Data)
│   ├── service/        # Lógica de negócio
│   ├── util/           # ContextoAplicacao (Singleton), FilaOS (Iterator),
│   │                   # OrdenadorOS (Template Method), ArvoreOS, TabelaHashOS
│   ├── validation/     # Validações de domínio por entidade
│   └── view/           # Telas Java Swing (CardLayout + paintComponent)
├── database/
│   ├── schema.sql      # Script de criação do banco (PostgreSQL)
│   └── seed.sql        # Dados iniciais: 20 marcas e 105 modelos
├── .github/
│   └── pull_request_template.md
├── CONTRIBUTING.md
└── README.md
```

### Regra de dependência entre camadas

```
View → Controller → Service → Validation + Repository → Model
```

A View **nunca** acessa Service ou Repository diretamente. O acesso ao Spring a partir das telas Swing é feito exclusivamente via `ContextoAplicacao.getBean()` (padrão Singleton).

---

## 🎨 Padrões de Projeto Implementados

| Padrão | Onde | O que faz |
|--------|------|-----------|
| **Singleton** | `ContextoAplicacao` | Ponto único de acesso ao contexto Spring a partir das Views Swing |
| **Iterator** | `FilaOS` + `FilaOSIterator` | Percorre a fila encadeada de OS sem expor a estrutura interna |
| **Template Method** | `OrdenadorOS` + subclasses | Insertion Sort com critério de ordenação variável (data ou valor) |
| **Factory Method** | `IServicoItemFactory` | Cria itens de serviço interno ou externo sem if/else no controller |
| **Decorator** | `ResumoOSDecorator` | Compõe o resumo da OS adicionando seções em tempo de execução |
| **Adapter** | `PecaExcelAdapter` | Traduz objetos Apache POI (Excel) para `PecaModel` |

### Estruturas de dados customizadas

| Estrutura | Classe | Algoritmo relacionado |
|-----------|--------|-----------------------|
| Fila encadeada (FIFO manual) | `FilaOS` | Busca sequencial via Iterator |
| Insertion Sort manual | `OrdenadorOS` | Ordenação por data ou valor |
| Busca Binária | `OrdenadorOS.buscarBinariaPorData()` | Aplicada após ordenação |
| Árvore Binária de Busca | `ArvoreOS` | Índice de consulta por id em O(log n) |
| Tabela Hash com encadeamento | `TabelaHashOS` | Lookup por placa em O(1) |

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java (Eclipse Temurin) | 21 | Linguagem principal |
| Spring Boot | 3.3.0 | Framework backend + injeção de dependência |
| Spring Data JPA | 3.3.0 | Persistência — herança JOINED, repositories |
| Lombok | 1.18.36 | Redução de boilerplate (getters, setters, equals) |
| PostgreSQL | 17 | Banco de dados relacional local |
| Apache POI | 5.2.5 | Importação de peças via Excel (.xlsx) |
| Java Swing | 21 | Interface gráfica desktop |
| Maven | 3.x | Build e gerenciamento de dependências |
| NetBeans IDE | — | Ambiente de desenvolvimento |

---

## ▶️ Como Executar

### Pré-requisitos

- **Java 21** (Eclipse Adoptium / Temurin) instalado
- **PostgreSQL 17** instalado e em execução
- **Maven** disponível (ou usar o embutido do NetBeans)

### 1. Clone o repositório

```bash
git clone https://github.com/Nomscodes/pi-car-auto-center.git
cd pi-car-auto-center
```

### 2. Configure o banco de dados

```sql
-- No psql ou pgAdmin, crie o banco:
CREATE DATABASE pi_car_auto_center;
```

```bash
# Aplique o schema e o seed:
psql -U postgres -d pi_car_auto_center -f database/schema.sql
psql -U postgres -d pi_car_auto_center -f database/seed.sql
```

> O arquivo `src/main/resources/application.properties` já está configurado para `localhost:5432` com usuário `postgres` e senha `postgres`. Ajuste se necessário.

### 3. Compile e execute

```bash
# Windows (PowerShell)
& "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" clean compile

# Linux/Mac
mvn clean compile
```

Ou execute diretamente pelo **botão Run (▶)** do NetBeans.

> ⚠️ Os ícones de erro no NetBeans são **cosméticos** — causados pela ausência do plugin Lombok para esta versão do NetBeans. O `BUILD SUCCESS` no Maven é o indicador real de saúde do projeto.

---

## 🗄️ Modelo de Dados — Entidades Principais

| Entidade | Tabela | Descrição |
|----------|--------|-----------|
| `PessoaModel` | `pessoa` | Base da herança JOINED — dados comuns a clientes e colaboradores |
| `ClienteModel` | `cliente` | Especialização de Pessoa |
| `PessoaFisicaModel` | `pessoaFisica` | CPF, RG, data de nascimento |
| `PessoaJuridicaModel` | `pessoaJuridica` | CNPJ, razão social, inscrição estadual |
| `ColaboradorModel` | `colaborador` | CPF, salário, data de admissão, função |
| `VeiculoModel` | `veiculo` | Placa (única), chassi (único), cor, modelo, cliente |
| `MarcaModel` | `marca` | Nome da montadora |
| `ModeloModel` | `modelo` | Nome do modelo, ano, marca |
| `OrdemServicoModel` | `ordemDeServico` | Status, datas, valor total, observações, veículo |
| `PecaModel` | `peca` | Código nacional (único), marca, modelo, ano, preço, garantia, fornecedor |
| `FornecedorModel` | `fornecedor` | CNPJ (único), razão social, telefone |
| `ServicoInternoModel` | `servicosInternos` | Descrição e valor cobrado |
| `ServicoExternoModel` | `servicoExterno` | Descrição e valor cobrado |

---

## 📝 Regras de Commit

Seguimos o padrão **Conventional Commits** em português. Todo commit deve ter a estrutura:

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

`cliente` · `veiculo` · `os` · `peca` · `fornecedor` · `colaborador` · `parceiro` · `garantia` · `pagamento` · `db` · `ui` · `util` · `config` · `adapter`

### Exemplos

```bash
# ✅ Corretos
git commit -m "feat(cliente): adiciona máscara de CPF em tempo real"
git commit -m "fix(ui): remove carregamento do banco dos construtores"
git commit -m "refactor(os): extrai lógica de ordenação para OrdenadorOS"
git commit -m "docs: atualiza README com stack atual"

# ❌ Incorretos
git commit -m "ajustes"                          # sem tipo e escopo
git commit -m "feat: cliente e veículo"          # mistura escopos
git commit -m "feat(client): add new form"       # em inglês
```

> ⚠️ **Regra principal: um commit = um escopo.** Nunca misture escopos em um único commit.

---

## 🔀 Estratégia de Branches

| Branch | Propósito |
|--------|-----------|
| `main` | Código estável — protegida, requer PR |
| `feature/*` | Novas funcionalidades |
| `fix/*` | Correções de bugs |
| `docs/*` | Documentação |

**Nunca commite diretamente na `main`.** Sempre abra um Pull Request.

---

## 📅 Cronograma

| Marco | Data |
|-------|------|
| Início do projeto | 01/06/2026 |
| Entrega de Requisitos e MER/DER | 08/06/2026 |
| **Entrega de todos os artefatos** | **15/06/2026** |
| **Apresentação final** | **16/06/2026** |

---

## 👥 Equipe

| Nome | GitHub | Papel |
|------|--------|-------|
| Caio Nunes de Abreu | [@Caio4breu](https://github.com/Caio4breu) | Backend / Configuração |
| Cassiano Nunes de Abreu | [@Nomscodes](https://github.com/Nomscodes) | Secretário / Repo Owner |
| Gabriel Naoki Uto Turigoe | [@GabrielNaokiUT](https://github.com/GabrielNaokiUT) | Models / Repositories |
| Wyllian Mariano | [@wyllianmn](https://github.com/wyllianmn) | Frontend / UI |

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
