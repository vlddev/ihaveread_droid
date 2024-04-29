package vlad.ihaveread

data class Book(val title: String, val author: String)

data class BookRead(val id: Int, val dateRead: String,
                    val langRead: String, val publishDate: String,
                    val medium: String, val score: Int,
                    val title: String, val author: String,
                    val genre: String, val note: String)
