package com.example.listmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.appbar.MaterialToolbar

class SublistasActivity : AppCompatActivity() {

    private lateinit var storageManager: StorageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nomeLista: String
    private var lista: Lista? = null
    private var adapter: SublistasAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplica tema antes de setContentView
        themeManager = ThemeManager(this)
        themeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sublistas)
        
        storageManager = StorageManager(this)
        nomeLista = intent.getStringExtra("NOME_LISTA") ?: ""

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = nomeLista
        setSupportActionBar(toolbar)

        // Habilita o botão de voltar na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewSublistas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        
        carregarSublistas()
    }
    
    override fun onResume() {
        super.onResume()
        carregarSublistas()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        // Trata o botão "up" (voltar) da ActionBar como o botão voltar do sistema
        onBackPressed()
        return true
    }
    
    private fun carregarSublistas() {
        lista = storageManager.carregarLista(nomeLista)
        val sublistas = lista?.sublistas ?: emptyList()
        
        if (adapter == null) {
            // Cria o adapter apenas na primeira vez
            adapter = SublistasAdapter(sublistas) { sublista ->
                abrirSublista(sublista.nome)
            }
            recyclerView.adapter = adapter
        } else {
            // Atualiza os dados do adapter existente
            adapter?.atualizarDados(sublistas)
        }
    }
    
    private fun abrirSublista(nomeSublista: String) {
        val intent = Intent(this, ItensActivity::class.java)
        intent.putExtra("NOME_LISTA", nomeLista)
        intent.putExtra("NOME_SUBLISTA", nomeSublista)
        startActivity(intent)
    }
}
