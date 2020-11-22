package xyz.komeo

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.object.entity.Message
import discord4j.core.object.reaction.ReactionEmoji
import xyz.komeo.reaction.JohnDeereReaction;

class BoomerBot {

    static void main(args) {
        def client = DiscordClientBuilder.create(System.getenv('ACCESS_TOKEN')).build().login().block()

        client.getEventDispatcher().on(ReadyEvent)
                .subscribe(event -> {
                    def self = event.getSelf();
                    println "Logged in as ${self.getUsername()} ${self.getDiscriminator()}"
                })

        client.getEventDispatcher().on(MessageCreateEvent)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!boomer"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Howdy partner!"))
                .subscribe()

        def validMessages = client.getEventDispatcher().on(MessageCreateEvent)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> !message.getContent().startsWith("!"))

        validMessages
                .filter(message -> message.getContent().toLowerCase().contains("boomer"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Are you winning son?"))
                .subscribe()

        validMessages
                .filter(message ->
                        message.getContent().toLowerCase().contains("boomster") ||
                                message.getContent().toLowerCase().contains("boomer") ||
                                message.getContent().toLowerCase().contains("monster") ||
                                message.getContent().toLowerCase().contains("good time") ||
                                message.getContent().toLowerCase().contains("good vibe") ||
                                message.getContent().toLowerCase().contains("nice")
                )
                .flatMap(message -> message.addReaction(ReactionEmoji.custom(Snowflake.of(640976634589085726),
                        "boomster_white", false)))
                .subscribe()

        JohnDeereReaction.consume(validMessages)

        client.onDisconnect().block()
    }
}
