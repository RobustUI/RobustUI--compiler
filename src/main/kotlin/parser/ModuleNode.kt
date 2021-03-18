package parser

import java.lang.Exception

class ModuleNode(val identifier: String): Node(name = identifier) {
    private lateinit var _inputStreamNode: StreamNode
    private lateinit var _outputStreamNode: StreamNode
    private var _eventStreamNode: StreamNode? = null
    private lateinit var _body: Node

    var inputStreamNode: StreamNode
        get() = _inputStreamNode
        set(value) {
            _inputStreamNode = value
        }
    var outputStreamNode: StreamNode
        get() = _outputStreamNode
        set(value) {
            _outputStreamNode = value
        }
    var eventStreamNode: StreamNode?
        get() = _eventStreamNode
        set(value) {
            _eventStreamNode = value
        }
    var body: Node
        get() = _body
        set(value) {
            _body = value
        }

    override fun type(): String {
        return "ModuleNode"
    }

    override fun addChild(node: Node) {
        throw Exception("Modules do not have children")
    }
}