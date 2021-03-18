package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node
import parser.SymbolContext

class IdentifierBuilder {
    companion object: CodeGeneratorBuilder {
        override fun build(node: Node, generator: CodeGenerator): String {
            var code = ""
            val name = Helper.latexEscape(Helper.removePrefix(node.name))

            code += "[.{\\sc ${node.type()}}({${name}}) "

            code += " ] "

            return code
        }

    }
}