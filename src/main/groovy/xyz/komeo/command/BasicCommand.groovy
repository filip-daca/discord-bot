package xyz.komeo.command

import discord4j.core.object.entity.Message
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.flow.Lex
import xyz.komeo.flow.Lexer

class BasicCommand implements ConsumesFlux {

    static lexes = [
            new Lex(["hell|hi|howd"])
    ]

    static responses = [
            'boooo-ya!',
            'hey!',
            'Howdy partner!',
            '*sips*'
    ]

    def consume(Flux messages) {
        messages
                .filter(message -> Lexer.messageMatches(message.getContent(), lexes))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(responses.shuffled().first()))
                .subscribe()
    }
}
