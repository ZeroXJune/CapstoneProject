package com.tpc.trikride.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.OutputStream

/**
 * Gets a finished report off the phone, as a PDF or a spreadsheet.
 *
 * Two routes, because admins want different things: "Save" opens the system
 * file picker so the report lands in Downloads or Drive under a name they
 * choose, and "Share" hands it straight to Gmail, Messenger or whatever else
 * they use to send it on. Neither needs a storage permission.
 *
 * Rendering and sending are separate calls on purpose. Drawing a PDF of a busy
 * month is slow enough to freeze the screen, so the caller runs the render on a
 * background thread and starts the chooser back on the main one, which is the
 * only thread allowed to.
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

    /** Lets a report render itself straight into the chosen file. */
    fun writeTo(context: Context, uri: Uri, render: (OutputStream) -> Unit): Boolean = try {
        context.contentResolver.openOutputStream(uri)?.use(render) != null
    } catch (e: Exception) {
        false
    }

    /** Draws a report into the cache directory, ready to be shared. */
    fun renderToCache(context: Context, fileName: String, render: (OutputStream) -> Unit): File? = try {
        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        File(dir, fileName).also { file -> file.outputStream().use(render) }
    } catch (e: Exception) {
        null
    }

    /** Drops a text report in the cache and opens the share sheet for it. */
    fun share(context: Context, fileName: String, content: String, subject: String): Boolean = try {
        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        val file = File(dir, fileName)
        file.writeText(content, Charsets.UTF_8)
        share(context, file, "text/csv", subject)
    } catch (e: Exception) {
        false
    }

    /** Opens the share sheet for a file already on disk. Main thread only. */
    fun share(context: Context, file: File, mime: String, subject: String): Boolean = try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val send = Intent(Intent.ACTION_SEND).apply {
            type = mime
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
