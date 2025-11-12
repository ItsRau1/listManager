package com.example.listmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SublistasAdapter(
    private var sublistas: List<Sublista>,
    private val onClick: (Sublista) -> Unit
) : RecyclerView.Adapter<SublistasAdapter.SublistaViewHolder>() {
    
    fun atualizarDados(novasSublistas: List<Sublista>) {
        sublistas = novasSublistas
        notifyDataSetChanged()
    }
    
    class SublistaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeTextView: TextView = view.findViewById(R.id.textNomeLista)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SublistaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return SublistaViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SublistaViewHolder, position: Int) {
        val sublista = sublistas[position]
        val qtdItens = sublista.itens.size
        holder.nomeTextView.text = "${sublista.nome} ($qtdItens ${if (qtdItens == 1) "item" else "itens"})"
        
        // Click para abrir
        holder.itemView.setOnClickListener { onClick(sublista) }
    }
    
    override fun getItemCount() = sublistas.size
}
