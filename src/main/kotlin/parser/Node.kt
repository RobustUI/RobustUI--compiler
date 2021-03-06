package parser

import kotlin.reflect.typeOf

abstract class Node (
    var name: String, // Label
 ) {
    val typeLookUpTable: MutableMap<String, String> = mutableMapOf()
    var parent: Node? = null
    var children: MutableList<Node> = mutableListOf()

    open fun addChild(node: Node) {
        children.add(node);
        node.parent = this
    }

    abstract fun type(): String

    override fun toString(): String {
        var s = "$name: ${this.type()}"

        if (!children.isEmpty()) {
            s += "{ " + children.map { it.toString() } +" }"
        }

        return s
    }
}