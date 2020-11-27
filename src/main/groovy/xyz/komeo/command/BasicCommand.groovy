package xyz.komeo.command

import discord4j.core.object.entity.Message
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux

class BasicCommand implements ConsumesFlux {

    def consume(Flux messages) {
        messages
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Howdy partner!"))
                .subscribe()
    }
}
