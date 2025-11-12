#!/bin/bash

# Script para instalar Android SDK Command Line Tools
# Uso: ./install-android-sdk.sh

echo "=========================================="
echo "  Instalando Android SDK"
echo "=========================================="
echo ""

# Define diretório de instalação
SDK_DIR="$HOME/Android/Sdk"
CMDLINE_TOOLS_VERSION="11076708"
CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip"

# Verifica se já está instalado
if [ -d "$SDK_DIR" ] && [ -f "$SDK_DIR/platform-tools/adb" ]; then
    echo "✅ Android SDK já instalado em: $SDK_DIR"
    echo ""
    echo "Configurando projeto..."
    echo "sdk.dir=$SDK_DIR" > local.properties
    echo "✅ Arquivo local.properties criado!"
    exit 0
fi

echo "📦 Instalando Android SDK em: $SDK_DIR"
echo ""

# Verifica dependências
if ! command -v unzip &> /dev/null; then
    echo "⚠️  'unzip' não encontrado. Instalando..."
    sudo apt-get update && sudo apt-get install -y unzip
fi

if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado!"
    echo ""
    echo "Instale Java primeiro:"
    echo "  sudo apt install openjdk-11-jdk"
    exit 1
fi

# Cria diretórios
mkdir -p "$SDK_DIR/cmdline-tools"
cd "$SDK_DIR/cmdline-tools" || exit 1

# Baixa command line tools
echo "📥 Baixando Android Command Line Tools..."
if command -v wget &> /dev/null; then
    wget -q --show-progress "$CMDLINE_TOOLS_URL" -O cmdline-tools.zip
elif command -v curl &> /dev/null; then
    curl -# -L "$CMDLINE_TOOLS_URL" -o cmdline-tools.zip
else
    echo "❌ wget ou curl não encontrado!"
    exit 1
fi

if [ $? -ne 0 ]; then
    echo "❌ Erro ao baixar command line tools"
    exit 1
fi

# Extrai
echo ""
echo "📦 Extraindo..."
unzip -q cmdline-tools.zip
rm cmdline-tools.zip

# Reorganiza estrutura de diretórios
if [ -d "cmdline-tools" ]; then
    mv cmdline-tools latest
fi

# Aceita licenças e instala componentes necessários
echo ""
echo "📦 Instalando componentes do Android SDK..."
echo "   (Isso pode levar alguns minutos)"
echo ""

export ANDROID_HOME="$SDK_DIR"
export PATH="$PATH:$SDK_DIR/cmdline-tools/latest/bin"

# Aceita todas as licenças automaticamente
yes | sdkmanager --licenses > /dev/null 2>&1

# Instala componentes necessários
sdkmanager --install "platform-tools" "platforms;android-33" "build-tools;33.0.0" "cmdline-tools;latest"

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ Android SDK instalado com sucesso!"
    echo "=========================================="
    echo ""
    echo "📁 Localização: $SDK_DIR"
    echo ""
    echo "Componentes instalados:"
    echo "  ✓ Platform Tools (adb, fastboot)"
    echo "  ✓ Android 13 (API 33)"
    echo "  ✓ Build Tools 33.0.0"
    echo ""
    
    # Cria local.properties
    cd "$OLDPWD" || exit 1
    echo "sdk.dir=$SDK_DIR" > local.properties
    echo "✅ Arquivo local.properties criado!"
    echo ""
    
    # Adiciona ao PATH permanentemente
    if ! grep -q "ANDROID_HOME" "$HOME/.bashrc"; then
        echo "" >> "$HOME/.bashrc"
        echo "# Android SDK" >> "$HOME/.bashrc"
        echo "export ANDROID_HOME=\"$SDK_DIR\"" >> "$HOME/.bashrc"
        echo "export PATH=\"\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools\"" >> "$HOME/.bashrc"
        
        echo "📝 Variáveis de ambiente adicionadas ao ~/.bashrc"
        echo ""
        echo "⚠️  Para usar em novas sessões, execute:"
        echo "   source ~/.bashrc"
    fi
    
    echo ""
    echo "=========================================="
    echo "Próximos passos:"
    echo "  1. Execute: source ~/.bashrc"
    echo "  2. Execute: ./build.sh"
    echo "=========================================="
    
else
    echo "❌ Erro ao instalar componentes do Android SDK"
    exit 1
fi
