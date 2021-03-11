package parser

import java.lang.Exception

class CaseNode(name: String): Node(name) {
    private lateinit var _guard: GuardNode
    private lateinit var _component: Node

    override fun type(): String {
        return "CaseNode"
    }

    fun setGuard(node: GuardNode) {
        _guard = node
        children.add(node)
    }

    fun getGuard(): GuardNode {
        return _guard
    }

    fun setComponent(node: Node) {
        _component = node
        children.add(node)
    }

    fun getComponent(): Node {
        return _component
    }

    override fun addChild(node: Node) {
        throw Exception("Not allowed to add child to CaseNode")
    }
}