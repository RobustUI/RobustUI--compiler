package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node
import parser.StateNode
import parser.SymbolContext

class StateBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            val node = node as StateNode

            var name = Helper.removePrefix(node.name)

            val symbolContext: SymbolContext = generator.getSymbolContext(node.name)
            if (symbolContext.initial) {
                name += '*'
            }

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