package com.zakariya.mzmusicplayer.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.zakariya.mzmusicplayer.model.Song
import com.zakariya.mzmusicplayer.util.Constants.baseProjection

class SongRepository(private val context: Context) {

    fun getAllSongs(): List<Song> {
        return songs(makeSongCursor())
    }

    private fun songs(cursor: Cursor?): List<Song> {
        val songs = arrayListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    private fun getSongFromCursorImpl(cursor: Cursor): Song {
        val title = cursor.getString(0)
        val id = cursor.getString(1)
        val path = cursor.getString(2)
        val artistName = cursor.getString(3)
        val albumName = cursor.getString(4)
        val duration = cursor.getLong(5)
        return Song(id, title, path, artistName, albumName, duration)
    }

    @SuppressLint("Recycle")
    private fun makeSongCursor(): Cursor? {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        return try {
            context.applicationContext.contentResolver.query(
                uri,
                baseProjection,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
        } catch (e: SecurityException) {
            null
        }
    }
}