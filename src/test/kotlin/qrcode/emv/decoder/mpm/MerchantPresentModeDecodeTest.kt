package qrcode.emv.decoder.mpm

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import qrcode.emv.decoder.mpm.MerchantPresentModeDecode
import qrcode.emv.model.crc16

class MerchantPresentModeDecodeTest {

    @Test
    fun testDecode() {
        val source =
            "00020101021127770012com.p2pqrpay0111EAWRPHM2XXX02089996440304125000134020460514+63-91788179125204601653036085802PH5914Komoo Testingg6006AMADEO63041E6F"
        val merchantPresentMode = MerchantPresentModeDecode(source)
        val decode = merchantPresentMode.decode()
        assertNotNull(decode)
        val encoded = decode.toString()
        assertEquals(source, encoded)

    }

    @Test
    fun testCrc16() {
        val source = "00020101021127770012com.p2pqrpay0111EAWR"
        assertEquals(27834, source.crc16())
    }


}