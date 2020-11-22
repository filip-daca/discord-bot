package xyz.komeo

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.object.entity.Message;

class BoomerBot {

    static void main(args) {
        def client = DiscordClientBuilder.create(System.getenv('ACCESS_TOKEN')).build().login().block();

        client.getEventDispatcher().on(ReadyEvent)
                .subscribe(event -> {
                    def self = event.getSelf();
                    println "Logged in as ${self.getUsername()} ${self.getDiscriminator()}"
                });

        client.getEventDispatcher().on(MessageCreateEvent)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!boomer"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Howdy partner!"))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().contains("boomer"))
                .filter(message -> !message.getContent().startsWith("!"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Are you winning son?"))
                .subscribe();

        client.onDisconnect().block();
    }
}