package com.tamil.voiceagent

class AgentEngine(private val context: android.content.Context) {
    data class Result(val success:Boolean,val tamilMessage:String)
    fun execute(raw:String):Result {
        return when(val c=TamilCommandParser.parse(raw)) {
            is ParsedCommand -> when(c.app) {
                AppTarget.YOUTUBE -> when(c.action){ActionType.OPEN,ActionType.PLAY->AppLauncher.openYouTube(context);ActionType.SEARCH->AppLauncher.searchYouTube(context,c.query);else->Result(false,"YouTube command support இல்லை.")}
                AppTarget.INSTAGRAM -> when(c.action){ActionType.OPEN->AppLauncher.openInstagram(context);else->Result(false,"Instagram action support இல்லை.")}
                AppTarget.CHAT,AppTarget.WHATSAPP -> when(c.action){ActionType.OPEN->AppLauncher.openWhatsApp(context);ActionType.TYPE->Result(false,"WhatsApp type automation requires Accessibility setup.");else->Result(false,"Chat action support இல்லை.")}
                else->Result(false,"எந்த app-ஐ சொல்லுறீங்கன்னு புரியல.")
            }
        }
    }
}
