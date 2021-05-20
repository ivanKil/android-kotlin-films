package com.lessons.films

class Auto(brand: String, name: String = "Нива") {
    val brand = brand
        get() {
            return "brand " + field
        }

    var name = name
        set(value) {
            field = value.toUpperCase()
        }
}