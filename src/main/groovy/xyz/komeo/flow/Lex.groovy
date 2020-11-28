package xyz.komeo.flow

class Lex {
    List<List<String>> bits

    Lex(bits) {
        this.bits = bits.collect {
            it.tokenize('|')
        }
    }

    boolean matches(String message) {
        int i = 0
        message.toLowerCase().tokenize(" ").each { word ->
            if (bits[i].any {bit -> word.startsWith(bit) }) {
                i++
            }
        }
        i == bits.size()
    }
}
