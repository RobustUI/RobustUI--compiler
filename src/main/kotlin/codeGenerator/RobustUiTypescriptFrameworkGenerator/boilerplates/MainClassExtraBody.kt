package codeGenerator.RobustUiTypescriptFrameworkGenerator.boilerplates

class MainClassExtraBody {
    companion object {
        fun generate(className: String, events: List<String>): String {
            var content = "protected machineDeclaration: MachineDeclaration;\n" +
                    "\n" +
                    "    constructor() {\n" +
                    "        super();\n" +
                    "\n" +
                    "        this.machineDeclaration = {\n" +
                    "            initialState: this.initialState,\n" +
                    "            states: this.states,\n" +
                    "            inputs: this.inputs,\n" +
                    "            outputs: this.outputs\n" +
                    "        };\n" +
                    "\n" +
                    "        this.initialize();\n" +
                    "    }\n" +
                    "\n" +
                    "    public registerElement(element: HTMLElement): void {\n"

            events.forEach {
                content += "        element.addEventListener(\"$it\", function() {\n" +
                        "            this.transition(\"$it\");\n" +
                        "        }.bind(this));\n"
            }

            content += "    }\n" +
                    "\n" +
                    "    public getOutputStream(label: ${className}OutputStreams): Observable<any> {\n" +
                    "        return super.getOutputStream(label);\n" +
                    "    }\n" +
                    "\n" +
                    "    public when(state: ${className}State, action: RobustUIActions): Observable<ActionEvent> {\n" +
                    "        return super.when(state, action);\n" +
                    "    }"

            return content
        }
    }
}