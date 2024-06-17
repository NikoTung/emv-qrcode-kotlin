package io.github.nikotung.qrcode.emv.decoder.cpm

import ConsumerPresentModeCode
import io.github.nikotung.qrcode.emv.core.cpm.*
import io.github.nikotung.qrcode.emv.model.ConsumerPresentMode
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class ConsumerPresentModeDecode(
    source: String,
    private val extraDecoder: MutableMap<ByteArray, (ByteArray) -> BERTLV> = mutableMapOf()
) {

    private val base64Decoded: ByteArray = Base64.Mime.decode(source)

    /**
     * Fixme:
     * Before, between, or after TLV-coded data objects, '00' or 'FF' bytes without any meaning may occur
     * (for example, due to erased or modified TLV-coded data objects).
     */
    fun decode(): ConsumerPresentMode {
        var next = 0
        val result = ConsumerPresentMode()
        while (next < base64Decoded.size) {
            val tag = Seeker.countTag(base64Decoded, next)
            val message = ConsumerPresentModeCode.valueBy(tag.value)

            val length = Seeker.countLength(base64Decoded, next + tag.bytes)

            val toIndex = next + tag.bytes + length.bytes + length.value
            val nextChunk = base64Decoded.copyOfRange(next, toIndex)
            val nextChunkValue = base64Decoded.copyOfRange(
                next + tag.bytes + length.bytes,
                toIndex
            )

            next = toIndex
            val decoder = extraDecoder[tag.value]
            if (decoder != null) {
                result.addTag(decoder(nextChunk))
                continue
            }

            val sub = if (message.template)
                handleTemplate(nextChunkValue)
            else mutableListOf()
            result.addTag(handle(tag.value, message, nextChunkValue, sub))
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