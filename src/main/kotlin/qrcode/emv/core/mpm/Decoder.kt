package qrcode.emv.core

import qrcode.emv.core.Tag

interface Decoder {
    fun decode(): TLV
}