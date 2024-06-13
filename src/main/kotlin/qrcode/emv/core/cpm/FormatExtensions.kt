package qrcode.emv.core.cpm

@OptIn(ExperimentalStdlibApi::class)
fun Format.hexByteArray(value: String): ByteArray {
    if (value.isBlank()) return byteArrayOf(0x00)
    return when (this) {
        Format.COMPRESSED_NUMERIC -> (if (value.length % 2 == 0) value else value + "F").hexToByteArray(HexFormat.UpperCase)
        Format.NUMERIC -> (if (value.length % 2 == 0) value else "0$value").hexToByteArray(HexFormat.UpperCase)
        Format.ALPHANUMERIC, Format.ALPHANUMERIC_SPECIAL -> value.encodeToByteArray()
        else -> value.hexToByteArray(HexFormat.UpperCase)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun Format.stringValue(value: ByteArray): String {
    return when (this) {
        Format.COMPRESSED_NUMERIC, Format.NUMERIC, Format.BINARY -> value.toHexString(HexFormat.UpperCase)
        Format.ALPHANUMERIC, Format.ALPHANUMERIC_SPECIAL -> String(value, Charsets.UTF_8)
    }
}