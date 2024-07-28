package com.vasnatech.mando.command;

import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.service.FormatService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UtilCommands extends AbstractCommands {

    public UtilCommands(Session session, FormatService formatService) {
        super(session, formatService);
    }

    @ShellMethod(group = "Util", key = "sleep", prefix = "-")
    public AttributedString sleep(int seconds) throws Exception {
        return execute(() -> {
            Thread.sleep(seconds * 1000L);
            return "awake after " + seconds + " seconds.";
        });
    }
}
