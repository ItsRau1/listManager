package com.example.listmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class ThemeManager(private val context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
        val THEME_LIGHT = R.style.Theme_ListManager_Light
        val THEME_DARK = R.style.Theme_ListManager_Dark
    }
    
    /**
     * Verifica se o modo escuro está ativado
     */
    fun isDarkMode(): Boolean {
        return prefs.getBoolean(KEY_IS_DARK_MODE, false)
    }
    
    /**
     * Alterna entre modo claro e escuro
     */
    fun toggleTheme(): Boolean {
        val isDark = !isDarkMode()
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, isDark).apply()
        return isDark
    }
    
    /**
     * Define o modo escuro
     */
    fun setDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, isDark).apply()
    }
    
    /**
     * Retorna o ID do tema atual
     */
    fun getCurrentTheme(): Int {
        return if (isDarkMode()) THEME_DARK else THEME_LIGHT
    }
    
    /**
     * Aplica o tema na Activity
     */
    fun applyTheme(activity: AppCompatActivity) {
        activity.setTheme(getCurrentTheme())
    }
}
