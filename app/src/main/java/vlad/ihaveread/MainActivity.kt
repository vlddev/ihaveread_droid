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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import vlad.ihaveread.ui.theme.IhavereadTheme

const val SEARCH_TYPE = "searchType"
const val SEARCH_TYPE_BY_DATE = 1
const val SEARCH_TYPE_BY_AUTHOR = 2
const val SEARCH_TYPE_BY_TITLE = 3
const val BOOK_READ_ID = "bookRead.id"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBCopier(this).maybeReplaceDbFromAssets(DBHelper.DATABASE_NAME)
        setContent {
            IhavereadTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) { //}, color = MaterialTheme.colorScheme.background) {
                    MainCard(this)
                }
            }
        }
    }

    fun openSearchActivity(searchType: Int) {
        startActivity(Intent(this, BookSearchActivity::class.java).apply {
            putExtra(SEARCH_TYPE, searchType)
        })
    }
}

@Composable
fun MainCard(activity: MainActivity) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 5.dp)
    ) {
        Column (modifier = Modifier.padding(all = 5.dp)) {
            Button( onClick = {activity.openSearchActivity(SEARCH_TYPE_BY_DATE)} )
            {
                Text(text = "By Year", softWrap = false)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button( onClick = {activity.openSearchActivity(SEARCH_TYPE_BY_AUTHOR)} )
            {
                Text(text = "By Author", softWrap = false)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button( onClick = {activity.openSearchActivity(SEARCH_TYPE_BY_TITLE)} )
            {
                Text(text = "By Title", softWrap = false)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainCard() {
    IhavereadTheme {
        MainCard(MainActivity())
    }
}
