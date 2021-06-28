package com.lessons.films

class MainActivity : MainActivityBasic() {
    override fun menuElements() = setOf(
        R.id.navigation_films, R.id.navigation_history,
        R.id.navigation_contacts, R.id.navigation_map
    )
}