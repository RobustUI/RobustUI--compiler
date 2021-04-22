package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.TransitionNode

class TransitionBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as TransitionNode;

            val target = Helper.removePrefixAndWhiteSpace(node.to)
            val label = Helper.removePrefixAndWhiteSpace(node.name)

            val file = generator.getCurrentFile()

            file.writeln("{")
            file.increaseIdentLevel()
            file.writeln("target: '$target',")
            file.writeln("label: '$label',")
            file.decreaseIdentLevel()
            file.writeln("},")
        }
    }
}
