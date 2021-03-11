package parser

class SimpleComponentNode(name: String): Node(name) {
    override fun type(): String {
        return "SimpleComponentNode"
    }
}