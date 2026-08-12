package com.tpc.trikride.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Gets a finished CSV off the phone.
 *
 * Two routes, because admins want different things: "Save" opens the system
 * file picker so the report lands in Downloads or Drive under a name they
 * choose, and "Share" hands it straight to Gmail, Messenger or whatever else
 * they use to send it on. Neither needs a storage permission.
 */
object ReportExporter {

    /** Writes [content] to the Uri the system file picker handed back. */
    fun writeTo(context: Context, uri: Uri, content: String): Boolean = try {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(content.toByteArray(Charsets.UTF_8))
        } != null
    } catch (e: Exception) {
        false
    }

    /** Drops the report in the cache and opens the share sheet for it. */
    fun share(context: Context, fileName: String, content: String, subject: String): Boolean = try {
        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        val file = File(dir, fileName)
        file.writeText(content, Charsets.UTF_8)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, "Send report").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        true
    } catch (e: Exception) {
        false
    }
}
