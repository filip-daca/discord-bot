package xyz.komeo.command

import discord4j.core.object.entity.Message
import groovy.transform.TupleConstructor
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.flow.Lex
import xyz.komeo.flow.Lexer

@TupleConstructor
class CountModsCommand implements ConsumesFlux {

    static final SITE_URL = 'https://komeo.xyz/ls2009mods/'
    static final DATA_TAG = 'data-portal-stats'
    static final DATA_VALUE = 'total-mod-count'

    static lexes = [
            new Lex(["pr|sh|giv", "mods"]),
            new Lex(["how", "ma", "mods"])
    ]

    static responses = [
            'nice!',
            'oh boy!',
            'good job!',
            'well done!'
    ]

    def consume(Flux messages) {
        messages
                .filter(message -> Lexer.messageMatches(message.getContent(), lexes))
                .flatMap(Message::getChannel)
                .flatMap { channel ->
                    def modCount = loadTotalModCount()
                    def response = responses.shuffled().first()
                    channel.createMessage("Cuttenrly we have `${modCount}` mods - ${response}")
                }
                .subscribe()
    }

    static def loadTotalModCount() {
        new URL(SITE_URL).getText()
                .find(/${DATA_TAG}="${DATA_VALUE}".*>(.*)</)
                .replaceAll('<.*', '')
                .replaceAll('.*>','')
                .toInteger()
    }
}
