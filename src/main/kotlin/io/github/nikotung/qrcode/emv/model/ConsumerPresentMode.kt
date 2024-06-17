package io.github.nikotung.qrcode.emv.model

import ConsumerPresentModeCode
import io.github.nikotung.qrcode.emv.core.cpm.Alphanumeric
import io.github.nikotung.qrcode.emv.core.cpm.BERTLV
import io.github.nikotung.qrcode.emv.core.cpm.hexByteArray

class ConsumerPresentMode(val tags: MutableList<BERTLV> = mutableListOf()) {

    fun addTag(tag: BERTLV) = tags.add(tag)

    //Debug only
    fun prettyString(): String = tags.joinToString("\n") { it.prettyString() }

    fun payloadFormatIndicator(): BERTLV =
        tags.first { it.tag().tag.contentEquals(ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR.tag) }


    fun addPayloadFormatIndicator(value: String) {
        val formatIndicator = ConsumerPresentModeCode.PAYLOAD_FORMAT_INDICATOR
        val hexByteArray = formatIndicator.format.hexByteArray(value)
        addTag(Alphanumeric(formatIndicator, hexByteArray, tag = formatIndicator.tag))
    }
    fun applicationTemplate(): List<BERTLV> =
        tags.filter { it.tag().tag.contentEquals(ConsumerPresentModeCode.APPLICATION_TEMPLATE.tag) }


    fun commonDataTemplate(): List<BERTLV> =
        tags.filter { it.tag().tag.contentEquals(ConsumerPresentModeCode.COMMON_DATA_TEMPLATE.tag) }

    fun others(): List<BERTLV> = tags.filter { it.tag().tag.contentEquals(ConsumerPresentModeCode.OTHERS.tag) }


    private fun bytes(): ByteArray {
        val allTags = listOf(payloadFormatIndicator()) + applicationTemplate() + commonDataTemplate() + others()
        return allTags.map { it.getBytes() }.reduce(ByteArray::plus)
    }

    override fun toString(): String = java.util.Base64.getEncoder().encodeToString(bytes())


}