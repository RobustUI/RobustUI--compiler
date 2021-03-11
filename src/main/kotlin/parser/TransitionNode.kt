package parser

import java.lang.Exception

class TransitionNode(val from: String, label: String, val to: String): Node(label) {
    override fun type(): String {
        return "TransitionNode"
    }

    override fun addChild(node: Node) {
        throw Exception("Not allowed to add children to a Transition")
    }

    override fun toString(): String {
        return "${type()} -> {  From: $from, Label: $name, To: $to }"
    }
}