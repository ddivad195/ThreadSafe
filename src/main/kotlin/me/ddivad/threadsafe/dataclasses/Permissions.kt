package me.ddivad.threadsafe.dataclasses

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

@Suppress("unused")
object Permissions {
    val GUILD_OWNER = Permissions(Permission.ManageGuild)
    val ADMINISTRATOR = Permissions(Permission.ManageGuild)
    val STAFF = Permissions(Permission.ManageThreads)
    val EVERYONE = Permissions(Permission.UseApplicationCommands)
}