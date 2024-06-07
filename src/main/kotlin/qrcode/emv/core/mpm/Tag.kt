package qrcode.emv.core

data class Tag(val tag: String, val value: String, val subTags: MutableList<Tag>) : TLV{
    override fun tag(): String {
        return tag
    }

    override fun value(): String {
        return value
    }

    override fun toString(): String {
        if (subTags.isEmpty()) {
            return String.format("%s%02d%s", tag, value.length, value)
        } else {
            val sb = StringBuilder()
            for (subTag in subTags) {
                sb.append(subTag.toString())
            }
            return String.format("%s%02d%s", tag, sb.length, sb.toString())
        }
    }

}
