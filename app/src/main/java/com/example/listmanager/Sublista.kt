package com.example.listmanager

data class Sublista(
    val nome: String,
    val itens: MutableList<Item> = mutableListOf()
)
