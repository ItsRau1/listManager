# Gerenciador de Listas - Android App

Uma aplicação Android para gerenciar listas e seus itens, armazenando os dados em arquivos de texto.

## 🚀 Início Rápido

**Método mais simples (1 comando):**
```bash
./deploy.sh
```

**Ou passo a passo (3 comandos):**
```bash
./setup.sh    # 1. Setup inicial (só uma vez)
./build.sh    # 2. Compilar APK
./install.sh  # 3. Instalar no dispositivo
```

📖 **Documentação Completa:**
- 📚 **[INDEX.md](INDEX.md)** - Índice de toda documentação
- 🚀 **[GUIA_RAPIDO.md](GUIA_RAPIDO.md)** - Compilar sem Android Studio (RECOMENDADO)
- 📱 **[EXEMPLO_USO.md](EXEMPLO_USO.md)** - Tutorial visual passo a passo
- 📂 **[SISTEMA_SUBLISTAS.md](SISTEMA_SUBLISTAS.md)** - Sistema hierárquico Shibata/Nagumo
- 🎨 **[SISTEMA_TEMAS.md](SISTEMA_TEMAS.md)** - Temas claro/escuro com toggle
- 📋 **[IMPORTACAO_LISTAS.md](IMPORTACAO_LISTAS.md)** - Importar listas de texto formatado ⭐ NOVO
- 🔄 **[TRANSFERENCIA_SUBLISTAS.md](TRANSFERENCIA_SUBLISTAS.md)** - Transferir itens entre sublistas
- ✓ **[ATIVAR_INATIVAR_ITENS.md](ATIVAR_INATIVAR_ITENS.md)** - Ativar/Inativar itens com swipe
- 🗑️ **[NOVAS_FUNCIONALIDADES.md](NOVAS_FUNCIONALIDADES.md)** - Exclusão de listas e itens
- 🔄 **[DRAG_AND_DROP.md](DRAG_AND_DROP.md)** - Reordenação manual (arrastar e soltar)
- 📋 **[CHEATSHEET.md](CHEATSHEET.md)** - Referência rápida de comandos
- 📗 **[INSTRUCOES.md](INSTRUCOES.md)** - Instruções completas (inclui Android Studio)

## Funcionalidades

- ✅ Criar múltiplas listas manualmente
- ✅ **Importar listas de texto** ⭐ NOVO
  - Nome automático com data (dd-MM-yyyy)
  - Parser de texto formatado
  - Suporte a sublistas Shibata/Nagumo
  - Itens com ou sem quantidade
- ✅ **Sistema de sublistas** - Cada lista contém 2 sublistas fixas:
  - **Shibata**
  - **Nagumo**
- ✅ Adicionar itens em cada sublista com:
  - Nome (String)
  - Quantidade (Double)
- ✅ Navegação hierárquica: **Listas → Sublistas → Itens** ⭐ NOVO
- ✅ Visualizar todas as listas criadas
- ✅ Visualizar sublistas com contador de itens ⭐ NOVO
- ✅ Visualizar todos os itens de uma sublista
- ✅ **Excluir itens** (long press sem arrastar)
- ✅ **Excluir listas** (long press sem arrastar)
- ✅ **Reordenar listas** (arrastar e soltar)
- ✅ **Reordenar itens dentro da sublista** (arrastar e soltar)
- ✅ **Transferir itens entre sublistas** (swipe →) ⭐ NOVO
  - Shibata ↔ Nagumo
  - Posicionamento inteligente (ativos/inativos)
  - Um gesto transfere
- ✅ **Ativar/Inativar itens** (swipe ←)
  - Itens inativos ficam ~~taxados~~
  - Itens inativos vão para o final
  - Reativar mantém posição
- ✅ **Sistema de temas claro/escuro** ⭐ NOVO
  - Toggle no menu (⋮) da barra superior
  - Tema claro: Off-white e Azul
  - Tema escuro: Preto suave e Roxo
  - Preferência persistente
- ✅ Armazenamento persistente em arquivos de texto
- ✅ Diálogos de confirmação antes de excluir

## Estrutura do Projeto

### Classes Principais

- **Item.kt**: Modelo de dados para um item (nome, quantidade e estado ativo/inativo)
- **Sublista.kt**: Modelo de dados para uma sublista contendo itens
- **Lista.kt**: Modelo de dados para uma lista contendo sublistas (Shibata e Nagumo)
- **StorageManager.kt**: Gerencia a persistência dos dados em arquivos de texto
- **ThemeManager.kt**: Gerencia temas e preferências do usuário
- **ImportadorLista.kt**: Parser e importador de listas a partir de texto ⭐ NOVO
- **MainActivity.kt**: Tela principal que exibe todas as listas
- **SublistasActivity.kt**: Tela que exibe as sublistas (Shibata e Nagumo)
- **ItensActivity.kt**: Tela que exibe os itens de uma sublista específica
- **ListasAdapter.kt**: Adapter para RecyclerView das listas
- **ItensAdapter.kt**: Adapter para RecyclerView dos itens

### Armazenamento

Os dados são salvos em arquivos de texto no diretório interno do aplicativo:
- Cada lista é salva em um arquivo separado: `<nome_da_lista>.txt`
- Formato com marcadores de sublista: `[SUBLISTA:Shibata]` e `[SUBLISTA:Nagumo]`
- Cada linha de item no formato: `nome;quantidade;ativo`
- Arquivo `_order.txt` armazena a ordem personalizada das listas
- Os arquivos ficam em: `/data/data/com.example.listmanager/files/listas/`

**Exemplo de arquivo (Compras.txt):**
```
[SUBLISTA:Shibata]
Arroz;5.0;true
Feijão;3.5;false
[SUBLISTA:Nagumo]
Café;1.0;true
Leite;2.5;true
```
- Campo 3: Estado (true=ativo, false=inativo)

## Como Usar

### 1. Criar uma Nova Lista

1. Na tela principal, toque no botão flutuante (**+**) no canto inferior direito
2. No menu que se expande, escolha "**Nova lista**" (ou "**Importar lista**")
3. Digite o nome da lista
4. Clique em "**Criar**"

### 2. Adicionar Itens a uma Lista

1. Clique em uma lista para visualizar as sublistas
2. Escolha uma sublista: **Shibata** ou **Nagumo**
3. Toque no botão flutuante "**Adicionar item**"
4. Preencha o nome e a quantidade do item
5. Clique em "**Adicionar**"

### 3. Visualizar Itens

- Clique em uma lista para ver as sublistas (Shibata e Nagumo)
- Clique em uma sublista para ver seus itens
- Cada item mostra o nome e a quantidade
- O título mostra: "Lista - Sublista"

## Requisitos

- Android SDK 24 ou superior (Android 7.0+)
- Kotlin 1.8.0
- Android Studio (para compilar)

## Compilação

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Adicione os ícones do launcher nas pastas mipmap (ou use ícones padrão)
4. Execute o projeto em um emulador ou dispositivo físico

## Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação
- **RecyclerView**: Para exibir listas de forma eficiente
- **CardView**: Para UI dos itens
- **Material Design**: Para componentes de UI modernos
- **File I/O**: Para persistência de dados em arquivos de texto

## Observações

- Os ícones do launcher (`ic_launcher.png` e `ic_launcher_round.png`) precisam ser adicionados nas pastas mipmap
- Os dados são armazenados localmente no dispositivo
- Não há validação para nomes duplicados de itens na mesma lista
- O aplicativo usa armazenamento interno, então os dados são privados ao app

## Estrutura de Arquivos de Dados

Exemplo de arquivo de lista (`Compras.txt`):
```
Arroz;5.0
Feijão;3.5
Açúcar;2.0
```

Cada linha segue o formato: `Nome;Quantidade`
