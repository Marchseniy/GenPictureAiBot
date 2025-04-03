package com.marchseniy.GenPictureAiBot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Command {
    public abstract String getName();
    public abstract String getDescription();
    public abstract int getOrder();
    public abstract void execute(Update update);
}
