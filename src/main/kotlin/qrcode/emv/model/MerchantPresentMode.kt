package qrcode.emv.model

import qrcode.emv.core.mpm.CRC_LENGTH
import qrcode.emv.core.mpm.MerchantPresentModeCode
import qrcode.emv.core.mpm.Tag
import qrcode.emv.exception.InvalidTagException

class MerchantPresentMode(private val tags: MutableMap<String, Tag>) {


    fun add(tag: Tag) {
        tags[tag.tag] = tag
    }

    fun get(id: String): Tag? {
        return tags[id]
    }

    fun get(id: Int): Tag? {
        return get(String.format("%02d", id))
    }




    fun getPayloadFormatIndicator(): Tag? = get(MerchantPresentModeCode.PAYLOAD_FORMAT_INDICATOR.tag.first)

    fun getPointOfInitiationMethod(): Tag? = get(MerchantPresentModeCode.POINT_OF_INITIATION_METHOD.tag.first)

    fun getMerchantAccountInformationReserved(): List<Tag> =
        MerchantPresentModeCode.MERCHANT_ACCOUNT_INFORMATION_RESERVED.tag.mapNotNull { get(it) }


    fun getMerchantAccountInformation(): List<Tag> =
        MerchantPresentModeCode.MERCHANT_ACCOUNT_INFORMATION_TEMPLATE.tag.mapNotNull { get(it) }


    fun getMerchantCategoryCode(): Tag? = get(MerchantPresentModeCode.MERCHANT_CATEGORY_CODE.tag.first)

    fun getTransactionCurrency(): Tag? = get(MerchantPresentModeCode.TRANSACTION_CURRENCY.tag.first)

    fun getTransactionAmount(): Tag? = get(MerchantPresentModeCode.TRANSACTION_AMOUNT.tag.first)

    fun getTipOrConvenienceIndicator(): Tag? = get(MerchantPresentModeCode.TIP_OR_CONVENIENCE_INDICATOR.tag.first)


    fun getValueOfConvenienceFeeFixed(): Tag? = get(MerchantPresentModeCode.VALUE_OF_CONVENIENCE_FEE_FIXED.tag.first)

    fun getValueOfConvenienceFeePercentage(): Tag? =
        get(MerchantPresentModeCode.VALUE_OF_CONVENIENCE_FEE_PERCENTAGE.tag.first)

    fun getCountryCode(): Tag? = get(MerchantPresentModeCode.COUNTRY_CODE.tag.first)

    fun getMerchantName(): Tag? = get(MerchantPresentModeCode.MERCHANT_NAME.tag.first)

    fun getMerchantCity(): Tag? = get(MerchantPresentModeCode.MERCHANT_CITY.tag.first)

    fun getPostalCode(): Tag? = get(MerchantPresentModeCode.POSTAL_CODE.tag.first)

    fun getAdditionalDataFieldTemplate(): Tag? = get(MerchantPresentModeCode.ADDITIONAL_DATA_FIELD_TEMPLATE.tag.first)

    fun getCrc(): Tag? = get(MerchantPresentModeCode.CRC.tag.first)


    fun getMerchantInformationLanguageTemplate(): Tag? =
        get(MerchantPresentModeCode.MERCHANT_INFORMATION_LANGUAGE_TEMPLATE.tag.first)

    fun getRfuForEmvco(): List<Tag> = MerchantPresentModeCode.RFU_FOR_EMVCO.tag.mapNotNull { get(it) }


    fun getUnreservedTemplates(): List<Tag> = MerchantPresentModeCode.UNRESERVED_TEMPLATES.tag.mapNotNull { get(it) }


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
