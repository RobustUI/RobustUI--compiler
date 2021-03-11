package parser

class Table<K, V>(val name: String? = null): HashMap<K, V>() {

    override fun toString(): String {
        var output = "";
        val leftAlignFormat = "| %-15s | %-4d |%n"
        val headline = name ?: ""
        output += "+-----------------+------+%n"
        output += "| ${headline.padEnd(23)}|%n"
        output += "+-----------------+------+%n"
        entries.forEach {
            output += "| ${it.key.toString().padEnd(15)} | ${it.value.toString().padEnd(4)} |%n"
        }
        output += "+-----------------+------+%n"

        return output;
    }
}