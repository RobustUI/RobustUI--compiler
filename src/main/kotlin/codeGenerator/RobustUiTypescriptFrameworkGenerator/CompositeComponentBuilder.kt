package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.*
import tokens.SimpleComponent

class CompositeComponentBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as CompositeComponentNode
            val name = Helper.removePrefix(node.name)
            var className = name
            if (node.typeLookUpTable.containsKey(node.name)) {
                className = Helper.removePrefix(node.typeLookUpTable.get(node.name)!!)
            }
            generator.addNewStateDeclarationGroupFor(name)
            val file = generator.getCurrentFile()
            file.openSection("topMainClass")
            file.writeln("export class $className extends RobustUICompositeMachine{")
            file.increaseIdentLevel()
            file.writeln("protected machines = new Map<${className}Machine, RobustUI>();")
            file.writeln("constructor() {")
            file.increaseIdentLevel()
            file.writeln("super();")
            node.children.forEach {
                var child = it as ModuleNode
                var label = Helper.removePrefix(child.body.name)
                var isSimpelComp = child.body is SimpleComponentNode
                var className = node.typeLookUpTable[label]!!
                if (isSimpelComp) {
                    file.writeln("this.machines.set(\"$label\", new $className(\"$label\"));")
                } else {
                    file.writeln("this.machines.set(\"$label\", new $className());")
                }
            }
            file.writeln("this.initialize();")
            this.connectCommunication(node.children as List<ModuleNode>, file, node.typeLookUpTable)
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("topMainClass")
            file.openSection("bottomMainClass")
            file.writeln("public registerElement(element: HTMLElement, machine: ${className}Machine): void {")
            file.increaseIdentLevel()
            file.writeln("super.registerElement(element, machine);")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.writeln("public unregisterElement(element: HTMLElement, machine: ${className}Machine): void {")
            file.increaseIdentLevel()
            file.writeln("super.unregisterElement(element, machine);")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.writeln("public onNewConfiguration(): Observable<Configuration[]> {")
            file.increaseIdentLevel()
            file.writeln("return super.onNewConfiguration();")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("bottomMainClass")


            file.openSection("declarations")
            file.write("export type ${className}Machine = ")

            var machines = getMachineDeclarations(node)

            file.writeln(machines)

            file.closeSection("declarations")


            node.children.forEach {
                val child = it as ModuleNode
                val fileName = node.typeLookUpTable[Helper.removePrefix(child.body.name)]!!

                if (!generator.fileExists(fileName)) {
                    child.body.typeLookUpTable.put(child.body.name, fileName)
                    generator.visit(it)
                }
            }
            generator.closeCurrentStateDeclarationGroup()
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

        private fun getMachineDeclarations(node: Node): String {
            var machines = ""

            node.children.forEach {
                var child = it as ModuleNode
                var label = Helper.removePrefix(child.body.name)
                machines += "\"$label\" | "
                machines += travelMachineDeclarations(child.body, label)
            }

            machines = machines.dropLast(3)
            machines += ";"

            return machines
        }

        private fun travelMachineDeclarations(node: Node, namespace: String): String {
            var machines = ""

            if (isAComponent(node)) {
                node.children.forEach {
                    var child = when(it) {
                        is ModuleNode -> it.body
                        is CaseNode -> (it.getComponent() as ModuleNode).body
                        else -> it
                    }
                    if (isAComponent(child)) {
                        var label = Helper.removePrefix(child.name)
                        machines += "\"$namespace::$label\" | "
                        machines += travelMachineDeclarations(child, "$namespace::$label")
                    }
                }
            }

            return machines
        }

        private fun isAComponent(node: Node): Boolean {
            return (node is SimpleComponentNode) || (node is CompositeComponentNode) || (node is SelectiveComponentNode);
        }

    }
}
