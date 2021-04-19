package tokens

import kotlinx.serialization.Serializable
import parser.*

@Serializable
data class SelectiveComponent(
    val label: String,
    val type: Int,
    val initialCase: String,
    val observer: Map<String, String>,
    val cases: List<Map<String, String>>,
    val inputs: List<String>,
    val outputs: List<String>,
): Token() {
    var originalName: String? = null

    init {
        if (originalName == null) {
            originalName = label
        }
    }

    override val name: String
        get() = label

    override fun rename(name: String): SelectiveComponent {
        val copy = this.copy(label = name)
        copy.originalName = originalName
        return copy
    }

    override fun accept(parser: Parser): Node {
        parser.prefix = label
        val node = SelectiveComponentNode(label)
        parser.symbolTable[label] = SymbolContext(node, false)

        parser.registerInputs(inputs)
        parser.registerOutputs(outputs)

        val stream = observer["input"]!!

        cases.forEach {
            node.typeLookUpTable.put(it["label"]!!, it["type"]!!)
            parser.prefix = label
            val token = parser.getTokenFor(it["type"]!!)!!.rename(parser.addPrefix(it["label"]!!))
            val caseNode = CaseNode(token.name + "-Case")
            val guardNode = GuardNode(caseNode.name + "-Expression")
            guardNode.stream = stream
            guardNode.gaurd = it["guard"]!!
            caseNode.setGuard(guardNode)
            caseNode.setComponent(parser.parse(token))
            caseNode.setInitial(it["label"]!! == initialCase);
            node.addChild(caseNode)
        }

        var module = ModuleNode(this.originalName!!)
        var inputsStream: StreamNode = StreamNode("inputs")
        var outputsStream: StreamNode = StreamNode("outputs")

        inputs.forEach {
            inputsStream.addChild(IdentifierNode(it))
        }

        outputs.forEach {
            outputsStream.addChild(IdentifierNode(it))
        }

        module.inputStreamNode = inputsStream
        module.outputStreamNode = outputsStream
        module.body = node

        return module
    }
}