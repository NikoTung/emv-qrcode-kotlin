package qrcode.emv.core.cpm

import ConsumerPresentModeCode

interface TLV<T, V> {
    fun tag(): T
    fun value(): V
    fun subTags(): List<TLV<T, V>> = emptyList()
}


interface BERTLV : TLV<ConsumerPresentModeCode, ByteArray> {

    fun format(): Format

    fun stringValue(): String {
        return format().stringValue(value())
    }

    //Debug only
    @OptIn(ExperimentalStdlibApi::class)
    fun prettyString(): String {
        val subTagString = subTags().joinToString("\n") { "    ${it.prettyString()}" }
        return buildString {
            append("${tag().tag.toHexString()} ${format().stringValue(value())}")
            if (subTagString.isNotEmpty()) {
                append("\n")
                append(subTagString)
            }
        }
    }

    //to be used in the QR code encode.
    fun getBytes(): ByteArray

    override fun subTags(): List<BERTLV> = emptyList()

}

abstract class BERTLVImpl(
    val code: ConsumerPresentModeCode,
    val value: ByteArray,
    val subTags: List<BERTLV> = emptyList(),
    val tag: ByteArray = ByteArray(0)
) : BERTLV {

    private fun getLength(): Int {
        return value().size
    }

    override fun getBytes(): ByteArray {
        if (getLength() == 0) {
            return ByteArray(1) { 0x00 }
        }
        return code.otherTag(byteArrayOf(0x00)) + Seeker.lengthToBytes(getLength()) + value()
    }

    override fun tag(): ConsumerPresentModeCode {
        return code
    }

    override fun value(): ByteArray {
        return value
    }

    override fun subTags(): List<BERTLV> {
        return subTags
    }
}

class Alphanumeric(
    code: ConsumerPresentModeCode, value: ByteArray, subTags: List<BERTLV> = emptyList(), tag: ByteArray = ByteArray(0)
) : BERTLVImpl(code, value, subTags, tag) {


    override fun format(): Format {
        return Format.ALPHANUMERIC
    }
}

class Binary(
    code: ConsumerPresentModeCode, value: ByteArray, subTags: List<BERTLV> = emptyList(), tag: ByteArray = ByteArray(0)
) : BERTLVImpl(code, value, subTags, tag) {

    override fun format(): Format {
        return Format.BINARY
    }

}

class AlphanumericSpecial(
    code: ConsumerPresentModeCode, value: ByteArray, subTags: List<BERTLV> = emptyList(), tag: ByteArray = ByteArray(0)
) : BERTLVImpl(code, value, subTags, tag) {

    override fun format(): Format {
        return Format.ALPHANUMERIC_SPECIAL
    }
}

class Numeric(
    code: ConsumerPresentModeCode, value: ByteArray, subTags: List<BERTLV> = emptyList(), tag: ByteArray = ByteArray(0)
) : BERTLVImpl(code, value, subTags, tag) {
    override fun format(): Format {
        return Format.NUMERIC
    }
}

class CompressedNumeric(
    code: ConsumerPresentModeCode, value: ByteArray, subTags: List<BERTLV> = emptyList(), tag: ByteArray = ByteArray(0)
) : BERTLVImpl(code, value, subTags, tag) {
    override fun format(): Format {
        return Format.COMPRESSED_NUMERIC
    }

}