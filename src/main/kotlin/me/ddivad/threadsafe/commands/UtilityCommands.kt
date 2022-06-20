package me.ddivad.threadsafe.commands

import dev.kord.common.annotation.KordPreview
import me.ddivad.threadsafe.dataclasses.Permissions
import me.ddivad.threadsafe.embeds.buildHelpEmbed
import me.ddivad.threadsafe.services.HelpService
import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.createMenu

@KordPreview
@Suppress("unused")
fun createInformationCommands(helpService: HelpService) = commands("Utility") {
    slash("help") {
        description = "Display help information."
        requiredPermission = Permissions.NONE
        execute(AnyArg("CommandName", "The command you want to see help for").optionalNullable(null)) {
            val input = args.first
            if (input.isNullOrBlank()) {
                val event = this
                respondPublic("Help Menu:", null)
                channel.createMenu { buildHelpEmbed(event) }
            } else {
                val cmd = discord.commands.find { command ->
                    command.names.any { it.equals(input, ignoreCase = true) }
                } ?: return@execute
                helpService.sendHelpEmbed(this, cmd)
            }
        }
    }
}