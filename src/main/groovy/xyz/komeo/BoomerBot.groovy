package xyz.komeo

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import xyz.komeo.command.BasicCommand
import xyz.komeo.flow.ConsumesFlux
import xyz.komeo.memory.BotMemory
import xyz.komeo.reaction.JohnDeereReaction
import xyz.komeo.reaction.NiceReaction
import xyz.komeo.reaction.SonReaction;

class BoomerBot {

    def client
    BotMemory memory

    Set<ConsumesFlux> commands = [
            new BasicCommand()
    ]

    Set<ConsumesFlux> reactions = [
            new SonReaction(),
            new NiceReaction(memory),
            new JohnDeereReaction()
    ]

    void initialize() {
        memory = new BotMemory()

        client = DiscordClientBuilder.create(System.getenv('ACCESS_TOKEN')).build().login().block()

        client.getEventDispatcher().on(ReadyEvent)
                .subscribe(event -> {
                    def self = event.getSelf();
                    println "Logged in as ${self.getUsername()} ${self.getDiscriminator()}"
                })
    }

    void registerActions() {
        def validMessages = client.getEventDispatcher().on(MessageCreateEvent)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))

        def commandMessages = validMessages
                .filter(message -> message.getContent().startsWith("!boomer"))

        commands.each { reaction ->
            reaction.consume(commandMessages)
        }

        def nonCommandMessages = validMessages
                .filter(message -> !message.getContent().startsWith("!"))

        reactions.each { reaction ->
            reaction.consume(nonCommandMessages)
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
