package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node

class ComponentBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val name = Helper.removePrefix(node.name)
            var code = "{"
            code += "text: { name: \"${node.type()}($name)\" },"

            code += "children: ["

            node.children.forEach {
                code += generator.visit(it)
            }

            code += "]},"

            return code
        }
    }
}