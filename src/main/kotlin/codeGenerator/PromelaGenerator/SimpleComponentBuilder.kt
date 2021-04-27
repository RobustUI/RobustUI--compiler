package codeGenerator.PromelaGenerator

import codeGenerator.*
import parser.Node
import parser.SimpleComponentNode

class SimpleComponentBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as SimpleComponentNode
            val file = generator.getCurrentFile()
            var name = Helper.removePrefixAndWhiteSpace(node.name)
            var className = name
            if (node.typeLookUpTable.containsKey(node.name)) {
                className = Helper.removePrefixAndWhiteSpace(node.typeLookUpTable.get(node.name)!!)
            }
            var namespace = Helper.retrieveRelativeNamespaceForNode(node)

            if (namespace != className) {
                namespace += Helper.divider+className
            }

            generator.addNewStateDeclarationGroupFor(name)
            val initialState = Helper.removePrefixAndWhiteSpace(generator.getInitialState(name))
            file.openSection("main")
            file.writeln("active [${Helper.convertPrefixToPromelaPrefix(namespace)}_N] proctype ${Helper.convertPrefixToPromelaPrefix(namespace)}Machine() {")
            file.increaseIdentLevel()
            file.writeln("mtype:${Helper.convertPrefixToPromelaPrefix(namespace)} CurrentState;")
            file.writeln("CurrentState =  ${Helper.convertPrefixToPromelaPrefix(namespace.toLowerCase()+Helper.divider)}$initialState;")
            file.writeln("do")
            file.writeln(":: if")
            file.increaseIdentLevel()
            node.children.forEach {
                generator.visit(it)
            }
            file.writeln("fi")
            file.decreaseIdentLevel()
            file.writeln("od")
            file.decreaseIdentLevel()

            file.writeln("}")
            file.closeSection("main")
            file.openSection("declarations")

            file.write("mtype:${Helper.convertPrefixToPromelaPrefix(namespace)} = {")
            var stateString = ""
            generator.states.get(name)!!.forEach {
                stateString += "${Helper.convertPrefixToPromelaPrefix(namespace.toLowerCase() + Helper.divider + it)}, "
            }
            stateString = stateString.dropLast(2)
            file.write(stateString)
            file.writeln("}")
            file.closeSection("declarations")
        }

    }
}