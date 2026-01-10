package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.UserInfo;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.withText;

public class ReplanDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldReplanDeliveryDate() {
        // данные
        UserInfo user = DataGenerator.generateUser();
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(7);

        // город
        $("[data-test-id=city] input").setValue(user.getCity());

        // дата — ПЕРВОЙ
        $("[data-test-id=date] input").doubleClick().sendKeys(firstDate);

        // имя и телефон — ПОСЛЕ даты (КЛЮЧЕВОЕ ЗАМЕЧАНИЕ)
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());

        // согласие и отправка
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.text("Запланировать")).click();

        // первое подтверждение
        $("[data-test-id=success-notification]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstDate));

        // новая дата
        $("[data-test-id=date] input").doubleClick().sendKeys(secondDate);
        $$("button").find(Condition.text("Запланировать")).click();

        // уведомление о перепланировании
        $("[data-test-id=replan-notification]")
                .shouldHave(Condition.text("У вас уже запланирована встреча"));

        // подтверждаем перепланирование
        $$("button").find(Condition.text("Перепланировать")).click();

        // финальное подтверждение
        $("[data-test-id=success-notification]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondDate));
    }
}

