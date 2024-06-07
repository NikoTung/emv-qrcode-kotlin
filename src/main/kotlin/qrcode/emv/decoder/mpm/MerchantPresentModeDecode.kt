package qrcode.emv.decoder

import qrcode.emv.core.ChunkIterator
import qrcode.emv.core.MerchantPresentModeCode
import qrcode.emv.core.Tag
import qrcode.emv.model.MerchantPresentMode

class MerchantPresentModeDecode(
    private val source: String,
    private val extraDecoder: Map<String, (String, String) -> Tag> = mapOf()
) {

    fun decode(): MerchantPresentMode {

        val mode = MerchantPresentMode(mutableMapOf())
        ChunkIterator(source).forEach { chunk ->
            val modeCode = MerchantPresentModeCode.valueBy(chunk.first)
            val tag = extraDecoder[chunk.first]?.invoke(chunk.first, chunk.second)
                ?: if (modeCode.template) TemplateDecoder(chunk.second, chunk.first).decode()
                else TagDecoder(chunk.second).decode()
            mode.add(tag)
        }
        return mode
    }
}