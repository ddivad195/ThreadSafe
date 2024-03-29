package me.ddivad.threadsafe.listeners

import dev.kord.core.behavior.getChannelOf
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.channel.thread.ThreadChannelCreateEvent
import dev.kord.core.event.channel.thread.ThreadUpdateEvent
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.ThreadStats
import me.jakejmattson.discordkt.dsl.edit
import me.jakejmattson.discordkt.dsl.listeners
import me.jakejmattson.discordkt.extensions.idDescriptor
import java.util.*

@Suppress("unused")
fun onThreadCreate(configuration: Configuration) = listeners {
    on<ThreadChannelCreateEvent> {
        val guild = channel.getGuildOrNull() ?: return@on
        val guildConfiguration = configuration[guild.asGuild().id] ?: return@on
        configuration.edit {
            guildConfiguration.stats.add(ThreadStats(channel.id, channel.parentId, Date().time / 1000))
        }

        guild
            .getChannelOf<TextChannel>(guildConfiguration.notificationChannel)
            .createMessage("""
                **Thread Created: ${channel.mention} (${channel.parent.mention})**
                **Created By:** ${channel.owner.asUser().idDescriptor()} 
            """.trimIndent())
    }
    on<ThreadUpdateEvent> {
        println(this.channel.isArchived)
    }
}