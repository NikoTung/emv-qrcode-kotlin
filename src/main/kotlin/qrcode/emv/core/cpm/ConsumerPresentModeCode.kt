import qrcode.emv.core.cpm.Format


interface Other {
    fun otherTag(byteArray: ByteArray): ByteArray = byteArray

}
enum class ConsumerPresentModeCode(val tag: ByteArray, val format: Format, val template: Boolean) : Other{
    //(M)
    PAYLOAD_FORMAT_INDICATOR(byteArrayOf(0x85.toByte()), Format.ALPHANUMERIC, false),

    //(M)
    APPLICATION_TEMPLATE(byteArrayOf(0x61.toByte()), Format.BINARY, true),

    //(O)
    COMMON_DATA_TEMPLATE(byteArrayOf(0x62.toByte()), Format.BINARY, true),

    //notes: not decode transparent data,just for forward to acquirer
    //(O)
    APPLICATION_SPECIFIC_TRANSPARENT_TEMPLATE(byteArrayOf(0x63.toByte()), Format.BINARY, false),

    //(O)
    COMMON_DATA_TRANSPARENT_TEMPLATE(byteArrayOf(0x64.toByte()), Format.BINARY, false),


    //(M), F: b T: '4F' L: 5–16
    APPLICATION_DEFINITION_FILE_NAME(byteArrayOf(0x4F.toByte()), Format.BINARY, false),

    //(O), F: ans* T: '50' L: 1-16. Special characters limited to space.
    APPLICATION_LABEL(byteArrayOf(0x50.toByte()), Format.ALPHANUMERIC_SPECIAL, false),

    //(C),F: cn T: '5A' L: var. up to 10
    APPLICATION_PAN(byteArrayOf(0x5A.toByte()), Format.COMPRESSED_NUMERIC, false),

    //(O), F: b T: '9F08' L: 2. If this data is absent, the POI uses '00 10' (version 1.0) as the default value
    APPLICATION_VERSION(byteArrayOf(0x9F.toByte(), 0x08.toByte()), Format.BINARY, false),

    //(O), F: ans T: '5F20' L: 2–26
    CARDHOLDER_NAME(byteArrayOf(0x5F.toByte(), 0x20.toByte()), Format.ALPHANUMERIC_SPECIAL, false),

    //(O), F: ans T: '5F50' L: var.
    ISSUER_URL(byteArrayOf(0x5F.toByte(), 0x50.toByte()), Format.ALPHANUMERIC_SPECIAL, false),

    //(O), F: n T: '9F25' L: 2
    LAST_4_DIGITS_OF_PAN(byteArrayOf(0x9F.toByte(), 0x25.toByte()), Format.NUMERIC, false),

    //(O),F: an T: '5F2D' L: 2–8
    LANGUAGE_PREFERENCE(byteArrayOf(0x5F.toByte(), 0x2D.toByte()), Format.ALPHANUMERIC, false),

    //(C), F: b T: '57' L: var. up to 19
    TRACK_2_EQUIVALENT_DATA(byteArrayOf(0x57.toByte()), Format.BINARY, false),

    //(O), F: n T: '9F19' L: 6
    TOKEN_REQUESTOR_ID(byteArrayOf(0x9F.toByte(), 0x19.toByte()), Format.NUMERIC, false),

    //(O),  F: an* T: '9F24' L: 29, Permitted characters are alphabetic upper case and numeric.
    PAYMENT_ACCOUNT_REFERENCE(byteArrayOf(0x9F.toByte(), 0x24.toByte()), Format.ALPHANUMERIC, false),

    OTHERS(byteArrayOf(0x00.toByte()), Format.BINARY, false);

    override fun otherTag(byteArray: ByteArray): ByteArray {
        return when (this) {
            OTHERS -> byteArray
            else -> tag
        }
    }
    companion object {

        fun valueBy(tag: ByteArray): ConsumerPresentModeCode {
            return entries.firstOrNull { it.tag.contentEquals(tag) } ?: OTHERS
        }

    }
}

