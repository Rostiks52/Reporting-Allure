package ru.netology.delivery.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.time.Duration.ofSeconds;

public class CardDeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());}
    @AfterAll
    static void tearDownAll() {SelenideLogger.removeListener("allure");}

    @Test
    void shouldFillingApplication() {
        open("http://localhost:9999");
        int firstMeet = DataGenerator.Filling.generateRandomNumber(3, 180);
        int secondMeet = DataGenerator.Filling.generateRandomNumber(firstMeet + 5, firstMeet + 180);
        $("[data-test-id='city'] input").setValue(DataGenerator.Filling.generateCustomerData("ru").getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.Filling.generateMeetingDay(firstMeet));
        $("[data-test-id='name'] input").setValue(DataGenerator.Filling.generateCustomerData("ru").getName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.Filling.generateCustomerData("ru").getPhone());
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, ofSeconds(15));
        $("[data-test-id='success-notification']>.notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + DataGenerator.Filling.generateMeetingDay(firstMeet)));
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.Filling.generateMeetingDay(secondMeet));
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $("[data-test-id='replan-notification']>.notification__content").
                shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(withText("Перепланировать")).click();
        $("[data-test-id='success-notification']>.notification__content").shouldBe(visible)
                .shouldHave(exactText("Встреча успешно запланирована на " + DataGenerator.Filling.generateMeetingDay(secondMeet)));
    }
}



