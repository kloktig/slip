import mu.KotlinLogging
import java.util.*

class Channel {
    val values = LinkedList<Byte>() as Queue<Byte>
}

@ExperimentalUnsignedTypes
class Slip(val channel: Channel) {
    companion object {
        const val END = 192.toByte()
        const val ESC = 219.toByte()
        const val ESC_END = 220.toByte()
        const val ESC_ESC = 221.toByte()
    }

    private val logger = KotlinLogging.logger {}

    private lateinit var receiveBuffer: Array<Byte>
    private var received: Int = 0

    fun getReceived() = receiveBuffer.take(received)

    fun sendPacket(packet: List<Byte>, length: Int) {
        sendChar(END)
        var idx = 0

        while (idx < length) {
            when (val byte = packet[idx++]) {
                END -> {
                    sendChar(ESC)
                    sendChar(ESC_END)
                }
                ESC -> {
                    sendChar(ESC)
                    sendChar(ESC_ESC)
                }
                else -> sendChar(byte)
            }
        }

        sendChar(END)
    }

    fun receivePacket(maxLength: Int): Int {
        received = 0
        receiveBuffer = Array(maxLength) { ESC }

        while (received < maxLength) {
            when (val byte = receiveChar()) {
                END ->
                    if (received > 0)
                        return received
                ESC -> {
                    val char = when (val b = receiveChar()) {
                        ESC_END -> END
                        ESC_ESC -> ESC
                        else -> b
                    }
                    addToReceivebuffer(char)
                }
                else -> addToReceivebuffer(byte)

            }
        }
        return -1
    }

    private fun sendChar(char: Byte) {
        logger.info { "sendChar: ${char.toUByte()}" }
        channel.values.add(char)
    }

    private fun receiveChar(): Byte {
        val char = channel.values.remove()
        logger.info { "receiveChar: ${char.toUByte()}" }
        return char
    }

    private fun addToReceivebuffer(byte: Byte) {
        logger.info { "addToReceivebuffer: idx: $received, value:${byte.toUByte()}" }
        receiveBuffer[received++] = byte
    }
}