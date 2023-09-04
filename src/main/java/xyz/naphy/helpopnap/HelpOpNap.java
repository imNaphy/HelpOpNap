package xyz.naphy.helpopnap;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import xyz.naphy.helpopnap.Commands.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = "helpopnap",
        name = "HelpOpNap",
        version = "1.0",
        authors = "naphy"
)
public class HelpOpNap {

    private Logger logger;
    private ProxyServer server;
    public static HelpOpNap plugin;
    public static ProxyServer publicServer;
    public static ConfigurationNode config;
    public static Path dataDirectory;
    public static boolean pluginEnabled;

    @Inject
    public HelpOpNap(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.server = server;
        publicServer = server;
        HelpOpNap.dataDirectory = dataDirectory;
        HelpOpNap.plugin = this;

        logger.info("The plugin has been loaded!");
    }

    @Subscribe
    public void commandLoader(ProxyInitializeEvent event) {
        server.getCommandManager().register(server.getCommandManager().metaBuilder("helpop")
                .plugin(this)
                .build(),
                new helpop()
        );
        server.getCommandManager().register(server.getCommandManager().metaBuilder("helpopnap")
                        .plugin(this)
                        .build(),
                new helpopnap()
        );
        server.getCommandManager().register(server.getCommandManager().metaBuilder("respond")
                        .plugin(this)
                        .build(),
                new respond()
        );
        server.getCommandManager().register(server.getCommandManager().metaBuilder("helplist")
                        .plugin(this)
                        .build(),
                new helplist()
        );
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        List<HelpClass.HelpOpClass> temp = new ArrayList<>();
        for (HelpClass.HelpOpClass help : HelpClass.helps) {
            if (event.getPlayer().equals(help.player)) {
                temp.add(help);
            }
        }
        if (!temp.isEmpty()) {
            for (HelpClass.HelpOpClass help : temp) {
                if (HelpClass.helps.size() <= 1) {
                    HelpClass.helps = new ArrayList<>();
                }
                else {
                    HelpClass.helps.remove(help);
                }
            }
        }
    }

    @Subscribe
    public void configLoader(ProxyInitializeEvent event) throws IOException {
        if (Files.notExists(dataDirectory)) {
            Files.createDirectory(dataDirectory);
        }
        final Path config = dataDirectory.resolve("config.yml");
        if (Files.notExists(config)) {
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(stream, config);
            }
        }
        final YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(config).build();
        HelpOpNap.config = loader.load();
        pluginEnabled = HelpOpNap.config.getNode("Enabled").getBoolean();
    }
}
