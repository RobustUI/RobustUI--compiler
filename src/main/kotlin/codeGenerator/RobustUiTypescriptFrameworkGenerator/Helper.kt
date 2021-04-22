package codeGenerator.RobustUiTypescriptFrameworkGenerator

class Helper {
    companion object {
        var divider = ""

        fun removePrefixAndWhiteSpace(string: String): String {
            return this.removeWhitespace(this.removePrefix(string))
        }

        fun removePrefix(string: String): String {
            return string.split(divider).last()
        }

        fun removeWhitespace(string: String): String {
            return string.replace(" ", "")
        }
    }
}