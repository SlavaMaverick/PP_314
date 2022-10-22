package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


//////////////////////// ПОЛУЧЕНИЕ ПОЛЬЗОВАТЕЛЕЙ И ОБРАБОТКА ОШИБОК ///////////////////////////////////////////

    @GetMapping("/admin")
    public List<User> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/admin/{id}")
    public User getOneUser(@PathVariable("id") int id){
        return userService.getById(id);
    }

    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e){
        UserErrorResponse response = new UserErrorResponse(
                "User with this id wasn't found",
                System.currentTimeMillis()
        );
        // В HTTP ответе будет отображено тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT FOUND - 404 статус
    }


//////////////////////// ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ И ОБРАБОТКА ОШИБОК ///////////////////////////////////////////

///// СПРАВОЧНО: КОРОТКИЙ СПОСОБ
//    @PostMapping("/admin")
//    public ResponseEntity<User> addUser(@RequestBody User user) {
//        userService.save(user);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


    ///// СПОСОБ С ОБРАБОТКОЙ ОШИБОК (ПО АЛИШЕВУ)
    //@RequestBody - Spring преобразует входящий JSON в объект User
    @PostMapping("/admin")
    private ResponseEntity<HttpStatus> create(@RequestBody @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(user);
        //Отправляем HTTP ответ пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e){
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP ответе будет тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // - 400 статус
    }


//////////////////////// РЕДАКТИРОВАНИЕ И УДАЛЕНИЕ (без отдельной обработки ошибок, можно добавить) ///////////

    @PostMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        userService.update(user.getId(), user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAllRoles(), HttpStatus.OK);
    }
}

