package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should plan and replan meeting successfully")
    void shouldPlanAndReplanMeetingSuccessfully() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(Condition.partialText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.exist);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not plan meeting without a city")
    void shouldNotPlanMeetingWithoutACity() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should plan meeting in 3 days and replan in 4")
    void shouldPlanMeetingIn3DaysAndReplanIn4() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 4;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(Condition.partialText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.exist);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not plan meeting in 2 days")
    void shouldNotPlanMeetingIn2Days() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 2;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    @DisplayName("Should plan meeting in 3 days and replan in 3")
    // но так не должно быть
    void shouldPlanMeetingIn3DaysAndReplanIn3() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 3;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(Condition.partialText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.exist);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should plan meeting in 3 days and should not replan meeting in 2 days")
    void shouldPlanMeetingIn3DaysAndNotReplanIn2Days() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 2;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    @DisplayName("Should plan meeting with default date")
    void shouldPlanMeetingWithDefaultDate() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.partialText("Встреча успешно запланирована на")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not plan meeting without a date")
    void shouldNotPlanMeetingWithoutADate() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Неверно введена дата"));
    }

    @Test
    @DisplayName("Should not plan meeting with the name on English")
    void shouldNotPlanMeetingNameOnEnglish() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("en");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("Should not plan meeting without a name")
    void shouldNotPlanMeetingWithoutName() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("en");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should not plan meeting without a phone number")
    void shouldNotPlanMeetingWithoutAPhone() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should not plan meeting without checkbox activation")
    void shouldNotPlanMeetingWithoutCheckbox() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(byText("Запланировать")).click();
        $("[data-test-id='agreement'] .checkbox__text").shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
