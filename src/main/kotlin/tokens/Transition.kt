package tokens

import kotlinx.serialization.Serializable
import parser.Node
import parser.Parser
import parser.TransitionNode

@Serializable
data class Transition(val from: String, val label: String, val to: String): Token() {
    override val name: String
        get () = label
    override fun rename(name: String): Transition {
        return this.copy(label = name);
    }

    override fun accept(parser: Parser): Node {
        return TransitionNode(from, label, to)
    }
}
