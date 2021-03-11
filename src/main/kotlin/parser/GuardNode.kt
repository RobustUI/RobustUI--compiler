package parser

import java.lang.Exception

class GuardNode(name: String): Node(name) {
    private lateinit var _stream: String
    private lateinit var _gaurd: String

    var stream: String
        get() = _stream
        set(value) {
            _stream = value
        }

    var gaurd: String
        get() = _gaurd
        set(value) {
            _gaurd = value
        }


    override fun type(): String {
        return "GuardNode"
    }

    override fun addChild(node: Node) {
        throw Exception("Cannot add child to ExpressionNode")
    }

    override fun toString(): String {
        return "($stream $gaurd)";
    }
}