package tokens

import kotlinx.serialization.Serializable
import parser.CompositeComponentNode
import parser.Node
import parser.Parser

@Serializable
data class CompositeComponent(
    val label: String,
    val type: Int,
    val components: List<Map<String, String>>,
    val inputs: List<String>,
    val outputs: List<String>
): Token() {
    override val name: String
        get() = label

    override fun rename(name: String): CompositeComponent {
        return this.copy(label = name)
    }

    override fun accept(parser: Parser): Node {

        val root = CompositeComponentNode(label)

        parser.registerInputs(inputs)
        parser.registerOutputs(outputs)

        components.forEach {
            parser.prefix = label
            val token = parser.getTokenFor(it["type"]!!)!!.rename(parser.addPrefix(it["label"]!!))
            val node = parser.parse(token)
            root.addChild(node)
        }

        return root;
    }
}