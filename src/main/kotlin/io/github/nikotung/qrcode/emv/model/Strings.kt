package io.github.nikotung.qrcode.emv.model


//The checksum shall be calculated according to [ISO/IEC 13239] using the polynomial
//'1021' (hex) and initial value 'FFFF' (hex).
//Following the calculation of the checksum, the resulting 2-byte hexadecimal value
//shall be encoded as a 4-character Alphanumeric Special value by converting each
//nibble to the corresponding Alphanumeric Special character. A nibble with hex
//value ‘0’ is converted to “0” (= hex value ‘30’), a nibble with hex value ‘1’ is
//converted to “1” (= hex value ‘31’) and so on. Hex values ‘A’ to ‘F’ must be
//converted to uppercase characters “A” to “F” (= hex values ‘41’ to ‘46’).

fun String.crc16(): Int {
    val polynomial = 0x1021 // 0001 0000 0010 0001 (0, 5, 12)

    var result = 0xFFFF // initial value

    val bytes = this.toByteArray()

    for (b in bytes) {
        for (i in 0 until 8) {
            val bit = (b.toInt() shr (7 - i) and 1) == 1
            val c15 = (result shr 15 and 1) == 1
            result = result shl 1
            if (c15 xor bit) {
                result = result xor polynomial
            }
        }
    }

    result = result and 0xffff

    return result
}