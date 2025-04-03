package com.marchseniy.GenPictureAiBot.service;

import com.marchseniy.GenPictureAiBot.commands.Command;
import com.marchseniy.GenPictureAiBot.commands.support.MessageHandler;
import com.marchseniy.GenPictureAiBot.config.BotConfig;
import com.marchseniy.GenPictureAiBot.service.exceptions.CommandNotFoundException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandManager commandManager;
    private final List<MessageHandler> messageHandlers;

    public TelegramBot(BotConfig config, CommandManager commandManager,
                       List<MessageHandler> messageHandlers) {
        this.config = config;
        this.commandManager = commandManager;
        this.messageHandlers = messageHandlers;

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
            messageHandlers.forEach(messageHandler -> messageHandler.onMessage(update, text));
        }
    }

    public Message sendMessage(long chatId, String textToSend, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(replyKeyboard);

        try {
            return execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPhoto(Long chatId, String imageUrl) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);

        InputFile photo = new InputFile(imageUrl);
        sendPhoto.setPhoto(photo);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhotoByBase64(Long chatId, String base64Image, ReplyKeyboard replyKeyboard) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

            String fileName = "photo.png";

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(byteArrayInputStream, fileName));
            sendPhoto.setReplyMarkup(replyKeyboard);

            execute(sendPhoto);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public ReplyKeyboardMarkup getKeyboard(List<String> elements) {
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
