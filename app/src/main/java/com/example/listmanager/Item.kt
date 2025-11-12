package com.example.listmanager

data class Item(
    val nome: String,
    val quantidade: Double,
    var ativo: Boolean = true
) {
    override fun toString(): String {
        return "$nome;$quantidade;$ativo"
    }
    
    companion object {
        fun fromString(line: String): Item? {
            val parts = line.split(";")
            return if (parts.size >= 2) {
                try {
                    val nome = parts[0]
                    val quantidade = parts[1].toDouble()
                    val ativo = if (parts.size >= 3) parts[2].toBoolean() else true
                    Item(nome, quantidade, ativo)
                } catch (e: NumberFormatException) {
                    null
                }
            } else {
                null
            }
        }
    }
}
