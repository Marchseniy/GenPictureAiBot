package com.marchseniy.GenPictureAiBot.commands;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public abstract class Command {
    private final String name;
    private final String description;
    private final int order;

    public Command(String name, String description, int order) {
        this.name = name;
        this.description = description;
        this.order = order;
    }

    public abstract void execute(Update update);
}
