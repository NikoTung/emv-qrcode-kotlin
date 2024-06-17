package io.github.nikotung.qrcode.emv.core.mpm

data class Tag(val tag: String, val value: String, val subTags: MutableList<Tag> = mutableListOf()) : TLV {
    override fun tag(): String {
        return tag
    }

    override fun value(): String {
        return value
    }

    override fun toString(): String {
        return String.format(
            "%s%02d%s",
            tag,
            if (subTags.isEmpty()) value.length else subTags.joinToString("").length,
            if (subTags.isEmpty()) value else subTags.joinToString("")
        )
    }

}
