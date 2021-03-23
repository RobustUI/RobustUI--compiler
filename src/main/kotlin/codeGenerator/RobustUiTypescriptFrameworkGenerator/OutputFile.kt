package codeGenerator.RobustUiTypescriptFrameworkGenerator

import java.util.*

class OutputFile(val fileName: String) {
    private var indentLevel = 0
    private var sections: MutableMap<String, String> = mutableMapOf()
    private lateinit var currentSection: String;
    private var currentSectionName: String? = null

    private var openedSections:  Stack<String> = Stack()

    fun openSection(name: String) {
        if (currentSectionName != null) {
            sections[currentSectionName!!] = currentSection
        }

        if (!sections.contains(name)) {
            sections[name] = ""
        }

        this.openedSections.push(name)
        this.currentSection = sections[name]!!
        this.currentSectionName = name;
    }

    fun closeSection(name: String) {
        if (currentSectionName != null) {
            sections[currentSectionName!!] = currentSection
        }
        val previousSection = this.openedSections.pop()

        this.currentSection = sections[previousSection]!!
        this.currentSectionName = previousSection;
    }

    fun closeCurrentSection() {
        if (this.openedSections.empty()) {
            return
        }
        if (currentSectionName != null) {
            sections[currentSectionName!!] = currentSection
        }
       this.openedSections.pop()
    }

    fun getAllSections(): Map<String, String> {
        return this.sections
    }

    fun writeln(content: String) {
        if (!this::currentSection.isInitialized) {
            throw error("No section to put the code in, this is a mistake, remember to call addSection!")
        }
        this.currentSection += content.padStart(indentLevel, '\t') + '\n'
    }

    fun write(content: String) {
        if (!this::currentSection.isInitialized) {
            throw error("No section to put the code in, this is a mistake, remember to call addSection!")
        }
        this.currentSection += content.padStart(indentLevel, '\t')

    }

    fun rawWrite(content: String) {
        if (!this::currentSection.isInitialized) {
            throw error("No section to put the code in, this is a mistake, remember to call addSection!")
        }
        this.currentSection += content;
    }

    fun increaseIdentLevel() {
        this.indentLevel++
    }

    fun decreaseIdentLevel() {
        this.indentLevel--
        if (this.indentLevel < 0) {
            this.indentLevel = 0
        }
    }
}