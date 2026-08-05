package com.example.listmanager

import android.content.Context
import java.io.File

class StorageManager(private val context: Context) {
    
    private val listsDir: File
        get() = File(context.filesDir, "listas").apply {
            if (!exists()) mkdirs()
        }
    
    private val orderFile: File
        get() = File(listsDir, "_order.txt")
    
    // Salva uma lista e suas sublistas em um arquivo de texto
    fun salvarLista(lista: Lista) {
        val arquivo = File(listsDir, "${lista.nome}.txt")
        val conteudo = StringBuilder()
        
        lista.sublistas.forEach { sublista ->
            conteudo.append("[SUBLISTA:${sublista.nome}]\n")
            sublista.itens.forEach { item ->
                conteudo.append("${item}\n")
            }
        }
        
        arquivo.writeText(conteudo.toString())
    }
    
    // Carrega uma lista do arquivo
    fun carregarLista(nomeLista: String): Lista {
        val arquivo = File(listsDir, "$nomeLista.txt")
        val sublistas = mutableListOf<Sublista>()
        
        if (arquivo.exists()) {
            var sublistaAtual: Sublista? = null
            
            arquivo.readLines().forEach { line ->
                when {
                    line.startsWith("[SUBLISTA:") -> {
                        // Salva sublista anterior se existir
                        sublistaAtual?.let { sublistas.add(it) }
                        // Cria nova sublista
                        val nomeSublista = line.substringAfter("[SUBLISTA:").substringBefore("]")
                        sublistaAtual = Sublista(nomeSublista)
                    }
                    line.isNotBlank() && sublistaAtual != null -> {
                        Item.fromString(line)?.let { sublistaAtual!!.itens.add(it) }
                    }
                }
            }
            // Adiciona última sublista
            sublistaAtual?.let { sublistas.add(it) }
        }
        
        // Se não houver sublistas, cria as padrão
        if (sublistas.isEmpty()) {
            sublistas.add(Sublista("Shibata"))
            sublistas.add(Sublista("Nagumo"))
        }
        
        return Lista(nomeLista, sublistas)
    }
    
    // Retorna lista de todas as listas cadastradas na ordem personalizada
    fun listarTodasListas(): List<String> {
        val todasListas = listsDir.listFiles()
            ?.filter { it.extension == "txt" && it.name != "_order.txt" }
            ?.map { it.nameWithoutExtension }
            ?.toSet()
            ?: emptySet()
        
        // Carrega ordem salva
        val ordemSalva = if (orderFile.exists()) {
            orderFile.readLines().filter { it.isNotBlank() }
        } else {
            emptyList()
        }
        
        // Combina ordem salva com novas listas (novas sem ordem aparecem primeiro)
        val resultado = mutableListOf<String>()
        resultado.addAll(todasListas.filter { it !in ordemSalva }.sortedDescending())
        resultado.addAll(ordemSalva.filter { it in todasListas })
        
        return resultado
    }
    
    // Salva a ordem personalizada das listas
    fun salvarOrdemListas(listas: List<String>) {
        orderFile.writeText(listas.joinToString("\n"))
    }
    
    // Adiciona um item a uma sublista específica
    fun adicionarItem(nomeLista: String, nomeSublista: String, item: Item) {
        val lista = carregarLista(nomeLista)
        lista.getSublista(nomeSublista)?.itens?.add(item)
        salvarLista(lista)
    }
    
    // Cria uma nova lista vazia e a insere no topo da ordem
    fun criarNovaLista(nomeLista: String): Boolean {
        val arquivo = File(listsDir, "$nomeLista.txt")
        return if (!arquivo.exists()) {
            arquivo.createNewFile()
            val ordemAtual = if (orderFile.exists()) {
                orderFile.readLines().filter { it.isNotBlank() }
            } else {
                emptyList()
            }
            salvarOrdemListas(listOf(nomeLista) + ordemAtual)
            true
        } else {
            false
        }
    }
    
    // Deleta uma lista
    fun deletarLista(nomeLista: String): Boolean {
        val arquivo = File(listsDir, "$nomeLista.txt")
        return arquivo.delete()
    }
    
    // Remove um item específico de uma sublista
    fun removerItem(nomeLista: String, nomeSublista: String, posicao: Int) {
        val lista = carregarLista(nomeLista)
        lista.getSublista(nomeSublista)?.let { sublista ->
            if (posicao >= 0 && posicao < sublista.itens.size) {
                sublista.itens.removeAt(posicao)
                salvarLista(lista)
            }
        }
    }
    
    // Move um item de uma posição para outra dentro da sublista
    fun moverItem(nomeLista: String, nomeSublista: String, dePosicao: Int, paraPosicao: Int) {
        val lista = carregarLista(nomeLista)
        lista.getSublista(nomeSublista)?.let { sublista ->
            if (dePosicao >= 0 && dePosicao < sublista.itens.size && 
                paraPosicao >= 0 && paraPosicao < sublista.itens.size) {
                val item = sublista.itens.removeAt(dePosicao)
                sublista.itens.add(paraPosicao, item)
                salvarLista(lista)
            }
        }
    }
}
