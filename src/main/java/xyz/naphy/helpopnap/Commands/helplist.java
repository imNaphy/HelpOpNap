package xyz.naphy.helpopnap.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.naphy.helpopnap.HelpClass;
import xyz.naphy.helpopnap.HelpOpNap;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class helplist implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (HelpClass.helps.isEmpty()) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("No Problems").getString().replace("%player%", ((Player) invocation.source()).getUsername()))));
            return;
        }
        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Help List").getString().replace("%player%", ((Player) invocation.source()).getUsername()))));
        for (HelpClass.HelpOpClass help : HelpClass.helps) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Help Format").getString().replace("%player%", help.player.getUsername()).replace("%problem%", help.problem)))
                    .clickEvent(ClickEvent.suggestCommand("/respond " + help.ID + " "))
                    .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Hover Message for List").getString().replace("%player%", help.player.getUsername()))))));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("helpopnap.staff");
    }

    public String replaceHex(String string) {
        String text = string.replace("&0", "<reset><#000000>")
                .replace("&1", "<reset><#0000AA>")
                .replace("&2", "<reset><#00AA00>")
                .replace("&3", "<reset><#00AAAA>")
                .replace("&4", "<reset><#AA0000>")
                .replace("&5", "<reset><#AA00AA>")
                .replace("&6", "<reset><#FFAA00>")
                .replace("&7", "<reset><#AAAAAA>")
                .replace("&8", "<reset><#555555>")
                .replace("&9", "<reset><#5555FF>")
                .replace("&a", "<reset><#55FF55>")
                .replace("&b", "<reset><#55FFFF>")
                .replace("&c", "<reset><#FF5555>")
                .replace("&d", "<reset><#FF55FF>")
                .replace("&e", "<reset><#FFFF55>")
                .replace("&f", "<reset><#FFFFFF>")
                .replace("&l", "<b>")
                .replace("&o", "<i>")
                .replace("&n", "<u>")
                .replace("&m", "<st>")
                .replace("&k", "<obf>")
                .replace("&r", "<reset>");

        return text;
    }
}
