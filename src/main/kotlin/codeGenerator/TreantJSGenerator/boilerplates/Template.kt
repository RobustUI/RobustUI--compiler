package codeGenerator.TreantJSGenerator.boilerplates

class Template {
    companion object {
        const val head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<meta charset=\"utf-8\">\n" +
                "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "\t<title></title>\n" +
                "\t<style>\n" +
                "\t\t\t\t/* required LIB STYLES */\n" +
                "\t\t/* .Treant se automatski dodaje na svaki chart conatiner */\n" +
                "\t\t.Treant { position: relative; overflow: hidden; padding: 0 !important; }\n" +
                "\t\t.Treant > .node,\n" +
                "\t\t.Treant > .pseudo { position: absolute; display: block; visibility: hidden; }\n" +
                "\t\t.Treant.loaded .node,\n" +
                "\t\t.Treant.loaded .pseudo { visibility: visible; }\n" +
                "\t\t.Treant > .pseudo { width: 0; height: 0; border: none; padding: 0; }\n" +
                "\t\t.Treant .collapse-switch { width: 3px; height: 3px; display: block; border: 1px solid black; position: absolute; top: 1px; right: 1px; cursor: pointer; }\n" +
                "\t\t.Treant .collapsed .collapse-switch { background-color: #868DEE; }\n" +
                "\t\t.Treant > .node img {\tborder: none; float: left; }\n" +
                "\t\tbody,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote,th,td { margin:0; padding:0; }\n" +
                "table { border-collapse:collapse; border-spacing:0; }\n" +
                "fieldset,img { border:0; }\n" +
                "address,caption,cite,code,dfn,em,strong,th,var { font-style:normal; font-weight:normal; }\n" +
                "caption,th { text-align:left; }\n" +
                "h1,h2,h3,h4,h5,h6 { font-size:100%; font-weight:normal; }\n" +
                "q:before,q:after { content:''; }\n" +
                "abbr,acronym { border:0; }\n" +
                "\n" +
                "body { background: #fff; }\n" +
                "/* optional Container STYLES */\n" +
                ".chart { height: 600px; margin: 5px; width: 900px; }\n" +
                ".Treant > .node { padding: 3px; border: 1px solid #484848; border-radius: 3px; }\n" +
                ".Treant > .node img { width: 100%; height: 100%; }\n" +
                "\n" +
                ".Treant .collapse-switch { width: 100%; height: 100%; border: none; }\n" +
                ".Treant .node.collapsed { background-color: #DEF82D; }\n" +
                ".Treant .node.collapsed .collapse-switch { background: none; }\n" +
                "\t</style>\n" +
                "\t<style>\n" +
                "\t\t.ps-container .ps-scrollbar-x {\n" +
                "    position: absolute; /* please don't change 'position' */\n" +
                "    bottom: 3px; /* there must be 'bottom' for ps-scrollbar-x */\n" +
                "    height: 8px;\n" +
                "    background-color: #aaa;\n" +
                "    border-radius: 4px;\n" +
                "    -webkit-border-radius: 4px;\n" +
                "    -moz-border-radius: 4px;\n" +
                "    opacity: 0;\n" +
                "    filter: alpha(opacity = 0);\n" +
                "    -webkit-transition: opacity.2s linear;\n" +
                "    -moz-transition: opacity .2s linear;\n" +
                "    transition: opacity .2s linear;\n" +
                "}\n" +
                "\n" +
                ".ps-container:hover .ps-scrollbar-x {\n" +
                "    opacity: 0.6;\n" +
                "    filter: alpha(opacity = 60);\n" +
                "}\n" +
                "\n" +
                ".ps-container .ps-scrollbar-x:hover {\n" +
                "    opacity: 0.9;\n" +
                "    filter: alpha(opacity = 90);\n" +
                "    cursor:default;\n" +
                "}\n" +
                "\n" +
                ".ps-container .ps-scrollbar-x.in-scrolling {\n" +
                "    opacity: 0.9;\n" +
                "    filter: alpha(opacity = 90);\n" +
                "}\n" +
                "\n" +
                ".ps-container .ps-scrollbar-y {\n" +
                "    position: absolute; /* please don't change 'position' */\n" +
                "    right: 3px; /* there must be 'right' for ps-scrollbar-y */\n" +
                "    width: 8px;\n" +
                "    background-color: #aaa;\n" +
                "    border-radius: 4px;\n" +
                "    -webkit-border-radius: 4px;\n" +
                "    -moz-border-radius: 4px;\n" +
                "    opacity: 0;\n" +
                "    filter: alpha(opacity = 0);\n" +
                "    -webkit-transition: opacity.2s linear;\n" +
                "    -moz-transition: opacity .2s linear;\n" +
                "    transition: opacity .2s linear;\n" +
                "}\n" +
                "\n" +
                ".ps-container:hover .ps-scrollbar-y {\n" +
                "    opacity: 0.6;\n" +
                "    filter: alpha(opacity = 60);\n" +
                "}\n" +
                "\n" +
                ".ps-container .ps-scrollbar-y:hover {\n" +
                "    opacity: 0.9;\n" +
                "    filter: alpha(opacity = 90);\n" +
                "    cursor: default;\n" +
                "}\n" +
                "\n" +
                ".ps-container .ps-scrollbar-y.in-scrolling {\n" +
                "    opacity: 0.9;\n" +
                "    filter: alpha(opacity = 90);\n" +
                "}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div id=\"tree-simple\" style=\"height: 100vh; width: 100vw;\"> </div>\n" +
                "\t<script src=\"vendor/treantjs.js\"></script>\n" +
                "\t<script src=\"vendor/raphaeljs.js\"></script>\n" +
                "\n" +
                "\t<script src=\"vendor/jquery.min.js\"></script>\n" +
                "\t<script src=\"vendor/jquery.easing.js\"></script>"

        const val tail = "</body>\n" +
                "</html>"

        fun resolve(code: String): String {
            var retCode = head
            retCode += "<script>$code</script>"
            retCode += tail

            return retCode
        }
    }
}