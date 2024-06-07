package qrcode.emv.decoder.cpm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConsumerPresentModeDecodeTest{

    val code = "hQVDUFYwMWETTwegAAAAVVVVUAhQcm9kdWN0MWETTwegAAAAZmZmUAhQcm" +
            "9kdWN0MmJJWggSNFZ4kBI0WF8gDkNBUkRIT0xERVIvRU1WXy0IcnVlc2RlZW5kIZ8Q" +
            "BwYBCgMAAACfJghYT9OF+iNLzJ82AgABnzcEbVjvEw=="
    @Test
    fun decode() {
        val modeDecode = ConsumerPresentModeDecode(code)
        val decode = modeDecode.decode()
        for (i in decode.tags) {
            println(i.prettyString())
        }
    }
}