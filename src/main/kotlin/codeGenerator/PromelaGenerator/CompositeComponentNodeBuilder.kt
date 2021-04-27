package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.CompositeComponentNode
import parser.ModuleNode
import parser.Node
import parser.SimpleComponentNode

class CompositeComponentNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as CompositeComponentNode
            val componentName = if (node.typeLookUpTable.contains(Helper.removePrefixAndWhiteSpace(node.name))) {
                node.typeLookUpTable[Helper.removePrefixAndWhiteSpace(node.name)]
            } else {
                Helper.removePrefixAndWhiteSpace(node.name)
            }
            var namespace = Helper.retrieveRelativeNamespaceForNode(node)
            var path = namespace + Helper.divider  + componentName + Helper.divider;
            if (componentName == namespace) {
                path = namespace + Helper.divider
            }

            node.children.forEach {
                val child = it as ModuleNode
                val fileName = path + node.typeLookUpTable[Helper.removePrefixAndWhiteSpace(child.body.name)]!!

                if (!generator.fileExists(fileName)) {
                    child.body.typeLookUpTable.put(child.body.name, fileName)
                    generator.visit(child)
                } else {
                    generator.incrementFileInstance(fileName)
                }
            }
        }

    }
}