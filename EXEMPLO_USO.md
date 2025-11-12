# 📱 Exemplo de Uso Completo

Este arquivo mostra **exatamente** o que você verá ao executar os comandos.

---

## 🔧 Passo 1: Setup Inicial

```bash
cd /home/ruandias/projects/ListManager
./setup.sh
```

**Saída esperada:**
```
======================================
  Setup - Gerenciador de Listas
======================================

📦 Baixando Gradle Wrapper...
✅ Gradle Wrapper baixado com sucesso!

======================================
✅ Setup concluído!
======================================

Próximos passos:
  1. Compile o APK: ./build.sh
  2. Instale no dispositivo: ./install.sh
```

⏱️ **Tempo:** ~10-30 segundos (depende da velocidade da internet)

---

## 🔨 Passo 2: Compilar APK

```bash
./build.sh
```

**Saída esperada:**
```
======================================
  Gerenciador de Listas - Build APK
======================================

🔨 Compilando APK...

> Task :app:preBuild
> Task :app:preDebugBuild
> Task :app:compileDebugKotlin
> Task :app:processDebugResources
> Task :app:compileDebugJavaWithJavac
> Task :app:dexBuilderDebug
> Task :app:mergeDebugDexes
> Task :app:packageDebug
> Task :app:assembleDebug

BUILD SUCCESSFUL in 1m 23s
45 actionable tasks: 45 executed

======================================
✅ APK compilado com sucesso!
======================================

📱 Localização do APK:
   app/build/outputs/apk/debug/app-debug.apk

📦 Tamanho:
   3.2M

🚀 Para instalar no dispositivo:
   1. Conecte o dispositivo via USB
   2. Ative 'Depuração USB' nas opções de desenvolvedor
   3. Execute: ./install.sh

   Ou copie o APK para o dispositivo e instale manualmente
```

⏱️ **Tempo:**
- Primeira compilação: 1-3 minutos
- Compilações seguintes: 10-30 segundos

📦 **Tamanho do APK:** ~3-5 MB

---

## 📲 Passo 3: Instalar no Dispositivo

### Preparar o Dispositivo Android

**No seu dispositivo Android:**

1. **Ativar Opções do Desenvolvedor:**
   - Abra Configurações
   - Vá em "Sobre o telefone" ou "Sobre o dispositivo"
   - Toque 7 vezes em "Número da versão" ou "Versão do build"
   - Você verá: "Agora você é um desenvolvedor!"

2. **Ativar Depuração USB:**
   - Volte para Configurações
   - Entre em "Sistema" → "Opções do desenvolvedor"
   - Ative "Depuração USB"
   - Ative "Instalar via USB" (se disponível)

3. **Conectar ao Computador:**
   - Conecte o cabo USB
   - No dispositivo, autorize o computador quando aparecer o popup

### Instalar o APK

```bash
./install.sh
```

**Saída esperada:**
```
======================================
  Instalando APK no Dispositivo
======================================

📱 Dispositivo(s) conectado(s):
List of devices attached
ABC123XYZ	device

📦 Instalando APK...
Performing Streamed Install
Success

======================================
✅ APK instalado com sucesso!
======================================

O app 'Gerenciador de Listas' está agora no seu dispositivo
```

⏱️ **Tempo:** ~5-10 segundos

---

## 🎯 Testando o App

### 1. Abrir o App

No seu dispositivo Android:
- Abra a gaveta de apps
- Procure por "Gerenciador de Listas"
- Toque para abrir

### 2. Criar Primeira Lista

Você verá a tela principal vazia.

**Passos:**
1. Toque no botão "**+ Nova Lista**"
2. Digite um nome (exemplo: "Compras do Mês")
3. Toque em "**Criar**"

A lista aparecerá na tela!

### 3. Adicionar Itens

**Passos:**
1. Toque na lista "Compras do Mês"
2. Toque no botão "**+ Adicionar Item**"
3. Preencha:
   - **Nome:** Arroz
   - **Quantidade:** 5
4. Toque em "**Adicionar**"

O item aparecerá mostrando:
```
Arroz
Quantidade: 5.0
```

**Adicione mais itens:**
- Feijão - 3.5
- Açúcar - 2.0
- Café - 1.5

### 4. Criar Mais Listas

Volte para a tela principal (botão voltar) e crie outras listas:
- "Lista de Tarefas"
- "Materiais de Construção"
- "Inventário"

---

## 🔄 Recompilar Após Mudanças

Se você modificar o código Kotlin ou XML:

```bash
./build.sh && ./install.sh
```

Isso irá:
1. Recompilar o APK
2. Reinstalar no dispositivo automaticamente

---

## 💾 Onde Ficam os Dados

Os dados são salvos em arquivos de texto:

**Localização:** `/data/data/com.example.listmanager/files/listas/`

**Exemplo de arquivo:** `Compras do Mês.txt`
```
Arroz;5.0
Feijão;3.5
Açúcar;2.0
Café;1.5
```

Cada linha = 1 item no formato: `nome;quantidade`

---

## 🎨 O que você vai ver no App

### Tela Principal
```
┌─────────────────────────────────┐
│  Minhas Listas                  │
│                                 │
│  [+ Nova Lista]                 │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Compras do Mês            │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Lista de Tarefas          │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Materiais de Construção   │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

### Tela de Itens (exemplo: Compras do Mês)
```
┌─────────────────────────────────┐
│  ← Compras do Mês               │
│                                 │
│  [+ Adicionar Item]             │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Arroz                     │  │
│  │ Quantidade: 5.0           │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Feijão                    │  │
│  │ Quantidade: 3.5           │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Açúcar                    │  │
│  │ Quantidade: 2.0           │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

---

## 📱 Instalação Manual (sem cabo USB)

Se você não quer usar cabo USB:

### 1. Copiar APK para o Dispositivo

**Opções:**

**A) Via Email:**
```bash
# Anexe o APK em um email para você mesmo
# Localização: app/build/outputs/apk/debug/app-debug.apk
```

**B) Via Google Drive/Dropbox:**
- Faça upload do APK
- Baixe no dispositivo

**C) Via Servidor Web Local:**
```bash
cd app/build/outputs/apk/debug
python3 -m http.server 8000
# No dispositivo, acesse: http://[IP_DO_PC]:8000
```

### 2. Instalar no Dispositivo

1. Abra o gerenciador de arquivos
2. Encontre o arquivo `app-debug.apk`
3. Toque no arquivo
4. Se aparecer aviso "Fontes desconhecidas":
   - Vá em Configurações
   - Segurança
   - Ative "Fontes desconhecidas" ou "Instalar apps desconhecidos"
5. Toque em "Instalar"
6. Pronto!

---

## 🆘 Problemas Comuns

### "Java não encontrado"

**Solução:**
```bash
# Instale Java
sudo apt install openjdk-11-jdk

# Verifique
java -version
```

### "Permission denied: ./build.sh"

**Solução:**
```bash
chmod +x setup.sh build.sh install.sh gradlew
```

### "No devices connected"

**Soluções:**
1. Reconecte o cabo USB
2. Tente outra porta USB
3. No dispositivo, desative e ative "Depuração USB"
4. Autorize o computador no popup do dispositivo

### Compilação falha

**Solução:**
```bash
# Limpe tudo e recompile
./gradlew clean
./build.sh
```

---

## 🎉 Pronto!

Agora você tem:
- ✅ APK compilado
- ✅ App instalado no dispositivo
- ✅ Listas funcionando
- ✅ Dados salvos automaticamente

**Aproveite seu Gerenciador de Listas!** 📝
