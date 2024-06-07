package qrcode.emv.model

import qrcode.emv.core.cpm.BERTLV

class ConsumerPresentMode(val tags: MutableList<BERTLV> = mutableListOf()) {

    fun addTag(tag: BERTLV) {
        tags.add(tag)
    }

}