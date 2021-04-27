package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.IdentifierNode
import parser.Node

class IdentifierNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var file = generator.getCurrentFile()
            var node = node as IdentifierNode
            var name = node.name
            if (node.name.contains("/")) {
                name = node.name.split('/')[0]
            }

            file.write(Helper.removeWhitespace(name))
        }
    }
}