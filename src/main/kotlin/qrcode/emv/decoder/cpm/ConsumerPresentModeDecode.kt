package qrcode.emv.decoder.cpm

import ConsumerPresentModeCode
import qrcode.emv.core.cpm.*
import qrcode.emv.model.ConsumerPresentMode
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class ConsumerPresentModeDecode(source: String) {

    private val base64Decoded: ByteArray = Base64.Mime.decode(source)

    fun decode(): ConsumerPresentMode {
        var current = 0
        var next = 0
        val result = ConsumerPresentMode()
        while (next < base64Decoded.size) {
            val tag = Seeker.countTag(base64Decoded, current)
            next += tag.bytes
            val message = ConsumerPresentModeCode.valueBy(tag.value)

            val length = Seeker.countLength(base64Decoded, next)
            next += length.bytes

            val sub = if (message.template)
                handleTemplate(base64Decoded.copyOfRange(next, next + length.value))
            else mutableListOf()
            next += length.value
            val value = base64Decoded.copyOfRange(current + tag.bytes + length.bytes, next)
            result.addTag(handle(tag.value, message, value, sub))
            current = next
        }

        return result
    }


    private fun handleTemplate(source: ByteArray): MutableList<BERTLV> {
        var next = 0
        val result = mutableListOf<BERTLV>()
        while (next < source.size) {
            val tag = Seeker.countTag(source, next)
            val message = ConsumerPresentModeCode.valueBy(tag.value)

            next += tag.bytes
            val length = Seeker.countLength(source, next)
            next += length.bytes
            val value = source.copyOfRange(next, next + length.value)
            next += length.value

            result.add(handle(tag.value, message, value))
        }

        return result
    }

    private fun handle(
        tag: ByteArray,
        code: ConsumerPresentModeCode,
        value: ByteArray,
        subTags: MutableList<BERTLV> = mutableListOf()
    ): BERTLV {
        return when (code.format) {
            Format.BINARY -> {
                Binary(code, value, subTags = subTags, tag = tag)
            }

            Format.NUMERIC -> {
                Numeric(code, value, subTags = subTags, tag = tag)
            }

            Format.ALPHANUMERIC -> {
                Alphanumeric(code, value, subTags = subTags, tag = tag)
            }

            Format.ALPHANUMERIC_SPECIAL -> {
                AlphanumericSpecial(code, value, subTags = subTags, tag = tag)
            }

            Format.COMPRESSED_NUMERIC -> {
                CompressedNumeric(code, value, subTags = subTags, tag = tag)
            }
        }
    }
}