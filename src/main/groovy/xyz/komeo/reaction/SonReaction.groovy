package xyz.komeo.reaction

import discord4j.core.object.entity.Message
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux

class SonReaction implements ConsumesFlux {

    static phrases = [
            'boomer',
    ]
    static response = 'Are you winning son?'

    def consume(Flux messages) {
        messages
                .filter(message -> phrases.contains(message.getContent().toLowerCase()))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(response))
                .subscribe()
    }
}
