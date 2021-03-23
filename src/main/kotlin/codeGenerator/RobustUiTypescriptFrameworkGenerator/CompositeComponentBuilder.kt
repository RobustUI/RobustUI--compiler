package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.CompositeComponentNode
import parser.IdentifierNode
import parser.ModuleNode
import parser.Node
import java.awt.event.InputEvent

class CompositeComponentBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as CompositeComponentNode
            val name = Helper.removePrefix(node.name)
            val file = generator.getCurrentFile()
            file.openSection("topMainClass")
            file.writeln("export class $name extends RobustUICompositeMachine{")
            file.increaseIdentLevel()
            file.writeln("protected machines = new Map<${name}Machine, RobustUIMachine>();")
            file.writeln("constructor() {")
            file.increaseIdentLevel()
            file.writeln("super();")
            node.children.forEach {
                var child = it as ModuleNode
                var label = Helper.removePrefix(child.body.name)
                var className = node.typeLookUpTable[label]!!
                file.writeln("this.machines.set(\"$label\", new $className());")
            }
            file.writeln("this.initialize();")
            this.connectCommunication(node.children as List<ModuleNode>, file, node.typeLookUpTable)
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("topMainClass")
            file.openSection("bottomMainClass")
            file.writeln("public registerElement(machine: ${name}Machine, element: HTMLElement): void {")
            file.increaseIdentLevel()
            file.writeln("this.machines.get(machine).registerElement(element);")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("bottomMainClass")


            file.openSection("declarations")
            file.write("export type ${name}Machine = ")

            var machines = ""
            node.children.forEach {
                var child = it as ModuleNode
                var label = Helper.removePrefix(child.body.name)
                machines += "\"$label\" | "
            }
            machines = machines.dropLast(3)
            machines += ";"
            file.writeln(machines)

            file.closeSection("declarations")
        }

        private fun  connectCommunication(children: List<ModuleNode>, file: OutputFile, lookUpTable: MutableMap<String, String>) {
            children.forEach { sender ->
                sender.outputStreamNode.children.forEach { outputEvent ->
                    children.forEach { receiver ->
                        receiver.inputStreamNode.children.forEach { inputEvent ->
                            val output = outputEvent as IdentifierNode
                            val input = inputEvent as IdentifierNode
                            val outputName = Helper.removePrefix(output.name).toLowerCase()
                            val inputName = Helper.removePrefix(input.name).toLowerCase()
                            if (outputName == inputName) {
                                val senderName = Helper.removePrefix(sender.body.name)
                                val receiverName = Helper.removePrefix(receiver.body.name)
                                file.writeln("(this.machines.get('$senderName') as ${lookUpTable[senderName]}).getOutputStream('$outputName').subscribe(_ => {")
                                file.increaseIdentLevel()
                                file.writeln("this.machines.get('$receiverName').sendInput('$inputName');")
                                file.decreaseIdentLevel()
                                file.writeln("});")
                            }
                        }
                    }
                }
            }
        }
    }
}
