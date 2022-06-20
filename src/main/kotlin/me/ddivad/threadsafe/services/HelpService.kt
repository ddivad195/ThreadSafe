package me.ddivad.threadsafe.services

import dev.kord.common.annotation.KordPreview
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.annotations.Service
import me.jakejmattson.discordkt.arguments.Argument
import me.jakejmattson.discordkt.commands.*

@KordPreview
@Service
class HelpService {
    suspend fun sendHelpEmbed(event: SlashCommandEvent<*>, command: Command) = event.respondPublic("") {
        color = event.discord.configuration.theme
        title = command.names.joinToString(", ")
        description = command.description

        val commandInvocation = "${event.prefix()}${command.names.first()}"
        val helpBundle = command.executions.map {
            """$commandInvocation ${it.generateStructure()}
                ${
                it.arguments.joinToString("\n") { arg ->
                    """- ${arg.name}: ${arg.description} (${arg.generateExample(event.context)})
                    """.trimMargin()
                }
            }
            """.trimMargin()
        }
        field {
            this.value = helpBundle.joinToString("\n\n") { it }
        }
    }

    private fun Argument<*, *>.generateExample(context: DiscordContext) =
        runBlocking { generateExamples(context) }
            .takeIf { it.isNotEmpty() }
            ?.random()
            ?: "<Example>"

    private fun Execution<*>.generateStructure() = arguments.joinToString(" ") {
        val type = it.name
        if (it.isOptional()) "[$type]" else type
    }
}