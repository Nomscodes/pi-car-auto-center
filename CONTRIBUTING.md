# 🤝 Guia de Contribuição — pi-car-auto-center

Obrigado por contribuir com o projeto! Siga as convenções abaixo para manter o histórico limpo e organizado.

---

## 🌿 Branches

| Branch | Uso |
|--------|-----|
| `main` | Código estável — só recebe via PR |
| `dev` | Branch de desenvolvimento principal |
| `feature/nome` | Nova funcionalidade |
| `fix/nome` | Correção de bug |
| `docs/nome` | Documentação |
| `refactor/nome` | Refatoração sem nova funcionalidade |

**Exemplo:**
```bash
git checkout -b feature/cadastro-cliente
git checkout -b fix/status-ordem-servico
```

---

## 📝 Conventional Commits

Todo commit deve seguir o padrão:

```
<tipo>(escopo opcional): descrição curta em português
```

### Tipos permitidos

| Tipo | Quando usar |
|------|-------------|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Alteração em documentação |
| `refactor` | Refatoração de código |
| `test` | Adição ou ajuste de testes |
| `chore` | Tarefas de configuração/build |
| `style` | Formatação, sem mudança de lógica |

### Exemplos

```bash
git commit -m "feat(cliente): adiciona cadastro de pessoa física"
git commit -m "fix(os): corrige transição de status para Finalizada"
git commit -m "docs: atualiza README com instruções de execução"
git commit -m "refactor(dao): separa DAOs de Peça e Fornecedor"
git commit -m "chore: adiciona .gitignore para arquivos Java"
```

---

## 🔁 Fluxo de trabalho

```bash
# 1. Atualiza o dev local
git checkout dev
git pull origin dev

# 2. Cria sua branch
git checkout -b feature/nome-da-funcionalidade

# 3. Desenvolve e commita
git add .
git commit -m "feat(escopo): descrição"

# 4. Sobe a branch
git push origin feature/nome-da-funcionalidade

# 5. Abre Pull Request para dev no GitHub
```

---

## ✅ Checklist antes do PR

- [ ] Código compila sem erros
- [ ] Nenhum arquivo `.db`, `.class` ou `.iml` commitado
- [ ] Commits seguem o padrão Conventional Commits
- [ ] Descrição do PR explica o que foi feito

---

## 👥 Equipe

| Nome | Responsabilidade |
|------|-----------------|
| Caio Nunes de Abreu | — |
| Cassiano Nunes de Abreu | — |
| Gabriel Naoki Uto Turigoe | — |
| Wyllian Mariano | — |
