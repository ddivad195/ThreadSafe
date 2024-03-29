package me.ddivad.threadsafe.embeds

import dev.kord.common.kColor
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.thread.ThreadChannel
import dev.kord.rest.builder.message.EmbedBuilder
import me.jakejmattson.discordkt.extensions.*
import java.awt.Color

fun EmbedBuilder.createNewThreadEmbed(thread: ThreadChannel, message: Message) {
    title = "New thread in ${message.channel.mention} (${thread.mention})"
    color = Color.MAGENTA.kColor
    author {
      icon = message.author?.pfpUrl
      name = message.author?.username
    }
    description = message.content.take(4000)
    message.author?.mention?.let { addField("", "Created By: $it ${TimeStamp.now(TimeStyle.RELATIVE)}") }
}