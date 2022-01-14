package me.ddivad.threadsafe.embeds

import dev.kord.common.entity.Snowflake
import dev.kord.common.kColor
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.Image
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import me.ddivad.threadsafe.dataclasses.ThreadStats
import me.jakejmattson.discordkt.extensions.toTimeString
import java.awt.Color

suspend fun EmbedBuilder.createThreadStatsEmbed(guild: Guild, threadStats: MutableList<ThreadStats>) {
    title = "Thread Statistics"
    color = Color.MAGENTA.kColor

    val filtered = threadStats.fold(HashMap<Snowflake, Int>()) { acc, next ->
        acc[next.channelId] = acc.getOrDefault(next.channelId, 0) + 1
        return@fold acc
    }.entries.map { "${guild.getChannel(it.key).mention} - ${it.value}" }

    description = """
        Showing total threads created:
        
        ${filtered.joinToString("\n")}
    """.trimIndent()

    footer {
        icon = guild.getIconUrl(Image.Format.PNG) ?: ""
        text = guild.name
    }
}

suspend fun EmbedBuilder.createThreadStatsEmbedForChannel(guild: Guild, threadStats: MutableList<ThreadStats>, channel: TextChannel) {
    val channelThreads = threadStats.filter { it.parentChannelId == channel.id }.sortedBy { it.dateTime }
    val mostRecentThread = channelThreads.lastOrNull()

    title = "Thread Statistics for **#${channel.name}**"
    color = Color.MAGENTA.kColor
    description = """
        Total threads created: **${channelThreads.size}**
        Current active threads: **${channel.activeThreads.count()}**
        ${if(mostRecentThread != null) "Last thread created: ${guild.getChannel(mostRecentThread.channelId).mention} (<t:${mostRecentThread.dateTime}:R>)" else ""}
    """.trimIndent()

    footer {
        icon = guild.getIconUrl(Image.Format.PNG) ?: ""
        text = guild.name
    }
}