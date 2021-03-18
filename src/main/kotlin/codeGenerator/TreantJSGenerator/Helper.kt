package codeGenerator.TreantJSGenerator

class Helper {
    companion object {
        var divider = ""

        fun removePrefix(string: String): String {
            return string.split(divider).last()
        }
    }
}