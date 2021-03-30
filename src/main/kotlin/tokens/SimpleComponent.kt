package tokens

import kotlinx.serialization.Serializable
import parser.*

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

    var originalName: String? = null

    init {
        if (originalName == null) {
            originalName = label
        }
    }

    override val name: String
        get () = label

    override fun rename(name: String): SimpleComponent {
        val copy = this.copy(label = name)
        copy.originalName = originalName
        return copy
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

        var module = ModuleNode(this.originalName!!)
        var inputsStream: StreamNode = StreamNode("inputs")
        var outputsStream: StreamNode = StreamNode("outputs")
        var eventsStream: StreamNode = StreamNode("events")

        inputs.forEach {
            inputsStream.addChild(IdentifierNode(it))
        }

        outputs.forEach {
            outputsStream.addChild(IdentifierNode(it))
        }

        events.forEach {
            eventsStream.addChild(IdentifierNode(it))
        }

        module.inputStreamNode = inputsStream
        module.outputStreamNode = outputsStream
        module.eventStreamNode = eventsStream
        module.body = root

        return module
    }
}