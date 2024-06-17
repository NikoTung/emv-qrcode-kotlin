package io.github.nikotung.qrcode.emv.decoder.mpm

import io.github.nikotung.qrcode.emv.core.mpm.ChunkIterator
import io.github.nikotung.qrcode.emv.core.mpm.MerchantPresentModeCode
import io.github.nikotung.qrcode.emv.core.mpm.Tag
import io.github.nikotung.qrcode.emv.model.MerchantPresentMode


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