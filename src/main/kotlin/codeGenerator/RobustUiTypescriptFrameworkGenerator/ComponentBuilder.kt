package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import codeGenerator.RobustUiTypescriptFrameworkGenerator.boilerplates.MainClassExtraBody
import parser.Node
import parser.SimpleComponentNode

class ComponentBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as SimpleComponentNode
            val name = Helper.removePrefix(node.name)
            val initialState = Helper.removePrefix(generator.getInitialState())
            val file = generator.getCurrentFile()
            generator.getMessageTableFor(node.name, "EventMessage")
            file.openSection("topMainClass")
            file.writeln("export class $name extends RobustUIMachine{")
            file.writeln("private states: StateDeclaration[] = [")
            file.increaseIdentLevel()
            node.children.forEach {
                generator.visit(it)
            }
            file.decreaseIdentLevel()
            file.writeln("];")
            file.writeln("private initialState = '$initialState'")
            file.closeSection("topMainClass")

            file.openSection("bottomMainClass")
            var events: List<String> = generator.getMessageTableFor(node.name, "EventMessage")

            events = events.map { Helper.removePrefix(it).split('/')[0] }

            file.writeln(MainClassExtraBody.generate(name, events))

            file.writeln("}")
            file.closeSection("bottomMainClass")

            file.openSection("declarations")
            file.write("export type ${name}State = ")
            var states = ""
            generator.states.forEach {
                states +="\"$it\" | "
            }
            states = states.dropLast(3)
            states += ";"
            file.writeln(states)

            file.write("export type ${name}OutputStreams = ")


            var outputsMessages: List<String> = generator.getMessageTableFor(node.name, "OutputMessage")

            var outputs = ""
            if (outputsMessages.isNotEmpty()) {
                outputsMessages.forEach {
                    outputs += "\"${Helper.removePrefix(it)}\" | "
                }
                outputs = outputs.dropLast(3)
            } else {
                outputs += "\"\""
            }

            outputs += ";"
            file.writeln(outputs)
            file.closeSection("declarations")
        }
    }
}
