package parser

class CompositeComponentNode(name: String): Node(name) {
    override fun type(): String {
        return "CompositeComponentNode"
    }
}