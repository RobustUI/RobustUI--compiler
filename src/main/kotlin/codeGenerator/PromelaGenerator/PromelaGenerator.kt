package codeGenerator.PromelaGenerator

import codeGenerator.CodeGenerator
import codeGenerator.CodeGeneratorFile
import codeGenerator.OutputFile
import parser.*
import java.lang.Exception
import java.util.*

class PromelaGenerator(val parser: Parser): CodeGeneratorFile {

    init {
        Helper.divider = parser.nameDivider
    }

    private val files: MutableMap<String, OutputFile> = mutableMapOf()
    private val openedFiles: Stack<OutputFile> = Stack()
    private var currentStateList: Stack<MutableList<String>> = Stack()
    private var numOfFileInstances: MutableMap<String, Int> = mutableMapOf()

    override val states: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun addStateDeclaration(state: String) {
        this.currentStateList.peek().add(state)
    }

    override fun addNewStateDeclarationGroupFor(name: String) {
        this.states.put(name, mutableListOf())
        this.currentStateList.push(this.states.get(name)!!)
    }

    override fun closeCurrentStateDeclarationGroup() {
        this.currentStateList.pop()
    }

    override fun getSymbolContext(nodeName: String): SymbolContext {
        return parser.symbolTable[nodeName]!!.copy()
    }

    override fun getInitialState(name: String): String {
        val initialStateList =  parser.symbolTable.filter {
            val keySplit = it.key.split(Helper.divider)

            if (keySplit.size > 2) {
                keySplit[keySplit.size - 2] == name && it.value.initial
            } else {
                keySplit[0] == name && it.value.initial
            }
        }
        return initialStateList.keys.first()
    }

    override fun generate(): Map<String, OutputFile> {
       var file = ""

       file += visit(parser.root)

       return files
    }

    override fun buildFile(file: OutputFile): String {
        val sections = file.getAllSections()
        var ret = ""

        ret += if (sections["defines"] != null) {
            sections["defines"]
        } else {
            ""
        }

        ret += if (sections["channelDefinitions"] != null) {
            sections["channelDefinitions"]
        } else {
            ""
        }

        ret += if (sections["declarations"] != null) {
            sections["declarations"]
        } else {
            ""
        }

        ret += if (sections["main"] != null) {
            sections["main"]
        } else {
            ""
        }

        ret += if (sections["environment"] != null) {
            sections["environment"]
        } else {
            ""
        }

        ret += if (sections["browserEnvironment"] != null) {
            sections["browserEnvironment"]
        } else {
            ""
        }

        return ret
    }

    override fun visit(node: Node) {
        when(node) {
            is ModuleNode -> ModuleBuilder.build(node, this)
            is SimpleComponentNode -> SimpleComponentBuilder.build(node, this)
            is StateNode -> StateNodeBuilder.build(node, this)
            is TransitionNode -> TransitionNodeBuilder.build(node, this)
            is StreamNode -> StreamNodeBuilder.build(node, this)
            is IdentifierNode -> IdentifierNodeBuilder.build(node, this)
            is CompositeComponentNode -> CompositeComponentNodeBuilder.build(node, this)
            is SelectiveComponentNode -> SelectiveComponentNodeBuilder.build(node, this)
            is CaseNode -> CaseNodeBuilder.build(node, this)
            else -> throw Exception("PromelaGenerator Exception: Could not code generate: $node")
        }
    }

    override fun createNewFile(fileName: String): OutputFile {
        val newfile = OutputFile(fileName)
        files.set(fileName, newfile)
        openedFiles.push(newfile)
        this.numOfFileInstances[fileName] = 1
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
        TODO("Not yet implemented")
    }

    override fun getMessageTypeFor(transitionLabelNamespace: String): String {
        return this.parser.messageTable.get(transitionLabelNamespace)!!
    }

    override fun fileExists(fileName: String): Boolean {
        return files.containsKey(fileName)
    }

    override fun incrementFileInstance(filename: String) {
        this.numOfFileInstances[filename] = this.numOfFileInstances[filename]!! + 1
    }

    override fun compileOutputAsString(): String {
        var result = ""
        val files = this.generate()
        this.numOfFileInstances.forEach{name, num ->
            result += "#define ${Helper.convertPrefixToPromelaPrefix(name)}_N $num\n"
        }

        files.forEach {
            result += this.buildFile(it.value);
        }

        return result
    }
}