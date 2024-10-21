package com.marchseniy.GenPictureAiBot.service;

import com.marchseniy.GenPictureAiBot.service.exceptions.CommandNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.List;

@Getter
@Component
public class CommandManager {
    public static final char COMMAND_PREFIX = '/';
    private final List<Command> commands;

    @Autowired
    public CommandManager(List<Command> commands) {
        this.commands = commands;
    }

    public void executeCommand(String commandName, Update update) throws CommandNotFoundException {
        for (Command command : commands) {
            if ((COMMAND_PREFIX + command.getName()).equals(commandName)) {
                command.execute(update);
                return;
            }
        }

        throw new CommandNotFoundException();
    }

    public boolean isCommand(String text) {
        return text.startsWith(Character.toString(CommandManager.COMMAND_PREFIX));
    }

    @PostConstruct
    private void init() {
        commands.sort(Comparator.comparingInt(Command::getOrder));
    }

}
