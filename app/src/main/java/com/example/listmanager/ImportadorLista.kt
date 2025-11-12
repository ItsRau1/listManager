package com.example.listmanager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Classe responsável por importar listas a partir de texto formatado
 */
class ImportadorLista {
    
    /**
     * Gera o nome da lista baseado na data atual
     * Formato: dd-MM-yyyy (ex: 11-11-2025)
     */
    fun gerarNomeLista(): String {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formato.format(Date())
    }
    
    /**
     * Importa uma lista a partir de texto formatado
     * 
     * Formato esperado:
     * *Shibata*
     * 
     * 1 - Arroz 5Kg
     * 2,3 - Feijão 1Kg
     * 
     * *Nagumo*
     * 
     * 2.5 - Mostarda
     * 1 - Ketchup
     * Tomate
     * Alface
     * 
     * @param textoImportacao Texto formatado para importar
     * @return Lista importada ou null se houver erro
     */
    fun importarDe(textoImportacao: String): Lista? {
        try {
            val nomeLista = gerarNomeLista()
            val sublistas = mutableListOf<Sublista>()
            
            var sublistaAtual: Sublista? = null
            
            // Processa linha por linha
            textoImportacao.lines().forEach { linha ->
                val linhaTrimmed = linha.trim()
                
                when {
                    // Linha marca início de sublista: *Nome*
                    linhaTrimmed.startsWith("*") && linhaTrimmed.endsWith("*") -> {
                        // Salva sublista anterior se existir
                        sublistaAtual?.let { sublistas.add(it) }
                        
                        // Extrai nome da sublista
                        val nomeSublista = linhaTrimmed.removeSurrounding("*").trim()
                        sublistaAtual = Sublista(nomeSublista)
                    }
                    
                    // Linha vazia ou só espaços - ignora
                    linhaTrimmed.isEmpty() -> {
                        // Ignora linhas vazias
                    }
                    
                    // Linha de item (se há sublista ativa)
                    sublistaAtual != null -> {
                        val item = parseItem(linhaTrimmed)
                        item?.let { sublistaAtual!!.itens.add(it) }
                    }
                }
            }
            
            // Adiciona última sublista
            sublistaAtual?.let { sublistas.add(it) }
            
            // Garante que temos Shibata e Nagumo
            val shibata = sublistas.find { it.nome.equals("Shibata", ignoreCase = true) } 
                ?: Sublista("Shibata")
            val nagumo = sublistas.find { it.nome.equals("Nagumo", ignoreCase = true) } 
                ?: Sublista("Nagumo")
            
            return Lista(nomeLista, mutableListOf(shibata, nagumo))
            
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    /**
     * Faz parse de uma linha de item
     * 
     * Formatos aceitos:
     * - "1 - Arroz 5Kg" → Item("Arroz 5Kg", 1.0)
     * - "2,3 - Feijão 1Kg" → Item("Feijão 1Kg", 2.3)
     * - "2.5 - Mostarda" → Item("Mostarda", 2.5)
     * - "Tomate" → Item("Tomate", 0.0)
     * 
     * @param linha Linha a ser processada
     * @return Item criado ou null se inválido
     */
    private fun parseItem(linha: String): Item? {
        try {
            // Verifica se tem quantidade (formato: "numero - nome")
            if (linha.contains(" - ")) {
                val partes = linha.split(" - ", limit = 2)
                if (partes.size == 2) {
                    val quantidadeStr = partes[0].trim()
                    val nome = partes[1].trim()
                    
                    if (nome.isNotBlank()) {
                        // Converte quantidade (aceita vírgula e ponto)
                        val quantidade = quantidadeStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        return Item(nome, quantidade, ativo = true)
                    }
                }
            } else {
                // Sem quantidade - usa 0
                val nome = linha.trim()
                if (nome.isNotBlank()) {
                    return Item(nome, 0.0, ativo = true)
                }
            }
            
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    /**
     * Valida se o texto tem o formato esperado
     * Verifica se tem ao menos uma sublista marcada com *
     */
    fun validarFormato(texto: String): Boolean {
        return texto.contains("*") && texto.trim().isNotBlank()
    }
}
