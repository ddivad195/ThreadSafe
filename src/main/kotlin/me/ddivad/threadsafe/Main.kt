package me.ddivad.threadsafe

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.threadsafe.dataclasses.Configuration
import me.ddivad.threadsafe.dataclasses.Permissions
import me.jakejmattson.discordkt.dsl.bot
import java.awt.Color

@KordPreview
@PrivilegedIntent
fun main() {
    val token = System.getenv("BOT_TOKEN") ?: null

    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        val configuration = data("config/config.json") { Configuration() }
        prefix {
            "/"
        }
        configure {
            deleteInvocation = false
            mentionAsPrefix = true
            commandReaction = null
            theme = Color.MAGENTA
            defaultPermissions = Permissions.STAFF
            intents = Intents(
                Intent.Guilds
            )
        }
    }
}