package qrcode.emv.decoder.mpm

import qrcode.emv.core.mpm.Decoder
import qrcode.emv.core.mpm.Tag
import qrcode.emv.core.mpm.tag_id_length
import qrcode.emv.core.mpm.value_length

class TemplateDecoder(private val source: String, val tag: String) : Decoder {
    override fun decode(): Tag {
        val  value = source.substring(value_length + tag_id_length)
        val tags = ListTagDecoder(value).decode()
        return Tag(tag, source, tags)
    }
}