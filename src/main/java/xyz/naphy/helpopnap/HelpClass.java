package xyz.naphy.helpopnap;

import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpClass {

    public static class HelpOpClass {
        public String ID;
        public Player player;
        public String problem;
    }

    public static List<HelpOpClass> helps = new ArrayList<>();
}
