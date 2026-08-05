# Instruções de Compilação e Instalação

## 🚀 MÉTODO RÁPIDO (Recomendado)

**Compilar APK sem Android Studio - Apenas 3 comandos!**

Veja o arquivo **[GUIA_RAPIDO.md](GUIA_RAPIDO.md)** para instruções simplificadas.

```bash
cd /home/ruandias/projects/ListManager
./setup.sh    # Setup inicial (só uma vez)
./build.sh    # Compilar APK
./install.sh  # Instalar no dispositivo (opcional)
```

O APK ficará em: `app/build/outputs/apk/debug/app-debug.apk`

---

## Método Tradicional (Android Studio)

Se você preferir usar o Android Studio:

### Pré-requisitos

1. **Android Studio** instalado (versão Arctic Fox ou superior)
2. **Java JDK 8** ou superior
3. Um dispositivo Android ou emulador configurado

### Passos para Compilar

### 1. Abrir o Projeto

1. Abra o Android Studio
2. Selecione "Open an Existing Project"
3. Navegue até a pasta `ListManager` e clique em OK

### 2. Sincronizar Gradle

1. O Android Studio automaticamente detectará o projeto Gradle
2. Clique em "Sync Now" quando solicitado
3. Aguarde o download das dependências (pode levar alguns minutos)

### 3. Adicionar Ícones (Opcional)

Para um ícone personalizado, você pode:
- Usar o Image Asset Studio do Android Studio:
  1. Clique com botão direito em `res` → New → Image Asset
  2. Configure seu ícone
  3. Clique em Next e Finish

Ou use os ícones padrão já configurados no projeto.

### 4. Compilar e Executar

**Opção A: Usando Emulador**
1. Clique em "AVD Manager" na barra de ferramentas
2. Crie ou inicie um dispositivo virtual
3. Clique no botão "Run" (ícone de play verde)
4. Selecione o emulador e clique em OK

**Opção B: Usando Dispositivo Físico**
1. Ative o "Modo de Desenvolvedor" no seu Android:
   - Configurações → Sobre o telefone
   - Toque 7 vezes em "Número da versão"
2. Ative "Depuração USB" em Opções do desenvolvedor
3. Conecte o dispositivo via USB
4. Clique no botão "Run"
5. Selecione seu dispositivo

### 5. Gerar APK

Para gerar um APK instalável:

1. Menu: Build → Build Bundle(s) / APK(s) → Build APK(s)
2. Aguarde a compilação
3. Clique em "locate" quando aparecer a notificação
4. O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`

## Solução de Problemas

### Erro de Sincronização Gradle

Se houver erro na sincronização:
- Verifique sua conexão com internet
- File → Invalidate Caches → Invalidate and Restart
- Aguarde nova sincronização

### Erro de Compilação

- Certifique-se que o Android SDK está instalado
- Verifique se o SDK 33 está instalado no SDK Manager
- Tools → SDK Manager → Instale Android 13.0 (Tiramisu)

### Ícone não aparece

- Use os ícones adaptativos já configurados
- Ou adicione PNGs manualmente nas pastas mipmap

## Usando o Aplicativo

### Primeira Execução

1. O app abre na tela principal (vazia)
2. Toque no botão flutuante (**+**) e depois em "Nova lista" para criar sua primeira lista
3. Digite um nome (ex: "Compras", "Tarefas", etc.)
4. Clique em "Criar"

### Adicionando Itens

1. Clique na lista que você criou e escolha uma sublista (Shibata ou Nagumo)
2. Toque no botão flutuante "Adicionar item"
3. Digite:
   - **Nome**: nome do item (ex: "Arroz")
   - **Quantidade**: número com ou sem decimais (ex: "5" ou "5.5")
4. Clique em "Adicionar"
5. O item aparecerá na lista

### Visualizando Dados

- **Tela Principal**: mostra todas as suas listas
- **Tela de Itens**: mostra todos os itens de uma lista específica
- Cada item mostra nome e quantidade

### Onde os Dados São Salvos

Os dados ficam em arquivos de texto em:
```
/data/data/com.example.listmanager/files/listas/
```

**Nota**: Você precisa de root para acessar essa pasta diretamente. Os dados são privados ao aplicativo.

## Estrutura do Código

```
ListManager/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/listmanager/
│   │       │   ├── MainActivity.kt           # Tela principal
│   │       │   ├── ItensActivity.kt          # Tela de itens
│   │       │   ├── Item.kt                   # Modelo de dados
│   │       │   ├── Lista.kt                  # Modelo de lista
│   │       │   ├── StorageManager.kt         # Gerencia arquivos
│   │       │   ├── ListasAdapter.kt          # Adapter listas
│   │       │   └── ItensAdapter.kt           # Adapter itens
│   │       ├── res/
│   │       │   ├── layout/                   # Layouts XML
│   │       │   ├── values/                   # Strings, cores, temas
│   │       │   └── drawable/                 # Ícones
│   │       └── AndroidManifest.xml
│   └── build.gradle                          # Configuração do app
├── build.gradle                              # Configuração do projeto
└── settings.gradle
```

## Dicas

- **Backup**: Os dados são locais. Se desinstalar, perde tudo
- **Nomes únicos**: Evite criar listas com mesmo nome
- **Formato de número**: Use ponto (.) para decimais, não vírgula
- **Performance**: O app carrega tudo na memória, ideal para até algumas centenas de itens

## Próximos Passos (Melhorias Possíveis)

- [ ] Adicionar função de deletar listas e itens
- [ ] Implementar edição de itens existentes
- [ ] Adicionar busca/filtro
- [ ] Exportar listas para compartilhar
- [ ] Adicionar total de quantidade por lista
- [ ] Tema escuro
- [ ] Ordenação de itens
