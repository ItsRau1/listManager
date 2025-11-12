package com.example.listmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListasAdapter(
    private val listas: List<String>,
    private val onClick: (String) -> Unit,
    private val onLongClick: (String) -> Unit
) : RecyclerView.Adapter<ListasAdapter.ListaViewHolder>() {
    
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
