package com.tamil.voiceagent

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class TamilAccessibilityService:AccessibilityService(){
 companion object { var instance:TamilAccessibilityService?=null }
 override fun onServiceConnected(){super.onServiceConnected();instance=this}
 override fun onAccessibilityEvent(event:AccessibilityEvent?){ }
 override fun onInterrupt(){ }
 override fun onDestroy(){if(instance===this)instance=null;super.onDestroy()}
}
