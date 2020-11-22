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
            .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
            .flatMap(Message::getChannel)
            .flatMap(channel -> channel.createMessage("Pong!"))
            .subscribe();

        client.onDisconnect().block();
    }
}