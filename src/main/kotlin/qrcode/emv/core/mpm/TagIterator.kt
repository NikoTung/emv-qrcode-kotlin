package qrcode.emv.core.mpm

class TagIterator(private var source: String) : Iterator<Pair<String, String>>{


    private var current: Int = 0
    private val length = source.length;

    private fun nextChuck(): Pair<String, String> {

        var endIndex = current + tag_id_length
        var startIndex = current
        val tag = source.substring(startIndex, endIndex)
        startIndex = endIndex
        endIndex += value_length
        val valueLength = source.substring(startIndex, endIndex)
        startIndex = endIndex
        endIndex += valueLength.toInt()
        val value = source.substring(startIndex, endIndex)
        current = endIndex
        return Pair(tag, value)
    }

    override fun hasNext(): Boolean {
        return current + tag_id_length + value_length <= length
    }

    override fun next(): Pair<String, String> {
        return nextChuck()
    }
}