package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import parser.*
import java.lang.Exception

class QTreeGenerator(val parser: Parser): CodeGenerator {

    init {
        Helper.divider = parser.nameDivider
    }

    override fun generate(): String {
        var file = "\\Tree"
        file += visit(parser.root)

        return file
    }

    override fun visit(node: Node): String {
        return when(node) {
            is SimpleComponentNode -> ComponentBuilder.build(node, this)
            is CompositeComponentNode -> ComponentBuilder.build(node, this)
            is StateNode -> StateBuilder.build(node, this)
            is TransitionNode -> TransitionBuilder.build(node, this)
            else -> throw Exception("QTreeGenerator Exception: Could not code generate: $node")
        }
    }
}