package io.github.nikotung.qrcode.emv.core.cpm

import io.github.nikotung.qrcode.emv.exception.DecodeException
import kotlin.experimental.and

class Seeker {

    companion object {
        private const val MAX_LENGTH_BYTE = 2 //the actually max bytes for the length
        private const val LAST_8_BIT_MASK = 0xFF //0b1111,1111

        /**
         * calculate how many bytes the value has.
         */
        private fun valueOfLength(source: ByteArray, start: Int = 0): Int {
            val length = countBytesOfLength(source, start)
            // the first byte shall skip if the bytes of length is 2
            val byte = if (MAX_LENGTH_BYTE == length) source[start + 1] else source[start]
            return byte.toInt() and LAST_8_BIT_MASK
        }

        fun countTag(source: ByteArray, start: Int = 0): Tag {
            val bytesOfTag = countBytesOfTag(source, start)
            val valueOfTag = valueOfTag(source, start)
            return Tag(bytesOfTag, valueOfTag)
        }

        fun countLength(source: ByteArray, start: Int = 0): Length {
            val bytesOfLength = countBytesOfLength(source, start)
            val valueOfLength = valueOfLength(source, start)
            return Length(bytesOfLength, valueOfLength)

        }

        /**
         * calculate how many bytes should be used to encode this length
         * return zero if the length of the value is 00
         */
        fun countLengthToBytes(length: Int): Int {
            if (length <= 0) return 0
            var fl = length
            var count = 0
            while (fl > 0) {
                count++
                fl = fl shr 8
            }
            return count
        }

        fun lengthToBytes(length: Int): ByteArray {
            return if (length <= Byte.MAX_VALUE) {
                byteArrayOf(length.toByte())
            } else {

                val bytes = countLengthToBytes(length)

                if (bytes > 1) throw DecodeException("The length to code value can't over one byte")

                val result = ByteArray(bytes + 1) //shall add extra byte for indicator and set bit8 to 1

                result[0] = (0x80 + bytes).toByte()
                result[1] = (length and LAST_8_BIT_MASK).toByte()
                return result
            }
        }


        /**
         * calculate how many bytes the length field has
         * * When bit b8 of the most significant byte of the length field is set to 0,
         * the length field consists of only one byte. Bits b7 to b1 code the number of bytes of the value field.
         * The length field is within the range 1 to 127
         * * When bit b8 of the most significant byte of the length field is set to 1,
         * the subsequent bits b7 to b1 of the most significant byte code the number of subsequent bytes in the length field.
         * The subsequent bytes code an integer representing the number of bytes in the value field.
         * Two bytes are necessary to express up to 255 bytes in the value field
         *
         * Notes: The length field of the data objects described in this specification is coded on one or two bytes
         */
        private fun countBytesOfLength(source: ByteArray, start: Int = 0): Int {
            return if (source[start] and NEXT_BYTE_BITMASK.toByte() == NEXT_BYTE_BITMASK.toByte()) {
                val byte = source[start] and Byte.MAX_VALUE
                val byteCount = byte.toInt()
                if (byteCount > 1) {
                    throw DecodeException("The bytes of length shall be coded on one or two bytes, not $byteCount")
                }
                byteCount + 1 //shall add the first byte
            } else {
                1
            }
        }


        private fun valueOfTag(source: ByteArray, start: Int = 0): ByteArray {
            val length = countBytesOfTag(source, start)
            val tag = ByteArray(length)
            System.arraycopy(source, start, tag, 0, length)
            return tag
        }


        /**
         * calculate how many bytes the tag field has
         */
        private fun countBytesOfTag(source: ByteArray, start: Int = 0): Int {
            var count = 0
            if (hasNext(source[start])) {
                do {
                    count++
                } while (notTheLastByte(source[start + count]))
            }
            return count + 1
        }

        /**
         * whether the tag field has more than one byte
         * according to ISO/IEC 8825, the coding rules of the subsequent bytes of a BER-TLV tag
         * when tag numbers ≥ 31 are used (that is, bits b5 - b1 of the first byte equal '11111').
         */
        fun hasNext(source: Byte): Boolean {
            return source and NEXT_BYTE_BITMASK.toByte() == NEXT_BYTE_BITMASK.toByte()
        }

        /**
         *  whether it is the last byte of the tag field
         *  The last five bits (b5-b1) is 11111 indicate there are subsequent bytes of a tag, in the subsequent byte,
         *  the first bit (b8) is 1 indicates there are another subsequent bytes of the tag, and so on.
         */
        private fun notTheLastByte(source: Byte): Boolean {
            return source and LAST_BYTE_MASK.toByte() == LAST_BYTE_MASK.toByte()
        }


    }
}