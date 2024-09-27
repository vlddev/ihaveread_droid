package vlad.ihaveread

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

class DBCopier(private val ctx: Context) {
    fun maybeReplaceDbFromAssets(dbName: String) {
        val appDataPath = ctx.applicationInfo.dataDir
        val dbFolder = File("$appDataPath/databases")
        dbFolder.mkdir()
        val dbFilePath = File("$appDataPath/databases/$dbName")
        try {
            if (!dbFilePath.exists()) {
                Log.d(LOG_TAG, "Copy database from assets")
                copyDbFromAssets(dbName, dbFilePath)
            }
        } catch (e: IOException) {
            //handle
            Log.e(LOG_TAG, "Error replacing database from assets: " + e.message)
        }
    }

    fun copyDbFromUrl(sourceDbUrl: String, dbName: String): Boolean {
        var ret = true;
        val destDbFile = File(ctx.applicationInfo.dataDir + "/databases/$dbName")
        try {
            URL(sourceDbUrl).openStream().use { input ->
                FileOutputStream(destDbFile).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            ret = false
            Log.e(LOG_TAG, "Error copying file.", e)
        }
        if (ret) Log.d(LOG_TAG, "Database file copied to " + destDbFile.absolutePath)
        return ret
    }

    @Throws(IOException::class)
    private fun copyDbFromAssets(assetDbName: String, destDbFile: File): Boolean {
        var ret: Boolean
        try {
            ctx.assets.open(assetDbName)
                .use { inputStream -> ret = copyFile(inputStream, destDbFile) }
        } catch (e: IOException) {
            ret = false
            Log.e(LOG_TAG, "Error copying file.", e)
        }
        if (ret) Log.d(LOG_TAG, "Database file copied to " + destDbFile.absolutePath)
        return ret
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, destFile: File): Boolean {
        var ret = true
        try {
            FileOutputStream(destFile).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
            }
        } catch (e: IOException) {
            ret = false
            Log.e(LOG_TAG, "Error copying file.", e)
        }
        return ret
    }

    fun download(link: String, path: String) {
        URL(link).openStream().use { input ->
            FileOutputStream(File(path)).use { output ->
                input.copyTo(output)
            }
        }
    }

    companion object {
        val LOG_TAG = DBCopier::class.java.simpleName
    }
}