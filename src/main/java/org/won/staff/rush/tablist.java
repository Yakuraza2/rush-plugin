package org.won.staff.rush;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class tablist {

    private final Rush main;
    public tablist(Rush main) {
        this.main = main;
    }

    public void present(Player p){
        p.setPlayerListFooter("Footer");
        p.setPlayerListHeader("Header");
        p.setPlayerListHeaderFooter("Header", "Footer");
    }

    public void setplayer(Player p, char categorie){ //'y' for yellow, 'p' for purple, 's' for spectator
        switch(categorie){
            case 'y':
                p.setPlayerListName(main.getConfig().getString("gui.display-names.yellow") + ChatColor.YELLOW + p.getDisplayName() );
                p.setDisplayName(main.getConfig().getString("gui.display-names.yellow") + p.getName() + ChatColor.RESET);
                break;
            case 'p':
                p.setPlayerListName(main.getConfig().getString("gui.display-names.purple") + ChatColor.LIGHT_PURPLE + p.getDisplayName());
                break;
            case 's':
                p.setPlayerListName(ChatColor.GRAY + "§iSPECT " + ChatColor.GRAY + p.getDisplayName() );
                break;
            default:
                p.setPlayerListName(ChatColor.BLUE + p.getName() );
                break;
        }

    }
}
