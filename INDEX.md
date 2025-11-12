# 📚 Índice de Documentação - Gerenciador de Listas

Guia completo de todos os arquivos e documentação do projeto.

---

## 🎯 Por Onde Começar?

**Quer compilar APK rapidamente?** → Leia **[GUIA_RAPIDO.md](GUIA_RAPIDO.md)**

**Primeira vez usando?** → Leia **[EXEMPLO_USO.md](EXEMPLO_USO.md)**

**Quer referência rápida?** → Veja **[CHEATSHEET.md](CHEATSHEET.md)**

---

## 📖 Documentação

### 🚀 [GUIA_RAPIDO.md](GUIA_RAPIDO.md)
**Para quem quer compilar APK SEM Android Studio**

- Método super rápido (1 comando)
- Método passo a passo (3 comandos)
- Instalação de pré-requisitos
- Solução de problemas
- Comandos úteis

**Comece aqui se:** Você quer gerar APK de forma simples e rápida

---

### 📱 [EXEMPLO_USO.md](EXEMPLO_USO.md)
**Tutorial visual completo com exemplos práticos**

- Saída esperada de cada comando
- Preparação do dispositivo Android
- Como testar o app
- Screenshots em ASCII
- Instalação manual (sem cabo USB)

**Comece aqui se:** É sua primeira vez compilando um app Android

---

### 📋 [CHEATSHEET.md](CHEATSHEET.md)
**Referência rápida de comandos**

- Comandos de compilação
- Comandos ADB
- Gradle direto
- Solução rápida de problemas
- Estrutura de arquivos

**Use quando:** Precisa lembrar um comando específico

---

### 📘 [README.md](README.md)
**Documentação completa do projeto**

- Visão geral do app
- Funcionalidades
- Estrutura do código
- Classes principais
- Sistema de armazenamento

**Leia para:** Entender como o app funciona internamente

---

### 📗 [INSTRUCOES.md](INSTRUCOES.md)
**Instruções detalhadas (inclui método com Android Studio)**

- Compilação via linha de comando (método rápido)
- Compilação via Android Studio (método tradicional)
- Uso do aplicativo
- Estrutura do código
- Próximos passos

**Use quando:** Quer usar Android Studio ou precisa de instruções detalhadas

---

## 🔧 Scripts de Build

### ⭐ [deploy.sh](deploy.sh)
**Script tudo-em-um (RECOMENDADO)**

Executa automaticamente:
1. Setup inicial (se necessário)
2. Compilação do APK
3. Pergunta se quer instalar no dispositivo

```bash
./deploy.sh
```

---

### 🛠️ [setup.sh](setup.sh)
**Setup inicial do projeto**

- Baixa Gradle Wrapper
- Configura permissões
- Prepara ambiente

Execute **uma única vez** antes de compilar:
```bash
./setup.sh
```

---

### 🔨 [build.sh](build.sh)
**Compilar APK**

- Limpa builds anteriores
- Compila o projeto
- Mostra localização do APK

```bash
./build.sh
```

Resultado: `app/build/outputs/apk/debug/app-debug.apk`

---

### 📲 [install.sh](install.sh)
**Instalar APK no dispositivo**

- Verifica dispositivos conectados
- Instala via ADB
- Mostra erros se houver

```bash
./install.sh
```

Pré-requisito: Dispositivo com USB Debug ativado

---

## 📁 Estrutura do Projeto

```
ListManager/
│
├── 📖 Documentação
│   ├── README.md          # Documentação principal
│   ├── GUIA_RAPIDO.md     # Guia simplificado ⭐
│   ├── EXEMPLO_USO.md     # Tutorial com exemplos
│   ├── CHEATSHEET.md      # Referência rápida
│   ├── INSTRUCOES.md      # Instruções completas
│   └── INDEX.md           # Este arquivo
│
├── 🔧 Scripts
│   ├── deploy.sh          # Tudo-em-um ⭐
│   ├── setup.sh           # Setup inicial
│   ├── build.sh           # Compilar APK
│   └── install.sh         # Instalar no dispositivo
│
├── 📱 Código Fonte
│   └── app/
│       ├── src/main/
│       │   ├── java/com/example/listmanager/
│       │   │   ├── MainActivity.kt
│       │   │   ├── ItensActivity.kt
│       │   │   ├── Item.kt
│       │   │   ├── Lista.kt
│       │   │   ├── StorageManager.kt
│       │   │   ├── ListasAdapter.kt
│       │   │   └── ItensAdapter.kt
│       │   │
│       │   ├── res/
│       │   │   ├── layout/
│       │   │   ├── values/
│       │   │   └── drawable/
│       │   │
│       │   └── AndroidManifest.xml
│       │
│       └── build.gradle
│
└── ⚙️ Configuração
    ├── build.gradle
    ├── settings.gradle
    ├── gradle.properties
    ├── .gitignore
    └── gradle/
        └── wrapper/
```

---

## 🗂️ Fluxo de Trabalho Recomendado

### Primeira Vez
```bash
1. Leia: EXEMPLO_USO.md
2. Execute: ./deploy.sh
3. Teste o app no dispositivo
```

### Desenvolvendo
```bash
1. Modifique código Kotlin/XML
2. Execute: ./build.sh
3. Execute: ./install.sh
```

### Referência Rápida
```bash
1. Abra: CHEATSHEET.md
2. Copie comando necessário
```

---

## 🎯 Casos de Uso

### "Quero apenas testar o app"
1. [GUIA_RAPIDO.md](GUIA_RAPIDO.md)
2. `./deploy.sh`

### "Nunca compilei app Android antes"
1. [EXEMPLO_USO.md](EXEMPLO_USO.md)
2. Siga passo a passo

### "Esqueci um comando"
1. [CHEATSHEET.md](CHEATSHEET.md)
2. Procure o comando

### "Quero entender o código"
1. [README.md](README.md)
2. Veja seção "Estrutura do Projeto"

### "Preciso usar Android Studio"
1. [INSTRUCOES.md](INSTRUCOES.md)
2. Seção "Método Tradicional"

### "Deu erro na compilação"
1. [GUIA_RAPIDO.md](GUIA_RAPIDO.md)
2. Seção "Solução de Problemas"

---

## 📊 Resumo dos Arquivos

| Arquivo | Propósito | Quando Usar |
|---------|-----------|-------------|
| **GUIA_RAPIDO.md** | Compilar sem Android Studio | Uso diário ⭐ |
| **EXEMPLO_USO.md** | Tutorial passo a passo | Primeira vez |
| **CHEATSHEET.md** | Referência rápida | Consulta |
| **README.md** | Documentação completa | Entender projeto |
| **INSTRUCOES.md** | Instruções detalhadas | Método tradicional |
| **deploy.sh** | Compilar + Instalar | Desenvolvimento ⭐ |
| **build.sh** | Apenas compilar | Build rápido |
| **install.sh** | Apenas instalar | APK já compilado |
| **setup.sh** | Configurar ambiente | Uma única vez |

---

## 💡 Dicas

### Para Iniciantes
1. Comece com **EXEMPLO_USO.md**
2. Use **deploy.sh** para tudo
3. Consulte **CHEATSHEET.md** quando precisar

### Para Desenvolvedores
1. Use **build.sh** durante desenvolvimento
2. Use **install.sh** para testar
3. Mantenha **CHEATSHEET.md** aberto

### Para Documentação
1. **README.md** - visão geral
2. **GUIA_RAPIDO.md** - instruções práticas
3. **EXEMPLO_USO.md** - tutorial detalhado

---

## 🔗 Links Externos Úteis

- [Documentação Android](https://developer.android.com/docs)
- [Kotlin Language](https://kotlinlang.org/docs/home.html)
- [Gradle Build Tool](https://gradle.org/guides/)
- [ADB Commands](https://developer.android.com/studio/command-line/adb)

---

**Última atualização:** Novembro 2025

**Dúvidas?** Consulte os arquivos de documentação listados acima! 📚
