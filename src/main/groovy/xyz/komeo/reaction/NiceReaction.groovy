package xyz.komeo.reaction

import discord4j.common.util.Snowflake
import discord4j.core.object.entity.Message
import discord4j.core.object.reaction.ReactionEmoji
import groovy.transform.TupleConstructor
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.memory.BotMemory

@TupleConstructor
class NiceReaction implements ConsumesFlux {

    BotMemory memory

    static boomsterEmoji = ReactionEmoji.custom(Snowflake.of(640976634589085726),
            "boomster_white", false)
    static phrases = [
            'boomster',
            'boomer',
            'monster',
            'good time',
            'good vibe',
            'nice'
    ]

    def consume(Flux messages) {
        messages
                .filter(message -> phrases.contains(message.getContent().toLowerCase()))
                .flatMap { Message message ->
                    message.getAuthor().ifPresent {author ->
                        memory.dailyCans[author.username] ?= 0
                        memory.dailyCans[author.username] += 1
                    }
                    message.addReaction(boomsterEmoji)
                }
                .subscribe()
    }
}
