package com.internshala.echo.databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.internshala.echo.Songs
import com.internshala.echo.fragments.FavoriteFragment

class EchoDatabase: SQLiteOpenHelper {


    object Staticated{
        const val DB_NAME="FavoriteDatabase"
        var DB_VERSION=1


        const val TABLE_NAME="FavoriteTable"
        const val COLUMN_ID="SongID"
        const val COLUMN_SONG_TITLE="SongTitle"
        const val COLUMN_SONG_ARTIST="SongArtist"
        const val COLUMN_SONG_PATH="SongPath"
    }



    override fun onCreate(db : SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + Staticated.TABLE_NAME + "(" + Staticated.COLUMN_ID +
                " INTEGER," + Staticated.COLUMN_SONG_ARTIST + " TEXT," + Staticated.COLUMN_SONG_TITLE + " TEXT,"
                + Staticated.COLUMN_SONG_PATH + " TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    {

    }
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION){

    }
    fun storeAsFavorite(id: Int?, artist: String?, songTitle: String?, path: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Staticated.COLUMN_ID, id)
        contentValues.put(Staticated.COLUMN_SONG_ARTIST, artist)
        contentValues.put(Staticated.COLUMN_SONG_TITLE, songTitle)
        contentValues.put(Staticated.COLUMN_SONG_PATH, path)
        db.insert(Staticated.TABLE_NAME, null, contentValues)
        db.close()
    }
    fun queryDBlist(): ArrayList<Songs>? {
        var songList=ArrayList<Songs>()
        try {
            val db = this.readableDatabase
            val query_params = "SELECT * FROM " + Staticated.TABLE_NAME
            val cSor = db.rawQuery(query_params, null)
            if (cSor.moveToFirst()) {
                do {
                    var _id = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                    var _artist = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                    var _title = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                    var _songPath = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                    songList.add(Songs(_id.toLong(), _title, _artist, _songPath, 0))
                } while (cSor.moveToNext())
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return songList

    }
    fun checkifIdExists(_id: Int): Boolean{
        var storeId=-1090
        val db=this.readableDatabase
        val queryparams="SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongID='$_id'"

        val cSor=db.rawQuery(queryparams,null)

        if(cSor.moveToFirst())
        {
            do{
                storeId=cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            }while(cSor.moveToNext())

        }

        else{
            return false
        }
        return storeId!=-1090

    }
    fun deletefavorite(_id: Int){
        val db=this.writableDatabase
        db.delete(Staticated.TABLE_NAME,Staticated.COLUMN_ID+"='$_id'",null)
        db.close()


    }
    fun checkSize(): Int{
        var counter =0
        val db=this.readableDatabase
        var query_params="SELECT * FROM "+ Staticated.TABLE_NAME
        val cSor=db.rawQuery(query_params,null)
        if(cSor.moveToFirst()){
            do{
                counter=counter+1
            }while(cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }
}
