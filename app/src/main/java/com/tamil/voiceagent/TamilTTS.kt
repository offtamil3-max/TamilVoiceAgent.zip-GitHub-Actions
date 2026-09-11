package com.tamil.voiceagent
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
class TamilTTS(private val context:Context){
 private var tts:TextToSpeech?=null; private var ready=false; private var pending:String?=null
 init{tts=TextToSpeech(context){status->if(status==TextToSpeech.SUCCESS){val r=tts?.setLanguage(Locale("ta","IN")); if(r==TextToSpeech.LANG_MISSING_DATA||r==TextToSpeech.LANG_NOT_SUPPORTED)tts?.setLanguage(Locale.ENGLISH); ready=true; pending?.let{ speak(it);pending=null }}}}
 fun speak(text:String){if(!ready){pending=text;return};tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"tva_${System.currentTimeMillis()}")}
 fun destroy(){tts?.stop();tts?.shutdown();tts=null;ready=false}
}
