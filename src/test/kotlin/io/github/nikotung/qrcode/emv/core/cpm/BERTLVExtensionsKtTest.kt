package io.github.nikotung.qrcode.emv.core.cpm

import ConsumerPresentModeCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BERTLVExtensionsKtTest {

    @Test
    fun tagClassTag() {
        val t1 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b00000000)
        )
        assertEquals(TagClass.UNIVERSAL, t1.tagClass())

        val t2 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b01000000)
        )
        assertEquals(TagClass.APPLICATION, t2.tagClass())

        val t3 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b10000000.toByte())
        )
        assertEquals(TagClass.CONTEXT_SPECIFIC, t3.tagClass())

        val t4 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b11000000.toByte())
        )

        assertEquals(TagClass.PRIVATE, t4.tagClass())

    }

    @Test
    fun dataTypeTest() {
        val t1 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b10000000.toByte())
        )
        assertEquals(DataType.PRIMITIVE, t1.dataType())

        val t2 = Alphanumeric(
            ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR,
            byteArrayOf(0x01, 0x02, 0x03),
            tag = byteArrayOf(0b01100000)
        )
        assertEquals(DataType.CONSTRUCTED, t2.dataType())
    }
}