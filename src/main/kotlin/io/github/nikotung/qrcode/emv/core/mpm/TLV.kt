package io.github.nikotung.qrcode.emv.core.mpm

interface TLV {

    fun tag(): String

    fun value(): String
    fun length(): Int {
        return value().length
    }
}