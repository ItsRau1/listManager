package com.example.listmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var storageManager: StorageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var importador: ImportadorLista
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var scrimFab: View
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabNovaListaOpcao: ExtendedFloatingActionButton
    private lateinit var fabImportar: ExtendedFloatingActionButton
    private var listas = mutableListOf<String>()
    private var adapter: ListasAdapter? = null
    private var fabMenuAberto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplica tema antes de setContentView
        themeManager = ThemeManager(this)
        themeManager.applyTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageManager = StorageManager(this)
        importador = ImportadorLista()

        recyclerView = findViewById(R.id.recyclerViewListas)
        toolbar = findViewById(R.id.toolbar)
        scrimFab = findViewById(R.id.scrimFab)
        fabMain = findViewById(R.id.fabMain)
        fabNovaListaOpcao = findViewById(R.id.fabNovaListaOpcao)
        fabImportar = findViewById(R.id.fabImportar)
        val fabContainer = findViewById<View>(R.id.fabContainer)
        aplicarInsetsNoFab(fabContainer)

        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        fabMain.setOnClickListener { alternarFabMenu() }
        scrimFab.setOnClickListener { fecharFabMenu() }
        fabNovaListaOpcao.setOnClickListener {
            fecharFabMenu()
            mostrarDialogNovaLista()
        }
        fabImportar.setOnClickListener {
            fecharFabMenu()
            mostrarDialogImportarLista()
        }

        carregarListas()
        configurarDragAndDrop()
    }

    override fun onBackPressed() {
        if (fabMenuAberto) {
            fecharFabMenu()
            return
        }
        super.onBackPressed()
    }

    private val interpoladorMovimento by lazy {
        AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)
    }
    private val offsetFabOculto by lazy { 24f * resources.displayMetrics.density }

    private fun alternarFabMenu() {
        if (fabMenuAberto) fecharFabMenu() else abrirFabMenu()
    }

    private fun abrirFabMenu() {
        fabMenuAberto = true
        fabMain.animate().cancel()
        fabMain.animate().rotation(45f).setInterpolator(interpoladorMovimento).setDuration(200).start()

        scrimFab.animate().cancel()
        if (scrimFab.visibility != View.VISIBLE) scrimFab.alpha = 0f
        scrimFab.visibility = View.VISIBLE
        scrimFab.animate().alpha(1f).setDuration(150).start()

        animarFabExibindo(fabImportar, 0L)
        animarFabExibindo(fabNovaListaOpcao, 30L)
    }

    private fun fecharFabMenu() {
        fabMenuAberto = false
        fabMain.animate().cancel()
        fabMain.animate().rotation(0f).setInterpolator(interpoladorMovimento).setDuration(200).start()

        scrimFab.animate().cancel()
        scrimFab.animate().alpha(0f).setDuration(150)
            .withEndAction { if (!fabMenuAberto) scrimFab.visibility = View.GONE }
            .start()

        animarFabOcultando(fabNovaListaOpcao, 0L)
        animarFabOcultando(fabImportar, 30L)
    }

    private fun animarFabExibindo(view: View, delay: Long) {
        view.animate().cancel()
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(delay)
            .setDuration(180)
            .setInterpolator(interpoladorMovimento)
            .withEndAction { view.visibility = if (fabMenuAberto) View.VISIBLE else View.INVISIBLE }
            .start()
    }

    private fun animarFabOcultando(view: View, delay: Long) {
        view.animate().cancel()
        view.animate()
            .alpha(0f)
            .translationY(offsetFabOculto)
            .setStartDelay(delay)
            .setDuration(150)
            .setInterpolator(interpoladorMovimento)
            .withEndAction { view.visibility = if (fabMenuAberto) View.VISIBLE else View.INVISIBLE }
            .start()
    }

    private fun aplicarInsetsNoFab(fabContainer: View) {
        val margemBase = resources.getDimensionPixelSize(R.dimen.fab_margin)
        ViewCompat.setOnApplyWindowInsetsListener(fabContainer) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = margemBase + barras.bottom
                marginEnd = margemBase + barras.right
            }
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        atualizarIconeTema(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                themeManager.setDarkMode(!themeManager.isDarkMode())
                recreate() // Recria a Activity para aplicar o tema
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun atualizarIconeTema(menu: Menu) {
        val item = menu.findItem(R.id.action_toggle_theme)
        item.setIcon(
            if (themeManager.isDarkMode()) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
        )
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
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Nova Lista")

        val view = layoutInflater.inflate(R.layout.dialog_nova_lista, null)
        val input = view.findViewById<TextInputEditText>(R.id.editNomeLista)
        builder.setView(view)

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
        val builder = MaterialAlertDialogBuilder(this)
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
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Importar Lista")
        
        // Infla o layout customizado
        val view = layoutInflater.inflate(R.layout.dialog_importar_lista, null)
        val editTextImportacao = view.findViewById<TextInputEditText>(R.id.editTextImportacao)
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
        if (adapter == null) {
            // Cria o adapter apenas na primeira vez
            adapter = ListasAdapter(
                listasIniciais = listas,
                onClick = { nomeLista -> abrirLista(nomeLista) },
                onLongClick = { nomeLista -> mostrarDialogExcluirLista(nomeLista) }
            )
            recyclerView.adapter = adapter
        } else {
            // Atualiza os dados do adapter existente
            adapter?.atualizarDados(listas)
        }
    }
    
    private fun abrirLista(nomeLista: String) {
        val intent = Intent(this, SublistasActivity::class.java)
        intent.putExtra("NOME_LISTA", nomeLista)
        startActivity(intent)
    }
}
