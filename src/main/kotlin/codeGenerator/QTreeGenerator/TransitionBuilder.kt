package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.Node
import parser.TransitionNode

class TransitionBuilder {
   companion object: CodeGeneratorBuilder {
       override fun build(node: Node, generator: CodeGenerator): String {
           val node = node as TransitionNode
           var code = ""

           val from = Helper.latexEscape(Helper.removePrefix(node.from))
           val label = Helper.latexEscape(Helper.removePrefix(node.name))
           val to = Helper.latexEscape(Helper.removePrefix(node.to))

           code += "{\\sc ${node.type()}}({$from, $label, $to})"
           return code
       }

   }
}