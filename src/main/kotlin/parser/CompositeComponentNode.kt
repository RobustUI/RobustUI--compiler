package parser

class CompositeComponentNode(name: String): Node(name) {
    val typeLookUpTable: MutableMap<String, String> = mutableMapOf()

    override fun type(): String {
        return "CompositeComponentNode"
    }
}