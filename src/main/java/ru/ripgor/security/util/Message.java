package ru.ripgor.security.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Класс для "связи" с пользователем.
 * Например, используется в формах создания или редактирования для отобаржения ошибок.
 * Или наоборот -- для сообщения об успехе
 */

@Component
@Scope("prototype")
public class Message {

    private String info;

    public Message() {
    }

    public Message(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
