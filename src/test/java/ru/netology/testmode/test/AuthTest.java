package ru.netology.testmode.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $("form");
        form.$("span[data-test-id=login] input.input__control[name=login]")
                .setValue(registeredUser.getLogin());
        form.$("span[data-test-id=password] input.input__control[name=password]")
                .setValue(registeredUser.getPassword());
        form.$("button").click();
        $("div#root h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        SelenideElement form = $("form");
        form.$("span[data-test-id=login] input.input__control[name=login]")
                .setValue(notRegisteredUser.getLogin());
        form.$("span[data-test-id=password] input.input__control[name=password]")
                .setValue(notRegisteredUser.getPassword());
        form.$("button").click();
        $("div[data-test-id=error-notification] div.notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        SelenideElement form = $("form");
        form.$("span[data-test-id=login] input.input__control[name=login]")
                .setValue(blockedUser.getLogin());
        form.$("span[data-test-id=password] input.input__control[name=password]")
                .setValue(blockedUser.getPassword());
        form.$("button").click();
        $("div[data-test-id=error-notification] div.notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        SelenideElement form = $("form");
        form.$("span[data-test-id=login] input.input__control[name=login]")
                .setValue(wrongLogin);
        form.$("span[data-test-id=password] input.input__control[name=password]")
                .setValue(registeredUser.getPassword());
        form.$("button").click();
        $("div[data-test-id=error-notification] div.notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        SelenideElement form = $("form");
        form.$("span[data-test-id=login] input.input__control[name=login]")
                .setValue(registeredUser.getLogin());
        form.$("span[data-test-id=password] input.input__control[name=password]")
                .setValue(wrongPassword);
        form.$("button").click();
        $("div[data-test-id=error-notification] div.notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
