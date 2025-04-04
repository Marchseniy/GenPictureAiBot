package com.marchseniy.GenPictureAiBot.commands;

import com.marchseniy.GenPictureAiBot.client.FusionBrainClient;
import com.marchseniy.GenPictureAiBot.commands.support.MessageHandler;
import com.marchseniy.GenPictureAiBot.commands.support.states.PromptState;
import com.marchseniy.GenPictureAiBot.commands.support.states.UserState;
import com.marchseniy.GenPictureAiBot.service.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@Component
public class CreateImageCommand extends Command implements MessageHandler {
    private final TelegramBot bot;
    private final FusionBrainClient fusionBrainClient;
    private final HashMap<Long, UserState> userCreateImageStates = new HashMap<>();

    @Lazy
    public CreateImageCommand(TelegramBot bot, FusionBrainClient fusionBrainClient) {
        super("createimage", "Создает изображение", 1);

        this.bot = bot;
        this.fusionBrainClient = fusionBrainClient;
    }

    @Override
    public void execute(Update update) {
        long userId = update.getMessage().getFrom().getId();

        UserState userState;
        if (!userCreateImageStates.containsKey(userId)) {
            userState = new PromptState(bot, update, fusionBrainClient);
            userCreateImageStates.put(userId, userState);
        }
        else {
            userState = userCreateImageStates.get(userId);
        }

        userState.action();
    }

    @Override
    public void onMessage(Update update, String txt) {
        long userId = update.getMessage().getFrom().getId();
        if (!userCreateImageStates.containsKey(userId)) {
            return;
        }

        UserState userState = userCreateImageStates.get(userId);
        userState.onParameter(txt);

        if (userState.isPassed()) {
            UserState nextUserState = userState.getNextState();
            if (nextUserState != null) {
                userCreateImageStates.put(userId, nextUserState);
                nextUserState.action();
                if (nextUserState.getNextState() == null) {
                    userCreateImageStates.remove(userId);
                }
            }
        }
    }
}
