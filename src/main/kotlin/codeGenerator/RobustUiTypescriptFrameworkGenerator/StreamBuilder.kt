package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.StreamNode

class StreamBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as StreamNode
            val name = Helper.removePrefix(node.name)
            val file = generator.getCurrentFile()


            file.write("public $name: StreamDeclaration[] = [")
            if (node.children.size > 0) {
                file.write("\n")
                file.increaseIdentLevel()
                node.children.forEach {
                    file.writeln("{")
                    file.increaseIdentLevel()
                    file.write("stream: \"")
                    generator.visit(it)
                    file.write("\",\n")
                    file.decreaseIdentLevel()
                    file.writeln("},")
                }
                file.decreaseIdentLevel()
            }
            file.writeln("];")
        }
    }
}
