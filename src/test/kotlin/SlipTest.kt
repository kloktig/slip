import Slip.Companion.END
import Slip.Companion.ESC
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

internal class SlipTest {
    val channel = Channel()
    val s1 = Slip(channel)
    var s2 = Slip(channel)

    @Test
    fun getReceived() {
        val packetToSend = listOf(1.toByte(), 2, 3, 4, 5)

        s1.sendPacket(packetToSend, 5)
        s2.receivePacket(10)

        assertThat(packetToSend).isEqualTo(s2.getReceived())
    }

    @Test
    fun getReceivedTooLong() {
        val packetToSend = listOf(1.toByte(), 2, 3, 4, 5)

        s1.sendPacket(packetToSend, 5)
        s2.receivePacket(3)

        assertThat(packetToSend.take(3)).isEqualTo(s2.getReceived())
    }

    @Test
    fun getReceivedEscaped() {
        val packetToSend = listOf(1, END, 3, ESC, END)

        s1.sendPacket(packetToSend, 5)
        s2.receivePacket(5)

        assertThat(packetToSend).isEqualTo(s2.getReceived())
    }

    @Test
    fun getReceivedEscapedTooLong() {
        val packetToSend = listOf(1, END, 3, ESC, END)

        s1.sendPacket(packetToSend, 5)
        s2.receivePacket(3)

        assertThat(packetToSend.take(3)).isEqualTo(s2.getReceived())
    }

    @Test
    fun getReceivedEmpty() {
        val packetToSend = listOf<Byte>()

        s1.sendPacket(packetToSend, 0)
        s2.receivePacket(0)

        assertThat(packetToSend.size).isEqualTo(0)
    }

    @Test
    fun getReceivedEmptyWrongLength() {

        assertThrows<Exception> {
            val packetToSend = listOf<Byte>()

            s1.sendPacket(packetToSend, 1)
            s2.receivePacket(1)

        }
    }
}