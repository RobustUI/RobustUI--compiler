import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorFile
import codeGenerator.QTreeGenerator.QTreeGenerator
import codeGenerator.RobustUiTypescriptFrameworkGenerator.RobustUiTypescriptFrameworkGenerator
import codeGenerator.TreantJSGenerator.TreantJSGenerator
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import parser.Parser
import tokens.CompositeComponent
import tokens.SelectiveComponent
import tokens.SimpleComponent
import tokens.Token
import java.io.File
import java.io.FilenameFilter
import java.lang.Error
import java.lang.Exception
import java.util.*

fun main(args: Array<String>) = mainBody {
    val parsedArgs = ArgParser(args).parseInto(::RunArguments)

    val parsedFiles: MutableList<JsonElement> = mutableListOf()

    parsedArgs.run {
        if (includeDir.isDirectory) {
            val mainFile = includeDir.listFiles { directory, filename ->
                filename == mainComponent
            }?.firstOrNull()

            if (mainFile == null) {
                throw Exception("Could not locate the file '$mainComponent' in the include directory '$includeDir'")
            }

            parsedFiles.add(Json.parseToJsonElement(mainFile.absoluteFile.readText(Charsets.UTF_8)))

            includeDir.listFiles()?.forEach {
                if (it.isFile && it.name != mainComponent && it.extension == "json") {
                    parsedFiles.add(Json.parseToJsonElement(it.absoluteFile.readText(Charsets.UTF_8)))
                }
            }
        }
    }

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


