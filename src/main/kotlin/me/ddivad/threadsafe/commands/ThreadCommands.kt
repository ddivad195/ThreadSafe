package me.ddivad.threadsafe.commands

import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.embeds.createThreadStatsEmbed
import me.ddivad.threadsafe.embeds.createThreadStatsEmbedForChannel
import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun threadCommands(configuration: Configuration) = commands("Thread") {
    command("stats") {
        description = "View thread stats for a channel"
        execute {
            val guildConfiguration = configuration[guild.id] ?: return@execute
            respond { createThreadStatsEmbed(guild, guildConfiguration.stats) }
        }

        execute(ChannelArg) {
            val channel = args.first
            val guildConfiguration = configuration[guild.id] ?: return@execute
            val threadsForChannel = guildConfiguration.stats.filter { it.channelId == channel.id }
            respond { createThreadStatsEmbedForChannel(guild, guildConfiguration.stats, channel) }
        }
    }
}