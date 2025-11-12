package com.example.listmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItensAdapter(
    private var itens: List<Item>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<ItensAdapter.ItemViewHolder>() {
    
    fun atualizarDados(novosItens: List<Item>) {
        itens = novosItens
        notifyDataSetChanged()
    }
    
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeTextView: TextView = view.findViewById(R.id.textNomeItem)
        val quantidadeTextView: TextView = view.findViewById(R.id.textQuantidadeItem)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ItemViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itens[position]
        holder.nomeTextView.text = item.nome
        holder.quantidadeTextView.text = "Quantidade: ${item.quantidade}"
        
        // Aplica strikethrough se inativo
        if (item.ativo) {
            holder.nomeTextView.paintFlags = holder.nomeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.quantidadeTextView.paintFlags = holder.quantidadeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            holder.nomeTextView.paintFlags = holder.nomeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.quantidadeTextView.paintFlags = holder.quantidadeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        
        // Long press para excluir
        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }
    
    override fun getItemCount() = itens.size
}
