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
            val name = Helper.removePrefixAndWhiteSpace(node.name)
            var className = name
            if (node.typeLookUpTable.containsKey(node.name)) {
                className = Helper.removePrefixAndWhiteSpace(node.typeLookUpTable.get(node.name)!!)
            }
            generator.addNewStateDeclarationGroupFor(name)
            val initialState = Helper.removePrefixAndWhiteSpace(generator.getInitialState(name))
            val file = generator.getCurrentFile()
            generator.getMessageTableFor(node.name, "EventMessage")
            file.openSection("topMainClass")
            file.writeln("export class $className extends RobustUIMachine{")
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

            events = events.map { Helper.removePrefixAndWhiteSpace(it).split('/')[0] }.distinct()

            file.writeln(MainClassExtraBody.generate(className, events))

            file.writeln("}")
            file.closeSection("bottomMainClass")

            file.openSection("declarations")
            file.write("export type ${className}State = ")
            var states = ""

            generator.states.get(name)!!.forEach {
                states +="\"$it\" | "
            }
            states = states.dropLast(3)
            states += ";"
            file.writeln(states)

            file.write("export type ${className}OutputStreams = ")


            var outputsMessages: List<String> = generator.getMessageTableFor(node.name, "OutputMessage")

            var outputs = ""
            if (outputsMessages.isNotEmpty()) {
                outputsMessages.forEach {
                    outputs += "\"${Helper.removePrefixAndWhiteSpace(it)}\" | "
                }
                outputs = outputs.dropLast(3)
            } else {
                outputs += "\"\""
            }

            outputs += ";"
            file.writeln(outputs)
            file.closeSection("declarations")
            generator.closeCurrentStateDeclarationGroup()
        }
    }
}
