package tokens

import kotlinx.serialization.Serializable
import parser.*
import java.util.*

@Serializable
data class CompositeComponent(
    val label: String,
    val type: Int,
    val components: List<Map<String, String>>,
    val inputs: List<String>,
    val outputs: List<String>
): Token() {
    var originalName: String? = null

    init {
        if (originalName == null) {
            originalName = label
        }
    }

    override val name: String
        get() = label

    override fun rename(name: String): CompositeComponent {
        val copy = this.copy(label = name)
        copy.originalName = originalName
        return copy
    }

    override fun accept(parser: Parser): Node {

        val root = CompositeComponentNode(label)

        parser.registerInputs(inputs)
        parser.registerOutputs(outputs)

        components.forEach {
            root.typeLookUpTable.put(it["label"]!!, it["type"]!!)
            parser.prefix = label
            val token = parser.getTokenFor(it["type"]!!)!!.rename(parser.addPrefix(it["label"]!!))
            val node = parser.parse(token)
            root.addChild(node)
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
        module.body = root
        root.parent = module

        return module
    }
}