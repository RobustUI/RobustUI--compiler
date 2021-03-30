package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.IdentifierNode
import parser.Node

class IdentifierBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as IdentifierNode
            val file = generator.getCurrentFile()
            val name = Helper.removePrefix(node.name)

            file.write(node.name)
        }
    }
}
