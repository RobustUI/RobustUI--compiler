package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.CaseNode
import parser.Node

class CaseNodeBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val node = node as CaseNode
            var code = ""

            val guard = Helper.latexEscape(node.getGuard().gaurd)
            val stream = Helper.latexEscape(Helper.removePrefix(node.getGuard().stream))

            code += "[."
            code += "{\\sc ${node.type()}}({$stream \$$guard\$}) "

            code += generator.visit(node.getComponent())

            code += "]"
            return code
        }
    }
}