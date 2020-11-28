package xyz.komeo.flow

import spock.lang.Specification

class LexerSpec extends Specification {

    def "matching simple commands"() {
        given:
        def lexes = [
                new Lex(["pr|sh|giv", "mon|can"]),
                new Lex(["how", "ma", "mon|can"])
        ]

        expect:
        Lexer.messageMatches(message, lexes)

        where:
        _ | message
        _ | "!boomer can you give me some monster stats?"
        _ | "!boomer how many monsters have you seen?"
        _ | "!boomer print cans data!"
    }

    def "skipping simple commands"() {
        given:
        def lexes = [
                new Lex(["pr|sh|giv", "mon|can"]),
                new Lex(["how", "ma", "mon|can"])
        ]

        expect:
        !Lexer.messageMatches(message, lexes)

        where:
        _ | message
        _ | "!boomer what's up?"
        _ | "!boomer how many days are we playing?"
        _ | "!boomer print user data!"
    }
}
