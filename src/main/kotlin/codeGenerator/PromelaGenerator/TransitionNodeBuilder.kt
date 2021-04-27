package codeGenerator.PromelaGenerator

import codeGenerator.CodeGeneratorFile
import codeGenerator.CodeGeneratorFileBuilder
import parser.Node
import parser.TransitionNode

class TransitionNodeBuilder {
    companion object: CodeGeneratorFileBuilder {
        override fun build(node: Node, generator: CodeGeneratorFile) {
            val node = node as TransitionNode

            val file = generator.getCurrentFile()
            var destPostfix = Helper.removePrefixAndWhiteSpace(node.to)
            var destPrefix =  Helper.retrieveRelativeNamespaceForNode(node) + Helper.divider
            destPrefix = Helper.convertPrefixToPromelaPrefix(destPrefix)
            val toName = destPrefix.toLowerCase() + destPostfix
            var transitionLabel = Helper.removePrefixAndWhiteSpace(node.name)
            val messageType = generator.getMessageTypeFor(node.name)

            when(messageType) {
                "InputMessage" -> file.writeln("${destPrefix+transitionLabel}?0 -> CurrentState = $toName")
                "OutputMessage" -> file.writeln("${destPrefix+transitionLabel}!0 -> CurrentState = $toName")
                "EventMessage" -> {
                    val split = transitionLabel.split('/')
                    if (split.size == 1) {
                        file.writeln("${destPrefix+transitionLabel}?1 -> CurrentState = $toName")
                    }else {
                        val output = transitionLabel.replace(Helper.removePrefixAndWhiteSpace(split[0]), "").replace("/", "").replace("!", "")
                        file.writeln("${destPrefix+Helper.convertPrefixToPromelaPrefix(split[0])}?1 ->")
                        file.writeln("${destPrefix+Helper.convertPrefixToPromelaPrefix(output)}!0 -> CurrentState = $toName")
                    }
                }
            }
        }
    }
}