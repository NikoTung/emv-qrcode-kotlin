package qrcode.emv.decoder

import qrcode.emv.core.Decoder
import qrcode.emv.core.Tag
import qrcode.emv.core.tag_id_length
import qrcode.emv.core.value_length

class TemplateDecoder(private val source: String, val tag: String) : Decoder {
    override fun decode(): Tag {
        val  value = source.substring(value_length + tag_id_length)
        val tags = ListTagDecoder(value).decode()
        return Tag(tag, source, tags)
    }
}