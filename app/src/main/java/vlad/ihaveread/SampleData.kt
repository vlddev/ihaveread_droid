package vlad.ihaveread

class SampleData {
    val booksSample : List<Book> = listOf(
        Book("Book 1", "Author 1"),
        Book("Book 2", "Author 1"),
        Book("Book 3", "Author 1"),
        Book("Boooook 1", "Author 2"),
        Book("Boooook 2", "Author 2"),
        Book("Boooook 3", "Author 2"),
    )

    val bookRead1 = BookRead( 1, "2023-01-ß1", "en",
        "2000", "ebook", 6,
        "Book 1", "Author 1", "genre", "book note")

    val booksReadSample : List<BookRead> = listOf(
        bookRead1,
        BookRead( 1, "2023-01-ß2", "de",
            "2002", "ebook", 6,
            "Book 2", "Author 1", "genre", "book note"),
        BookRead( 1, "2023-01-ß3", "uk",
            "2003", "ebook", 8,
            "Book 3", "Author 1", "genre", "book note"),
        BookRead( 1, "2023-01-ß1", "en",
            "2000", "ebook", 6,
            "Booooook 1", "Author 2", "genre", "book note"),
    )
}