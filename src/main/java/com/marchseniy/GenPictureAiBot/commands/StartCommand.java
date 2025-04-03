package com.marchseniy.GenPictureAiBot.commands;

import com.marchseniy.GenPictureAiBot.service.TelegramBot;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand extends Command {
    @Getter
    private final String name = "start";
    @Getter
    private final String description = "Запускает бота";
    @Getter
    private final int order = -1;

    private final TelegramBot bot;

    @Lazy
    public StartCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String answer;

        answer = "Список команд:\n\n" + bot.getCommandsDescription();

        bot.sendMessage(chatId, answer, null);
    }
}
