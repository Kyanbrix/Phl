package utilities

import com.github.Kyanbrix.Main
import net.dv8tion.jda.api.entities.Member

object GuildConfig {
    val OWNER_ID : Long = 683613536823279794
    private const val STAFF_ROLE : Long = 1308529359765766294
    private const val MUTE_ROLE : Long = 1315995856029093918
    const val ALLOWLISTED_ROLE : Long = 1231288565950972017
    val BLOCKED_CHANNELS : List<Long> = listOf(942957845522047037,939972627173359708,939972980249886821,1309128784372568065)

    fun isOwner(id : Long) : Boolean {
        return id == OWNER_ID
    }

    fun isStaff (member : Member) : Boolean {
        return member.roles.contains(Main.getInstance().jda.getRoleById(STAFF_ROLE))
    }

    fun isMute(member: Member) : Boolean {
        return member.roles.contains(Main.getInstance().jda.getRoleById(MUTE_ROLE))
    }


}