package me.ddivad.threadsafe.commands

import dev.kord.common.entity.ArchiveDuration
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.getChannelOf
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.toJavaInstant
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.Permissions
import me.ddivad.threadsafe.embeds.createNewThreadEmbed
import me.ddivad.threadsafe.embeds.createThreadStatsEmbed
import me.ddivad.threadsafe.embeds.createThreadStatsEmbedForChannel
import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.TimeStamp
import me.jakejmattson.discordkt.extensions.TimeStyle
import me.jakejmattson.discordkt.extensions.jumpLink
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Suppress("unused")
fun threadCommands(configuration: Configuration) = commands("Thread") {
    slash("stats", "View thread stats for a channel") {
        execute(ChannelArg<TextChannel>("Channel", "A channel to view stats for").optionalNullable(null)) {
            val channel = args.first
            val guildConfiguration = configuration[guild.id] ?: return@execute
            if (channel != null) {
                respondPublic("") { createThreadStatsEmbedForChannel(guild, guildConfiguration.stats, channel) }
            } else {
                respondPublic("") { createThreadStatsEmbed(guild, guildConfiguration.stats) }
            }
        }
    }

    message("Post as thread", "thread", "Post message as a thread", Permissions.EVERYONE) {
        val response = interaction?.deferEphemeralResponse()
        val guildConfiguration = configuration[guild.id] ?: return@message

        if (ChronoUnit.HOURS.between(arg.timestamp.toJavaInstant(), Instant.now()) < 1) {
            response?.respond {
                content = "You can only bump your question with a thread after 1 hour. Try again in ${
                    TimeStamp.at(
                        arg.timestamp.toJavaInstant().plus(Duration.ofHours(1)),
                        TimeStyle.RELATIVE
                    )
                }"
            }
            return@message
        }

        val thread = this.channel.asChannelOf<TextChannel>()
            .startPublicThreadWithMessage(this.arg.id, "${this.arg.content.take(96)}...", ArchiveDuration.Day)
        guild.getChannelOf<TextChannel>(guildConfiguration.threadChannel)
            .createMessage { embed { createNewThreadEmbed(thread, arg) } }
        response?.respond { content = "Thread started: ${thread.lastMessage?.asMessage()?.jumpLink()}" }
    }
}