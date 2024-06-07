package qrcode.emv.decoder

import qrcode.emv.core.Decoder
import qrcode.emv.core.Tag
import qrcode.emv.core.TagIterator

class TagDecoder(source: String) : Decoder {

    private val tagIterator = TagIterator(source)

    override fun decode(): Tag {
        val hasNext = tagIterator.hasNext()
        takeIf { hasNext } ?: throw Exception("Invalid tag")
        val nextChuck = tagIterator.next()

        return Tag(nextChuck.first, nextChuck.second, mutableListOf())

    }
}