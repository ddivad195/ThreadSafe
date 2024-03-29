package me.ddivad.threadsafe.dataclasses

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import kotlinx.serialization.Serializable
import me.jakejmattson.discordkt.dsl.Data
import me.jakejmattson.discordkt.dsl.edit

@Serializable
data class Configuration(
        var prefix: String = "++",
        val guildConfigurations: MutableMap<ULong, GuildConfiguration> = mutableMapOf()
) : Data() {
    operator fun get(id: Snowflake) = guildConfigurations[id.value]

    fun setup(guild: Guild, notificationChannelId: Snowflake, threadChannelId: Snowflake) {
        if (guildConfigurations[guild.id.value] != null) return

        val newConfiguration = GuildConfiguration(
                guild.id.value,
                notificationChannelId,
                threadChannelId
        )
        edit {
            guildConfigurations[guild.id.value] = newConfiguration
        }
    }
}

@Serializable
data class GuildConfiguration(
    val id: ULong,
    var notificationChannel: Snowflake,
    var threadChannel: Snowflake,
    val stats: MutableList<ThreadStats> = mutableListOf()
)

@Serializable
data class ThreadStats(val channelId: Snowflake, val parentChannelId: Snowflake, val dateTime: Long)