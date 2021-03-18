package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node
import parser.TransitionNode

class TransitionBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val node = node as TransitionNode

            val label = Helper.removePrefix(node.name)
            val from = Helper.removePrefix(node.from)
            val to = Helper.removePrefix(node.to)

            return "{text: { name: \"${node.type()}($from, $label, $to)\" },},"
        }
    }
}