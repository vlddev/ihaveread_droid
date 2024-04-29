package vlad.ihaveread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vlad.ihaveread.ui.theme.IhavereadTheme

class BookDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookId = this.intent.getIntExtra(BOOK_READ_ID, -1)
        var bookRead = SampleData().bookRead1
        if (bookId != null) {
            bookRead = DBHelper(this, null).getBookReadById(bookId.toString())!!
        }
        setContent {
            IhavereadTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BookDetailsCard(bookRead)
                }
            }
        }
    }
}

@Composable
fun BookDetailsCard(book: BookRead) {
    val labelFontSize = 9.sp
    val labelColor = Color.Gray
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 5.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Authors", fontSize = labelFontSize, color = labelColor)
            Text(text = book.author)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Title", fontSize = labelFontSize, color = labelColor)
            Text(text = book.title)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Published", fontSize = labelFontSize, color = labelColor)
            Text(text = book.publishDate)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Genres", fontSize = labelFontSize, color = labelColor)
            Text(text = book.genre)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Notes", fontSize = labelFontSize, color = labelColor)
            Text(text = book.note)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Read on", fontSize = labelFontSize, color = labelColor)
            Text(text = convDate(book.dateRead))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Language", fontSize = labelFontSize, color = labelColor)
            Text(text = book.langRead)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Medium", fontSize = labelFontSize, color = labelColor)
            Text(text = book.medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Score", fontSize = labelFontSize, color = labelColor)
            Text(text = book.score.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookDetailsCard() {
    IhavereadTheme {
        BookDetailsCard(BookRead( 1, "2023-01-01", "en",
            "2000", "ebook", 6,
            "Book title", "Book Author", "genre", "book note"))
    }
}
