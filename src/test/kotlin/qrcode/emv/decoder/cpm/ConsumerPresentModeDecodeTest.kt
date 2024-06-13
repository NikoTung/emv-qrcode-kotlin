package qrcode.emv.decoder.cpm

import ConsumerPresentModeCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import qrcode.emv.core.cpm.*
import qrcode.emv.model.ConsumerPresentMode

class ConsumerPresentModeDecodeTest {

    /**
     * Binary Data (shown as hex bytes):
     * 85 05 43 50 56 30 31
     * 61 1A
     *       4F 07 A0 00 00 00 55 55 55
     *       57 0F 12 34 56 78 90 12 34 58 D1 91 22 01 12 34 5F
     */
    val code = "hQVDUFYwMWEaTwegAAAAVVVVVw8SNFZ4kBI0WNGRIgESNF8="

    @Test
    fun decode() {
        val modeDecode = ConsumerPresentModeDecode(code)
        val decode = modeDecode.decode()
        println(decode.prettyString())
        val tags = mutableSetOf("85", "61")

        decode.tags.forEach {
            assertTrue(tags.contains(it.tag().tagString()))
        }
        val applicationTemplates = decode.applicationTemplate()
        assertEquals(1, applicationTemplates.size)
        assertEquals(0, decode.commonDataTemplate().size)

        assertEquals("CPV01", decode.payloadFormatIndicator().stringValue())
        assertEquals("4350563031", decode.payloadFormatIndicator().hexValue())

        val applicationTemplate = applicationTemplates[0]
        assertEquals("4F07A0000000555555570F1234567890123458D191220112345F", applicationTemplate.hexValue())

        val subTags = applicationTemplate.subTags()
        assertEquals(2, subTags.size)

        //4F 57
        val expectedSubTags = mutableSetOf(
            ConsumerPresentModeCode.APPLICATION_DEFINITION_FILE_NAME.tagString(),
            ConsumerPresentModeCode.TRACK_2_EQUIVALENT_DATA.tagString()
        )
        subTags.forEach {
            assertTrue(expectedSubTags.contains(it.tag().tagString()))
        }

        applicationTemplate.applicationDefinitionFileName().apply {
            assertEquals("A0000000555555", hexValue())
        }

        applicationTemplate.track2EquivalentData()?.apply {
            assertEquals("1234567890123458D191220112345F", hexValue())
        }

    }

    @Test
    fun encode() {
        val modeEncode = ConsumerPresentMode()

        modeEncode.addPayloadFormatIndicator("CPV01")

        var byteArray = Format.BINARY.hexByteArray("A0000000555555")

        val applicationDefinitionFileName = ConsumerPresentModeCode.APPLICATION_DEFINITION_FILE_NAME
        val adf = Binary(applicationDefinitionFileName, byteArray, tag = applicationDefinitionFileName.tag)

        val trackData = ConsumerPresentModeCode.TRACK_2_EQUIVALENT_DATA
        byteArray = Format.BINARY.hexByteArray("1234567890123458D191220112345F")

        val track = Binary(trackData, byteArray, tag = trackData.tag)
        val applicationTemplateCode = ConsumerPresentModeCode.APPLICATION_TEMPLATE

        val template = Binary(
            applicationTemplateCode,
            adf.getBytes() + track.getBytes(),
            tag = applicationTemplateCode.tag,
            subTags = mutableListOf(adf, track)
        )
        modeEncode.addTag(template)

        assertEquals(code, modeEncode.toString())

    }


    /**
     * Binary Data (shown as hex bytes):
     * 85 05 43 50 56 30 31
     * 61 13
     *       4F 07 A0 00 00 00 55 55 55
     *       50 08 50 72 6F 64 75 63 74 31
     * 61 13
     *       4F 07 A0 00 00 00 66 66 66
     *       50 08 50 72 6F 64 75 63 74 32
     * 62 49
     *       5A 08 12 34 56 78 90 12 34 58
     *       5F 20 0E 43 41 52 44 48 4F 4C 44 45 52 2F 45 4D 56
     *       5F 2D 08 72 75 65 73 64 65 65 6E
     *       64 21
     *             9F 10 07 06 01 0A 03 00 00 00
     *             9F 26 08 58 4F D3 85 FA 23 4B CC
     *             9F 36 02 00 01
     *             9F 37 04 6D 58 EF 13
     */
    val code1 = "hQVDUFYwMWETTwegAAAAVVVVUAhQcm9kdWN0MWETTwegAAAAZmZmUAhQcm" +
            "9kdWN0MmJJWggSNFZ4kBI0WF8gDkNBUkRIT0xERVIvRU1WXy0IcnVlc2RlZW5kIZ8Q" +
            "BwYBCgMAAACfJghYT9OF+iNLzJ82AgABnzcEbVjvEw=="

    @Test
    fun decode_multi_application_templates() {
        val modeDecode = ConsumerPresentModeDecode(code1)
        val decode = modeDecode.decode()
        println(decode.prettyString())
        val applicationTemplates = decode.applicationTemplate()
        assertEquals(2, applicationTemplates.size)
        val at1 = applicationTemplates[0]
        assertEquals("4F07A0000000555555500850726F6475637431", at1.hexValue())
        at1.track2EquivalentData()?.apply {
            assertEquals("50726F6475637431", hexValue())
        }
        at1.applicationDefinitionFileName().apply {
            assertEquals("A0000000555555", hexValue())
        }
        val at2 = applicationTemplates[1]
        assertEquals("4F07A0000000666666500850726F6475637432", at2.hexValue())
        at2.track2EquivalentData()?.apply {
            assertEquals("50726F6475637432", hexValue())
        }
        at2.applicationDefinitionFileName().apply {
            assertEquals("A0000000666666", hexValue())
        }

        val commonDataTemplates = decode.commonDataTemplate()
        assertEquals(1, commonDataTemplates.size)
        val cdt = commonDataTemplates[0]
        assertEquals(
            "5A0812345678901234585F200E43415244484F4C4445522F454D565F2D08727565736465656E64219F100706010A030000009F2608584FD385FA234BCC9F360200019F37046D58EF13",
            cdt.hexValue()
        )

        cdt.applicationPAN()?.apply {
            assertEquals("1234567890123458", hexValue())
            assertEquals("1234567890123458", stringValue())
        }

        cdt.cardholderName()?.apply {
            assertEquals("CARDHOLDER/EMV", stringValue())
            assertEquals("43415244484F4C4445522F454D56", hexValue())
        }

        cdt.languagePreference()?.apply {
            assertEquals("727565736465656E", hexValue())
            assertEquals("ruesdeen", stringValue())
        }

        assertEquals(code1, decode.toString())
    }
}