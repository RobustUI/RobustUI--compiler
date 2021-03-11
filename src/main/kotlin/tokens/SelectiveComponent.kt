package tokens

import kotlinx.serialization.Serializable
import parser.*

@Serializable
data class SelectiveComponent(
    val label: String,
    val type: Int,
    val observer: Map<String, String>,
    val cases: List<Map<String, String>>,
    val inputs: List<String>,
    val outputs: List<String>,
): Token() {
    override val name: String
        get() = label

    override fun rename(name: String): SelectiveComponent {
        return this.copy(label = name)
    }

    override fun accept(parser: Parser): Node {
        parser.prefix = label
        val node = SelectiveComponentNode(label)
        parser.symbolTable[label] = SymbolContext(node, false)

        parser.registerInputs(inputs)
        parser.registerOutputs(outputs)

        val stream = observer["input"]!!

        cases.forEach {
            parser.prefix = label
            val token = parser.getTokenFor(it["type"]!!)!!.rename(parser.addPrefix(it["type"]!!))
            val caseNode = CaseNode(token.name + "-Case")
            val guardNode = GuardNode(caseNode.name + "-Expression")
            guardNode.stream = stream
            guardNode.gaurd = it["guard"]!!
            caseNode.setGuard(guardNode)
            caseNode.setComponent(parser.parse(token))

            node.addChild(caseNode)
        }

        return node
    }
}