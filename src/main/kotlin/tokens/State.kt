package tokens

import kotlinx.serialization.Serializable
import parser.Node
import parser.Parser
import parser.StateNode

@Serializable
data class State(val label: String, val type: Int): Token() {
    override val name: String
        get() = label
    override fun rename(name: String): State {
        return this.copy(label = name)
    }

    override fun accept(parser: Parser): Node {
        return StateNode(label)
    }
}