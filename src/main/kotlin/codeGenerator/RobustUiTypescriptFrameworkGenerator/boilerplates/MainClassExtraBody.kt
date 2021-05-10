package codeGenerator.RobustUiTypescriptFrameworkGenerator.boilerplates

class MainClassExtraBody {
    companion object {
        fun generate(className: String, events: List<String>): String {
            var content = "protected machineDeclaration: MachineDeclaration;\n" +
                    "\n" +
                    "    constructor(name: string) {\n" +
                    "        super(name);\n" +
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
                    "\n"

            content += "\n" +
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