package com.marchseniy.GenPictureAiBot.commands;

import com.marchseniy.GenPictureAiBot.service.Command;
import com.marchseniy.GenPictureAiBot.service.TelegramBot;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateImageCommand implements Command {
    @Getter
    private final String name = "createimage";
    @Getter
    private final String description = "Создает изображение";
    @Getter
    private final int order = 1;
    private final TelegramBot bot;

    @Lazy
    public CreateImageCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();

        bot.sendMessage(chatId, "Введите промпт", null);
    }
}
