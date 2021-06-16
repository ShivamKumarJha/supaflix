package com.shivamkumarjha.supaflix.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.persistence.XmoviesDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFileReceiver(
    private val downloadManager: DownloadManager,
    private val download: Download
) : BroadcastReceiver() {

    @Inject
    lateinit var xmoviesDao: XmoviesDao

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val extras = intent.extras
            val query: DownloadManager.Query = DownloadManager.Query()
            query.setFilterById(extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
            val cursor: Cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status: Int =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    addToDatabase(cursor, context)
                }
            }
            cursor.close()
        }
        Objects.requireNonNull(context).unregisterReceiver(this)
    }

    private fun addToDatabase(cursor: Cursor, context: Context) {
        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fullPath =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            val path = Uri.parse(fullPath).path
            if (!path.isNullOrEmpty()) {
                File(path)
            } else {
                null
            }
        } else {
            val fullPath =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
            File(fullPath)
        }
        val filePath = file?.absolutePath
        if (!filePath.isNullOrEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.download_completed) + "\n" + filePath,
                Toast.LENGTH_LONG
            ).show()
            download.filePath = filePath
            GlobalScope.launch(Dispatchers.IO) {
                xmoviesDao.addToDownload(download)
            }
        }
    }
}