package xyz.komeo.reaction

import discord4j.common.util.Snowflake
import discord4j.core.object.reaction.ReactionEmoji
import reactor.core.publisher.Flux
import xyz.komeo.flow.ConsumesFlux

class NiceReaction implements ConsumesFlux {

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
                .flatMap(message -> message.addReaction(boomsterEmoji))
                .subscribe()
    }
}
