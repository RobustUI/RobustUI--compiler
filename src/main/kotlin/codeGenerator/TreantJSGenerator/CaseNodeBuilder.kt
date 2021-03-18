package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.CaseNode
import parser.Node

class CaseNodeBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val node = node as CaseNode
            var code = "{"

            val guard = node.getGuard().gaurd
            val stream = Helper.removePrefix(node.getGuard().stream)
            code += "text: { name: \"${node.type()}($stream $guard)\" },"
            code += "children: ["

            code += generator.visit(node.getComponent())

            code += "]},"
            return code
        }
    }
}