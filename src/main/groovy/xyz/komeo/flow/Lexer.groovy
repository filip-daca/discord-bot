package xyz.komeo.flow

class Lexer {

    static boolean messageMatches(String message, List<Lex> lexes) {
        boolean matches = false

        lexes.each {
            matches = matches || it.matches(message)
        }
    }

}
