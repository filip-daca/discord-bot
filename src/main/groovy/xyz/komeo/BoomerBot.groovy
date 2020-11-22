package xyz.komeo

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.object.entity.Message
import xyz.komeo.reaction.JohnDeereReaction
import xyz.komeo.reaction.NiceReaction;

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

        NiceReaction.consume(validMessages)
        JohnDeereReaction.consume(validMessages)

        client.onDisconnect().block()
    }
}
