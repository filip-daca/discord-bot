package xyz.komeo.flow

import spock.lang.Specification

class LexSpec extends Specification {

    def "matching simple bits"() {
        given:
        Lex lex = new Lex(bits)

        expect:
        lex.matches(message)

        where:
        bits   | message
        ["h"]  | "hello"
        ["h"]  | "oh hello"
        ["he"] | "well, hello"
        ["he"] | " oks sd he he he"
    }

    def "not matching simple bits"() {
        given:
        Lex lex = new Lex(bits)

        expect:
        !lex.matches(message)

        where:
        bits   | message
        ["h"]  | "ello"
        ["h"]  | "oh ello"
        ["he"] | "well, yello"
        ["he"] | " oks sd ke khe hhe"
    }

    def "matching longer bits"() {
        given:
        Lex lex = new Lex(bits)

        expect:
        lex.matches(message)

        where:
        bits        | message
        ["h", "bo"] | "hello boy"
        ["h", "bo"] | "hi bobby"
        ["h", "bo"] | "hello my dear boy"
        ["h", "bo"] | "hello there bobby hi"
    }

    def "matching combined bits"() {
        given:
        Lex lex = new Lex(bits)

        expect:
        lex.matches(message)

        where:
        bits    | message
        ["h|g"] | "hello"
        ["h|g"] | "greetings friend"
        ["h|g"] | "oh hi"
        ["h|g"] | "oh good day"
    }

    def "matching complex bits"() {
        given:
        Lex lex = new Lex(bits)

        expect:
        lex.matches(message)

        where:
        bits                    | message
        ["w|h|te|g", "weather"] | "So, what's the weather today?"
        ["w|h|te|g", "weather"] | "tell me about the weather"
        ["w|h|te|g", "weather"] | "can you give my some weather info?"
        ["w|h|te|g", "weather"] | "bot, how is the weather?"

    }
}
