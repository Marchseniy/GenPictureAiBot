package com.marchseniy.GenPictureAiBot.service;

import com.marchseniy.GenPictureAiBot.client.FusionBrainClient;
import com.marchseniy.GenPictureAiBot.config.BotConfig;
import com.marchseniy.GenPictureAiBot.service.exceptions.CommandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandManager commandManager;
    private final FusionBrainClient fusionBrainClient;

    @Autowired
    public TelegramBot(BotConfig config, CommandManager commandManager, FusionBrainClient fusionBrainClient) {
        this.config = config;
        this.commandManager = commandManager;
        this.fusionBrainClient = fusionBrainClient;

        setCommandsMenu();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
//        fusionBrainClient.getImage("sunset over mountains", "realistic", 1024, 1024)
//                .thenAccept(response -> {
//                    sendPhoto(update.getMessage().getChatId(), response.getImageUrl());
//                    System.out.println("Image generated: " + response.getImageUrl());
//                })
//                .exceptionally(ex -> {
//                    System.err.println("Error generating image: " + ex.getMessage());
//                    return null;
//                });

        if (!(update.hasMessage() && update.getMessage().hasText())) {
            return;
        }

        String text = update.getMessage().getText();

        if (commandManager.isCommand(text)) {
            try {
                commandManager.executeCommand(text, update);
            } catch (CommandNotFoundException e) {
                reportIncorrectCommand(update);
            }
        }
        else {

        }
    }

    public void sendMessage(long chatId, String textToSend, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(replyKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(Long chatId, String imageUrl) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageUrl));

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public ReplyKeyboard getKeyboard(List<String> elements) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (String element : elements) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(element);
            keyboardRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public String getCommandsDescription() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Command command : commandManager.getCommands()) {
            stringBuilder
                    .append(CommandManager.COMMAND_PREFIX)
                    .append(command.getName())
                    .append(" - ")
                    .append(command.getDescription())
                    .append('\n');
        }

        return stringBuilder.toString();
    }

    private void setCommandsMenu() {
        List<BotCommand> botCommands = new ArrayList<>();

        for (Command command : commandManager.getCommands()) {
            if (command.getOrder() < 1) {
                continue;
            }

            botCommands.add(new BotCommand(command.getName(), command.getDescription()));
        }

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(botCommands);

        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void reportIncorrectCommand(Update update) {
        String answer = "Такой команды не существует.\nВот список всех поддерживаемых мною команд:\n\n" + getCommandsDescription();
        sendMessage(update.getMessage().getChatId(), answer, null);
    }
}
