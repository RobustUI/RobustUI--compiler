package codeGenerator

import parser.Node

interface CodeGeneratorFileBuilder {
    fun build(node: Node, generator: CodeGeneratorFile)
}