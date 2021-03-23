package codeGenerator

import codeGenerator.RobustUiTypescriptFrameworkGenerator.OutputFile
import parser.Node

interface CodeGeneratorFileBuilder {
    fun build(node: Node, generator: CodeGeneratorFile)
}