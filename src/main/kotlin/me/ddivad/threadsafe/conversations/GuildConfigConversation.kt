package me.ddivad.threadsafe.conversations

import dev.kord.core.entity.Guild
import me.ddivad.threadsafe.dataclasses.Configuration
import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.arguments.EveryArg
import me.jakejmattson.discordkt.arguments.RoleArg
import me.jakejmattson.discordkt.conversations.conversation

class ConfigurationConversation(private val configuration: Configuration) {
    fun createConfigurationConversation(guild: Guild) = conversation {
        val prefix = prompt(EveryArg, "Bot prefix:")
        val adminRole = prompt(RoleArg, "Admin role:")
        val staffRole = prompt(RoleArg, "Staff role:")
        val notificationChannel = prompt(ChannelArg, "Thread notification channel:")

        configuration.setup(guild, prefix, adminRole, staffRole, notificationChannel)
    }
}