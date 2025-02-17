package me.ibrahimsn.lib

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.File

class AssetsContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "me.ibrahimsn.lib.assetsprovider"
        private const val FILE_PATH = "countries.json"
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, FILE_PATH, 1)
        }
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String = "application/json"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val context = context ?: return null

        if (URI_MATCHER.match(uri) == 1) {
            val file = File(context.cacheDir, FILE_PATH)

            context.assets.open(FILE_PATH).use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }

            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        }

        throw IllegalArgumentException("Unknown URI: $uri")
    }
}
