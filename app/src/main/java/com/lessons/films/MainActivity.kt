package com.lessons.films

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        findViewById<MaterialButton>(R.id.but_1).setOnClickListener {
            Toast.makeText(this, R.string.click_but_1, Toast.LENGTH_SHORT).show()
            var us = User("Иван", 34, "Новосибирск")
            var text = us.toString() + "\n" + us.copy(adress = "Владивосток")
            val tv = findViewById<TextView>(R.id.tv_1)
            addText(tv, text + "\n")

            for (i in 1..10) {
                addText(tv, i.toString() + " ")
            }

            addText(tv, "\n")
            for (i in 1..100 step 10) {
                addText(tv, i.toString() + " ")
            }

            addText(tv, "\n")
            val auto = Auto("ВАЗ")
            addText(tv, auto.brand + "\n")
            auto.name = "Жигули"
            addText(tv, auto.name + "\n")

            for (i in listOf("q", "w", "e", "r", "t", "y"))
                addText(tv, i)
        }
    }

    fun addText(tv: TextView, text: String) = with(tv) { this.text = this.text.toString() + text }
}