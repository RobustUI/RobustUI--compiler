package codeGenerator.QTreeGenerator

import parser.Parser


class Helper {
    companion object {
        var divider = ""
        fun latexEscape(string: String): String {
            return string.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("&", "\\&")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("~", "\\~")
                .replace("^", "\\^")
        }

        fun removePrefix(string: String): String {
            return string.split(divider).last()
        }
    }
}