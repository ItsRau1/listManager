#!/bin/bash

# Script que executa tudo: setup + build + install
# Uso: ./deploy.sh

echo "=========================================="
echo "  Deploy Completo - Gerenciador de Listas"
echo "=========================================="
echo ""

# 1. Setup (se necessário)
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ] || [ ! -s "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "🔧 Executando setup inicial..."
    ./setup.sh
    if [ $? -ne 0 ]; then
        echo "❌ Erro no setup!"
        exit 1
    fi
    echo ""
else
    echo "✅ Setup já realizado"
    echo ""
fi

# 1.5. Instala Android SDK se necessário
if [ ! -f "local.properties" ]; then
    echo "🔧 Android SDK não encontrado. Instalando..."
    ./install-android-sdk.sh
    if [ $? -ne 0 ]; then
        echo "❌ Erro ao instalar Android SDK!"
        exit 1
    fi
    echo ""
else
    echo "✅ Android SDK já configurado"
    echo ""
fi

# 2. Build
echo "🔨 Compilando APK..."
./build.sh
if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação!"
    exit 1
fi
echo ""

# 3. Install (pergunta antes)
echo "=========================================="
read -p "📲 Deseja instalar no dispositivo agora? (s/N): " -n 1 -r
echo ""
echo "=========================================="

if [[ $REPLY =~ ^[SsYy]$ ]]; then
    echo ""
    ./install.sh
else
    echo ""
    echo "ℹ️  APK compilado em:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Para instalar depois, execute: ./install.sh"
fi

echo ""
echo "=========================================="
echo "✅ Deploy finalizado!"
echo "=========================================="
