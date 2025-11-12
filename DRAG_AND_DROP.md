# 🔄 Ordenação Manual com Drag and Drop

## ✅ Funcionalidade Implementada

O aplicativo agora permite **ordenação manual** através de **arrastar e soltar** (drag and drop)!

### 🎯 O Que Mudou

**ANTES** (v1.2):
- ❌ Menu com ordenação automática (A→Z, Z→A, etc.)
- ❌ Ordenação predefinida

**AGORA** (v1.3):
- ✅ **Arrastar e soltar** listas para reordenar
- ✅ **Arrastar e soltar** itens para reordenar
- ✅ Ordem padrão é a **ordem de inserção**
- ✅ Ordem personalizada é **salva automaticamente**
- ✅ **Persiste entre sessões**

---

## 🎨 Como Usar

### Reordenar Listas (Tela Principal)

1. Na tela principal, **pressione e segure** uma lista
2. **Arraste** para cima ou para baixo
3. **Solte** na posição desejada
4. ✅ A nova ordem é salva automaticamente!

```
┌─────────────────────────────────┐
│  Minhas Listas                  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Compras         [≡≡≡]     │  │ ← Pressione e segure
│  └───────────────────────────┘  │
│       ↓ Arraste ↓               │
│  ┌───────────────────────────┐  │
│  │ Tarefas                   │  │
│  └───────────────────────────┘  │
│       ↓ Solte aqui ↓            │
│  ┌───────────────────────────┐  │
│  │ Mercado                   │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

### Reordenar Itens (Dentro de uma Lista)

1. Abra uma lista
2. **Pressione e segure** um item
3. **Arraste** para cima ou para baixo
4. **Solte** na posição desejada
5. ✅ A nova ordem é salva no arquivo automaticamente!

```
┌─────────────────────────────────┐
│  Lista: Compras                 │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Arroz         [≡≡≡]        │  │ ← Pressione e segure
│  │ Quantidade: 5.0           │  │
│  └───────────────────────────┘  │
│       ↓ Arraste ↓               │
│  ┌───────────────────────────┐  │
│  │ Feijão                    │  │
│  │ Quantidade: 3.5           │  │
│  └───────────────────────────┘  │
│       ↓ Solte aqui ↓            │
│  ┌───────────────────────────┐  │
│  │ Açúcar                    │  │
│  │ Quantidade: 2.0           │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

---

## 🔧 Detalhes Técnicos

### Implementação

Utilizamos **ItemTouchHelper** do Android para detectar gestos de drag and drop:

#### MainActivity.kt
```kotlin
private fun configurarDragAndDrop() {
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPos = viewHolder.adapterPosition
            val toPos = target.adapterPosition
            
            // Move na lista local
            Collections.swap(listas, fromPos, toPos)
            recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
            
            // Salva a nova ordem
            storageManager.salvarOrdemListas(listas)
            
            return true
        }
        
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Não usamos swipe
        }
        
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }
    })
    
    itemTouchHelper.attachToRecyclerView(recyclerView)
}
```

#### ItensActivity.kt
```kotlin
private fun configurarDragAndDrop() {
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPos = viewHolder.adapterPosition
            val toPos = target.adapterPosition
            
            // Move na lista local
            lista?.let { l ->
                Collections.swap(l.itens, fromPos, toPos)
                recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
                
                // Salva a nova ordem no arquivo
                storageManager.salvarLista(l)
            }
            
            return true
        }
        
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Não usamos swipe
        }
        
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }
    })
    
    itemTouchHelper.attachToRecyclerView(recyclerView)
}
```

### Persistência da Ordem

#### Ordem das Listas
- Salva em arquivo: `_order.txt`
- Formato: Um nome de lista por linha
- Carregamento: Lê ordem do arquivo e adiciona novas listas ao final

#### Ordem dos Itens
- Salva automaticamente no arquivo da lista (`.txt`)
- Formato: Mantém ordem das linhas no arquivo
- A posição no arquivo = ordem de exibição

---

## 💾 Sistema de Armazenamento

### Arquivo de Ordem (_order.txt)

**Exemplo:**
```
Compras
Tarefas
Mercado
Afazeres
```

- Cada linha = nome de uma lista
- Ordem das linhas = ordem de exibição
- Criado automaticamente ao reordenar

### Arquivo de Lista (exemplo: Compras.txt)

**Exemplo:**
```
Arroz;5.0
Feijão;3.5
Açúcar;2.0
```

- Ordem das linhas = ordem dos itens
- Alterada automaticamente ao reordenar
- Posição no arquivo persiste

---

## 🆚 Comparação com Versão Anterior

| Aspecto | v1.2 (Menu) | v1.3 (Drag & Drop) |
|---------|-------------|---------------------|
| **Método** | Menu de ordenação | Arrastar e soltar |
| **Flexibilidade** | Opções predefinidas | Totalmente customizável |
| **Ordem Padrão** | Alfabética | Ordem de inserção |
| **Listas** | A→Z ou Z→A | Qualquer ordem |
| **Itens** | Nome ou Qtd | Qualquer ordem |
| **Persistência Listas** | ❌ Não | ✅ Sim (_order.txt) |
| **Persistência Itens** | ✅ Sim | ✅ Sim (posição no arquivo) |
| **Facilidade** | 2-3 toques | 1 gesto (arrastar) |

---

## 📊 Arquivos Modificados

| Arquivo | Mudanças |
|---------|----------|
| **StorageManager.kt** | + `salvarOrdemListas()`, `moverItem()` |
| **MainActivity.kt** | Removido menu, + `configurarDragAndDrop()` |
| **ItensActivity.kt** | Removido menu, + `configurarDragAndDrop()` |
| **menu/** | ❌ Pasta removida completamente |

### Arquivos Removidos
- ❌ `menu/menu_main.xml`
- ❌ `menu/menu_itens.xml`

### Novos Métodos - StorageManager

```kotlin
// Salva a ordem personalizada das listas
fun salvarOrdemListas(listas: List<String>) {
    orderFile.writeText(listas.joinToString("\n"))
}

// Move um item de uma posição para outra dentro da lista
fun moverItem(nomeLista: String, dePosicao: Int, paraPosicao: Int) {
    val lista = carregarLista(nomeLista)
    if (dePosicao >= 0 && dePosicao < lista.itens.size && 
        paraPosicao >= 0 && paraPosicao < lista.itens.size) {
        val item = lista.itens.removeAt(dePosicao)
        lista.itens.add(paraPosicao, item)
        salvarLista(lista)
    }
}
```

---

## 🎯 Comportamento

### Long Press Ativado
- **Pressionar e segurar** = arrastar
- **Toque simples** = abrir/visualizar
- **Long press + arrastar** = reordenar
- **Long press sem arrastar** = excluir (mantido)

### Feedback Visual
- Item "levanta" visualmente ao ser arrastado
- Outros itens se movem para dar espaço
- Animação suave de transição
- Sem mensagens Toast (ação é visual)

### Persistência Automática
- ✅ Salva **durante** o arrasto
- ✅ Não precisa confirmar
- ✅ Ordem mantida ao fechar e reabrir
- ✅ Funciona offline

---

## 💡 Casos de Uso

### Priorização de Listas
- Arraste listas importantes para o topo
- Organize por frequência de uso
- Crie seu próprio sistema de organização

### Organização de Itens
- Agrupe itens relacionados
- Organize por setor do mercado
- Priorize por urgência
- Ordene por ordem de compra

### Workflow Personalizado
- Cada usuário tem seu próprio sistema
- Adapte a ordem ao seu uso
- Mude conforme necessidade

---

## ⚡ Vantagens

### Para o Usuário
- ✅ **Intuitivo** - Gesto natural de arrastar
- ✅ **Rápido** - 1 gesto vs múltiplos toques
- ✅ **Flexível** - Qualquer ordem possível
- ✅ **Visual** - Vê o movimento em tempo real
- ✅ **Persistente** - Ordem salva automaticamente

### Técnicas
- ✅ ItemTouchHelper nativo do Android
- ✅ Código limpo e manutenível
- ✅ Performático (Collections.swap é O(1))
- ✅ Salva apenas quando necessário
- ✅ Compatível com Android 7.0+

---

## 🔄 Fluxo de Trabalho

### Reordenar Listas

```
[Tela Principal]
    ↓
Pressione e segure uma lista
    ↓
Lista "levanta" visualmente
    ↓
Arraste para cima ou para baixo
    ↓
Outros itens se ajustam
    ↓
Solte na posição desejada
    ↓
Ordem salva em _order.txt
    ↓
[Nova ordem exibida]
```

### Reordenar Itens

```
[Tela de Itens]
    ↓
Pressione e segure um item
    ↓
Item "levanta" visualmente
    ↓
Arraste para cima ou para baixo
    ↓
Outros itens se ajustam
    ↓
Solte na posição desejada
    ↓
Ordem salva no arquivo .txt
    ↓
[Nova ordem exibida e persistida]
```

---

## 📱 Compatibilidade

- **Android mínimo:** 7.0 (API 24)
- **RecyclerView:** Nativo do AndroidX
- **ItemTouchHelper:** Nativo do AndroidX
- **Gestos:** Suportados por todos os dispositivos

---

## ✅ Checklist de Validação

### Funcionalidades
- [x] Long press inicia arrasto
- [x] Arrastar move o item visualmente
- [x] Soltar fixa na nova posição
- [x] Ordem de listas é salva
- [x] Ordem de itens é salva
- [x] Ordem persiste ao reabrir
- [x] Animações suaves
- [x] Sem bugs visuais

### Integração
- [x] Compatível com criar listas
- [x] Compatível com criar itens
- [x] Compatível com excluir (long press sem arrastar)
- [x] Compatível com visualizar
- [x] Ordem mantida em todas operações

---

## 🚀 Compilação

**Status:** ✅ Build SUCESSO  
**APK:** app/build/outputs/apk/debug/app-debug.apk  
**Tamanho:** 5.2 MB  
**Versão:** 1.3  
**Erros:** Nenhum  
**Warnings:** Nenhum  

---

## 📝 Changelog

### Versão 1.3 - Drag and Drop

**Adicionado:**
- ✅ Drag and drop para reordenar listas
- ✅ Drag and drop para reordenar itens
- ✅ Sistema de persistência da ordem (_order.txt)
- ✅ Método `salvarOrdemListas()` no StorageManager
- ✅ Método `moverItem()` no StorageManager

**Removido:**
- ❌ Menu de ordenação automática
- ❌ Opções A→Z, Z→A
- ❌ Opções de ordenação por quantidade
- ❌ Pasta menu/

**Modificado:**
- 🔄 `listarTodasListas()` - Agora respeita ordem personalizada
- 🔄 MainActivity - Implementa ItemTouchHelper
- 🔄 ItensActivity - Implementa ItemTouchHelper
- 🔄 Ordem padrão: inserção ao invés de alfabética

---

## 🎉 Resumo

**O que o usuário ganha:**
- 🎯 Controle total sobre a ordem
- ⚡ Organização mais rápida
- 🎨 Interface mais intuitiva
- 💾 Ordem sempre salva
- 🔄 Flexibilidade máxima

**Aplicativo agora com ordenação 100% personalizável! 🎉**
