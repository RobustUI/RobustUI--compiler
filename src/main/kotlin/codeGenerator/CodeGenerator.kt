package codeGenerator

import parser.Node
import java.io.File
import javax.swing.text.html.parser.Parser

interface CodeGenerator {
    fun generate(): String
    fun visit(node: Node): String
}