package me.ddivad.threadsafe.dataclasses

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import dev.kord.core.entity.channel.Channel
import kotlinx.serialization.Serializable
import me.jakejmattson.discordkt.dsl.Data

@Serializable
data class Configuration(
        val ownerId: String = "insert-owner-id",
        var prefix: String = "++",
        val guildConfigurations: MutableMap<ULong, GuildConfiguration> = mutableMapOf()
) : Data() {
    operator fun get(id: Snowflake) = guildConfigurations[id.value]
    fun hasGuildConfig(guildId: Snowflake) = guildConfigurations.containsKey(guildId.value)

    fun setup(guild: Guild, prefix: String, adminRole: Role, staffRole: Role, notificationChannel: Channel) {
        if (guildConfigurations[guild.id.value] != null) return

        val newConfiguration = GuildConfiguration(
                guild.id.value,
                prefix,
                staffRole.id,
                adminRole.id,
                notificationChannel.id
        )
        guildConfigurations[guild.id.value] = newConfiguration
        save()
    }
}

@Serializable
data class GuildConfiguration(
    val id: ULong,
    var prefix: String = "++",
    var staffRoleId: Snowflake,
    var adminRoleId: Snowflake,
    var notificationChannel: Snowflake
)