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
            var initialCase: CaseNode? = null
            file.openSection("main")
            file.writeln("active [${Helper.convertPrefixToPromelaPrefix(namespace)}_N] proctype ${Helper.convertPrefixToPromelaPrefix(namespace)}Machine() {")
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