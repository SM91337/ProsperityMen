package com.sm9.prosperitymen;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import static com.sm9.prosperitymen.ProsperityMen.*;

public class CmdReload extends CommandBase
{
    @Override
    public String getName() {
        return "pmreload";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "pmreload";
    }

    @Override
    public void execute(MinecraftServer localServer, ICommandSender commandSender, String[] sArgs) throws CommandException {
        loadConfig();
        commandSender.sendMessage(new TextComponentString("Prosperity Men config reloaded successfully!"));
    }
}