package qrcode.emv.model

import qrcode.emv.core.mpm.CRC_LENGTH
import qrcode.emv.core.mpm.MerchantPresentModeCode
import qrcode.emv.core.mpm.Tag
import qrcode.emv.exception.InvalidTagException

class MerchantPresentMode(val tags: MutableMap<String, Tag>) {


    fun add(tag: Tag) {
        tags[tag.tag] = tag
    }

    fun get(id: String): Tag {
        return tags[id] ?: throw InvalidTagException("Tag $id not found")
    }

    fun get(id: Int): Tag {
        return get(String.format("%02d", id))
    }


    fun getPayloadFormatIndicator(): Tag {
        return get(MerchantPresentModeCode.PAYLOAD_FORMAT_INDICATOR.tag.first)
    }

    fun getPointOfInitiationMethod(): Tag {
        return get(MerchantPresentModeCode.POINT_OF_INITIATION_METHOD.tag.first)
    }

    fun getMerchantAccountInformationReserved(): List<Tag> {
        return MerchantPresentModeCode.MERCHANT_ACCOUNT_INFORMATION_RESERVED.tag.mapNotNull { get(it) }
    }

    fun getMerchantAccountInformation(): List<Tag> {
        return MerchantPresentModeCode.MERCHANT_ACCOUNT_INFORMATION_TEMPLATE.tag.mapNotNull { get(it) }
    }

    fun getMerchantCategoryCode(): Tag {
        return get(MerchantPresentModeCode.MERCHANT_CATEGORY_CODE.tag.first)
    }

    fun getTransactionCurrency(): Tag {
        return get(MerchantPresentModeCode.TRANSACTION_CURRENCY.tag.first)
    }

    fun getTransactionAmount(): Tag {
        return get(MerchantPresentModeCode.TRANSACTION_AMOUNT.tag.first)
    }

    fun getTipOrConvenienceIndicator(): Tag {
        return get(MerchantPresentModeCode.TIP_OR_CONVENIENCE_INDICATOR.tag.first)
    }

    fun getValueOfConvenienceFeeFixed(): Tag {
        return get(MerchantPresentModeCode.VALUE_OF_CONVENIENCE_FEE_FIXED.tag.first)
    }

    fun getValueOfConvenienceFeePercentage(): Tag {
        return get(MerchantPresentModeCode.VALUE_OF_CONVENIENCE_FEE_PERCENTAGE.tag.first)
    }

    fun getCountryCode(): Tag {
        return get(MerchantPresentModeCode.COUNTRY_CODE.tag.first)
    }

    fun getMerchantName(): Tag {
        return get(MerchantPresentModeCode.MERCHANT_NAME.tag.first)
    }

    fun getMerchantCity(): Tag {
        return get(MerchantPresentModeCode.MERCHANT_CITY.tag.first)
    }

    fun getPostalCode(): Tag {
        return get(MerchantPresentModeCode.POSTAL_CODE.tag.first)
    }

    fun getAdditionalDataFieldTemplate(): Tag {
        return get(MerchantPresentModeCode.ADDITIONAL_DATA_FIELD_TEMPLATE.tag.first)
    }

    fun getCrc(): Tag {
        return get(MerchantPresentModeCode.CRC.tag.first)
    }

    fun getMerchantInformationLanguageTemplate(): Tag {
        return get(MerchantPresentModeCode.MERCHANT_INFORMATION_LANGUAGE_TEMPLATE.tag.first)
    }

    fun getRfuForEmvco(): List<Tag> {
        return MerchantPresentModeCode.RFU_FOR_EMVCO.tag.mapNotNull { get(it) }
    }

    fun getUnreservedTemplates(): List<Tag> {
        return MerchantPresentModeCode.UNRESERVED_TEMPLATES.tag.mapNotNull { get(it) }
    }


    override fun toString(): String {
        val stringWithoutCrcChecksum = tags.toSortedMap().entries
            .filter { !MerchantPresentModeCode.CRC.tag.contains(Integer.parseInt(it.key)) }
            .joinToString("") {
                it.value.toString()
            }

        val s = stringWithoutCrcChecksum + MerchantPresentModeCode.CRC.tag.first + CRC_LENGTH
        val crc = s.crc16()
        return s + String.format("%04X", crc).uppercase()
    }

}
