package codeGenerator.PromelaGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.StateNode

class StateNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as StateNode
            var name = Helper.removePrefixAndWhiteSpace(node.name)
            val file = generator.getCurrentFile()
            var namespace = Helper.retrieveRelativeNamespaceForNode(node)
            generator.addStateDeclaration(Helper.removePrefixAndWhiteSpace(node.name))

            file.writeln(":: (CurrentState == ${Helper.convertPrefixToPromelaPrefix(namespace.toLowerCase()+Helper.divider+name)}) ->")
            file.increaseIdentLevel()
            node.children.forEach {
                generator.visit(it)
            }
            file.decreaseIdentLevel()
        }

    }
}