package qrcode.emv.core

class MerchantPresentMode(val tags: MutableMap<String, Tag>) {



    fun add(tag: Tag) {
        tags[tag.tag] = tag
    }

    fun get(id: String): Tag {
        return tags[id] ?: throw IllegalArgumentException("Tag $id not found")
    }

    fun get(id: Int): Tag {
        return get(String.format("%02d", id))
    }

    //todo: sets of getter for all tags & adders

}
