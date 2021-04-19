import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorFile
import codeGenerator.QTreeGenerator.QTreeGenerator
import codeGenerator.RobustUiTypescriptFrameworkGenerator.RobustUiTypescriptFrameworkGenerator
import codeGenerator.TreantJSGenerator.TreantJSGenerator
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import parser.Parser
import tokens.CompositeComponent
import tokens.SelectiveComponent
import tokens.SimpleComponent
import tokens.Token
import java.io.File
import java.lang.Error
import java.util.*

fun main() {
    val onOff = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentonOffComponent.json")
    val onOffAdapter = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentonOffAdapter.json")
    val selective = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentLightLockSelective.json")
    val lightLockController = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentLightLockController.json")
    val main = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentLightLockControllerWithControls.json")


    val parsedFiles: MutableList<JsonElement> = mutableListOf(
        Json.parseToJsonElement(main),
        Json.parseToJsonElement(onOff),
        Json.parseToJsonElement(onOffAdapter),
        Json.parseToJsonElement(selective),
        Json.parseToJsonElement(lightLockController),
    )

    val tokens: MutableMap<String, Token> = mutableMapOf()

    parsedFiles.forEach {
        val comp = when(it.jsonObject["type"].toString()){
            "1" -> Json { ignoreUnknownKeys = true }.decodeFromJsonElement<SimpleComponent>(it)
            "2" -> Json { ignoreUnknownKeys = true }.decodeFromJsonElement<CompositeComponent>(it)
            "3" -> Json { ignoreUnknownKeys = true }.decodeFromJsonElement<SelectiveComponent>(it)
            else -> throw Error("Couldn't tokenize: $it")
        }


        tokens[comp.name] = comp
    }

    val parser = Parser(tokens)

    val codeGenerator = RobustUiTypescriptFrameworkGenerator(parser)
    val files = codeGenerator.generate()

    files.forEach {
        print(codeGenerator.buildFile(it.value))
    }

   //File("/home/morten/Projects/trean-example/output.html").writeText(codeGenerator.generate())
}

fun readFileDirectlyAsText(fileName: String): String
        = File(fileName).readText(Charsets.UTF_8)




