package me.ddivad.threadsafe

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.Permissions
import me.ddivad.threadsafe.services.BotStatsService
import me.jakejmattson.discordkt.dsl.bot
import me.jakejmattson.discordkt.extensions.addInlineField
import me.jakejmattson.discordkt.extensions.pfpUrl
import java.awt.Color

@KordPreview
@PrivilegedIntent
suspend fun main() {
    val token = System.getenv("BOT_TOKEN") ?: null
    val prefix = System.getenv("DEFAULT_PREFIX") ?: "<none>"

    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        val configuration = data("config/config.json") { Configuration() }

        prefix {
            guild?.let { configuration[it.id]?.prefix } ?: prefix
        }

        configure {
            allowMentionPrefix = true
            commandReaction = null
            theme = Color.MAGENTA
            permissions(Permissions.STAFF)
            intents = Intents(
                Intent.Guilds
            )
        }

        mentionEmbed {
            val botStats = it.discord.getInjectionObjects(BotStatsService::class)
            val channel = it.channel
            val self = channel.kord.getSelf()

            color = it.discord.configuration.theme

            thumbnail {
                url = self.pfpUrl
            }

            field {
                name = self.tag
                value = "A Discord bot to keep track of thread creation and usage."
            }

            addInlineField("Prefix", it.prefix())
            addInlineField("Contributors", "ddivad#0001")

            val kotlinVersion = KotlinVersion.CURRENT
            val versions = it.discord.versions
            field {
                name = "Build Info"
                value = "```" +
                        "Version: 1.0.0\n" +
                        "DiscordKt: ${versions.library}\n" +
                        "Kotlin: $kotlinVersion" +
                        "```"
            }

            field {
                name = "Uptime"
                value = botStats.uptime
            }
            field {
                name = "Ping"
                value = botStats.ping
            }
        }
    }
}