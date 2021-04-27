package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.ModuleNode
import parser.Node
import parser.StreamNode

class StreamNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as StreamNode
            val file = generator.getCurrentFile()
            var namespace = Helper.retrieveRelativeNamespaceForNode((node.parent!! as ModuleNode).body)
            var chanName = if (namespace == node.parent!!.name) {
                "$namespace${Helper.divider}"
            } else {
                "${namespace}${Helper.divider}${node.parent!!.name}${Helper.divider}"
            }
            file.openSection("channelDefinitions")
            node.children.forEach {
                file.write("chan ${Helper.convertPrefixToPromelaPrefix(chanName)}")
                generator.visit(it)
                file.writeln(" = [0] of { byte }")
            }

            file.closeSection("channelDefinitions")

            node.children.forEach {

                file.write(":: ${Helper.convertPrefixToPromelaPrefix(chanName)}")
                generator.visit(it)
                when(node.name) {
                    "inputs" -> file.writeln("!0 -> goto end")
                    "outputs" -> file.writeln("?0 -> goto end")
                    "events" -> file.writeln("!1 -> goto end")
                }

            }
        }

    }
}