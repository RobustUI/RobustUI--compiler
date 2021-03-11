package codeGenerator.QTreeGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import parser.CompositeComponentNode
import parser.Node
import parser.SimpleComponentNode
import java.lang.Exception

class ComponentBuilder {
     companion object: CodeGeneratorBuilder {
         override fun build(node: Node, generator: CodeGenerator): String {
             val node = when(node){
                 is SimpleComponentNode -> node
                 is CompositeComponentNode -> node
                 else -> throw Exception("ComponentBuilder Exception: Not a component " + node)
             }

             var code = "[."
             val name = Helper.latexEscape(Helper.removePrefix(node.name))
             code += "{\\sc ${node.type()}}({${name}})"

             code += " "

             node.children.forEach {
               code += generator.visit(it)
             }

             code += "]"

             return code;
         }
     }
}