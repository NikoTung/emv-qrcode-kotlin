package qrcode.emv.core.cpm

enum class Format {
    /**
     * Alphanumeric data objects contain a single character per byte. The
     * permitted characters are alphabetic (a to z and A to Z, upper and lower
     * case) and numeric (0 to 9).
     */
    ALPHANUMERIC,

    /**
     * Alphanumeric Special data objects contain a single character per byte. The
     * permitted characters and their coding are shown in the Common Character
     * Set table in [EMV] Book 4 Annex B.
     */
    ALPHANUMERIC_SPECIAL,

    /**
     * These data objects consist of either unsigned binary numbers or bit
     * combinations that are defined elsewhere in this specification.
     */
    BINARY,

    /**
     * Compressed numeric data objects consist of two numeric digits (having
     * values in the range Hex '0'–'9') per byte. These data objects are left justified
     * and padded with trailing hexadecimal 'F's
     */
    COMPRESSED_NUMERIC,

    /**
     * Numeric data objects consist of two numeric digits (having values in the
     * range Hex '0' – '9') per byte. These digits are right-justified and padded with
     * leading hexadecimal zeroes. Other specifications sometimes refer to this
     * data format as Binary Coded Decimal (BCD) or unsigned packed.
     */
    NUMERIC;
}

data class Length(val bytes: Int, val value: Int)

data class Tag(val bytes: Int, val value: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (bytes != other.bytes) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes
        result = 31 * result + value.contentHashCode()
        return result
    }
}


const val CLASS_BITMASK = 0b11000000 // 0xC0 Tag class: bits 7-8 of the initial octet
const val TYPE_BITMASK = 0b00100000 // 0x20 Tag type: bit 6 of the initial octet
const val LAST_BYTE_MASK = 0b10000000 //0x80
const val NEXT_BYTE_BITMASK = 0b00011111 // 0x1F
enum class TagClass(val i: Int) {
    UNIVERSAL(0b00000000), //0x00
    APPLICATION(0b01000000), //0x40
    CONTEXT_SPECIFIC(0b10000000), //0x80
    PRIVATE(0b11000000) //0xC0
}

enum class DataType(val i: Int) {
    PRIMITIVE(0b00000000), //0x00
    CONSTRUCTED(0b00100000) //0x20
}