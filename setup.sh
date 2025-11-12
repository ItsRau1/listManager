#!/bin/bash

# Script de setup inicial do projeto
# Uso: ./setup.sh

echo "======================================"
echo "  Setup - Gerenciador de Listas"
echo "======================================"
echo ""

# Baixa o Gradle Wrapper JAR se não existir
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ] || [ ! -s "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "📦 Baixando Gradle Wrapper..."
    
    mkdir -p gradle/wrapper
    
    WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v7.5.0/gradle/wrapper/gradle-wrapper.jar"
    
    if command -v curl &> /dev/null; then
        curl -L -o gradle/wrapper/gradle-wrapper.jar "$WRAPPER_URL"
    elif command -v wget &> /dev/null; then
        wget -O gradle/wrapper/gradle-wrapper.jar "$WRAPPER_URL"
    else
        echo "❌ Erro: curl ou wget não encontrado!"
        echo "Instale um deles para baixar o Gradle Wrapper"
        exit 1
    fi
    
    if [ $? -eq 0 ] && [ -s "gradle/wrapper/gradle-wrapper.jar" ]; then
        echo "✅ Gradle Wrapper baixado com sucesso!"
    else
        echo "❌ Erro ao baixar Gradle Wrapper"
        echo ""
        echo "Solução alternativa:"
        echo "1. Abra o projeto no Android Studio"
        echo "2. O Android Studio irá baixar automaticamente"
        exit 1
    fi
else
    echo "✅ Gradle Wrapper já existe"
fi

# Torna gradlew executável
chmod +x gradlew
chmod +x build.sh
chmod +x install.sh

echo ""
echo "======================================"
echo "✅ Setup concluído!"
echo "======================================"
echo ""
echo "Próximos passos:"
echo "  1. Compile o APK: ./build.sh"
echo "  2. Instale no dispositivo: ./install.sh"
echo ""
