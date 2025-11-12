# 📋 Cheatsheet - Comandos Rápidos

## 🚀 Compilar e Instalar

```bash
# Tudo de uma vez (RECOMENDADO)
./deploy.sh

# Ou passo a passo
./setup.sh      # Só na primeira vez
./build.sh      # Compilar APK
./install.sh    # Instalar no dispositivo
```

## 📦 Localização do APK

```
app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 Comandos Úteis

```bash
# Compilar e instalar em sequência
./build.sh && ./install.sh

# Limpar build anterior
./gradlew clean

# Compilar APK release (otimizado)
./gradlew assembleRelease

# Ver tamanho do APK
ls -lh app/build/outputs/apk/debug/app-debug.apk

# Listar dispositivos conectados
adb devices

# Desinstalar app do dispositivo
adb uninstall com.example.listmanager

# Ver logs do app
adb logcat | grep "ListManager"

# Enviar APK para dispositivo (sem instalar)
adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/

# Instalar APK manualmente
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Abrir app no dispositivo
adb shell am start -n com.example.listmanager/.MainActivity
```

## 🛠️ Gradle Direto

```bash
# Compilar debug
./gradlew assembleDebug

# Compilar release
./gradlew assembleRelease

# Limpar
./gradlew clean

# Ver tasks disponíveis
./gradlew tasks

# Build offline (mais rápido)
./gradlew assembleDebug --offline

# Com daemon (fica em background)
./gradlew assembleDebug --daemon
```

## 📱 No Dispositivo

### Ativar Depuração USB

1. Configurações → Sobre o telefone
2. Toque 7x em "Número da versão"
3. Configurações → Sistema → Opções do desenvolvedor
4. Ative "Depuração USB"

### Permitir Fontes Desconhecidas

1. Configurações → Segurança
2. Ative "Fontes desconhecidas"

## 🐛 Solução Rápida de Problemas

```bash
# Java não encontrado
sudo apt install openjdk-11-jdk

# Permissão negada
chmod +x *.sh gradlew

# Erro de compilação
./gradlew clean && ./build.sh

# Dispositivo não encontrado
adb kill-server && adb start-server
adb devices
```

## 📂 Estrutura de Pastas

```
app/src/main/
├── java/com/example/listmanager/  # Código Kotlin
├── res/layout/                    # Layouts XML
├── res/values/                    # Strings, cores, temas
└── AndroidManifest.xml           # Manifesto

app/build/outputs/apk/debug/      # APK compilado aqui
```

## 📄 Arquivos de Dados (no dispositivo)

```
/data/data/com.example.listmanager/files/listas/
├── Compras.txt
├── Tarefas.txt
└── ...
```

Formato: `nome;quantidade` (uma linha por item)

## 🔗 Links Rápidos

- **GUIA_RAPIDO.md** - Tutorial completo
- **EXEMPLO_USO.md** - Exemplo visual passo a passo
- **README.md** - Documentação do projeto
- **INSTRUCOES.md** - Instruções com Android Studio

---

**Dica:** Salve este arquivo nos favoritos para acesso rápido! ⭐
