package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.*

class SelectiveComponentBuilder {
    companion object: CodeGeneratorFileBuilder  {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as SelectiveComponentNode
            val name = Helper.removePrefixAndWhiteSpace(Helper.removeWhitespace(node.name))
            var className = name
            if (node.typeLookUpTable.containsKey(node.name)) {
                className = Helper.removePrefixAndWhiteSpace(node.typeLookUpTable.get(node.name)!!)
            }
            generator.addNewStateDeclarationGroupFor(name)
            val file = generator.getCurrentFile()
            file.openSection("topMainClass")
            file.writeln("export class $className extends RobustUISelectiveMachine{")
            file.increaseIdentLevel()
            file.writeln("protected machines = new Map<${className}Machines, RobustUI>();")
            file.writeln("constructor() {")
            file.increaseIdentLevel()
            file.writeln("super();")
            node.children.forEach {
                var child = it as CaseNode
                var label = Helper.removePrefixAndWhiteSpace((child.getComponent() as ModuleNode).body.name)
                var isSimpelComp = (child.getComponent() as ModuleNode).body is SimpleComponentNode
                var className = node.typeLookUpTable[label]!!
                if (isSimpelComp) {
                    file.writeln("this.machines.set(\"$label\", new $className(\"$label\"));")
                } else {
                    file.writeln("this.machines.set(\"$label\", new $className());")
                }
            }
            file.writeln("this.initialize();")
            file.writeln("this.inputStream.subscribe(value => {")
            file.increaseIdentLevel()
            val numOfCases = node.children.size
            var currentCase = 0
            var initialCase: CaseNode? = null
            node.children.forEach {
                currentCase++
                var child = it as CaseNode
                var label = Helper.removePrefixAndWhiteSpace((child.getComponent() as ModuleNode).body.name)
                if (!child.isInitial()) {
                    file.writeln("if (value ${child.getGuard().gaurd}) {")
                    file.increaseIdentLevel()
                    file.writeln("this.switchMachine('$label')")
                    file.decreaseIdentLevel()
                    if (currentCase < numOfCases - 1) {
                        file.write("} else ")
                    } else {
                        file.writeln("}")
                    }
                } else {
                    initialCase = child
                }
            }
            if (initialCase != null) {
                var label = Helper.removePrefixAndWhiteSpace((initialCase!!.getComponent() as ModuleNode).body.name)

                file.writeln(" else {")
                file.writeln("this.switchMachine('${label}')")
                file.writeln("}")
            }
            file.decreaseIdentLevel()
            file.writeln("});")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("topMainClass")

            file.openSection("bottomMainClass")

            file.writeln("public get onMachineSwitch(): Observable<${className}Machines> {")
            file.increaseIdentLevel()
            file.writeln("return this.machineSwitchStream as Observable<${className}Machines>");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.writeln("public onNewConfiguration(machine: ${className}Machines): Observable<Configuration[]> {")
            file.increaseIdentLevel()
            file.writeln("return super.onNewConfiguration(machine)");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.writeln("public getOutputStream(name: string): Observable<any> {")
            file.increaseIdentLevel()
            file.writeln("return super.getOutputStream(name);");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("bottomMainClass")


            file.openSection("declarations")
            file.write("export type ${className}Machines = ")


            file.writeln(getMachineDeclarations(node))

            file.closeSection("declarations")

            node.children.forEach {
                val child = (it as CaseNode).getComponent() as ModuleNode
                val fileName = node.typeLookUpTable[Helper.removePrefixAndWhiteSpace(child.body.name)]!!

                if (!generator.fileExists(fileName)) {
                    child.body.typeLookUpTable.put(child.body.name, fileName)
                    generator.visit(child)
                }
            }

            generator.closeCurrentStateDeclarationGroup()
        }

        private fun getMachineDeclarations(node: Node): String {
            var machines = ""

            node.children.forEach {
                var child = it as CaseNode
                var label = Helper.removePrefixAndWhiteSpace((child.getComponent() as ModuleNode).body.name)
                machines += "\"$label\" | "
                machines += travelMachineDeclarations((child.getComponent() as ModuleNode).body, label)
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
                        var label = Helper.removePrefixAndWhiteSpace(child.name)
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