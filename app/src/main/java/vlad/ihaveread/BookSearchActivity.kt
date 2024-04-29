package vlad.ihaveread

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vlad.ihaveread.ui.theme.IhavereadTheme

class BookSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchType = this.intent.getIntExtra(SEARCH_TYPE, SEARCH_TYPE_BY_DATE)
        setContent {
            IhavereadTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SearchCard(this, searchType)
                }
            }
        }
    }

    fun openDetailsActivity(bookRead: BookRead) {
        startActivity(Intent(this, BookDetailsActivity::class.java).apply {
            putExtra(BOOK_READ_ID, bookRead.id)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCard(activity: BookSearchActivity, searchType: Int) {
    var internSearchType by remember { mutableStateOf(searchType) }
    var searchText by remember { mutableStateOf(if(internSearchType == SEARCH_TYPE_BY_DATE) "2023" else "") }
    var books by remember { mutableStateOf(mutableListOf<BookRead>()) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it }
            )
            Button(
                onClick = {
                    when(internSearchType) {
                        SEARCH_TYPE_BY_DATE -> {
                            books = DBHelper(
                                activity,
                                null
                            ).getBooksByYear(searchText.trim()) as MutableList<BookRead>
                        }
                        SEARCH_TYPE_BY_AUTHOR -> {
                            books = DBHelper(
                                activity,
                                null
                            ).getBooksByAuthor(searchText.trim()) as MutableList<BookRead>
                        }
                        SEARCH_TYPE_BY_TITLE -> {
                            books = DBHelper(
                                activity,
                                null
                            ).getBooksByTitle(searchText.trim()) as MutableList<BookRead>
                        }
                    }
                })
            {
                Text(text = "Search", softWrap = false)
            }
        }
        Text(text = "${books.size} entries") //TODO set in trailer
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            BookList(books) { book ->
                activity.openDetailsActivity(book)
            }
        }
    }
}

@Composable
fun BookCard(book: BookRead, onClick: (book: BookRead) -> Unit) {
    // Add padding around our message
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 5.dp)
        .clickable { onClick(book) },
        //elevation = CardDefaults.cardElevation( defaultElevation =  10.dp )
    ) {
        Row (modifier = Modifier.padding(all = 5.dp)) {
            Column() {
                Text(
                    text = book.langRead,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = book.score.toString(), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column() {
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = book.title, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun BookList(books: List<BookRead>, onClick: (book: BookRead) -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookCard(book) {
                onClick(book)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookCard() {
    IhavereadTheme {
        BookCard(BookRead( 1, "2023-01-01", "en",
            "2000", "ebook", 6,
            "Book title", "Book Author", "genre", "book note")) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookList() {
    val books = SampleData().booksReadSample
    IhavereadTheme {
        BookList(books) {}
    }
}