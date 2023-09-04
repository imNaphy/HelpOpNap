package xyz.naphy.helpopnap.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.naphy.helpopnap.HelpClass;
import xyz.naphy.helpopnap.HelpOpNap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class respond implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length < 2) return;
        String args = String.join(" ", invocation.arguments());
        args = args.substring(9);
        for (HelpClass.HelpOpClass help : HelpClass.helps) {
            if (help.ID.equals(invocation.arguments()[0])) {
                help.player.sendMessage(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Player Message").getString().replace("%player%", ((Player) invocation.source()).getUsername()).replace("%message%", String.join(" ", args)).replace("%problem%", help.problem))));
                if (HelpClass.helps.size() <= 1) {
                    HelpClass.helps = new ArrayList<>();
                }
                else {
                    HelpClass.helps.remove(help);
                }
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (invocation.arguments().length == 1) {
            List<String> choices = new ArrayList<>();
            if (!HelpClass.helps.isEmpty()) {
                for (HelpClass.HelpOpClass help : HelpClass.helps) {
                    choices.add(help.ID);
                }
            }
            return CompletableFuture.completedFuture(choices);
        }
        if (invocation.arguments().length > 1) {
            List<String> response = new ArrayList<>();
            response.add("<message>");
            return CompletableFuture.completedFuture(response);
        }
        return CompletableFuture.completedFuture(List.of());
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
