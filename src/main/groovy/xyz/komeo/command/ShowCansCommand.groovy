package xyz.komeo.command

import discord4j.core.object.entity.Message
import groovy.transform.TupleConstructor
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.flow.Lex
import xyz.komeo.flow.Lexer
import xyz.komeo.memory.BotMemory

@TupleConstructor
class ShowCansCommand implements ConsumesFlux {

    BotMemory memory

    static lexes = [
            new Lex(["pr|sh|giv", "mon|can"]),
            new Lex(["how", "ma", "mon|can"])
    ]

    def consume(Flux messages) {
        messages
                .filter(message -> Lexer.messageMatches(message.getContent(), lexes))
                .flatMap(Message::getChannel)
                .flatMap { channel ->
                    def sorted = memory.dailyCans.sort({ k1, k2 -> memory.dailyCans[k1] <=> memory.dailyCans[k2] } as Comparator)
                    def stats = "Here comes top 3 players:\n" +
                            sorted.take(3).collect { "- ${it.key} = `${it.value}` " }.join("\n")
                    channel.createMessage(stats)
                }
                .subscribe()
    }
}
