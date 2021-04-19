package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.CaseNode
import parser.ModuleNode
import parser.Node
import parser.SelectiveComponentNode

class SelectiveComponentBuilder {
    companion object: CodeGeneratorFileBuilder  {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as SelectiveComponentNode
            val name = Helper.removePrefix(Helper.removeWhitespace(node.name))
            generator.addNewStateDeclarationGroupFor(name)
            val file = generator.getCurrentFile()
            file.openSection("topMainClass")
            file.writeln("export class $name extends RobustUISelectiveMachine{")
            file.increaseIdentLevel()
            file.writeln("protected machines = new Map<${name}Machines, RobustUI>();")
            file.writeln("constructor() {")
            file.increaseIdentLevel()
            file.writeln("super();")
            node.children.forEach {
                var child = it as CaseNode
                var label = Helper.removePrefix((child.getComponent() as ModuleNode).body.name)
                var className = node.typeLookUpTable[label]!!
                file.writeln("this.machines.set(\"$label\", new $className());")
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
                var label = Helper.removePrefix((child.getComponent() as ModuleNode).body.name)
                if (!child.isInitial()) {
                    file.writeln("if (value ${child.getGuard().gaurd}) {")
                    file.increaseIdentLevel()
                    file.writeln("this.switchMachine('$label')")
                    file.decreaseIdentLevel()
                    if (currentCase < numOfCases) {
                        file.write("} else ")
                    } else {
                        file.writeln("}")
                    }
                } else {
                    initialCase = child
                }
            }
            if (initialCase != null) {
                file.writeln(" else {")
                file.writeln("this.switchMachine('${initialCase!!.getComponent().name}')")
                file.writeln("}")
            }
            file.decreaseIdentLevel()
            file.writeln("});")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("topMainClass")

            file.openSection("bottomMainClass")

            file.writeln("public get onMachineSwitch(): Observable<${name}Machines> {")
            file.increaseIdentLevel()
            file.writeln("return this.machineSwitchStream as Observable<${name}Machines>");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.writeln("public onNewConfiguration(machine: ${name}Machines): Observable<string | Map<string, string>> {")
            file.increaseIdentLevel()
            file.writeln("return super.onNewConfiguration(machine)");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.writeln("public getOutputStream(name: string): Observable<any> {")
            file.increaseIdentLevel()
            file.writeln("return super.getOutputStream(name);");
            file.decreaseIdentLevel()
            file.writeln("}")

            file.writeln("public registerElement(element: HTMLElement, machineName: ${name}Machines): void {")
            file.increaseIdentLevel()
            file.writeln("super.registerElement(element, machineName);")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.decreaseIdentLevel()
            file.writeln("}")
            file.closeSection("bottomMainClass")


            file.openSection("declarations")
            file.write("export type ${name}Machines = ")

            var machines = ""
            node.children.forEach {
                var child = it as CaseNode
                var label = Helper.removePrefix((child.getComponent() as ModuleNode).body.name)
                machines += "\"$label\" | "
            }
            machines = machines.dropLast(3)
            machines += ";"
            file.writeln(machines)

            file.closeSection("declarations")

            node.children.forEach {
                val child = (it as CaseNode).getComponent() as ModuleNode
                val fileName = node.typeLookUpTable[Helper.removePrefix(child.body.name)]!!

                if (!generator.fileExists(fileName)) {
                    child.body.typeLookUpTable.put(child.body.name, fileName)
                    generator.visit(child)
                }
            }

            generator.closeCurrentStateDeclarationGroup()
        }

    }
}