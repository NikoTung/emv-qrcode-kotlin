package qrcode.emv.core.mpm

interface Decoder {
    fun decode(): TLV
}