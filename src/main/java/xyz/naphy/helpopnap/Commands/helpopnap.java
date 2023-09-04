package xyz.naphy.helpopnap.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import xyz.naphy.helpopnap.HelpOpNap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class helpopnap implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length == 0) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex("&b[HelpOpNap] &7List of available commands: \n\n &b/helpopnap toggle <on/off> &7- Turns the plugin on/off.\n &b/helpopnap reload &7- Reloads the plugin.")));
            return;
        }
        if (invocation.arguments()[0].equalsIgnoreCase("reload")) {
            if (Files.notExists(HelpOpNap.dataDirectory)) {
                try {
                    Files.createDirectory(HelpOpNap.dataDirectory);
                } catch (IOException e) {
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin <#AA0000>failed <#AAAAAA>to reload!"));
                    throw new RuntimeException(e);
                }
            }
            final Path config = HelpOpNap.dataDirectory.resolve("config.yml");
            if (Files.notExists(config)) {
                try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    Files.copy(stream, config);
                } catch (IOException e) {
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin <#AA0000>failed <#AAAAAA>to reload!"));
                    throw new RuntimeException(e);
                }
            }
            final YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(config).build();
            try {
                HelpOpNap.config = loader.load();
            } catch (IOException e) {
                invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin <#AA0000>failed <#AAAAAA>to reload!"));
                throw new RuntimeException(e);
            }
            HelpOpNap.pluginEnabled = HelpOpNap.config.getNode("Enabled").getBoolean();
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin has been reloaded <#55FF55>successfully<#AAAAAA>!"));
        } else if (invocation.arguments()[0].equalsIgnoreCase("toggle")) {
            if (invocation.arguments()[1].equalsIgnoreCase("on")) {
                if (HelpOpNap.pluginEnabled) {
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin is already <#55FF55>enabled<#AAAAAA>!"));
                } else {
                    HelpOpNap.pluginEnabled = true;
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin has been <#55FF55>enabled<#AAAAAA>!"));
                }
            } else if (invocation.arguments()[1].equalsIgnoreCase("off")) {
                if (!HelpOpNap.pluginEnabled) {
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin is already <#FF5555>disabled<#AAAAAA>!"));
                } else {
                    HelpOpNap.pluginEnabled = false;
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<#55FFFF>[HelpOpNap] <#AAAAAA>The plugin has been <#FF5555>disabled<#AAAAAA>!"));
                }
            }
        } else {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(replaceHex("&b[HelpOpNap] &7List of available commands: \n\n &b/helpopnap toggle <on/off> &7- Turns the plugin on/off.\n &b/helpopnap reload &7- Reloads the plugin.")));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (invocation.arguments().length == 1) {
            List<String> choices = new ArrayList<>();
            choices.add("toggle");
            choices.add("reload");
            return CompletableFuture.completedFuture(choices);
        }
        if (invocation.arguments()[0].equalsIgnoreCase("toggle")) {
            List<String> toggle = new ArrayList<>();
            toggle.add("off");
            toggle.add("on");
            return CompletableFuture.completedFuture(toggle);
        }
        return CompletableFuture.completedFuture(List.of());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("helpopnap.manager");
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
