package me.totalfreedom.totalfreedommod.command;

import lombok.Getter;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FMsg;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import org.bukkit.ChatColor;

// FreedomMod-Command-Loader | BETA: v4.0
public class CmdLoader extends FreedomService
{

    @Getter
    private final SimpleCommandHandler<TotalFreedomMod> handler;

    public CmdLoader(TotalFreedomMod plugin)
    {
        super(plugin);

        handler = new SimpleCommandHandler<>(plugin);
    }

    @Override
    protected void onStart()
    {
        handler.clearCommands();
        handler.setExecutorFactory(new FreedomCommandExecutor.FreedomExecutorFactory(plugin));
        //
        handler.setCommandClassPrefix("C_");
        handler.setPermissionMessage(FMsg.F + ChatColor.RED + "You have no permisions to use this command!");
        handler.setOnlyConsoleMessage(FMsg.F + ChatColor.RED + "This is an Rank 'CONSOLE' Command only!");
        handler.setOnlyPlayerMessage(FMsg.F + ChatColor.RED + "This command can only be used by operators!");
        //
        handler.loadFrom(FreedomCommand.class.getPackage());
        handler.registerAll("TotalFreedomMod", true);
        FLog.info("Loading all 'TotalFreedomMod' Commands under the 'C_' tag.");
        FLog.info("Loaded " + handler.getExecutors().size() + " commands.");
    }

    @Override
    protected void onStop()
    {
        handler.clearCommands();
    }

}
