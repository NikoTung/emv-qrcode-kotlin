package qrcode.emv.core.cpm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import qrcode.emv.exception.DecodeException

class SeekerTest {

    @Test
    fun tag_shall_have_next_byte() {
        val byte = 0b00011111.toByte()
        assertTrue(Seeker.hasNext(byte))
    }

    @Test
    fun tag_shall_not_have_next_byte() {
        val byte = 0b00001111.toByte()
        for (i in byte downTo 1) {
            assertFalse(Seeker.hasNext(i.toByte()))
        }
    }

    @Test
    fun getTag() {
        var tags = byteArrayOf(0b10011111.toByte(), 0b10001111.toByte(), 0b00000011.toByte())
        var tag = Seeker.countTag(tags)
        assertEquals(3, tag.bytes)
        assertArrayEquals(tags, tag.value)

        tags = byteArrayOf(0b10011111.toByte(), 0b00001111.toByte(), 0b00000011.toByte())
        tag = Seeker.countTag(tags)
        assertEquals(2, tag.bytes)
        assertArrayEquals(byteArrayOf(0b10011111.toByte(), 0b00001111.toByte()), tag.value)

        tags = byteArrayOf(0b10000111.toByte(), 0b00001111.toByte(), 0b00000011.toByte())
        tag = Seeker.countTag(tags)
        assertEquals(1, tag.bytes)
        assertArrayEquals(byteArrayOf(0b10000111.toByte()), tag.value)

    }

    @Test
    fun lengthToBytes() {
        assertEquals(0, Seeker.countLengthToBytes(0))
        assertEquals(1, Seeker.countLengthToBytes(10))
        assertEquals(1, Seeker.countLengthToBytes(255))

        assertEquals(2, Seeker.countLengthToBytes(257))

        assertThrows(DecodeException::class.java) {
            Seeker.lengthToBytes(259)
        }

        for (i in 1..127) {
            assertArrayEquals(byteArrayOf(i.toByte()), Seeker.lengthToBytes(i))
        }

        for (i in 128..255) {
            assertEquals(2, Seeker.lengthToBytes(i).size)
        }

    }


}