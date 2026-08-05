package com.example.listmanager

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Collections

class ItensActivity : AppCompatActivity() {

    private lateinit var storageManager: StorageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fabNovoItem: ExtendedFloatingActionButton
    private lateinit var nomeLista: String
    private lateinit var nomeSublista: String
    private var lista: Lista? = null
    private var sublista: Sublista? = null
    private var adapter: ItensAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplica tema antes de setContentView
        themeManager = ThemeManager(this)
        themeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itens)
        
        storageManager = StorageManager(this)
        nomeLista = intent.getStringExtra("NOME_LISTA") ?: ""
        nomeSublista = intent.getStringExtra("NOME_SUBLISTA") ?: ""

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "$nomeLista - $nomeSublista"
        setSupportActionBar(toolbar)

        // Habilita o botão de voltar na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewItens)
        fabNovoItem = findViewById(R.id.fabNovoItem)
        aplicarInsetsNoFab(fabNovoItem)

        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        fabNovoItem.setOnClickListener {
            mostrarDialogNovoItem()
        }
        
        carregarItens()
        configurarDragAndDrop()
    }
    
    override fun onResume() {
        super.onResume()
        // Recarrega itens sempre que a Activity volta ao foco
        // Isso garante que transferências sejam refletidas
        carregarItens()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        // Trata o botão "up" (voltar) da ActionBar como o botão voltar do sistema
        onBackPressed()
        return true
    }
    
    private fun carregarItens() {
        lista = storageManager.carregarLista(nomeLista)
        sublista = lista?.getSublista(nomeSublista)
        atualizarAdapter()
    }
    
    private fun atualizarAdapter() {
        val itens = sublista?.itens ?: emptyList()

        if (adapter == null) {
            // Cria o adapter apenas na primeira vez
            adapter = ItensAdapter(itens) { posicao ->
                mostrarDialogExcluirItem(posicao)
            }
            recyclerView.adapter = adapter
        } else {
            // Atualiza os dados do adapter existente
            adapter?.atualizarDados(itens)
        }
    }

    private fun mostrarDialogNovoItem() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Novo Item")
        
        val layout = layoutInflater.inflate(R.layout.dialog_novo_item, null)
        val inputNome = layout.findViewById<TextInputEditText>(R.id.editNomeItem)
        val inputQuantidade = layout.findViewById<TextInputEditText>(R.id.editQuantidadeItem)
        
        builder.setView(layout)
        
        builder.setPositiveButton("Adicionar") { dialog, _ ->
            val nome = inputNome.text.toString().trim()
            val quantidadeStr = inputQuantidade.text.toString().trim()
            
            if (nome.isNotEmpty() && quantidadeStr.isNotEmpty()) {
                try {
                    val quantidade = quantidadeStr.toDouble()
                    val item = Item(nome, quantidade)
                    storageManager.adicionarItem(nomeLista, nomeSublista, item)
                    carregarItens()
                    Toast.makeText(this, "Item adicionado!", Toast.LENGTH_SHORT).show()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Quantidade inválida!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun mostrarDialogExcluirItem(posicao: Int) {
        val item = sublista?.itens?.getOrNull(posicao) ?: return
        
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Excluir Item")
        builder.setMessage("Deseja realmente excluir o item \"${item.nome}\"?")
        
        builder.setPositiveButton("Excluir") { dialog, _ ->
            storageManager.removerItem(nomeLista, nomeSublista, posicao)
            Toast.makeText(this, "Item excluído!", Toast.LENGTH_SHORT).show()
            carregarItens()
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun configurarDragAndDrop() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END  // Ambas direções
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                
                // Move na sublista local
                sublista?.let { sl ->
                    Collections.swap(sl.itens, fromPos, toPos)
                    recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
                    
                    // Salva a nova ordem no arquivo
                    lista?.let { storageManager.salvarLista(it) }
                }
                
                return true
            }
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                
                // O ItemTouchHelper ainda está finalizando a própria animação/estado
                // interno de swipe neste ponto. Mutar o adapter agora, de forma
                // síncrona, é o que fazia o RecyclerView "pular" o scroll para o
                // fim da lista. Adiar para o próximo frame evita a disputa.
                recyclerView.post {
                    when (direction) {
                        ItemTouchHelper.END -> {
                            // Swipe direita → esquerda: Ativar/Inativar
                            sublista?.let { sl ->
                                val item = sl.itens[position]

                                // Remove no lugar antigo + insere no destino (fade), em vez de um
                                // "move" — que fazia o RecyclerView rolar a tela para acompanhar
                                // o destino.
                                if (item.ativo) {
                                    // Inativa e move para o final
                                    item.ativo = false
                                    sl.itens.removeAt(position)
                                    sl.itens.add(item)
                                    Toast.makeText(this@ItensActivity, "Item inativado", Toast.LENGTH_SHORT).show()

                                    lista?.let { storageManager.salvarLista(it) }
                                    adapter?.moverItem(position, sl.itens.size - 1, sl.itens) ?: atualizarAdapter()
                                } else {
                                    // Ativa e sobe para logo após o último item ativo
                                    // (acima dos inativos, abaixo dos demais ativos)
                                    item.ativo = true
                                    sl.itens.removeAt(position)
                                    val indexPrimeiroInativo = sl.itens.indexOfFirst { !it.ativo }
                                    val destino = if (indexPrimeiroInativo != -1) indexPrimeiroInativo else sl.itens.size
                                    sl.itens.add(destino, item)
                                    Toast.makeText(this@ItensActivity, "Item ativado", Toast.LENGTH_SHORT).show()

                                    lista?.let { storageManager.salvarLista(it) }
                                    adapter?.moverItem(position, destino, sl.itens) ?: atualizarAdapter()
                                }
                            }
                        }

                        ItemTouchHelper.START -> {
                            // Swipe esquerda → direita: Transferir entre sublistas
                            lista?.let { l ->
                                sublista?.let { sl ->
                                    val item = sl.itens[position]

                                    // Determina sublista de destino
                                    val sublistaDestino = if (nomeSublista == "Shibata") {
                                        l.getSublista("Nagumo")
                                    } else {
                                        l.getSublista("Shibata")
                                    }

                                    sublistaDestino?.let { dest ->
                                        // Remove da origem
                                        sl.itens.removeAt(position)

                                        // Adiciona no destino na posição correta
                                        if (item.ativo) {
                                            // Item ativo: insere no final dos ativos (antes dos inativos)
                                            val indexPrimeiroInativo = dest.itens.indexOfFirst { !it.ativo }
                                            if (indexPrimeiroInativo != -1) {
                                                dest.itens.add(indexPrimeiroInativo, item)
                                            } else {
                                                dest.itens.add(item)
                                            }
                                        } else {
                                            // Item inativo: insere no final
                                            dest.itens.add(item)
                                        }

                                        val nomeDestino = if (nomeSublista == "Shibata") "Nagumo" else "Shibata"
                                        Toast.makeText(
                                            this@ItensActivity,
                                            "Item transferido para $nomeDestino",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Salva e recarrega
                                        storageManager.salvarLista(l)
                                        carregarItens()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            override fun isLongPressDragEnabled(): Boolean {
                return true
            }
        })
        
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun aplicarInsetsNoFab(fab: View) {
        val margemBase = resources.getDimensionPixelSize(R.dimen.fab_margin)
        ViewCompat.setOnApplyWindowInsetsListener(fab) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = margemBase + barras.bottom
                marginEnd = margemBase + barras.right
            }
            insets
        }
    }
}
