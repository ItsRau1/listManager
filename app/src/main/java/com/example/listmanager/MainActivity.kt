package com.example.listmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.Collections

class MainActivity : AppCompatActivity() {
    
    private lateinit var storageManager: StorageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var importador: ImportadorLista
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNovaLista: Button
    private lateinit var btnImportarLista: Button
    private lateinit var switchTheme: SwitchMaterial
    private var listas = mutableListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplica tema antes de setContentView
        themeManager = ThemeManager(this)
        themeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        storageManager = StorageManager(this)
        importador = ImportadorLista()
        
        recyclerView = findViewById(R.id.recyclerViewListas)
        btnNovaLista = findViewById(R.id.btnNovaLista)
        btnImportarLista = findViewById(R.id.btnImportarLista)
        switchTheme = findViewById(R.id.switchTheme)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Configura estado inicial do switch
        switchTheme.isChecked = themeManager.isDarkMode()
        
        // Listener do switch de tema
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            themeManager.setDarkMode(isChecked)
            recreate() // Recria a Activity para aplicar o tema
        }
        
        btnNovaLista.setOnClickListener {
            mostrarDialogNovaLista()
        }
        
        btnImportarLista.setOnClickListener {
            mostrarDialogImportarLista()
        }
        
        carregarListas()
        configurarDragAndDrop()
    }
    
    override fun onResume() {
        super.onResume()
        carregarListas()
    }
    
    private fun carregarListas() {
        listas.clear()
        listas.addAll(storageManager.listarTodasListas())
        atualizarAdapter()
    }
    
    private fun mostrarDialogNovaLista() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nova Lista")
        
        val input = EditText(this)
        input.hint = "Nome da lista"
        builder.setView(input)
        
        builder.setPositiveButton("Criar") { dialog, _ ->
            val nomeLista = input.text.toString().trim()
            if (nomeLista.isNotEmpty()) {
                if (storageManager.criarNovaLista(nomeLista)) {
                    carregarListas()
                    Toast.makeText(this, "Lista criada com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Lista já existe!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nome inválido!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun mostrarDialogExcluirLista(nomeLista: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Excluir Lista")
        builder.setMessage("Deseja realmente excluir a lista \"$nomeLista\"?\n\nTodos os itens serão perdidos!")
        
        builder.setPositiveButton("Excluir") { dialog, _ ->
            if (storageManager.deletarLista(nomeLista)) {
                Toast.makeText(this, "Lista excluída com sucesso!", Toast.LENGTH_SHORT).show()
                carregarListas()
            } else {
                Toast.makeText(this, "Erro ao excluir lista!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun mostrarDialogImportarLista() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Importar Lista")
        
        // Infla o layout customizado
        val view = layoutInflater.inflate(R.layout.dialog_importar_lista, null)
        val editTextImportacao = view.findViewById<EditText>(R.id.editTextImportacao)
        builder.setView(view)
        
        builder.setPositiveButton("Importar") { dialog, _ ->
            val textoImportacao = editTextImportacao.text.toString()
            
            if (textoImportacao.isBlank()) {
                Toast.makeText(this, "Digite o texto da lista!", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            
            // Valida formato
            if (!importador.validarFormato(textoImportacao)) {
                Toast.makeText(
                    this, 
                    "Formato inválido! Use *Nome* para sublistas", 
                    Toast.LENGTH_LONG
                ).show()
                return@setPositiveButton
            }
            
            // Importa a lista
            val lista = importador.importarDe(textoImportacao)
            
            if (lista != null) {
                // Verifica se já existe
                if (storageManager.listarTodasListas().contains(lista.nome)) {
                    Toast.makeText(
                        this, 
                        "Já existe uma lista com o nome ${lista.nome}!\nExclua-a antes de importar.", 
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Salva a lista
                    storageManager.salvarLista(lista)
                    
                    // Adiciona no topo da ordem
                    val listasAtuais = storageManager.listarTodasListas().toMutableList()
                    if (!listasAtuais.contains(lista.nome)) {
                        listasAtuais.add(0, lista.nome)
                        storageManager.salvarOrdemListas(listasAtuais)
                    }
                    
                    carregarListas()
                    
                    Toast.makeText(
                        this, 
                        "Lista \"${lista.nome}\" importada com sucesso!\n" +
                        "Shibata: ${lista.getSublista("Shibata")?.itens?.size ?: 0} itens\n" +
                        "Nagumo: ${lista.getSublista("Nagumo")?.itens?.size ?: 0} itens", 
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this, 
                    "Erro ao importar lista!\nVerifique o formato.", 
                    Toast.LENGTH_LONG
                ).show()
            }
            
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
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
    
    private fun atualizarAdapter() {
        val adapter = ListasAdapter(
            listas = listas,
            onClick = { nomeLista -> abrirLista(nomeLista) },
            onLongClick = { nomeLista -> mostrarDialogExcluirLista(nomeLista) }
        )
        recyclerView.adapter = adapter
    }
    
    private fun abrirLista(nomeLista: String) {
        val intent = Intent(this, SublistasActivity::class.java)
        intent.putExtra("NOME_LISTA", nomeLista)
        startActivity(intent)
    }
}
