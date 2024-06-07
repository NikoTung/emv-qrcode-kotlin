package qrcode.emv.decoder

import qrcode.emv.core.Tag
import qrcode.emv.core.TagIterator

class ListTagDecoder(source: String)  {

    private val tagIterator = TagIterator(source)
    fun decode(): MutableList<Tag> {

        val tags = mutableListOf<Tag>()
        while (tagIterator.hasNext()) {
            val chunk = tagIterator.next()
            val tag = Tag(chunk.first, chunk.second, mutableListOf())
            tags.add(tag)
        }
        return tags

    }
}