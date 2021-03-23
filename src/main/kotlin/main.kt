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

fun main() {
    val murden = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentMurden.json")
    val mikkel = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentMikkel.json")
    val a = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentA.json")
    val selective = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentI am Selective.json")
    val csgo = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentCSGO.json")
    val onOffSimpleComponent = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentOnOffSimpleComponent.json")
    val onOffAdapterComponent = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentOnOffAdapterComponent.json")
    val otherOnOffSimpleComponent = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentOtherOnOffSimpleComponent.json")
    val LightLockController = readFileDirectlyAsText("/home/morten/Projects/RobustUI-electron/src/app/JSON/componentLightLockController.json")

    val parsedFiles: MutableList<JsonElement> = mutableListOf(
        Json.parseToJsonElement(LightLockController),
        Json.parseToJsonElement(onOffSimpleComponent),
        Json.parseToJsonElement(onOffAdapterComponent)
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
        print(codeGenerator.buildFile(it))
    }

   //File("/home/morten/Projects/trean-example/output.html").writeText(codeGenerator.generate())
}

fun readFileDirectlyAsText(fileName: String): String
        = File(fileName).readText(Charsets.UTF_8)




