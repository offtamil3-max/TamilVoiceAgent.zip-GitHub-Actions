package com.tamil.voiceagent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

class VoiceRecognizer(private val context:Context, private val onResult:(String)->Unit, private val onError:(String)->Unit){
 private var recognizer:SpeechRecognizer?=null
 fun startListening(){
  if(!SpeechRecognizer.isRecognitionAvailable(context)){onError("இந்த phone-ல் voice recognition available இல்லை.");return}
  recognizer=SpeechRecognizer.createSpeechRecognizer(context).apply{setRecognitionListener(object:RecognitionListener{
   override fun onReadyForSpeech(p0:Bundle?){}
   override fun onBeginningOfSpeech(){}
   override fun onRmsChanged(p0:Float){}
   override fun onBufferReceived(p0:ByteArray?){}
   override fun onEndOfSpeech(){}
   override fun onError(error:Int){onError("Voice புரியல. மீண்டும் பேசுங்க.")}
   override fun onResults(results:Bundle?){val a=results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(!a.isNullOrEmpty())onResult(a[0]) else onError("Voice புரியல. மீண்டும் பேசுங்க.")}
   override fun onPartialResults(results:Bundle?){ }
   override fun onEvent(type:Int,params:Bundle?){ }
  })
  val i=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale("ta","IN"));putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"ta-IN");putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3)}
  startListening(i)
 }
 }
 fun stopListening(){recognizer?.stopListening()}
 fun destroy(){recognizer?.destroy();recognizer=null}
}
