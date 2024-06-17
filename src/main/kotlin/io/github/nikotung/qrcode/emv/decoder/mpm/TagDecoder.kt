package io.github.nikotung.qrcode.emv.decoder.mpm

import io.github.nikotung.qrcode.emv.core.mpm.Decoder
import io.github.nikotung.qrcode.emv.core.mpm.Tag
import io.github.nikotung.qrcode.emv.core.mpm.TagIterator
import io.github.nikotung.qrcode.emv.exception.InvalidTagException


class TagDecoder(source: String) : Decoder {

    private val tagIterator = TagIterator(source)

    override fun decode(): Tag {
        val hasNext = tagIterator.hasNext()
        takeIf { hasNext } ?: throw InvalidTagException("Invalid tag")
        val nextChuck = tagIterator.next()

        return Tag(nextChuck.first, nextChuck.second)

    }
}