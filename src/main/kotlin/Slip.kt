import java.util.*

class Slip(val channel: Queue<Byte>) {
    companion object {
        const val END = 192.toByte()
        const val ESC = 219.toByte()
        const val ESC_END = 220.toByte()
        const val ESC_ESC = 221.toByte()
    }

    private lateinit var receiveBuffer: Array<Byte>
    var received: Int = 0


    fun sendPacket(packet: List<Byte>, length: Int) {
        sendChar(END)
        var len = length

        while (len-- > 0) {
            when (val byte = packet[len]) {
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

    fun receivePacket(packet: List<Byte>, maxLength: Int): Int {
        received = 0
        receiveBuffer = Array(maxLength) { ESC }

        while (received < maxLength) {
            when (val byte = receiveChar()) {
                END ->
                    if (received > 0)
                        return received
                    else
                        received = maxLength // TODO: What to do here?
                ESC -> {
                    when (val b = receiveChar()) {
                        ESC_END -> "" // TODO: Print
                        ESC_ESC -> "" // TODO: Print
                        else -> addToReceivebuffer(b)
                    }
                }
                else -> addToReceivebuffer(byte)

            }
        }
        return -1
    }

    private fun sendChar(char: Byte) {
        // Handle Bytestuffing
        channel.add(char)
    }

    private fun receiveChar(): Byte {
        // Handle Bytestuffing
        return channel.remove()
    }

    private fun addToReceivebuffer(byte: Byte) {
        receiveBuffer[received++] = byte
    }


}