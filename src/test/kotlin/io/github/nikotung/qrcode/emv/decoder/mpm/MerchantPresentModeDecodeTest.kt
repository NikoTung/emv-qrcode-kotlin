package io.github.nikotung.qrcode.emv.decoder.mpm

import io.github.nikotung.qrcode.emv.core.mpm.Tag
import io.github.nikotung.qrcode.emv.model.MerchantPresentMode
import io.github.nikotung.qrcode.emv.model.crc16
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class MerchantPresentModeDecodeTest {

    @Test
    fun testDecode() {
        val source =
            "00020101021229300012D156000000000510A93FO3230Q31280012D15600000001030812345678520441115802CN5914BEST TRANSPORT6007BEIJING64200002ZH0104最佳运输0202北京540523.7253031565502016233030412340603***0708A60086670902ME91320016A0112233449988770708123456786304A13A"
        val merchantPresentMode = MerchantPresentModeDecode(source)
        val decode = merchantPresentMode.decode()
        assertNotNull(decode)
        assertEquals("000201", decode.getPayloadFormatIndicator().toString())
        assertEquals("01", decode.getPayloadFormatIndicator()?.value())
        assertEquals("010212", decode.getPointOfInitiationMethod().toString())
        assertEquals("12", decode.getPointOfInitiationMethod()?.value())

        assertEquals("6304A13A", decode.getCrc()?.toString())
        assertEquals("A13A", decode.getCrc()?.value())

        decode.getMerchantAccountInformation().let {
            assertEquals(2, it.size)
            val tag29 = it[0]
            assertEquals(2, tag29.subTags.size)
            val subTags29 = tag29.subTags
            for (subTag in subTags29) {
                when (subTag.tag) {
                    "00" -> {
                        assertEquals("D15600000000", subTag.value())
                        assertEquals("0012D15600000000", subTag.toString())
                    }

                    "05" -> {
                        assertEquals("A93FO3230Q", subTag.value())
                        assertEquals("0510A93FO3230Q", subTag.toString())
                    }
                }
            }

            val tag31 = it[1]
            assertEquals(2, tag31.subTags.size)
            for (subTag in tag31.subTags) {
                when (subTag.tag) {
                    "00" -> {
                        assertEquals("D15600000001", subTag.value())
                        assertEquals("0012D15600000001", subTag.toString())
                    }

                    "03" -> {
                        assertEquals("12345678", subTag.value())
                        assertEquals("030812345678", subTag.toString())
                    }
                }
            }
        }

        decode.getMerchantCategoryCode()?.let {
            assertEquals("4111", it.value())
            assertEquals("52044111", it.toString())
        }
        decode.getCountryCode()?.let {
            assertEquals("CN", it.value())
            assertEquals("5802CN", it.toString())
        }

        decode.getMerchantName()?.let {
            assertEquals("BEST TRANSPORT", it.value())
            assertEquals("5914BEST TRANSPORT", it.toString())
        }
        decode.getMerchantCity()?.let {
            assertEquals("BEIJING", it.value())
            assertEquals("6007BEIJING", it.toString())
        }

        decode.getMerchantInformationLanguageTemplate()?.let {
            assertEquals(3, it.subTags.size)
            val tag00 = it.subTags[0]
            assertEquals("ZH", tag00.value())
            assertEquals("0002ZH", tag00.toString())

            val tag01 = it.subTags[1]
            assertEquals("最佳运输", tag01.value())
            assertEquals("0104最佳运输", tag01.toString())

            val tag02 = it.subTags[2]
            assertEquals("北京", tag02.value())
            assertEquals("0202北京", tag02.toString())
        }

        decode.getTransactionAmount()?.let {
            assertEquals("23.72", it.value())
            assertEquals("540523.72", it.toString())
        }

        decode.getTransactionCurrency()?.let {
            assertEquals("156", it.value())
            assertEquals("5303156", it.toString())
        }

        decode.getTipOrConvenienceIndicator()?.let {
            assertEquals("01", it.value())
            assertEquals("550201", it.toString())
        }

        decode.getAdditionalDataFieldTemplate()?.let { it ->
            assertEquals(4, it.subTags.size)
            val storeLabelTag = it.subTags[0]
            assertEquals("1234", storeLabelTag.value())
            assertEquals("03041234", storeLabelTag.toString())

            val customerLabelTag = it.subTags[1]
            assertEquals("***", customerLabelTag.value())
            assertEquals("0603***", customerLabelTag.toString())

            //terminal label
            it.subTags[2].let {
                assertEquals("A6008667", it.value())
                assertEquals("0708A6008667", it.toString())
            }

            //additional consumer data request
            it.subTags[3].let {
                assertEquals("ME", it.value())
                assertEquals("0902ME", it.toString())
            }
        }

        decode.getUnreservedTemplates().let {
            assertEquals(1, it.size)
            it[0].let { i ->

                assertEquals(2, i.subTags.size)
                i.subTags[0].let { t ->
                    assertEquals("A011223344998877", t.value())
                    assertEquals("0016A011223344998877", t.toString())
                }

                i.subTags[1].let { t ->
                    assertEquals("12345678", t.value())
                    assertEquals("070812345678", t.toString())
                }

            }
        }
    }

    @Test
    fun testCrc16() {
        val source = "00020101021127770012com.p2pqrpay0111EAWR"
        assertEquals(27834, source.crc16())
    }

    @Test
    fun encode() {
        val payload = Tag("00", "01")
        val poi = Tag("01", "12")
        val amount = Tag("54", "100")
        val merchantName = Tag("59", "BEST TRANSPORT")
        val merchantCity = Tag("60", "BEIJING")
        val merchantCategoryCode = Tag("52", "4111")
        val countryCode = Tag("58", "CN")
        val currency = Tag("53", "156")


        val account00 = Tag("00", "D15600000000")
        val account05 = Tag("05", "A93FO3230Q")
        val merchantAccountInformation =
            Tag("29", account00.toString() + account05.toString(), subTags = mutableListOf(account00, account05))


        val qrcode = MerchantPresentMode(
            mutableMapOf(
                "00" to payload,
                "01" to poi,
                "54" to amount,
                "59" to merchantName,
                "60" to merchantCity,
                "52" to merchantCategoryCode,
                "58" to countryCode,
                "53" to currency,
                "29" to merchantAccountInformation
            )
        ).toString()

        assertEquals("00020101021229300012D156000000000510A93FO3230Q52044111530315654031005802CN5914BEST TRANSPORT6007BEIJING63047F8D", qrcode)
    }


}