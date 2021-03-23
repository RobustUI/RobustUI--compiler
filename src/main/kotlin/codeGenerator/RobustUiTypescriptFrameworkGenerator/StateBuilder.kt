package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.StateNode

class StateBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as StateNode;
            var name = Helper.removePrefix(node.name)
            val file = generator.getCurrentFile()

            generator.states.add(name)

            file.writeln("{")
            file.increaseIdentLevel()
            file.writeln("name: '$name',")
            file.writeln("transitions: [")
            file.increaseIdentLevel()
            node.children.forEach {
                generator.visit(it)
            }
            file.decreaseIdentLevel()
            file.writeln("]")
            file.decreaseIdentLevel()
            file.writeln("},")
        }
    }
}
