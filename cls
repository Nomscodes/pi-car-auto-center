b034461 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): fotos dos modelos, duplo clique e correção logos achatadas
84b71e7 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona modelos completos por marca em PanelSelecaoModelo
2659629 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): remove label nome e quadra cards de marca em PanelSelecaoMarca
02aa30b - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona logos reais e duplo clique em PanelSelecaoMarca
e7258e1 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelLogin com layout split navy/cream
7c3194d - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige carregamento da logo com debug, tentativa .jfif e fallback classloader
a4d541c - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): declara resources explicitamente no pom.xml para incluir imagens
783c7a1 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): logo 260x130, exibe splash 2s antes de navegar para dashboard no login
518f50c - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): chama splash.reiniciar() ao navegar para TELA_SPLASH e armazena instância
b74ab8f - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): adiciona reiniciar() em PanelSplash para reutilizar animação pós-login
8d1d6aa - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): reescreve PanelSplash com timer automático 3s, JProgressBar gold e navegação para LOGIN
e230227 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): adiciona getInstance(), inicia com splash e chama atualizarTodas() ao trocar tela
298bdbb - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): adiciona atualizarUsuario() e registro estático de instâncias em SidebarPanel
0d43ed7 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelCadastroServicos
6ef6591 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelMarcasModelos
c2eff73 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelCadastroFornecedor
9c47129 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelCadastroColaborador
6716e2b - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelCadastroPeca
f6b4513 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelListaClientes
524d167 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige campo Pesquisar com placeholder, FocusListener e painelBusca em PanelListaOS
d4e8fda - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelCadastroServicos
17b8040 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelMarcasModelos
a510af1 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelCadastroFornecedor
1f596ae - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelCadastroColaborador
96ab91c - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelCadastroPeca
3f9f186 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): aplica padding 16px, rowHeight 32 e intercellSpacing 12 em PanelListaClientes
a576d8f - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): reduz marcas mockadas para 7 especificadas em PanelComposicaoOS
f391e3a - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): altera botão Nova OS para navegar direto para TELA_COMPOSICAO em PanelListaOS
ad48861 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige navegação de PanelCadastroVeiculo para TELA_LISTA_CLIENTES após salvar
9eba9aa - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): altera botão Nova OS da sidebar para navegar direto para TELA_COMPOSICAO
6eda61a - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige logo para 220x110, adiciona label AUTO CENTER e define usuário no login
4880e99 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona busca em tempo real em PanelMarcasModelos
31ba141 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona busca em tempo real em PanelCadastroServicos
729b6ae - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroFornecedor com lista e busca em tempo real
a898197 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroColaborador com lista, busca e JRadioButton de função
f0f1439 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona busca em tempo real em PanelCadastroPeca
b5d1a15 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona busca em tempo real em PanelListaClientes
85f044c - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelComposicaoOS com formulário interativo e total dinâmico
1a8f255 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): corrige fluxo Nova OS e adiciona busca em tempo real em PanelListaOS
b305832 - NomsCodes, cassiano.abreuu@gmail.com : refactor(ui): torna modoNovaOS campo estático em PanelSelecaoMarca
0ac4044 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): atualiza SidebarPanel com ícones Segoe UI Symbol e username dinâmico
00b24fc - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona carregamento de logo via ImageIcon em PanelLogin
e8803d1 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelMarcasModelos com abas Marcas/Modelos
95e6b7d - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroServicos com abas Internos/Externos
a6d165a - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroFornecedor com grid 3x2
0dff9d9 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroColaborador com JComboBox de funcao
42972d5 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroPeca com botao Importar Excel green
4df432a - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroVeiculo com grid 2 colunas e cascata marca/modelo
8e3e030 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelCadastroCliente com toggle PF/PJ e grid 2 colunas
5264632 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelListaClientes com chips de filtro PF/PJ
90fa4fd - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelSelecaoModelo com sidebar preservando logica
631eadc - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelSelecaoMarca com sidebar e 16 marcas
94ebced - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelComposicaoOS com card de detalhe e totais
9e01f79 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelListaOS com tabela e pills de status
1780009 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelDashboard com metricas e sidebar
31098a1 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelSplash com fundo navy e barra de progresso gold
aafa47f - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): redesenha PanelLogin com card cream centralizado
c548fce - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): cria SidebarPanel componente reutilizavel de navegacao lateral
94459e4 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona constante COR_BORDER ao MainFrame
e0fd3e2 - Gabriel Naoki, gabrielnaokiu@gmail.com : Merge branch 'main' of https://github.com/Nomscodes/pi-car-auto-center
ee6804a - Gabriel Naoki, gabrielnaokiu@gmail.com : build: atualiza configuração do pom.xml com ajuste do professor
7a8d1ea - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #56 from Nomscodes/feature/86e1pte4k-services
f564227 - Gabriel Naoki, gabrielnaokiu@gmail.com : fix(os): remove enfileiramento duplicado do beforeInsert
9b3284f - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #55 from Nomscodes/feature/86e1pte4k-services
0244526 - Gabriel Naoki, gabrielnaokiu@gmail.com : fix(os): corrige enfileiramento, expõe fila e adiciona processamento FIFO
4e5be78 - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #54 from Nomscodes/feature/86e1pte4k-services
56754c3 - Gabriel Naoki, gabrielnaokiu@gmail.com : fix(peca): adiciona @Transactional(readOnly = true) no método findByCodigoNacional
ece15ac - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #53 from Nomscodes/feature/86e1pte4k-validations
e4f9729 - Gabriel Naoki, gabrielnaokiu@gmail.com : feat(colaborador): adiciona validação de campos do colaborador
aedb4d3 - Caio Abreu, abreuocaio@gmail.com : Merge pull request #52 from Nomscodes/feature/design-patterns-and-structures
e881455 - Caio4breu, abreuocaio@gmail.com : feat(os): adiciona TabelaHashOS como indice de lookup por placa em O(1)
b5bdfdd - Caio4breu, abreuocaio@gmail.com : feat(os): adiciona ArvoreOS como indice de busca rapida por id em O(log n)
736f7ab - Caio4breu, abreuocaio@gmail.com : feat(os): adiciona busca binaria por data no OrdenadorOS
3c621eb - Caio4breu, abreuocaio@gmail.com : refactor(os): renomeia buscarPorId para buscarSequencialPorId na FilaOS
8abdf7b - Caio4breu, abreuocaio@gmail.com : feat(os): implementa ResumoComPecas com busca real no repositorio
2434017 - Caio4breu, abreuocaio@gmail.com : fix(peca): adiciona validacao de campos obrigatorios na importacao Excel
b9b9926 - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #51 from Nomscodes/feature/86e1pte4k-models-repositories
fe8078d - Gabriel Naoki, gabrielnaokiu@gmail.com : fix(fornecedor): adiciona validação de telefone duplicado no insert
5b4296c - Cassiano Abreu, cassiano.abreuu@gmail.com : Merge pull request #50 from Nomscodes/fix/inconsistencias-modelo
922cdaf - NomsCodes, cassiano.abreuu@gmail.com : fix(modelo): anoModelo LocalDate para Integer e idOS persistido em itemPedidoServicoExterno
82cfd9b - Cassiano Abreu, cassiano.abreuu@gmail.com : Merge pull request #49 from Nomscodes/fix/lombok-warnings
fa613d6 - NomsCodes, cassiano.abreuu@gmail.com : fix(model): adiciona EqualsAndHashCode callSuper=false em todos os models
bde31c7 - Cassiano Abreu, cassiano.abreuu@gmail.com : Merge pull request #48 from Nomscodes/fix/criticos-backend
7db96c1 - NomsCodes, cassiano.abreuu@gmail.com : fix(util): corrige mensagens de constraint para sintaxe do PostgreSQL
2627e72 - NomsCodes, cassiano.abreuu@gmail.com : fix(db): corrige funcaoColaborador ausente, colaborador duplicado e FK veiculo->cliente
425800a - NomsCodes, cassiano.abreuu@gmail.com : fix(colaborador): substitui idFuncao por ManyToOne para FuncaoColaboradorModel
adda1e6 - NomsCodes, cassiano.abreuu@gmail.com : fix(colaborador): remove existsByEmail invalido do repositorio
dd781ae - NomsCodes, cassiano.abreuu@gmail.com : fix(fornecedor): remove throw incondicional em validateInsert
9a6e9f0 - NomsCodes, cassiano.abreuu@gmail.com : fix(config): corrige url do banco de dados para pi_car_auto_center
85d5cfa - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): refatora PanelListaOS com cards de resumo, busca e ordenacao
740d0ca - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): centraliza logo no login
ea6bfef - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige layout do login com GridBagLayout e tamanhos fixos
e563f43 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige logo do login
a23d0d3 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): badge usuario no dashboard, CNPJ obrigatorio, logo login
e4da9bd - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige cards dashboard, centraliza login, adiciona usuario e botao sair
1ae0d3b - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): atualiza MainFrame com todas as constantes de tela
a0e204a - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona login, lista de clientes com exclusao e corrige fornecedor
4dd46fe - NomsCodes, cassiano.abreuu@gmail.com : fix(fornecedor): remove existsByTelefone da validation
863304f - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroFornecedor e atualiza PanelSelecaoModelo
6716c4e - Cassiano Abreu, cassiano.abreuu@gmail.com : Merge pull request #47 from Nomscodes/feature/ui-telas
020bd7d - Cassiano Abreu, cassiano.abreuu@gmail.com : Merge branch 'main' into feature/ui-telas
54c306c - NomsCodes, cassiano.abreuu@gmail.com : fix(db): remove unique do telefone em fornecedor no schema
6c6e1c7 - NomsCodes, cassiano.abreuu@gmail.com : fix(fornecedor): remove unique do telefone, adiciona FornecedorValidation
7f308b3 - NomsCodes, cassiano.abreuu@gmail.com : docs(db): adiciona seed com 20 marcas e 105 modelos do mercado brasileiro
2b007ee - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelMarcasModelos com abas e dialog de nova marca
2acd457 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroServicos com abas interno e externo
da5536f - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroColaborador com funcao e dados profissionais
eee7b75 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroPeca com banner de importacao Excel
4c9b04b - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): recarrega modelos ao navegar para tela de selecao de modelo
a55d583 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): limita raio dos circulos para nao cortar texto em tela cheia
f141dea - Gabriel Naoki Uto Turigoe, gabrielnaokiu@gmail.com : Merge pull request #46 from Nomscodes/feature/86e1pte4k-validations
7bcff50 - Gabriel Naoki, gabrielnaokiu@gmail.com : feat(fornecedor): adiciona IFornecedorValidation e FornecedorValidation
d03b4b0 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige tamanho dos circulos de marca e modelo
d060904 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): card Veiculos vai para selecao de marca antes do cadastro
9e27872 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): cards em circulo, adiciona PanelSelecaoModelo e fluxo veiculo
25ddc34 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige navegacao do card Veiculos para tela de cadastro
b73bba8 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroVeiculo com cascata marca-modelo
e6bac99 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige layout do cadastro de cliente removendo espaco vazio
4261017 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): atualiza MainFrame com PanelCadastroCliente
cb8a6d1 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelCadastroCliente com toggle PF/PJ
91d89de - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): registra PanelComposicaoOS no MainFrame
a36c53f - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige clique botao Abrir e padding celulas da tabela
94d65cb - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelComposicaoOS com itens, total e acoes
1d2c0e3 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): adiciona padding nas celulas e cabecalho da tabela OS
450af79 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): remove scroll lateral da tabela e corrige voltar em Veiculos
c98ac5f - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige botao ordenar e aumenta tamanho minimo da janela
7825b72 - NomsCodes, cassiano.abreuu@gmail.com : fix(ui): corrige visual e funcionalidade do botão voltar
b0b546e - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelListaOS com tabela, filtros e badges de status
14ad7b8 - NomsCodes, cassiano.abreuu@gmail.com : feat(ui): adiciona PanelSelecaoMarca com grid de marcas estilo Webmotors
4c4d0fa - NomsCodes, cassiano.abreuu@gmail.com : chore(config): pom.xml funcional com Java 21 e Lombok 1.18.36
2f7fabf - NomsCodes, cassiano.abreuu@gmail.com : chore(config): atualiza maven-compiler-plugin para 3.14.0
bfd7cd2 - NomsCodes, cassiano.abreuu@gmail.com : chore(config): atualiza Lombok para 1.18.36 compatível com Java 24
aa3b586 - NomsCodes, cassiano.abreuu@gmail.com : chore(config): atualiza java.version para 24
b20da03 - NomsCodes, cassiano.abreuu@gmail.com : chore(config): atualiza Lombok para 1.18.34 compatível com Java 21
5de9d75 - NomsCodes, cassiano.abreuu@gmail.com : chore(config): atualiza java.version para 21
570a5eb - NomsCodes, cassiano.abreuu@gmail.com : chore(config): adiciona Lombok como dependência e annotation processor
