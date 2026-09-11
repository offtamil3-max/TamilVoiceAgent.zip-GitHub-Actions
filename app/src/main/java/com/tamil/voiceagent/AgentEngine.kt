package com.tamil.voiceagent

class AgentEngine(private val context: android.content.Context) {
    data class Result(val success: Boolean, val tamilMessage: String)

    private fun map(result: AppLauncher.Result) = Result(result.success, result.tamilMessage)

    fun execute(raw: String): Result {
        val command = TamilCommandParser.parse(raw)
        return when (command.app) {
            AppTarget.YOUTUBE -> when (command.action) {
                ActionType.OPEN, ActionType.PLAY -> map(AppLauncher.openYouTube(context))
                ActionType.SEARCH -> map(AppLauncher.searchYouTube(context, command.query))
                else -> Result(false, "YouTube command support இல்லை.")
            }
            AppTarget.INSTAGRAM -> when (command.action) {
                ActionType.OPEN -> map(AppLauncher.openInstagram(context))
                else -> Result(false, "Instagram action support இல்லை.")
            }
            AppTarget.CHAT, AppTarget.WHATSAPP -> when (command.action) {
                ActionType.OPEN -> map(AppLauncher.openWhatsApp(context))
                ActionType.TYPE -> Result(false, "WhatsApp type automation requires Accessibility setup.")
                else -> Result(false, "Chat action support இல்லை.")
            }
            AppTarget.UNKNOWN -> Result(false, "எந்த app-ஐ சொல்லுறீங்கன்னு புரியல.")
        }
    }
}
