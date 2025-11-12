package com.example.listmanager

data class Lista(
    val nome: String,
    val sublistas: MutableList<Sublista> = mutableListOf()
) {
    init {
        // Garante que sempre existam as duas sublistas
        if (sublistas.isEmpty()) {
            sublistas.add(Sublista("Shibata"))
            sublistas.add(Sublista("Nagumo"))
        }
    }
    
    fun getSublista(nome: String): Sublista? {
        return sublistas.find { it.nome == nome }
    }
}
