package com.example.listmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ItensAdapter(
    itensIniciais: List<Item>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<ItensAdapter.ItemViewHolder>() {

    // Cópia congelada: nunca guardamos a referência mutável viva de fora,
    // senão o DiffUtil acaba comparando a lista contra ela mesma já alterada.
    private var itens: List<Item> = itensIniciais.toList()

    fun atualizarDados(novosItens: List<Item>) {
        val novaLista = novosItens.toList()
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = itens.size
            override fun getNewListSize() = novaLista.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                itens[oldPos].nome == novaLista[newPos].nome &&
                    itens[oldPos].quantidade == novaLista[newPos].quantidade
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                itens[oldPos] == novaLista[newPos]
        })
        itens = novaLista
        diff.dispatchUpdatesTo(this)
    }

    // Usado ao (in)ativar um item: em vez de um "move" (que faz o RecyclerView
    // tentar acompanhar o destino e rolar a tela), tratamos como uma remoção no
    // lugar antigo seguida de uma inserção na posição final — a mesma técnica
    // que já funciona bem ao transferir um item para outra sub-lista. Isso
    // fecha o espaço imediatamente e faz o item reaparecer com um fade suave.
    fun moverItem(posicaoOrigem: Int, posicaoDestino: Int, itensAtualizados: List<Item>) {
        if (posicaoOrigem !in itens.indices) {
            atualizarDados(itensAtualizados)
            return
        }

        itens = itens.toMutableList().also { it.removeAt(posicaoOrigem) }
        notifyItemRemoved(posicaoOrigem)

        val novaLista = itensAtualizados.toList()
        itens = novaLista
        notifyItemInserted(posicaoDestino.coerceIn(0, (novaLista.size - 1).coerceAtLeast(0)))
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
