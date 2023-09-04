package xyz.naphy.helpopnap.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.naphy.helpopnap.HelpClass;
import xyz.naphy.helpopnap.HelpOpNap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class helpop implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!HelpOpNap.pluginEnabled) return;

        HelpClass.HelpOpClass temp = new HelpClass.HelpOpClass();
        temp.player = (Player) invocation.source();
        temp.ID = GenerateSecret(8);
        temp.problem = String.join(" ", invocation.arguments());
        HelpClass.helps.add(temp);

        Component component = MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Helpop Message").getString().replace("%player%", ((Player) invocation.source()).getUsername()).replace("%message%", String.join(" ", invocation.arguments()))))
                .clickEvent(ClickEvent.suggestCommand("/respond " + temp.ID + " "))
                .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Hover Message").getString().replace("%player%", ((Player) invocation.source()).getUsername()).replace("%message%", String.join(" ", invocation.arguments()))))));

        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex(HelpOpNap.config.getNode("Message Sent").getString().replace("%player%", ((Player) invocation.source()).getUsername()).replace("%message%", String.join(" ", invocation.arguments())))));
        for (Player player : HelpOpNap.publicServer.getAllPlayers()) {
            if (player.hasPermission("helpopnap.staff")) {
                player.sendMessage(component);
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<String> choices = new ArrayList<>();
        choices.add("<message>");
        return CompletableFuture.completedFuture(choices);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("helpopnap.helpop");
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

    public static String GenerateSecret(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int)(AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
