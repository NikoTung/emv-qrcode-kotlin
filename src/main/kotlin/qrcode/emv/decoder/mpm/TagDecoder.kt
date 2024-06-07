package qrcode.emv.decoder.mpm

import qrcode.emv.core.mpm.Decoder
import qrcode.emv.core.mpm.Tag
import qrcode.emv.core.mpm.TagIterator
import qrcode.emv.exception.InvalidTagException

class TagDecoder(source: String) : Decoder {

    private val tagIterator = TagIterator(source)

    override fun decode(): Tag {
        val hasNext = tagIterator.hasNext()
        takeIf { hasNext } ?: throw InvalidTagException("Invalid tag")
        val nextChuck = tagIterator.next()

        return Tag(nextChuck.first, nextChuck.second)

    }
}