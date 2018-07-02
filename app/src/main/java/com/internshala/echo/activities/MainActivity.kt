package com.internshala.echo.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.internshala.echo.fragments.MainScreenFragment
import com.internshala.echo.R
import com.internshala.echo.adapters.NavigationDrawerAdapter
import com.internshala.echo.fragments.SongPlayingFragment

class MainActivity : AppCompatActivity() {
    var navigationDrawerIconsList: ArrayList<String> = arrayListOf()
    var images_for_navDrawer= intArrayOf(R.drawable.navigation_allsongs,R.drawable.navigation_favorites,
            R.drawable.navigation_settings,R.drawable.navigation_aboutus)
    object Statified {
        var drawerlayout: DrawerLayout? = null
        var notificationManager: NotificationManager?=null
    }
    var trackNotificationBuilder: Notification?=null
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationDrawerIconsList.add("All Songs")
        navigationDrawerIconsList.add("Favorities")
        navigationDrawerIconsList.add("Settings")
        navigationDrawerIconsList.add("About Us")
        val toolbar=findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        MainActivity.Statified.drawerlayout=findViewById(R.id.drawer_layout)
        val toggle=ActionBarDrawerToggle(this@MainActivity,MainActivity.Statified.drawerlayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statified.drawerlayout?.addDrawerListener(toggle)
        toggle.syncState()
        val mainScreenFragment= MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragment,"MainScreenFragment")
                .commit()
        var _navigationAdapter=NavigationDrawerAdapter(navigationDrawerIconsList,images_for_navDrawer,this@MainActivity)
        _navigationAdapter.notifyDataSetChanged()

        var navigation_recycler_view=findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recycler_view.layoutManager=LinearLayoutManager(this@MainActivity)
        navigation_recycler_view.itemAnimator=DefaultItemAnimator()
        navigation_recycler_view.adapter=_navigationAdapter
        navigation_recycler_view.setHasFixedSize(true)
        val intent= Intent(this,MainActivity::class.java)
        val pIntent=PendingIntent.getActivity(this,System.currentTimeMillis().toInt(),intent,0)
        trackNotificationBuilder=Notification.Builder(this)
                .setContentTitle("A track is Playing in the Background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statified.notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    }

    override fun onStart() {
        super.onStart()
        try{
            Statified.notificationManager?.cancel(1978)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }
    override fun onStop(){
        super.onStop()
        try{
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean)
            {
                Statified.notificationManager?.notify(1978,trackNotificationBuilder)
            }
        }catch(e:Exception){
            e.printStackTrace()
        }
    }
    override fun onResume(){
        super.onResume()
        try{
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean)
            {
                Statified.notificationManager?.cancel(1978)
            }
        }catch(e:Exception){
            e.printStackTrace()
        }
    }
}


