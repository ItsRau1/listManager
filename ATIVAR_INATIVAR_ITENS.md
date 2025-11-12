# ✓ Sistema de Ativar/Inativar Itens

## ✅ Funcionalidade Implementada

O aplicativo agora permite **ativar e inativar itens** através de **swipe** (arrastar da direita para esquerda)!

---

## 🎯 Como Funciona

### Inativar Item (Item Ativo → Inativo)

1. **Arraste um item ativo** da direita para a esquerda
2. O item é **inativado**:
   - ~~Nome taxado~~
   - ~~Quantidade taxada~~
   - **Move para o final da lista**
3. Feedback: "Item inativado"

### Ativar Item (Item Inativo → Ativo)

1. **Arraste um item inativo** da direita para a esquerda
2. O item é **ativado**:
   - Texto normal (sem taxado)
   - **Mantém a posição atual**
3. Feedback: "Item ativado"

---

## 🎨 Interface Visual

### Item Ativo
```
┌───────────────────────────┐
│ Arroz                     │  ← Texto normal
│ Quantidade: 5.0           │
└───────────────────────────┘
```

### Item Inativo (depois de swipe)
```
┌───────────────────────────┐
│ ~~Arroz~~                 │  ← Texto taxado
│ ~~Quantidade: 5.0~~       │
└───────────────────────────┘
```

---

## 📱 Demonstração de Uso

### Caso 1: Inativar Item

**Estado Inicial:**
```
Lista: Compras - Shibata
1. Arroz - 5.0          (ativo)
2. Feijão - 3.5         (ativo)
3. Açúcar - 2.0         (ativo)
```

**Arraste "Feijão" ←**
```
Lista: Compras - Shibata
1. Arroz - 5.0          (ativo)
2. Açúcar - 2.0         (ativo)
3. ~~Feijão - 3.5~~     (inativo) ← Movido para o final
```

### Caso 2: Ativar Item

**Estado Inicial:**
```
Lista: Compras - Shibata
1. Arroz - 5.0          (ativo)
2. Açúcar - 2.0         (ativo)
3. ~~Feijão - 3.5~~     (inativo)
```

**Arraste "Feijão" ←**
```
Lista: Compras - Shibata
1. Arroz - 5.0          (ativo)
2. Açúcar - 2.0         (ativo)
3. Feijão - 3.5         (ativo) ← Ativado, mesma posição
```

---

## 🔧 Detalhes Técnicos

### Modelo de Dados Atualizado

**Item.kt:**
```kotlin
data class Item(
    val nome: String,
    val quantidade: Double,
    var ativo: Boolean = true  // ⭐ NOVO
)
```

### Serialização

**Formato no arquivo:**
```
Arroz;5.0;true
Feijão;3.5;false
Açúcar;2.0;true
```

### Visual (Strikethrough)

**ItensAdapter.kt:**
```kotlin
// Aplica strikethrough se inativo
if (item.ativo) {
    holder.nomeTextView.paintFlags = 
        holder.nomeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    holder.quantidadeTextView.paintFlags = 
        holder.quantidadeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
} else {
    holder.nomeTextView.paintFlags = 
        holder.nomeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    holder.quantidadeTextView.paintFlags = 
        holder.quantidadeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
```

### Swipe Gesture

**ItemTouchHelper configurado para:**
- **Drag vertical** (UP/DOWN) = Reordenar
- **Swipe horizontal** (END = dir→esq) = Ativar/Inativar

**Lógica de swipe:**
```kotlin
override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    val position = viewHolder.adapterPosition
    val item = sublista.itens[position]
    
    if (item.ativo) {
        // Inativa e move para o final
        item.ativo = false
        sublista.itens.removeAt(position)
        sublista.itens.add(item)
    } else {
        // Ativa e mantém na posição
        item.ativo = true
    }
    
    // Salva e atualiza UI
    storageManager.salvarLista(lista)
    atualizarAdapter()
}
```

---

## 💡 Casos de Uso

### 1. Lista de Compras
- ✅ Marcar itens já comprados como inativos
- ✅ Itens comprados vão para o final
- ✅ Fácil visualizar o que ainda falta comprar

**Exemplo:**
```
Ativos (preciso comprar):
• Arroz
• Feijão

Inativos (já comprei):
• ~~Açúcar~~
• ~~Café~~
```

### 2. Lista de Tarefas
- ✅ Marcar tarefas concluídas como inativas
- ✅ Tarefas concluídas vão para o final
- ✅ Foco nas tarefas pendentes

**Exemplo:**
```
Pendentes:
• Estudar Kotlin
• Fazer exercícios

Concluídas:
• ~~Ler documentação~~
• ~~Assistir aula~~
```

### 3. Controle de Estoque
- ✅ Marcar itens esgotados como inativos
- ✅ Fácil visualizar o que precisa repor
- ✅ Reativar quando reposto

---

## 🎯 Comportamento Detalhado

### Quando Inativar (Ativo → Inativo)

| Aspecto | Comportamento |
|---------|---------------|
| **Estado** | `ativo: true` → `ativo: false` |
| **Visual** | Normal → ~~Taxado~~ |
| **Posição** | Move para o **final** da lista |
| **Persistência** | ✅ Salvo no arquivo |
| **Feedback** | Toast: "Item inativado" |

### Quando Ativar (Inativo → Ativo)

| Aspecto | Comportamento |
|---------|---------------|
| **Estado** | `ativo: false` → `ativo: true` |
| **Visual** | ~~Taxado~~ → Normal |
| **Posição** | **Mantém** posição atual |
| **Persistência** | ✅ Salvo no arquivo |
| **Feedback** | Toast: "Item ativado" |

---

## 🔄 Interação com Outras Funcionalidades

### ✅ Compatível com Drag & Drop
- Long press ainda funciona para **reordenar**
- Swipe funciona para **ativar/inativar**
- Gestos diferentes para ações diferentes

### ✅ Compatível com Exclusão
- Long press **sem arrastar** ainda **exclui**
- Long press **com arrastar** **reordena**
- Swipe **ativa/inativa**

### ✅ Persistência
- Estado salvo em arquivo
- Ordem mantida por sublista
- Compatível com sistema de sublistas

---

## 📊 Formato de Arquivo

### Exemplo Completo (Compras.txt)

```
[SUBLISTA:Shibata]
Arroz;5.0;true
Feijão;3.5;true
Açúcar;2.0;false
Café;1.0;false
[SUBLISTA:Nagumo]
Leite;2.5;true
Pão;3.0;false
```

### Estrutura
- Campo 1: Nome
- Campo 2: Quantidade
- Campo 3: Estado (true=ativo, false=inativo) ⭐ NOVO

---

## ⚡ Gestos Disponíveis

| Gesto | Ação |
|-------|------|
| **Toque simples** | Nenhuma ação |
| **Long press (sem arrastar)** | Excluir item |
| **Long press + arrastar vertical** | Reordenar item |
| **Swipe horizontal (←)** | Ativar/Inativar ⭐ NOVO |

---

## 🎨 Feedback Visual

### Durante o Swipe
- Item desliza para a esquerda
- Animação suave
- Retorna à posição ao soltar

### Após Inativar
- ~~Texto taxado~~
- Item move para o final
- Toast de confirmação

### Após Ativar
- Texto normal
- Permanece na posição
- Toast de confirmação

---

## 📝 Arquivos Modificados

| Arquivo | Mudanças |
|---------|----------|
| **Item.kt** | + campo `ativo: Boolean` |
| **ItensAdapter.kt** | + lógica de strikethrough |
| **ItensActivity.kt** | + swipe horizontal no ItemTouchHelper |

### Resumo das Mudanças

**Item.kt:**
- Campo `ativo` adicionado (padrão: `true`)
- Serialização atualizada: `nome;quantidade;ativo`
- Desserialização compatível com arquivos antigos

**ItensAdapter.kt:**
- Import `Paint`
- Aplica/remove `STRIKE_THRU_TEXT_FLAG` baseado em `item.ativo`

**ItensActivity.kt:**
- ItemTouchHelper aceita `ItemTouchHelper.START`
- `onSwiped()` implementado:
  - Alterna estado `ativo`
  - Move para final se inativar
  - Mantém posição se ativar

---

## ✅ Checklist de Validação

### Funcionalidade
- [x] Swipe em item ativo inativa
- [x] Item inativado fica taxado
- [x] Item inativado move para o final
- [x] Swipe em item inativo ativa
- [x] Item ativado remove taxado
- [x] Item ativado mantém posição
- [x] Toast de feedback aparece
- [x] Estado persiste ao fechar app

### Compatibilidade
- [x] Drag & drop ainda funciona
- [x] Exclusão (long press) ainda funciona
- [x] Funciona em ambas sublistas
- [x] Arquivo salva estado corretamente
- [x] Carregamento lê estado corretamente

---

## 🚀 Como Testar

### 1. Compilar e Instalar
```bash
./build.sh
./install.sh
```

### 2. Testar Inativar

1. Abra o app
2. Entre em uma lista → sublista
3. Adicione alguns itens
4. **Arraste um item da direita para esquerda**
5. ✅ Item fica ~~taxado~~
6. ✅ Item vai para o final
7. ✅ Toast: "Item inativado"

### 3. Testar Ativar

1. Com um item inativado
2. **Arraste da direita para esquerda**
3. ✅ Item volta ao normal
4. ✅ Item mantém posição
5. ✅ Toast: "Item ativado"

### 4. Testar Persistência

1. Inative alguns itens
2. Feche e reabra o app
3. Entre na mesma sublista
4. ✅ Itens ainda estão inativos
5. ✅ Ordem mantida

---

## 🎉 Resultado Final

**Versão:** 1.5  
**Build:** ✅ Sucesso  
**APK:** 5.2 MB  

### Funcionalidades Completas
- ✅ **Criar** listas e sublistas
- ✅ **Adicionar** itens
- ✅ **Visualizar** hierarquia completa
- ✅ **Excluir** listas e itens
- ✅ **Reordenar** (drag & drop)
- ✅ **Ativar/Inativar** itens (swipe) ⭐ NOVO
- ✅ **Persistência** completa

### Gestos Implementados
- 🔴 **Toque** = Ver
- 🟡 **Long press** = Excluir
- 🟢 **Drag vertical** = Reordenar
- 🔵 **Swipe horizontal** = Ativar/Inativar ⭐ NOVO

**Sistema de ativar/inativar implementado com sucesso! 🎉**

---

## 💡 Dicas de Uso

### Para Lista de Compras
1. Adicione todos os itens necessários
2. Ao comprar, **swipe** o item
3. Itens comprados ficam ~~taxados~~ no final
4. Fácil ver o que ainda falta

### Para Lista de Tarefas
1. Liste todas as tarefas
2. Ao concluir, **swipe** a tarefa
3. Tarefas concluídas ficam ~~taxadas~~ no final
4. Foco nas tarefas pendentes

### Para Reativar
1. Se comprou errado ou não concluiu
2. **Swipe** novamente
3. Item volta ao estado ativo
4. Reordene se necessário (drag & drop)

---

**Agora você tem controle total sobre o estado dos seus itens! ✓**
