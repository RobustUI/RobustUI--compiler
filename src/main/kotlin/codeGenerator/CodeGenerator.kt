package codeGenerator

import parser.Node
import parser.SymbolContext
import parser.Table

interface CodeGenerator {
    fun getSymbolContext(nodeName: String): SymbolContext
    fun generate(): String
    fun visit(node: Node): String
}