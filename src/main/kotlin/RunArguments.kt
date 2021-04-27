import com.xenomachina.argparser.ArgParser
import java.io.File

class RunArguments(parser: ArgParser) {
    val target by parser.storing(
        "-t", "--target",
        help = "Specify the code target options are: typescript, treeant, qtree and promela")

    val includeDir by parser.storing(
        "-i", "--includeDir", help = "Directory to search for component dependencies") { File(this) }

    val mainComponent by parser.positional(
        "MAINCOMPONENT", help = "Component file name that should be compiled")
}