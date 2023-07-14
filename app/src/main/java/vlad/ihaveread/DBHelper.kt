package vlad.ihaveread

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // do nothing
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // do nothing
    }

    fun getBooksByYear(year: String): List<BookRead> {
        var ret = mutableListOf<BookRead>()

        val sql = """SELECT distinct br.book_id, br.date_read, 
            br.lang_read, b.publish_date, br.medium, br.score, 
            (select group_concat(a.name, '; ') from author a, author_book ab where ab.book_id = b.id and ab.author_id = a.id) authors,
            ifnull((select bn.name from book_names bn where bn.book_id = b.id and bn.lang = br.lang_read), b.title) title,
            b.genre, b.note
        from book_readed br, book b, author_book ab, author a 
        where 
        br.book_id = b.id
        and br.book_id = ab.book_id
        and ab.author_id = a.id
        and br.date_read like ?
        order by br.date_read"""

        val db = this.readableDatabase

        val cursor = db.rawQuery(sql, arrayOf("$year%" ))
        if (cursor.count > 0) {
            ret = getBooks(cursor) as MutableList<BookRead>
            Log.d(LOG_TAG, "${ret.size} rows found")
        } else {
            Log.d(LOG_TAG, "Nothing found")
        }
        cursor.close()
        return ret
    }

    fun getBooksByAuthor(author: String): List<BookRead> {
        var ret = mutableListOf<BookRead>()

        val sql = """SELECT distinct br.book_id, br.date_read, 
            br.lang_read, b.publish_date, br.medium, br.score, 
            (select group_concat(a.name, '; ') from author a, author_book ab where ab.book_id = b.id and ab.author_id = a.id) authors,
            ifnull((select bn.name from book_names bn where bn.book_id = b.id and bn.lang = br.lang_read), b.title) title,
            b.genre, b.note
        from book_readed br, book b, author_book ab, author a 
        where 
        br.book_id = b.id
        and br.book_id = ab.book_id
        and ab.author_id = a.id
        and a.name like ?
        order by br.date_read"""

        val db = this.readableDatabase

        val cursor = db.rawQuery(sql, arrayOf("%$author%" ))
        if (cursor.count > 0) {
            ret = getBooks(cursor) as MutableList<BookRead>
            Log.d(LOG_TAG, "${ret.size} rows found")
        } else {
            Log.d(LOG_TAG, "Nothing found")
        }
        cursor.close()
        return ret
    }

    fun getBooksByTitle(title: String): List<BookRead> {
        var ret = mutableListOf<BookRead>()

        val sql = """SELECT distinct br.book_id, br.date_read, 
            br.lang_read, b.publish_date, br.medium, br.score, 
            (select group_concat(a.name, '; ') from author a, author_book ab where ab.book_id = b.id and ab.author_id = a.id) authors,
            ifnull((select bn.name from book_names bn where bn.book_id = b.id and bn.lang = br.lang_read), b.title) title,
            b.genre, b.note
        from book_readed br, book b, author_book ab, author a 
        where 
        br.book_id = b.id
        and br.book_id = ab.book_id
        and ab.author_id = a.id
        and b.id in (select distinct book_id from book_names where name like ?)
        order by br.date_read"""

        val db = this.readableDatabase

        val cursor = db.rawQuery(sql, arrayOf("%$title%" ))
        if (cursor.count > 0) {
            ret = getBooks(cursor) as MutableList<BookRead>
            Log.d(LOG_TAG, "${ret.size} rows found")
        } else {
            Log.d(LOG_TAG, "Nothing found")
        }
        cursor.close()
        return ret
    }

    fun getBooks(cursor: Cursor): List<BookRead> {
        val ret = mutableListOf<BookRead>()
        while (cursor.moveToNext()) {
            ret.add(readBook(cursor))
        }
        Log.d(LOG_TAG, "${ret.size} books found")
        return ret
    }

    fun readBook(cursor: Cursor): BookRead {
        return BookRead(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
            cursor.getString(3), cursor.getString(4)?:"", cursor.getInt(5),
            cursor.getString(7), cursor.getString(6),
            cursor.getString(8)?:"", cursor.getString(9)?:"")
    }

    fun getBookReadById(id: String): BookRead? {
        val sql = """SELECT br.book_id, br.date_read, 
            br.lang_read, b.publish_date, br.medium, br.score, 
            (select group_concat(a.name, '; ') from author a, author_book ab where ab.book_id = b.id and ab.author_id = a.id) authors,
            ifnull((select bn.name from book_names bn where bn.book_id = b.id and bn.lang = br.lang_read), b.title) title,
            b.genre, b.note
        from book_readed br, book b, author_book ab, author a 
        where 
        br.book_id = b.id
        and br.book_id = ab.book_id
        and ab.author_id = a.id
        and b.id = ?"""

        val db = this.readableDatabase

        val cursor = db.rawQuery(sql, arrayOf("$id" ))
        var ret: BookRead? = null
        if (cursor.moveToNext()) {
            ret = readBook(cursor)
        }
        return ret
    }

    fun getBookAuthors(bookId: Int): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("select a.name from author a, author_book ab where a.id = ab.author_id and ab.book_id = ?",
            arrayOf("" + bookId ))
        val builder = StringBuilder()
        while (cursor.moveToNext()) {
            builder.append(cursor.getString(0)).append("; ")
        }
        return builder.toString()
    }

    @SuppressLint("Range")
    fun testDb() {
        execSql("pragma table_list")
        execSql("pragma table_info('author')")
    }

    fun execSql(sql: String) {
        Log.d(LOG_TAG, "Exec SQL: $sql")
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()

        while (cursor.moveToNext()) {
            val builder = StringBuilder()
            for (i in 0 until cursor.columnCount) {
                builder.append(cursor.getString(i)).append(" | ")
            }
            Log.d(LOG_TAG, "${builder.toString()}")
        }
    }

    companion object{
        val LOG_TAG = DBHelper::class.java.simpleName
        // database name
        val DATABASE_NAME = "ihaveread.db"

        // database version
        val DATABASE_VERSION = 1
    }
}
