package com.example.listmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ListasAdapter(
    listasIniciais: List<String>,
    private val onClick: (String) -> Unit,
    private val onLongClick: (String) -> Unit
) : RecyclerView.Adapter<ListasAdapter.ListaViewHolder>() {

    // Cópia congelada: nunca guardamos a referência mutável viva de fora,
    // senão o DiffUtil acaba comparando a lista contra ela mesma já alterada.
    private var listas: List<String> = listasIniciais.toList()

    fun atualizarDados(novasListas: List<String>) {
        val novaLista = novasListas.toList()
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = listas.size
            override fun getNewListSize() = novaLista.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                listas[oldPos] == novaLista[newPos]
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                listas[oldPos] == novaLista[newPos]
        })
        listas = novaLista
        diff.dispatchUpdatesTo(this)
    }

    class ListaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeTextView: TextView = view.findViewById(R.id.textNomeLista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return ListaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        val nomeLista = listas[position]
        holder.nomeTextView.text = nomeLista

        // Click para abrir
        holder.itemView.setOnClickListener { onClick(nomeLista) }

        // Long press para excluir
        holder.itemView.setOnLongClickListener {
            onLongClick(nomeLista)
            true
        }
    }

    override fun getItemCount() = listas.size
}
