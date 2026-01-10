package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.UserInfo;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ReplanDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldReplanDeliveryDate() {
        UserInfo user = DataGenerator.generateUser();
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(7);

        // город
        $("[data-test-id=city] input").setValue(user.getCity());

        // дата — первой
        $("[data-test-id=date] input").doubleClick().sendKeys(firstDate);

        // имя и телефон — после даты
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());

        // согласие
        $("[data-test-id=agreement]").click();

        // кнопка Запланировать (ждём, что она кликабельна)
        $$("button")
                .find(text("Запланировать"))
                .shouldBe(enabled)
                .click();

        // ✅ сообщение об успешном планировании — ЖДЁМ
        $("[data-test-id=success-notification]")
                .shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + firstDate));

        // новая дата
        $("[data-test-id=date] input").doubleClick().sendKeys(secondDate);
        $$("button")
                .find(text("Запланировать"))
                .shouldBe(enabled)
                .click();

        // ✅ сообщение о перепланировании — ЖДЁМ
        $("[data-test-id=replan-notification]")
                .shouldBe(visible)
                .shouldHave(text("У вас уже запланирована встреча"));

        // кнопка Перепланировать
        $$("button")
                .find(text("Перепланировать"))
                .shouldBe(enabled)
                .click();

        // ✅ финальное сообщение — ЖДЁМ
        $("[data-test-id=success-notification]")
                .shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + secondDate));
    }
}


