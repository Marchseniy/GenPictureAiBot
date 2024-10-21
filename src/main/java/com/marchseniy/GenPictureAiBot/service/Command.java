package com.marchseniy.GenPictureAiBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String getName();
    String getDescription();
    int getOrder();
    void execute(Update update);
}
