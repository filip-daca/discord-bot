package reaction

import discord4j.common.util.Snowflake
import discord4j.core.object.entity.Message
import discord4j.core.object.reaction.ReactionEmoji
import reactor.core.publisher.Flux

class JohnDeereReaction {

    static johnDeereEmoji = ReactionEmoji.custom(Snowflake.of(640977678643494914),
            "john_deere", false)
    static phrases = [
            'john deere',
            'johndeere'
    ]
    static responses = [
            'Tomorrow morning, we\'re mowing the lawn!',
            'Green machine goes "brrrrrrrrr"!'
    ]

    static consume(Flux messages) {
        def johnDeereMessages = messages
                .filter(message -> phrases.contains(message.getContent().toLowerCase()))

        johnDeereMessages
                .flatMap(message -> message.addReaction(johnDeereEmoji))
                .subscribe()

        johnDeereMessages
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(responses.shuffled().first()))
                .subscribe()
    }

}
