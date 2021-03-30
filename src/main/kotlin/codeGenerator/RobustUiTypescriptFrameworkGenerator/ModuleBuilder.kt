package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.ModuleNode
import parser.Node

class ModuleBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as ModuleNode

            var file = generator.createNewFile(node.name)

            generator.visit(node.body)

            file.openSection("middleMainClass")

            generator.visit(node.outputStreamNode)
            generator.visit(node.inputStreamNode)

            if (node.eventStreamNode != null) {
                generator.visit(node.eventStreamNode!!)
            } else {
                file.writeln("public events: StreamDeclaration[] = [];")
            }

            file.closeSection("middleMainClass")

            generator.closeCurrentFile()
        }
    }
}
