package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.*

class SelectiveComponentNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as SelectiveComponentNode
            val file = generator.getCurrentFile()
            var namespace = Helper.retrieveRelativeNamespaceForNode(node)
            val componentName = if (node.typeLookUpTable.contains(node.name)) {
                Helper.removePrefixAndWhiteSpace(node.typeLookUpTable[node.name]!!)
            } else {
                Helper.removePrefixAndWhiteSpace(node.name)
            }

            var path = namespace + Helper.divider  + componentName
            if (componentName == namespace) {
                path = componentName
            }

            var initialCase: CaseNode? = null
            file.openSection("main")
            file.writeln("active [${Helper.convertPrefixToPromelaPrefix(path)}_N] proctype ${Helper.convertPrefixToPromelaPrefix(path)}Machine() {")
            file.increaseIdentLevel()
            file.writeln("end:")
            file.writeln("if")
            file.increaseIdentLevel()
            node.children.forEach {
                var child = it as CaseNode

                if (!child.isInitial()) {
                    generator.visit(it)
                } else {
                    initialCase = it
                }
            }
            if (initialCase != null) {
                generator.visit(initialCase!!)
            }
            file.writeln("fi")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("main")
        }

    }
}