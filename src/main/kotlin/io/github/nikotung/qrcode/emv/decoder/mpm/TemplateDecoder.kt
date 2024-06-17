package io.github.nikotung.qrcode.emv.decoder.mpm

import io.github.nikotung.qrcode.emv.core.mpm.Decoder
import io.github.nikotung.qrcode.emv.core.mpm.Tag
import io.github.nikotung.qrcode.emv.core.mpm.tag_id_length
import io.github.nikotung.qrcode.emv.core.mpm.value_length


class TemplateDecoder(private val source: String, val tag: String) : Decoder {
    override fun decode(): Tag {
        val  value = source.substring(value_length + tag_id_length)
        val tags = ListTagDecoder(value).decode()
        return Tag(tag, source, tags)
    }
}