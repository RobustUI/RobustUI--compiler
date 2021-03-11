package codeGenerator

import parser.Node

interface CodeGeneratorBuilder {
    fun build(node: Node, generator: CodeGenerator): String
}