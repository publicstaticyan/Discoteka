package discoteka.vo

import discoteka.enums.MessageType

data class MessageVO(
    val type: MessageType,
    val payload: Map<String, Any>
)
