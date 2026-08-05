package com.example.listmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SublistasAdapter(
    sublistasIniciais: List<Sublista>,
    private val onClick: (Sublista) -> Unit
) : RecyclerView.Adapter<SublistasAdapter.SublistaViewHolder>() {

    // Cópia congelada: nunca guardamos a referência mutável viva de fora,
    // senão o DiffUtil acaba comparando a lista contra ela mesma já alterada.
    private var sublistas: List<Sublista> = sublistasIniciais.toList()

    fun atualizarDados(novasSublistas: List<Sublista>) {
        val novaLista = novasSublistas.toList()
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = sublistas.size
            override fun getNewListSize() = novaLista.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                sublistas[oldPos].nome == novaLista[newPos].nome
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                sublistas[oldPos] == novaLista[newPos]
        })
        sublistas = novaLista
        diff.dispatchUpdatesTo(this)
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
