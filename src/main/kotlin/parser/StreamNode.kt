package parser

class StreamNode(name: String): Node(name) {
    override fun type(): String {
        return "StreamNode"
    }
}