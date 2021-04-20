package codeGenerator.TreantJSGenerator

import codeGenerator.CodeGenerator
import codeGenerator.TreantJSGenerator.boilerplates.Template
import parser.*
import java.lang.Exception

class TreantJSGenerator(val parser: Parser): CodeGenerator {
    init {
        Helper.divider = parser.nameDivider
    }

    override fun getSymbolContext(nodeName: String): SymbolContext {
        return parser.symbolTable.get(nodeName)!!.copy()
    }

    override fun generate(): String {
        var file = "simple_chart_config = {\n" +
                "\t\t    chart: {\n" +
                "            container: \"#tree-simple\",\n" +
                "            \n" +
                "            node: {\n" +
                "                collapsable: true\n" +
                "            }\n" +
                "        },\n" +
                "        nodeStructure: "
        file += visit(parser.root)
        file += "};"
        file += "var my_chart = new Treant(simple_chart_config);"
        return Template.resolve(file)
    }

    override fun visit(node: Node): String {
        return when(node) {
            is ModuleNode -> ModuleBuilder.build(node, this)
            is SimpleComponentNode -> ComponentBuilder.build(node, this)
            is CompositeComponentNode -> ComponentBuilder.build(node, this)
            is SelectiveComponentNode -> ComponentBuilder.build(node, this)
            is StateNode -> StateBuilder.build(node, this)
            is TransitionNode -> TransitionBuilder.build(node, this)
            is CaseNode -> CaseNodeBuilder.build(node, this)
            is StreamNode -> StreamBuilder.build(node, this)
            is IdentifierNode -> IdentifierBuilder.build(node, this)
            else -> throw Exception("TreantJS Exception: Could not code generate: $node")
        }
    }

    override fun compileOutputAsString(): String {
        return this.generate();
    }
}