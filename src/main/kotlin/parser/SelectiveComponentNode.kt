package parser

class SelectiveComponentNode(name: String): Node(name) {
    override fun type(): String {
        return "SelectiveComponentNode"
    }
}