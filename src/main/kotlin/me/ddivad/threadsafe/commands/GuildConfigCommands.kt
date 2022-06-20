package me.ddivad.threadsafe.commands

import me.ddivad.threadsafe.conversations.ConfigurationConversation
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.EveryArg
import me.jakejmattson.discordkt.arguments.RoleArg
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun guildConfigCommands(configuration: Configuration) = commands("Configuration") {
    command("setup") {
        description = "Configure a guild to use this bot."
        requiredPermission = Permissions.ADMINISTRATOR
        execute {
            if (configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            ConfigurationConversation(configuration)
                    .createConfigurationConversation(guild)
                    .startPublicly(discord, author, channel)
            respond("${guild.name} setup")
        }
    }

    slash("setprefix") {
        description = "Set the bot prefix."
        requiredPermission = Permissions.ADMINISTRATOR
        execute(EveryArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Please run the **configure** command to set this initially.")
                return@execute
            }
            val prefix = args.first
            configuration[guild.id]?.prefix = prefix
            configuration.save()
            respondPublic("Prefix set to: **$prefix**", null)
        }
    }

    slash("setstaffrole") {
        description = "Set the bot staff role."
        requiredPermission = Permissions.ADMINISTRATOR
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Please run the **configure** command to set this initially.")
                return@execute
            }
            val role = args.first
            configuration[guild.id]?.staffRoleId = role.id
            configuration.save()
            respondPublic("Role set to: **${role.name}**", null)
        }
    }

    slash("setadminrole") {
        description = "Set the bot admin role."
        requiredPermission = Permissions.ADMINISTRATOR
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Please run the **configure** command to set this initially.")
                return@execute
            }
            val role = args.first
            configuration[guild.id]?.adminRoleId = role.id
            configuration.save()
            respondPublic("Role set to: **${role.name}**", null)
        }
    }
}