package com.hao.common.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi

/**
 * @author 李敬卫 2021/02/17
 *
 */
object CameraUtils {
    var type = "image/*"

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getImageBitMapApi29Above(uri: Uri?, context: Context?): Bitmap? {
        var image: Bitmap? = null
        if (uri != null) {
            context?.contentResolver?.openFileDescriptor(uri, "r")?.use { pfd ->
                image = BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor)
            }
        }
        return image
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getImageBitMapApi29Above(data: Intent, context: Context?): Bitmap? {
        val uri = data.data
        var image: Bitmap? = null
        if (uri != null) {
            getImageBitMapApi29Above(uri, context)
        }
        return image
    }

    //Intent 29 以下获取Bitmap对象
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getImageBitMapApi29Down(data: Intent, context: Context?): Bitmap? {
        val imagePath = getImagePathApi29Down(data, context)
        return if (imagePath != null) {
            BitmapFactory.decodeFile(imagePath)
        } else null
    }

    //Uri 29 以下获取Bitmap对象
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getImageBitMapApi29Down(uri: Uri?, context: Context?): Bitmap? {
        if(null==uri)
            return null
        val imagePath = getImagePathApi29Down(uri, context)
        return if (imagePath != null) {
            BitmapFactory.decodeFile(imagePath)
        } else null
    }

    /**
     * 根据Uri获取bitmap
     */
    fun getImageBitMap(uri: Uri?, context: Context?): Bitmap? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getImageBitMapApi29Above(uri, context)
        } else {
            return getImageBitMapApi29Down(uri, context)
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getImagePathApi29Down(data: Intent, context: Context?): String? {
        val uri = data.data
        if (uri == null) {
            return null
        } else {
            var imagePath: String? = null
            if (DocumentsContract.isDocumentUri(context, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if ("com.android.providers.media.documents" == uri.authority) {
                    val id = docId.split(":").toTypedArray()[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imagePath =
                        getImageSpecifiedPathApi29Down(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            selection,
                            context)
                } else if ("com.android.providers.downloads.documents" == uri.authority) {
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(docId))
                    imagePath = getImageSpecifiedPathApi29Down(contentUri, null, context)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                imagePath = getImageSpecifiedPathApi29Down(uri, null, context)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                imagePath = uri.path
            }
            return imagePath
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getImagePathApi29Down(uri: Uri, context: Context?): String? {
        var imagePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":").toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath =
                    getImageSpecifiedPathApi29Down(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection,
                        context)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(docId))
                imagePath = getImageSpecifiedPathApi29Down(contentUri, null, context)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            imagePath = getImageSpecifiedPathApi29Down(uri, null, context)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri.path
        }
        return imagePath

    }


    @SuppressLint("Range")
    private fun getImageSpecifiedPathApi29Down(
        uri: Uri?,
        selection: String?,
        context: Context?,
    ): String? {
        var path: String? = null
        val cursor: Cursor? =
            uri?.let { context?.contentResolver?.query(it, null, selection, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }
}