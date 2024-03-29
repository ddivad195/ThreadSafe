package me.ddivad.threadsafe.commands

import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun guildConfigCommands(configuration: Configuration) = commands("Configuration") {
    slash("setup", "Set up a guild to use ThreadSafe", Permissions.ADMINISTRATOR) {
        execute(
            ChannelArg("user-channel", "Channel that users will see threads posted to"),
            ChannelArg("staff-channel", "Channel that staff will see threads posted to")
        ) {
            val (userChannel, staffChannel) = args
            configuration.setup(guild, staffChannel.id, userChannel.id)
            respondPublic("bot setup")
        }
    }
}
