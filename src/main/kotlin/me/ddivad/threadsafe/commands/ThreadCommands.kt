package me.ddivad.threadsafe.commands

import dev.kord.core.entity.channel.TextChannel
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.embeds.createThreadStatsEmbed
import me.ddivad.threadsafe.embeds.createThreadStatsEmbedForChannel
import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun threadCommands(configuration: Configuration) = commands("Thread") {
    slash("stats") {
        description = "View thread stats for a channel"
        execute(ChannelArg<TextChannel>("Channel", "A channel to view stats for").optionalNullable(null)) {
            val guildConfiguration = configuration[guild.id] ?: return@execute
            val channel = args.first

            if (channel != null) {
                val guildConfiguration = configuration[guild.id] ?: return@execute
                val threadsForChannel = guildConfiguration.stats.filter { it.channelId == channel.id }
                respondPublic("") { createThreadStatsEmbedForChannel(guild, guildConfiguration.stats, channel) }
            } else {
                respondPublic("") { createThreadStatsEmbed(guild, guildConfiguration.stats) }
            }
        }
    }
}