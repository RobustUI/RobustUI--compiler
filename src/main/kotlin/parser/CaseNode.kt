package parser

import java.lang.Exception

class CaseNode(name: String): Node(name) {
    private lateinit var _guard: GuardNode
    private lateinit var _component: Node
    private var _initial: Boolean = false

    override fun type(): String {
        return "CaseNode"
    }

    fun setGuard(node: GuardNode) {
        _guard = node
    }

    fun getGuard(): GuardNode {
        return _guard
    }

    fun setComponent(node: Node) {
        _component = node
    }

    fun getComponent(): Node {
        return _component
    }

    fun setInitial(initial: Boolean) {
        _initial = initial
    }

    fun isInitial(): Boolean {
        return _initial
    }

    override fun addChild(node: Node) {
        throw Exception("Not allowed to add child to CaseNode")
    }
}