package com.tamil.voiceagent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SetupActivity : AppCompatActivity() {
    private lateinit var micStatus: TextView
    private lateinit var accessStatus: TextView
    private lateinit var micBtn: Button
    private lateinit var accessBtn: Button
    private lateinit var doneBtn: Button

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_setup)
        micStatus = findViewById(R.id.tvMicStatus)
        accessStatus = findViewById(R.id.tvAccessibilityStatus)
        micBtn = findViewById(R.id.btnGrantMic)
        accessBtn = findViewById(R.id.btnGrantAccessibility)
        doneBtn = findViewById(R.id.btnDone)
        micBtn.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 10)
        }
        accessBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        doneBtn.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        val mic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        micStatus.text = if (mic) "✅ Ready!" else "❌ இல்லை"
        micBtn.text = if (mic) "✅ Enabled" else "Enable பண்ணு"
        micBtn.isEnabled = !mic

        val a11y = TamilAccessibilityService.instance != null
        accessStatus.text = if (a11y) "✅ Ready!" else "❌ இல்லை"
        accessBtn.text = if (a11y) "✅ Enabled" else "Enable பண்ணு"
    }
}
