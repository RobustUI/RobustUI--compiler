package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.ModuleNode
import parser.Node

class ModuleBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            var node = node as ModuleNode

            val name = Helper.removePrefix(node.name)
            var code = "{"
            code += "text: { name: \"${node.type()}($name)\" },"

            code += "children: ["

            code += generator.visit(node.inputStreamNode)
            code += generator.visit(node.outputStreamNode)

            if (node.eventStreamNode != null) {
                code += generator.visit(node.eventStreamNode!!)
            }

            code += generator.visit(node.body)

            code += "]},"

            return code
        }
    }
}