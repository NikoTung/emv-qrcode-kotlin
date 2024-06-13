package qrcode.emv.decoder.mpm

import qrcode.emv.core.mpm.ChunkIterator
import qrcode.emv.core.mpm.MerchantPresentModeCode
import qrcode.emv.core.mpm.Tag
import qrcode.emv.model.MerchantPresentMode

class MerchantPresentModeDecode(
    private val source: String,
    private val extraDecoder: MutableMap<String, (String, String) -> Tag> = mutableMapOf()
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