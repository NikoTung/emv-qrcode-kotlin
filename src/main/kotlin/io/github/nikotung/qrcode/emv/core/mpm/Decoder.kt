package io.github.nikotung.qrcode.emv.core.mpm

interface Decoder {
    fun decode(): TLV
}