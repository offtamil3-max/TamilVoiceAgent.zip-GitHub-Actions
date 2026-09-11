package com.tamil.voiceagent

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var tvCommand: TextView
    private lateinit var tvMicLabel: TextView
    private lateinit var btnMic: ImageButton
    private lateinit var btnYouTube: LinearLayout
    private lateinit var btnInstagram: LinearLayout
    private lateinit var btnChat: LinearLayout
    private lateinit var btnSetup: ImageButton
    private lateinit var tts: TamilTTS
    private lateinit var agentEngine: AgentEngine
    private var voiceRecognizer: VoiceRecognizer? = null
    private var isListening = false
    private var micPulseAnimator: ObjectAnimator? = null

    companion object { private const val REQUEST_RECORD_AUDIO = 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews(); initEngines(); setupClickListeners(); checkFirstRun()
    }
    override fun onResume() { super.onResume(); updatePermissionStatus() }
    override fun onDestroy() { voiceRecognizer?.destroy(); tts.destroy(); micPulseAnimator?.cancel(); super.onDestroy() }

    private fun bindViews() {
        tvStatus=findViewById(R.id.tvStatus); tvCommand=findViewById(R.id.tvCommand); tvMicLabel=findViewById(R.id.tvMicLabel)
        btnMic=findViewById(R.id.btnMic); btnYouTube=findViewById(R.id.btnYouTube); btnInstagram=findViewById(R.id.btnInstagram); btnChat=findViewById(R.id.btnChat); btnSetup=findViewById(R.id.btnSetup)
    }
    private fun initEngines() { tts=TamilTTS(this); agentEngine=AgentEngine(this) }
    private fun setupClickListeners() {
        btnMic.setOnClickListener { if (isListening) stopListening() else startVoiceRecognition() }
        btnSetup.setOnClickListener { startActivity(Intent(this, SetupActivity::class.java)) }
        btnYouTube.setOnClickListener { val r=AppLauncher.openYouTube(this); showResult(r.tamilMessage,r.success) }
        btnInstagram.setOnClickListener { val r=AppLauncher.openInstagram(this); showResult(r.tamilMessage,r.success) }
        btnChat.setOnClickListener { val r=AppLauncher.openWhatsApp(this); showResult(r.tamilMessage,r.success) }
    }
    private fun checkFirstRun() {
        val prefs=getSharedPreferences("tamil_va_prefs",MODE_PRIVATE)
        if (prefs.getBoolean("first_run",true)) { prefs.edit().putBoolean("first_run",false).apply(); startActivity(Intent(this,SetupActivity::class.java)) }
    }
    private fun updatePermissionStatus() {
        val hasMic=hasMicPermission()
        tvStatus.text=if(hasMic) getString(R.string.status_ready) else getString(R.string.err_no_mic)
    }
    private fun hasMicPermission() = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED
    private fun startVoiceRecognition() {
        if(!hasMicPermission()){ ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.RECORD_AUDIO),REQUEST_RECORD_AUDIO); return }
        isListening=true; tvStatus.text=getString(R.string.status_listening); tvMicLabel.text="கேக்குறேன்…"; btnMic.setImageResource(android.R.drawable.ic_btn_speak_now)
        micPulseAnimator=ObjectAnimator.ofFloat(btnMic,"alpha",1f,0.45f).apply { duration=650; repeatMode=ObjectAnimator.REVERSE; repeatCount=ObjectAnimator.INFINITE; interpolator=AccelerateDecelerateInterpolator(); start() }
        voiceRecognizer=VoiceRecognizer(this,onResult={ text -> runOnUiThread { stopListening(); tvCommand.text=text; tvStatus.text=getString(R.string.status_processing); val result=agentEngine.execute(text); showResult(result.tamilMessage,result.success) } },onError={ msg -> runOnUiThread { stopListening(); showResult(msg,false) } })
        voiceRecognizer?.startListening()
    }
    private fun stopListening() {
        isListening=false; voiceRecognizer?.stopListening(); micPulseAnimator?.cancel(); micPulseAnimator=null; btnMic.alpha=1f; tvMicLabel.text=getString(R.string.mic_button_label); updatePermissionStatus()
    }
    private fun showResult(message:String,success:Boolean){ tvStatus.text=message; if(message.isNotBlank()) tts.speak(message); if(!success) Toast.makeText(this,message,Toast.LENGTH_SHORT).show() }
}
