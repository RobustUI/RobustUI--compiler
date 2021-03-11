package tokens

import kotlinx.serialization.Serializable
import parser.Node
import parser.Parser
import parser.SimpleComponentNode
import parser.SymbolContext

@Serializable
data class SimpleComponent(
    var label: String,
    val type: Int,
    val initialState: String,
    val states: List<State>,
    val events: List<String>,
    val inputs: List<String>,
    val outputs: List<String>,
    val transitions: List<Transition>
): Token() {
    override val name: String
        get () = label

    override fun rename(name: String): SimpleComponent {
       return this.copy(label = name)
    }

    override fun accept (parser: Parser): Node {
        parser.prefix = label;
        val root = SimpleComponentNode(label)
        parser.symbolTable[label] = SymbolContext(root, false)

        parser.registerInputs(inputs)
        parser.registerOutputs(outputs)
        parser.registerEvents(events)

        states.forEach {
            val renamed = it.copy(label = parser.addPrefix(it.label))
            val node = parser.parse(renamed)
            root.addChild(node)

            if (it.label == initialState) {
                parser.symbolTable[renamed.label] = SymbolContext(node, true)
            } else {
                parser.symbolTable[renamed.label] = SymbolContext(node, false)
            }
        }

        transitions.forEach {
            val renamed = it.copy(label = parser.addPrefix(it.label), from = parser.addPrefix(it.from), to = parser.addPrefix(it.to))
            val node = parser.parse(renamed)
            parser.symbolTable[renamed.from]!!.node.addChild(node)
        }

        return root
    }
}