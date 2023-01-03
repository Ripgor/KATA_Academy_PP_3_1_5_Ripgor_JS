package ru.ripgor.security.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ripgor.security.model.User;
import ru.ripgor.security.service.UserService;
import ru.ripgor.security.util.Message;
import ru.ripgor.security.util.UserExistsException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private final UserService userService;

    public MyRestController(UserService userService) {
        this.userService = userService;
    }

    // Маппинг здесь необычный -- мы работаем не с представлениями, а с... "элементами сервера"
    // Поэтому ссылка в браузере не будет выглядеть как ".../api/users"
    // Из каждого метода улетает ResponseEntity -- так правильнее и лучше
    // Дополнительно возвращаем статусы -- для наглядности
    // представлениями потом займется JS, но ему нужны объекты -- их и отдаем
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Принимаем юзера от клиента с помощью @RequestBody
    // Если кривые входные данные -- отдадим сообщение об этом и еще статус навесим
    // если все хорошо -- тихонько сохраним юзера в базу и скажем клиенту, что все хорошо
    // но если вдруг нет -- кинем исключение о том, что пользователь уже есть
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<Message> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new Message(error), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (UserExistsException u) {
            throw new UserExistsException("User with username exist");
        }
    }

    // Ловим из ссылки id, удаляем по нему юзера и говорим что все прекрасно
    // Люблю delete-методы -- самые легкие
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Message> pageDelete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new Message("User deleted"), HttpStatus.OK);
    }

    // Метод нужен для вкладки User
    @GetMapping("users/{id}")
    public ResponseEntity<User> getUser (@PathVariable("id") int id) {
        User user = userService.getUser(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    // Метод нужен для навигационной панели -- выводим там активного юзера и его роли
    // Юзера ищем через объект Principal -- зарегестрированного и авторизованного пользователя
    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername (Principal principal) {
        User user = userService.getUser(principal.getName());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    // Метод редактирования пользователя
    // почти копия метода создания
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<Message> pageEdit(@PathVariable("id") int id,
                                                  @Valid @RequestBody User user,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new Message(error), HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // С помощью этого метода мы забираем самое важное у BindingResult -- сообщение -- и отдаем его в Message
    // Но для начала конвертируем в строку -- чтобы не париться в представлениях
    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }

}