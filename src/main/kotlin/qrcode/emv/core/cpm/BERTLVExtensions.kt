package qrcode.emv.core.cpm

import ConsumerPresentModeCode

fun BERTLV.applicationDefinitionFileName(): BERTLV {
    return subTags().first {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.APPLICATION_DEFINITION_FILE_NAME.tag)
    }
}

fun BERTLV.track2EquivalentData(): BERTLV? {
    return subTags().firstOrNull {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.TRACK_2_EQUIVALENT_DATA.tag)
    }
}

fun BERTLV.applicationPAN(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.APPLICATION_PAN.tag)
    }
}

fun BERTLV.cardholderName(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.CARDHOLDER_NAME.tag)
    }
}

fun BERTLV.languagePreference(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.LANGUAGE_PREFERENCE.tag)
    }
}

fun BERTLV.applicationLabel(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.APPLICATION_LABEL.tag)
    }
}

fun BERTLV.issuerURL(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.ISSUER_URL.tag)
    }
}

fun BERTLV.applicationVersion(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.APPLICATION_VERSION.tag)
    }
}

fun BERTLV.tokenRequestorID(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.TOKEN_REQUESTOR_ID.tag)
    }
}

fun BERTLV.paymentAccountReference(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.PAYMENT_ACCOUNT_REFERENCE.tag)
    }
}

fun BERTLV.last4DigitsOfPAN(): BERTLV? {
    return subTags().firstOrNull() {
        it.tag().tag.contentEquals(ConsumerPresentModeCode.LAST_4_DIGITS_OF_PAN.tag)
    }
}


fun BERTLV.tagClass() = TagClass.entries.first { it.i == byteArrayTag()[0].toInt() and CLASS_BITMASK }

fun BERTLV.dataType(): DataType = DataType.entries.first { it.i == (byteArrayTag()[0].toInt() and TYPE_BITMASK) }