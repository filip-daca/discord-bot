package xyz.komeo

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.object.entity.Message
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.reaction.JohnDeereReaction
import xyz.komeo.reaction.NiceReaction
import xyz.komeo.reaction.SonReaction;

class BoomerBot {

    def client

    Set<ConsumesFlux> reactions = [
            new SonReaction(),
            new NiceReaction(),
            new JohnDeereReaction()
    ]

    void initialize() {
        client = DiscordClientBuilder.create(System.getenv('ACCESS_TOKEN')).build().login().block()

        client.getEventDispatcher().on(ReadyEvent)
                .subscribe(event -> {
                    def self = event.getSelf();
                    println "Logged in as ${self.getUsername()} ${self.getDiscriminator()}"
                })
    }

    void registerActions() {
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

        reactions.each { reaction ->
            reaction.consume(validMessages)
        }
    }

    void registerDisconnect() {
        client.onDisconnect().block()
    }

    static void main(args) {
        println "Bot starting up ..."
        BoomerBot bot = new BoomerBot()
        println "... initializing ..."
        bot.initialize()
        println "... registrating actions ..."
        bot.registerActions()
        println "... registering disconnect ..."
        bot.registerDisconnect()
        println "... up and running"
    }
}
