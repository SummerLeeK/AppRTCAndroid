
object SignalMessageFactory {
    val JOIN_ROOM = 0x01
    val LEAVE_ROOM = 0x02
    val SEND_OFFER = 0x03
    val SEND_ANSWER = 0x04
    val SEND_ICECANDIDATE = 0x05


    fun buildJoinRoom(roomId:String) :SignalMessage{
        return SignalMessage(JOIN_ROOM,roomId)
    }



}