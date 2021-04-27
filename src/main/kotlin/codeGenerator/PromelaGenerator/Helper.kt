package codeGenerator.PromelaGenerator

import parser.*


class Helper {
    companion object {
        var divider = ""

        fun removePrefixAndWhiteSpace(string: String): String {
            return this.removeWhitespace(this.removePrefix(string))
        }

        fun removePrefix(string: String): String {
            return string.split(divider).last()
        }

        fun removeWhitespace(string: String): String {
            return string.replace(" ", "")
        }

        fun convertPrefixToPromelaPrefix(chanName: String): String {
            return removeWhitespace(chanName.replace(this.divider, "_____"))
        }

        fun retrieveRelativeNamespaceForNode(node: Node): String {
            val componentParent = getNearestComponent(node)
            var key = ""
            var destPrefix = ""
            var lastInput = ""
            node.name.split(divider).forEach {
                key += it
                if (componentParent.typeLookUpTable.containsKey(key)) {
                    lastInput = componentParent.typeLookUpTable.get(key)!!
                    if (lastInput.startsWith(removeWhitespace(destPrefix))) {
                        lastInput = lastInput.drop(removeWhitespace(destPrefix).length)
                    }
                }else {
                    lastInput = it
                }
                destPrefix += lastInput+divider
                key += divider
            }
            destPrefix = destPrefix.dropLast(divider.length)
            destPrefix = destPrefix.dropLast(lastInput.length)
            destPrefix = destPrefix.dropLast(divider.length)
            return removeWhitespace(if (destPrefix.isNotEmpty()) {
                destPrefix
            } else {
                lastInput
            })
        }


        fun getNearestComponent(node: Node): Node {
            if (node is SimpleComponentNode) {
                return node
            }

            if (node is CompositeComponentNode) {
                return node
            }

            if (node is SelectiveComponentNode) {
                return node
            }

            var target: Node = node
            while (!(target is ModuleNode)){
                target = target.parent!!
            }

            return target.body
        }
    }
}