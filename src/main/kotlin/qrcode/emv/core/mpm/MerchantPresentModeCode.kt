package qrcode.emv.core.mpm

enum class MerchantPresentModeCode(val tag: IntRange, val mandatory: Boolean, val template: Boolean) {

    PAYLOAD_FORMAT_INDICATOR(0..0, true, false),
    POINT_OF_INITIATION_METHOD(1..1, false, false),

    //“02”-“03” Reserved for Visa
    //“04”-“05” Reserved for Mastercard
    //“06”-“08” Reserved by EMVCo
    //“09”-“10” Reserved for Discover
    //“11”-“12” Reserved for Amex
    //“13”-“14” Reserved for JCB
    //“15”-“16” Reserved for UnionPay
    //“17”-“25” Reserved by EMVCo
    MERCHANT_ACCOUNT_INFORMATION_RESERVED(2..25, true, false),

    MERCHANT_ACCOUNT_INFORMATION_TEMPLATE(26..51, true, true),
    MERCHANT_CATEGORY_CODE(52..52, true, false),
    TRANSACTION_CURRENCY(53..53, true, false),
    TRANSACTION_AMOUNT(54..54, false, false),
    TIP_OR_CONVENIENCE_INDICATOR(55..55, false, false),
    VALUE_OF_CONVENIENCE_FEE_FIXED(56..56, false, false),
    VALUE_OF_CONVENIENCE_FEE_PERCENTAGE(57..57, false, false),
    COUNTRY_CODE(58..58, true, false),
    MERCHANT_NAME(59..59, true, false),
    MERCHANT_CITY(60..60, true, false),
    POSTAL_CODE(61..61, false, false),
    ADDITIONAL_DATA_FIELD_TEMPLATE(62..62, false, true),
    CRC(63..63, true, false),
    MERCHANT_INFORMATION_LANGUAGE_TEMPLATE(64..64, false, true),
    RFU_FOR_EMVCO(65..79, false, false),
    UNRESERVED_TEMPLATES(80..99, false, true);


    companion object {

        fun valueBy(tag: String): MerchantPresentModeCode {
            return entries.first { it.tag.contains(Integer.parseInt(tag)) }
        }

        fun valueBy(tag: Int): MerchantPresentModeCode {
            return entries.first { it.tag.contains(tag) }
        }
    }


}