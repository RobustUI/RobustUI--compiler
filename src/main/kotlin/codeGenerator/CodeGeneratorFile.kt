package codeGenerator

import parser.Node
import parser.SymbolContext

interface CodeGeneratorFile: GenericGenerator {
    val states: MutableMap<String, MutableList<String>>
    fun addStateDeclaration(state: String)
    fun addNewStateDeclarationGroupFor(name: String)
    fun closeCurrentStateDeclarationGroup()
    fun getSymbolContext(nodeName: String): SymbolContext
    fun getInitialState(name: String): String
    fun generate(): Map<String, OutputFile>
    fun buildFile(file: OutputFile): String
    fun visit(node: Node)
    fun createNewFile(fileName: String): OutputFile
    fun closeCurrentFile()
    fun getCurrentFile(): OutputFile
    fun getMessageTableFor(componentIdentifier: String, type: String): List<String>
    fun getMessageTypeFor(transitionLabelNamespace: String): String
    fun fileExists(fileName: String): Boolean
    fun incrementFileInstance(filename: String)
}