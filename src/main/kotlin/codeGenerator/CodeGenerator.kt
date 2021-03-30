package codeGenerator

import parser.Node
import parser.SymbolContext

interface CodeGenerator {
    fun getSymbolContext(nodeName: String): SymbolContext
    fun generate(): String
    fun visit(node: Node): String
}