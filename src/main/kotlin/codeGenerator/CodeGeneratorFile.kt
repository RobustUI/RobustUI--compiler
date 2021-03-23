package codeGenerator

import codeGenerator.RobustUiTypescriptFrameworkGenerator.OutputFile
import parser.Node
import parser.SymbolContext

interface CodeGeneratorFile {
    val states: MutableList<String>
    fun getSymbolContext(nodeName: String): SymbolContext
    fun getInitialState(): String
    fun generate(): List<OutputFile>
    fun buildFile(file: OutputFile): String
    fun visit(node: Node)
    fun createNewFile(fileName: String): OutputFile
    fun closeCurrentFile()
    fun getCurrentFile(): OutputFile
    fun getMessageTableFor(componentIdentifier: String, type: String): List<String>
}