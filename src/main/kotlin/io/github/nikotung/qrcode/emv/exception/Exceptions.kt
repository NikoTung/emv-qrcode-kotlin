package io.github.nikotung.qrcode.emv.exception

class DecodeException(message: String) : RuntimeException(message)

class InvalidTagException(s: String) : IllegalArgumentException(s)