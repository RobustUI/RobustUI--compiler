package tokens


import parser.Node
import parser.Parser

abstract class Token {
    abstract val name: String
    abstract fun rename(name: String): Token
    abstract fun accept(parser: Parser): Node
}