package me.apexfreedom.apexfreedommod.commands;

import lombok.Getter;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FMsg;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import org.bukkit.ChatColor;

public class AF_CommandLoader extends FreedomService
{

    @Getter
    private final SimpleCommandHandler<TotalFreedomMod> handler;

    public AF_CommandLoader(TotalFreedomMod plugin)
    {
        super(plugin);
        
        handler = new SimpleCommandHandler<>(plugin);
    }

    @Override
    protected void onStart()
    {
        handler.clearCommands();
        handler.setExecutorFactory(new AF_CommandExecutor.FreedomExecutorFactory(plugin));
        handler.setCommandClassPrefix("Command_");
        handler.setPermissionMessage(FMsg.F + ChatColor.RED + "No permisions to use this Command!");
        handler.setOnlyConsoleMessage(ChatColor.RED + "This command can only be used from the console.");
        handler.setOnlyPlayerMessage(ChatColor.RED + "This command can only be used by players.");

        handler.loadFrom(AF_Command.class.getPackage());
        handler.registerAll("TotalFreedomMod", true);

        FLog.info("Loaded " + handler.getExecutors().size() + " commands.");
    }

    @Override
    protected void onStop()
    {
        handler.clearCommands();
    }

}
