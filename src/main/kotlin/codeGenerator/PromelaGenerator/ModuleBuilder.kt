package codeGenerator.PromelaGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorBuilder
import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.ModuleNode
import parser.Node
import parser.SelectiveComponentNode

class ModuleBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            var node = node as ModuleNode

            var namespace = Helper.retrieveRelativeNamespaceForNode(node.body)
            var combinedPrefixNamespace = if (namespace.toLowerCase() != node.name.toLowerCase()) {
                namespace+Helper.divider+node.name
            } else {
                node.name
            }

            var file = generator.createNewFile(combinedPrefixNamespace)

            generator.visit(node.body)
            if (node.body !is SelectiveComponentNode) {
                file.openSection("environment")
                file.writeln("active proctype ${Helper.convertPrefixToPromelaPrefix(combinedPrefixNamespace)}Env() {")
                file.writeln("end:")
                if (node.inputStreamNode.children.size > 0 || node.outputStreamNode.children.size > 0) {
                    file.writeln("if")

                    generator.visit(node.inputStreamNode)
                    generator.visit(node.outputStreamNode)

                    file.writeln("fi")
                }
                file.writeln("}")
                file.closeSection("environment")

                if (node.eventStreamNode != null && node.eventStreamNode!!.children.size > 0) {
                    file.openSection("browserEnvironment")
                    file.writeln("active proctype ${Helper.convertPrefixToPromelaPrefix(combinedPrefixNamespace)}BrowserEnv() {")
                    file.writeln("end:")
                    file.writeln("if")
                    generator.visit(node.eventStreamNode!!)
                    file.writeln("fi")
                    file.writeln("}")
                    file.closeSection("browserEnvironment")
                }
            } else {
                var streamName = Helper.convertPrefixToPromelaPrefix(combinedPrefixNamespace + Helper.divider + node.inputStreamNode.children.first().name)

                file.openSection("declarations")
                file.writeln("byte $streamName;")
                file.closeSection("declarations")
            }

            generator.closeCurrentFile()
        }
    }
}