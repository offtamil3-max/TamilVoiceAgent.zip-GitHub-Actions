package com.tamil.voiceagent

data class ParsedCommand(val app: AppTarget,val action: ActionType,val query:String="",val message:String="")
enum class AppTarget { YOUTUBE, INSTAGRAM, CHAT, WHATSAPP, UNKNOWN }
enum class ActionType { OPEN, SEARCH, PLAY, TYPE, READ_COMMENTS, UNKNOWN }
object TamilCommandParser {
 private val youtube=setOf("youtube","யூட்யூப்","யூட்யூப","yt","you tube")
 private val instagram=setOf("instagram","இன்ஸ்டாகிராம்","இன்ஸ்டா","insta","ig")
 private val chat=setOf("chat","whatsapp","வாட்ஸ்அப்","message","msg","சாட்","மெசேஜ்","messenger")
 private fun hasAny(i:String,s:Set<String>)=s.any{i.contains(it)}
 fun parse(rawInput:String):ParsedCommand { val i=rawInput.lowercase().trim(); val app=when{hasAny(i,youtube)->AppTarget.YOUTUBE;hasAny(i,instagram)->AppTarget.INSTAGRAM;hasAny(i,chat)->AppTarget.CHAT;else->AppTarget.UNKNOWN}; val action=when{(i.contains("comment")||i.contains("கமெண்ட்"))&&app==AppTarget.INSTAGRAM->ActionType.READ_COMMENTS;i.contains("type")||i.contains("டைப்")||i.contains("send")||i.contains("அனுப்பு")||i.contains("write")||i.contains("எழுது")->ActionType.TYPE;(i.contains("play")||i.contains("போடு"))&&app==AppTarget.YOUTUBE->ActionType.PLAY;i.contains("search")||i.contains("தேடு")||i.contains("find")||i.contains("கண்டுபிடி")->ActionType.SEARCH;else->ActionType.OPEN}; val q=if(action==ActionType.SEARCH||action==ActionType.PLAY) cleanQuery(i) else ""; val m=if(action==ActionType.TYPE) extractMessage(i) else ""; return ParsedCommand(app,action,q,m) }
 private fun cleanQuery(i:String):String { var q=i; (youtube+instagram+chat).forEach{q=q.replace(it,"")}; listOf(" la "," ல "," le "," search "," தேடு "," play "," ப்ளே "," pannu "," panni "," செய்யவும் ").forEach{q=q.replace(it," ")}; return q.replace(Regex("\\s+")," ").trim() }
 private fun extractMessage(i:String):String { val markers=listOf(" nu type","னு type","ன்னு type"," nu send"," nu sollu"," என்று அனுப்பு","ன்னு அனுப்பு"); for(m in markers){val idx=i.indexOf(m);if(idx>0)return i.substring(0,idx).substringAfterLast("whatsapp").trim()}; return i.replace("type"," ").replace("send"," ").trim() }
}
