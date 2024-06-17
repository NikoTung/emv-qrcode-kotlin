package qrcode.emv.core.cpm

import ConsumerPresentModeCode
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import kotlin.test.assertContentEquals

class AlphanumericTest {

    @Test
    fun testEncode() {

        val actual = Alphanumeric(
            ConsumerPresentModeCode.APPLICATION_VERSION,
            ByteArray(0),
            tag = ConsumerPresentModeCode.APPLICATION_VERSION.tag
        )
        assertContentEquals(ByteArray(0), actual.getBytes())
        val empty = ByteArray(0)
        val bytes = byteArrayOf(0x01, 0x02)
        assertContentEquals(bytes + empty, bytes)
    }
}