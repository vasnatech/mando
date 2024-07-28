package com.vasnatech.mando.command;

import com.vasnatech.commons.function.CheckedSupplier;
import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.service.FormatService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class AbstractCommands {

    final Session session;
    final FormatService formatService;

    AbstractCommands(Session session, FormatService formatService) {
        this.session = session;
        this.formatService = formatService;
    }

    <T> AttributedString execute(CheckedSupplier<T> supplier) throws Exception {
        try {
            return formatService.success(supplier.get());
        } catch (Exception e) {
            return formatService.fail(e);
        }
    }
}
