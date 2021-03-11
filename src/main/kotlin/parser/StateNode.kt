package parser

class StateNode(name: String): Node(name) {
    override fun type(): String {
        return "StateNode"
    }
}