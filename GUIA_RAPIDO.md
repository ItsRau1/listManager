# 🚀 Guia Rápido - Compilar APK

Forma **SUPER SIMPLES** de gerar APK sem Android Studio!

## ⚡ Método SUPER Rápido (1 comando)

```bash
cd /home/ruandias/projects/ListManager
./deploy.sh
```

Esse comando faz **tudo automaticamente**: setup + compilar + perguntar se quer instalar!

---

## ⚡ Método Passo a Passo (3 comandos)

```bash
cd /home/ruandias/projects/ListManager

# 1. Setup inicial (só precisa fazer uma vez)
./setup.sh

# 2. Compilar APK
./build.sh

# 3. Instalar no dispositivo (opcional)
./install.sh
```

Pronto! O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📋 Passo a Passo Detalhado

### Pré-requisitos

Você precisa ter instalado:
- **Java JDK 11+** (verifique com: `java -version`)
- **Android SDK** (instalado automaticamente pelo script)
- **ADB** (vem com Android SDK, para instalar via USB)

#### Instalar Java (se necessário)

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk

# Fedora
sudo dnf install java-11-openjdk-devel

# Arch
sudo pacman -S jdk-openjdk
```

#### Instalar ADB (opcional, só para instalar via USB)

```bash
# Ubuntu/Debian
sudo apt install adb

# Fedora
sudo dnf install android-tools

# Arch
sudo pacman -S android-tools
```

### 1️⃣ Setup Inicial

**Execute uma única vez:**

```bash
cd /home/ruandias/projects/ListManager
./setup.sh
```

Isso irá:
- Baixar o Gradle Wrapper
- Configurar permissões

### 2️⃣ Compilar APK

```bash
./build.sh
```

Isso irá:
- Limpar builds anteriores
- Compilar o projeto
- Gerar o APK em `app/build/outputs/apk/debug/app-debug.apk`

**Tempo estimado:** 1-3 minutos na primeira vez, depois mais rápido

### 3️⃣ Instalar no Dispositivo

**Opção A: Instalação Automática via USB**

```bash
./install.sh
```

Antes, certifique-se de:
1. Conectar o dispositivo via USB
2. Ativar "Depuração USB" no dispositivo
3. Autorizar o computador quando o dispositivo perguntar

**Opção B: Instalação Manual**

1. Copie o APK para o dispositivo:
   ```bash
   # Via ADB
   adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/

   # Ou envie por email, Bluetooth, etc.
   ```

2. No dispositivo:
   - Abra o gerenciador de arquivos
   - Navegue até a pasta Downloads ou onde copiou o APK
   - Toque no arquivo APK
   - Permita instalação de fontes desconhecidas (se solicitado)
   - Toque em "Instalar"

---

## 🔧 Comandos Úteis

### Compilar e Instalar (tudo de uma vez)

```bash
./build.sh && ./install.sh
```

### Ver dispositivos conectados

```bash
adb devices
```

### Desinstalar app do dispositivo

```bash
adb uninstall com.example.listmanager
```

### Compilar APK Release (menor e otimizado)

```bash
./gradlew assembleRelease
# APK estará em: app/build/outputs/apk/release/app-release-unsigned.apk
```

### Ver logs do app em tempo real

```bash
adb logcat | grep "ListManager"
```

---

## 🐛 Solução de Problemas

### "Java não encontrado"

```bash
# Verifique se Java está instalado
java -version

# Se não, instale conforme instruções acima
```

### "Gradle download failed"

- Verifique sua conexão com internet
- Tente novamente: `./setup.sh`

### "Erro de compilação"

```bash
# Limpe o projeto e tente novamente
./gradlew clean
./build.sh
```

### "Dispositivo não encontrado"

```bash
# Verifique conexão USB
adb devices

# Se não aparecer:
# 1. Desconecte e reconecte o cabo USB
# 2. Autorize o computador no dispositivo
# 3. Tente outra porta USB
```

### "Instalação negada"

- Ative "Fontes desconhecidas" ou "Instalar apps desconhecidos"
- Configurações → Segurança → Fontes desconhecidas

---

## 📱 Testando o App

Após instalar:

1. Abra o app "Gerenciador de Listas"
2. Toque em "+ Nova Lista"
3. Digite um nome (ex: "Compras")
4. Toque em "Criar"
5. Toque na lista criada
6. Toque em "+ Adicionar Item"
7. Preencha nome e quantidade
8. Veja o item aparecer!

---

## 💡 Dicas

### Compilação Mais Rápida

```bash
# Use o daemon do Gradle (fica em background)
./gradlew assembleDebug --daemon

# Compilação offline (após primeira compilação)
./gradlew assembleDebug --offline
```

### Recompilar apenas quando houver mudanças

```bash
# O Gradle detecta automaticamente mudanças
./gradlew assembleDebug
```

### Ver tamanho do APK

```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### Compartilhar APK

O APK é um arquivo único que pode ser:
- Enviado por email
- Compartilhado via Bluetooth
- Hospedado em servidor web
- Enviado por WhatsApp/Telegram

---

## 🎯 Resumo dos Scripts

| Script | O que faz |
|--------|-----------|
| `deploy.sh` | ⭐ Faz tudo: setup + build + install (RECOMENDADO) |
| `setup.sh` | Setup inicial, baixa dependências |
| `build.sh` | Compila o APK |
| `install.sh` | Instala no dispositivo via USB |
| `gradlew` | Gradle wrapper (usado pelos scripts) |

---

## 📚 Mais Informações

- **README.md** - Documentação completa do projeto
- **INSTRUCOES.md** - Instruções detalhadas (com Android Studio)
- **build.gradle** - Configuração do build

---

**Pronto!** Agora você pode compilar APKs rapidamente sem precisar do Android Studio! 🎉
