#!/bin/bash

# Script simplificado para compilar APK
# Uso: ./build.sh

echo "======================================"
echo "  Gerenciador de Listas - Build APK"
echo "======================================"
echo ""

# Configura ANDROID_HOME
export ANDROID_HOME="$HOME/Android/Sdk"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

# Verifica se gradlew existe
if [ ! -f "gradlew" ]; then
    echo "❌ Erro: gradlew não encontrado!"
    echo "Execute primeiro: ./setup.sh"
    exit 1
fi

# Verifica se Android SDK está instalado
if [ ! -f "local.properties" ]; then
    echo "❌ Android SDK não configurado!"
    echo "Execute primeiro: ./install-android-sdk.sh"
    exit 1
fi

# Torna gradlew executável
chmod +x gradlew

echo "🔨 Compilando APK..."
echo ""

# Limpa build anterior
./gradlew clean

# Compila APK de debug
./gradlew assembleDebug

# Verifica se foi criado
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

if [ -f "$APK_PATH" ]; then
    echo ""
    echo "======================================"
    echo "✅ APK compilado com sucesso!"
    echo "======================================"
    echo ""
    echo "📱 Localização do APK:"
    echo "   $APK_PATH"
    echo ""
    echo "📦 Tamanho:"
    ls -lh "$APK_PATH" | awk '{print "   " $5}'
    echo ""
    echo "🚀 Para instalar no dispositivo:"
    echo "   1. Conecte o dispositivo via USB"
    echo "   2. Ative 'Depuração USB' nas opções de desenvolvedor"
    echo "   3. Execute: ./install.sh"
    echo ""
    echo "   Ou copie o APK para o dispositivo e instale manualmente"
    echo ""
else
    echo ""
    echo "❌ Erro ao compilar APK!"
    echo "Verifique os erros acima"
    exit 1
fi
