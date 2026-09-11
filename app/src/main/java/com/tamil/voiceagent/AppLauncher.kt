package com.tamil.voiceagent

import android.content.Context
import android.content.Intent
import android.net.Uri

object AppLauncher {
    data class Result(val success:Boolean,val tamilMessage:String)
    private fun open(context:Context, packageName:String, fallback:String, msg:String):Result {
        return try {
            val pm=context.packageManager
            val launch=pm.getLaunchIntentForPackage(packageName)
            if(launch!=null){context.startActivity(launch);Result(true,msg)} else {context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(fallback)));Result(true,msg)}
        } catch(e:Exception){Result(false,"App open ஆகல.")}
    }
    fun openYouTube(c:Context)=open(c,"com.google.android.youtube","https://www.youtube.com","YouTube திறந்தது! 🎬")
    fun openInstagram(c:Context)=open(c,"com.instagram.android","https://www.instagram.com","Instagram திறந்தது! 📸")
    fun openWhatsApp(c:Context)=open(c,"com.whatsapp","https://wa.me/","Chat திறந்தது! 💬")
    fun searchYouTube(c:Context,q:String):Result{ return try{c.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(q))));Result(true,"தேடுறேன்…")}catch(e:Exception){Result(false,"YouTube search ஆகல.")} }
}
