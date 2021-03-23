package codeGenerator.RobustUiTypescriptFrameworkGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorFile
import parser.*
import java.lang.Exception
import java.util.*

class RobustUiTypescriptFrameworkGenerator(val parser: Parser): CodeGeneratorFile {
    init {
        Helper.divider = parser.nameDivider
    }

    override val states: MutableList<String> = mutableListOf()
    private val files: MutableList<OutputFile> = mutableListOf()
    private val openedFiles: Stack<OutputFile> = Stack()

    override fun getSymbolContext(nodeName: String): SymbolContext {
        return parser.symbolTable.get(nodeName)!!.copy()
    }

    override fun getInitialState(): String {
        val foo =  parser.symbolTable.filter { it.value.initial };
        return foo.keys.first()
    }

    override fun generate(): List<OutputFile> {
        visit(parser.root)

        return files
    }

    override fun buildFile(file: OutputFile): String {
        val sections = file.getAllSections()
        var output = "";
        output += if (sections["imports"] != null) {
            sections["imports"]
        } else {
            ""
        }
        output += if (sections["declarations"] != null) {
            sections["declarations"]
        } else {
            ""
        }

        output += if (sections["topMainClass"] != null) {
            sections["topMainClass"]
        } else {
            ""
        }

        output += if (sections["middleMainClass"] != null) {
            sections["middleMainClass"]
        } else {
            ""
        }

        output += if (sections["bottomMainClass"] != null) {
            sections["bottomMainClass"]
        } else {
            ""
        }

        return output
    }

    override fun visit(node: Node) {
        when(node) {
            is ModuleNode -> ModuleBuilder.build(node,this)
            is SimpleComponentNode -> ComponentBuilder.build(node, this)
            is CompositeComponentNode -> CompositeComponentBuilder.build(node, this)
            is SelectiveComponentNode -> ComponentBuilder.build(node, this)
            is StateNode -> StateBuilder.build(node, this)
            is TransitionNode -> TransitionBuilder.build(node, this)
            is CaseNode -> CaseNodeBuilder.build(node, this)
            is StreamNode -> StreamBuilder.build(node, this)
            is IdentifierNode -> IdentifierBuilder.build(node, this)
            else -> throw Exception("RobustUiTypescriptFrameworkGenerator Exception: Could not code generate: $node")
        }
    }

    override fun createNewFile(fileName: String): OutputFile {
        val newfile = OutputFile(fileName)
        files.add(newfile)
        openedFiles.push(newfile)
        return getCurrentFile()
    }

    override fun closeCurrentFile() {
        openedFiles.peek().closeCurrentSection()
       openedFiles.pop()
    }

    override fun getCurrentFile(): OutputFile {
        return openedFiles.peek()
    }

    override fun getMessageTableFor(componentIdentifier: String, type: String): List<String> {
        return this.parser.messageTable.filter {
            it.key.split(componentIdentifier+this.parser.nameDivider).size == 2 &&
            it.value.equals(type, ignoreCase = true)
        }.map { it.key }
    }
}