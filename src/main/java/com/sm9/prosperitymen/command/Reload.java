package com.sm9.prosperitymen.command;

import com.sm9.prosperitymen.common.Config.MainConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class Reload extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "pmreload";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender commandSender) {
        return "pmreload";
    }

    @Override
    public void execute(@Nonnull MinecraftServer localServer, ICommandSender commandSender, @Nonnull String[] args) {
        MainConfig.loadConfig();
        commandSender.sendMessage(new TextComponentString("Prosperity Men config reloaded successfully!"));
    }
}