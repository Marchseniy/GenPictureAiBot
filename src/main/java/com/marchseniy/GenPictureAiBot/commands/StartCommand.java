package com.marchseniy.GenPictureAiBot.commands;

import com.marchseniy.GenPictureAiBot.service.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand extends Command {
    private final TelegramBot bot;

    @Lazy
    public StartCommand(TelegramBot bot) {
        super("start", "Запускает бота", -1);

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
