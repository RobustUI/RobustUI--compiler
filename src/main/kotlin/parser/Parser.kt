package parser

import tokens.Token

data class SymbolContext (val node: Node, val initial: Boolean)

class Parser(val tokens: Map<String, Token>) {
    val symbolTable: Table<String, SymbolContext> = Table("Symbol Table")
    val messageTable: Table<String, String> = Table("Message Table")
    val nameDivider  = "@_@_##"
        get() = field
    var prefix: String = ""
    var root: Node

    init {
        root = parse(tokens.values.first())
    }

    fun parse (element: Token): Node {
      return element.accept(this)
    }

    fun getTokenFor(name: String): Token? {
        return tokens[name]
    }

    fun registerInputs(inputs: List<String>) {
        inputs.forEach {
            messageTable[addPrefix(it)] = "InputMessage"
        }
    }

    fun registerOutputs(outputs: List<String>) {
        outputs.forEach {
            messageTable[addPrefix(it)] = "OutputMessage"
        }
    }

    fun registerEvents(events: List<String>) {
        events.forEach {
            messageTable[addPrefix(it)] = "EventMessage"
        }
    }

    fun addPrefix(target: String): String {
        return prefix + nameDivider + target
    }
}

