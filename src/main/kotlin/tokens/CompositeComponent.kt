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

        var module = ModuleNode(UUID.randomUUID().toString())
        var inputsStream: StreamNode = StreamNode(root.name + "_inputStream")
        var outputsStream: StreamNode = StreamNode(root.name + "_outputStream")

        inputs.forEach {
            inputsStream.addChild(IdentifierNode(it))
        }

        outputs.forEach {
            outputsStream.addChild(IdentifierNode(it))
        }

        module.inputStreamNode = inputsStream
        module.outputStreamNode = outputsStream
        module.body = root

        return module
    }
}