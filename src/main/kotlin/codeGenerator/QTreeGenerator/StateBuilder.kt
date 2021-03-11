package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node
import parser.StateNode


class StateBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val node = node as StateNode

            var code = ""
            val name = Helper.latexEscape(Helper.removePrefix(node.name))
            code += "[.{\\sc ${node.type()}}({${name}}) "

            node.children.forEach {
                code += generator.visit(it)
            }

            code += " ] "

            return code;
        }
    }
}