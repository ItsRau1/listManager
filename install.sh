#!/bin/bash

# Script para instalar APK no dispositivo conectado
# Uso: ./install.sh

echo "======================================"
echo "  Instalando APK no Dispositivo"
echo "======================================"
echo ""

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

# Verifica se APK existe
if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK não encontrado!"
    echo "Execute primeiro: ./build.sh"
    exit 1
fi

# Verifica se adb está instalado
if ! command -v adb &> /dev/null; then
    echo "❌ ADB não encontrado!"
    echo ""
    echo "Instale o Android Debug Bridge (ADB):"
    echo "  Ubuntu/Debian: sudo apt install adb"
    echo "  Fedora: sudo dnf install android-tools"
    echo "  Arch: sudo pacman -S android-tools"
    echo ""
    exit 1
fi

# Verifica se há dispositivos conectados
DEVICES=$(adb devices | grep -w "device" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo "❌ Nenhum dispositivo Android conectado!"
    echo ""
    echo "Conecte seu dispositivo via USB e:"
    echo "  1. Ative 'Opções do desenvolvedor'"
    echo "  2. Ative 'Depuração USB'"
    echo "  3. Autorize o computador no dispositivo"
    echo ""
    exit 1
fi

echo "📱 Dispositivo(s) conectado(s):"
adb devices
echo ""

echo "📦 Instalando APK..."
adb install -r "$APK_PATH"

if [ $? -eq 0 ]; then
    echo ""
    echo "======================================"
    echo "✅ APK instalado com sucesso!"
    echo "======================================"
    echo ""
    echo "O app 'Gerenciador de Listas' está agora no seu dispositivo"
else
    echo ""
    echo "❌ Erro ao instalar APK!"
    echo ""
    echo "Possíveis soluções:"
    echo "  - Verifique se autorizou o computador no dispositivo"
    echo "  - Tente desinstalar a versão anterior primeiro"
    echo "  - Execute: adb uninstall com.example.listmanager"
fi
