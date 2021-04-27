package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.CaseNode
import parser.ModuleNode
import parser.Node

class CaseNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as CaseNode
            var file = generator.getCurrentFile()
            var namespace = Helper.retrieveRelativeNamespaceForNode(node)
            var chanName = Helper.convertPrefixToPromelaPrefix(namespace + Helper.divider + node.getGuard().stream)
            if (node.getGuard().gaurd.toLowerCase() == "default") {
                file.writeln(":: else -> $chanName++;")
            } else {
                file.writeln(":: $chanName ${node.getGuard().gaurd} -> $chanName++;")
            }

            val child = node.getComponent() as ModuleNode
            val fileName = node.parent!!.typeLookUpTable[Helper.removePrefixAndWhiteSpace(child.body.name)]!!

            if (!generator.fileExists(fileName)) {
                child.body.typeLookUpTable.put(child.body.name, fileName)
                generator.visit(child)
            }
        }

    }
}