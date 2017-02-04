package me.totalfreedom.totalfreedommod.rank;

// Misc
import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{

    TF_DEVELOPER("a", "Developer", ChatColor.DARK_PURPLE, "TF-Developer"),
    DEVELOPER("a", "ApexFreedomMod Developer", ChatColor.DARK_PURPLE, "AF-Developer"),
    COS("the", "Chief of Security", ChatColor.RED, "Chief of Security"),
    EXE("a", "Fourm Administrator", ChatColor.RED, "Administrator"),
    FOUNDER("the", "Main Owner", ChatColor.RED, "Founder"),
    CO_OWNER("a", "Co Owner", ChatColor.RED, "Co-Owner");

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final ChatColor color;

    private Title(String determiner, String name, ChatColor color, String tag)
    {
        this.determiner = determiner;
        this.name = name;
        this.tag = "[" + tag + "]";
        this.coloredTag = ChatColor.DARK_GRAY + "[" + color + tag + ChatColor.DARK_GRAY + "]" + color;
        this.color = color;
    }

    @Override
    public String getColoredName()
    {
        return color + name;
    }

    @Override
    public String getColoredLoginMessage()
    {
        return determiner + " " + color + ChatColor.ITALIC + name;
    }

}
