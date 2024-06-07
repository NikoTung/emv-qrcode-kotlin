package qrcode.emv.core

interface TLV {

    fun tag(): String

    fun value(): String
    fun length(): Int {
        return value().length
    }
}