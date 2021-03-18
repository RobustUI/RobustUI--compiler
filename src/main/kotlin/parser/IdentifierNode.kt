package parser

import java.lang.Exception

class IdentifierNode(name: String): Node(name) {
    override fun type(): String {
        return "IdentifierNode"
    }

    override fun addChild(node: Node) {
        throw Exception("Identifier Nodes can not have children!")
    }
}