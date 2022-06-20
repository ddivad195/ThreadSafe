package me.ddivad.threadsafe.dataclasses


import me.jakejmattson.discordkt.dsl.Permission
import me.jakejmattson.discordkt.dsl.PermissionSet
import me.jakejmattson.discordkt.dsl.permission
import me.jakejmattson.discordkt.extensions.toSnowflake

object Permissions : PermissionSet {
    val BOT_OWNER = permission("Bot Owner") { users(discord.getInjectionObjects<Configuration>().ownerId.toSnowflake()) }
    val GUILD_OWNER = permission("Guild Owner") { guild?.ownerId?.let { users(it) } }
    val ADMINISTRATOR = permission("Administrator") {
        discord.getInjectionObjects<Configuration>()[guild!!.id]?.adminRoleId?.let {
            roles(it)
        }
    }
    val STAFF = permission("Staff") {
        discord.getInjectionObjects<Configuration>()[guild!!.id]?.staffRoleId?.let {
            roles(it)
        }
    }
    val NONE = permission("None") { guild?.everyoneRole?.let { roles(it.id) } }
    override val hierarchy: List<Permission> = listOf(NONE, STAFF, ADMINISTRATOR, GUILD_OWNER, BOT_OWNER)
    override val commandDefault: Permission = STAFF
}