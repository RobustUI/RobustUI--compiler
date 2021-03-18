package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node

class IdentifierBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val name = Helper.removePrefix(node.name)

            return "{text: { name: \"${node.type()}($name)\" },},"
        }
    }
}