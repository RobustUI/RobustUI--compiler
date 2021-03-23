package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.TransitionNode

class TransitionBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as TransitionNode;

            val target = Helper.removePrefix(node.to)
            val label = Helper.removePrefix(node.name)

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
