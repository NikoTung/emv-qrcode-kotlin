package qrcode.emv.core.cpm

class BinaryTLV {

    companion object {
        val LAST_BYTE_MASK = 0b10000000 //0x80
        val NEXT_BYTE_BITMASK = 0b00011111 // 0x1F
        private val CLASS_BITMASK = 0b11000000 // 0xC0 Tag class: bits 7-8 of the initial octet
        private val TYPE_BITMASK = 0b00100000 // 0x20 Tag type: bit 6 of the initial octet

    }

    fun t() {
        val b = 0x80
        val c = b and LAST_BYTE_MASK
    }

}