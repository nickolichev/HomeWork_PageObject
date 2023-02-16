package netology.test;

import netology.data.DataHelper;
import netology.page.DashboardPage;
import netology.page.LoginPage;
import netology.page.TransferPage;
import netology.page.VerificationPage;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static netology.page.LoginPage.*;
import static netology.page.TransferPage.*;
import static netology.page.VerificationPage.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferTest {

  private static final String url = "http://localhost:9999";
  private static final String errorLoginOrPassword = "Ошибка! Неверно указан логин или пароль";
  private static final String errorField = "Поле обязательно для заполнения";
  private static final String errorCode = "Ошибка! Неверно указан код! Попробуйте ещё раз.";
  private static final String CodeFourthAttempt = "Ошибка! Превышено количество попыток ввода кода!";
  private static final String errorOccurred = "Ошибка! Произошла ошибка";
  private static final String yourCards = "Ваши карты";

  private LoginPage loginPage;
  private VerificationPage verificationPage;
  private TransferPage transferPage;

  @BeforeEach
  public void setUp() {
    open(url);
    loginPage = new LoginPage();
    verificationPage = new VerificationPage();
    transferPage = new TransferPage();
  }

  public void stop() {
    closeWebDriver();
  }

  // Тестируем корректный перевод с 1-й карты на 2-ю и обратно со 2-й на 1-ю
  @Order(1)
  @Test
  public void testCorrectTransferBetweenFirstCardAndSecondCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.clearFormFields();
    transferPage.transferFromFirstCard();
    transferPage.updatedCardBalance();

    DashboardPage.ActualBalanceFirstCard actualBalanceFirstCard = new DashboardPage.ActualBalanceFirstCard();
    DashboardPage.ActualBalanceSecondCard actualBalanceSecondCard = new DashboardPage.ActualBalanceSecondCard();
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();

    Assertions.assertEquals(Integer.toString(amountAndBalanceInfo.balanceAfterPaymentFromFirstCard()), Integer.toString(actualBalanceFirstCard.getBalanceFirstCard()));
    Assertions.assertEquals(Integer.toString(amountAndBalanceInfo.balanceAfterPaymentToSecondCard()), Integer.toString(actualBalanceSecondCard.getBalanceSecondCard()));

    // Требуется для сборки в Appveyor CI. Если блок отсутствует, то в Appveyor CI тест падает
    // Или придется делить тетст на два отдельных теста, что приведет к взаимозависимости этих тестов.
    // Тесты должны будут проводится в определенной последовательности
    stop();
    open(url);

    loginPage.validAuth();
    verificationPage.verifyCode();
    dashboardPage.firstCardTopUp();
    transferPage.clearFormFields();
    transferPage.transferFromSecondCard();
    transferPage.updatedCardBalance();

    Assertions.assertEquals(Integer.toString(amountAndBalanceInfo.balanceAfterPaymentFromSecondCard()), Integer.toString(actualBalanceSecondCard.getBalanceSecondCard()));
    Assertions.assertEquals(Integer.toString(amountAndBalanceInfo.balanceAfterPaymentToFirstCard()), Integer.toString(actualBalanceFirstCard.getBalanceFirstCard()));
  }

  // Тестируем ввод невалидного логина + невалидного пароля
  @Order(2)
  @Test
  public void testInvalidLoginPasswordCombination() {
    loginPage.invalidLogin();
    loginPage.invalidPassword();

    Assertions.assertEquals(errorLoginOrPassword, getNotificationErrorLogin());
    Assertions.assertEquals(errorLoginOrPassword, getNotificationErrorPassword());
  }

  // Проверяем сообщение об ошибке при пустых полях логина и пароля
  @Order(3)
  @Test
  public void testBlankLoginAndPassword() {
    loginPage.cleanFieldsLoginPassword();

    Assertions.assertEquals(errorField, getNotificationFieldLogin());
    Assertions.assertEquals(errorField, getNotificationFieldPassword());
  }

  // Тестируем ввод невалидного sms кода
  @Order(4)
  @Test
  public void testInvalidCode() {
    loginPage.validAuth();
    verificationPage.invalidCode();
    // без вызова .waitAppearElement() падает в Appveyor CI (в локальной сборке - стабильно проходит)
    // Предпологаю, что в Appveyor CI Assertions начинает проверку не дождавшись элемента
    // "TransferTest > testInvalidCode() FAILED org.opentest4j.AssertionFailedError:
    // expected: <Ошибка! Неверно указан код! Попробуйте ещё раз.> but was: <>"
    VerificationPage.waitAppearElement();
    Assertions.assertEquals(errorCode, getNotificationErrorCode());
  }

  // Тестируем оплату c невалидного номера 1-й карты
  @Order(5)
  @Test
  public void testInvalidFirstCardNumberPayment() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.transferFromInvalidFirstCard();
    // без вызова .waitForOneSeconds() падает в Appveyor CI (в локальной сборке - стабильно проходит)
    // Предпологаю, что в Appveyor CI Assertions начинает проверку не дождавшись элемента
    // TransferTest > testInvalidFirstCardNumberPayment() FAILED org.opentest4j.AssertionFailedError:
    // expected: <Ошибка! Произошла ошибка> but was: <>
//    TransferPage.waitAppearElement();
    Assertions.assertEquals(errorOccurred, getErrorNotification());
  }

  // Тестируем оплату c невалидного номера 2-й карты
  @Order(6)
  @Test
  public void testInvalidSecondCardNumberPayment() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.firstCardTopUp();
    transferPage.transferFromInvalidSecondCard();
    // без вызова .waitForOneSeconds() падает в Appveyor CI (в локальной сборке - стабильно проходит)
    // Предпологаю, что в Appveyor CI Assertions начинает проверку не дождавшись элемента
    // TransferTest > testInvalidSecondCardNumberPayment() FAILED org.opentest4j.AssertionFailedError:
    // expected: <Ошибка! Произошла ошибка> but was: <>
//    TransferPage.waitAppearElement();
    Assertions.assertEquals(errorOccurred, getErrorNotification());
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Тестируем оплату с 1-й карты на 1-ю карту
  @Order(7)
  @Test
  public void testValidPaymentFromFirstCardToFirstCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.firstCardTopUp();
    transferPage.transferFromFirstCard();

    // Корректный текст в expected должен быть примерно такой: "Ошибка! Указанный номер карты не может быть использован",
    // и в этом случае (с данным текстом) настоящий тест должен был бы упасть
    Assertions.assertEquals(yourCards, getHeading());
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Тестируем оплату со 2-й карты на 2-ю карту
  @Order(8)
  @Test
  public void testValidPaymentFromSecondCardToSecondCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.transferFromSecondCard();

    // Корректный текст в expected должен быть примерно такой: "Ошибка! Указанный номер карты не может быть использован",
    // и в этом случае (с данным текстом) настоящий тест должен был бы упасть
    Assertions.assertEquals(yourCards, getHeading());
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Поле "Сумма" оставляем пустым при оплате с 1-й карты
  @Order(9)
  @Test
  public void testBlankAmountTransferFromFirstCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.transferFromFirstCardWithEmptyAmountField();

    // Корректный текст expected должен быть: "Поле обязательно для заполнения",
    // и в этом случае (с данным текстом) настоящий тест должен был бы упасть
    Assertions.assertEquals(yourCards, getHeading());
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Поле "Сумма" оставляем пустым при оплате со 2-й карты
  @Order(10)
  @Test
  public void testBlankAmountTransferFromSecondCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.firstCardTopUp();
    transferPage.transferFromSecondCardWithEmptyAmountField();

    // Корректный текст expected должен быть: "Поле обязательно для заполнения",
    // и в этом случае (с данным текстом) настоящий тест должен был бы упасть
    Assertions.assertEquals(yourCards, getHeading());
  }

  // Тестируем кнопку Отмена
  @Order(11)
  @Test
  public void testTransferCancelButtonFunctionality() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.transferCancelButton();

    Assertions.assertEquals(yourCards, getHeading());
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Тестируем перевод с 1-й карты на 2-ю, в суммме превышающей остаток на карте
  @Order(12)
  @Test
  public void testPaymentOverBalanceFromFirstCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.secondCardTopUp();
    transferPage.clearFormFields();
    transferPage.transferFromFirstCardPaymentOverBalance();
    transferPage.updatedCardBalance();

    DashboardPage.ActualBalanceFirstCard actualBalanceFirstCard = new DashboardPage.ActualBalanceFirstCard();
    DashboardPage.ActualBalanceSecondCard actualBalanceSecondCard = new DashboardPage.ActualBalanceSecondCard();
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();

    Assertions.assertEquals(amountAndBalanceInfo.balanceAfterOverLimitPaymentFromFirstCard(),
            Integer.toString(actualBalanceFirstCard.getBalanceFirstCard()));

    Assertions.assertEquals(amountAndBalanceInfo.balanceAfterOverLimitPaymentToSecondCard(),
            Integer.toString(actualBalanceSecondCard.getBalanceSecondCard()));

    // Требуется для сборки в Appveyor CI. Если блок отсутствует, то в Appveyor CI тест падает
    // Или придется делить тетст на два отдельных теста, что приведет к взаимозависимости этих тестов.
    // Тесты должны будут проводится в определенной последовательности
    stop();
    open(url);

    loginPage.validAuth();
    verificationPage.verifyCode();
    dashboardPage.firstCardTopUp();
    transferPage.clearFormFields();
    transferPage.backTransferToFirstCard();
    transferPage.updatedCardBalance();
  }

  // Баг. Этот тест должен был бы упасть. Оформлено issue
  // Тестируем перевод со 2-й карты на 1-ю, в суммме превышающей остаток на карте
  @Order(13)
  @Test
  public void testPaymentOverBalanceFromSecondCard() {
    loginPage.validAuth();
    verificationPage.verifyCode();
    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.firstCardTopUp();
    transferPage.clearFormFields();
    transferPage.transferFromSecondCardPaymentOverBalance();
    transferPage.updatedCardBalance();

    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    DashboardPage.ActualBalanceFirstCard actualBalanceFirstCard = new DashboardPage.ActualBalanceFirstCard();
    DashboardPage.ActualBalanceSecondCard actualBalanceSecondCard = new DashboardPage.ActualBalanceSecondCard();

    Assertions.assertEquals(amountAndBalanceInfo.balanceAfterOverLimitPaymentFromSecondCard(),
            Integer.toString(actualBalanceSecondCard.getBalanceSecondCard()));
    Assertions.assertEquals(amountAndBalanceInfo.balanceAfterOverLimitPaymentToFirstCard(),
            Integer.toString(actualBalanceFirstCard.getBalanceFirstCard()));

    // Требуется для сборки в Appveyor CI. Если блок отсутствует, то в Appveyor CI тест падает
    // Или придется делить тетст на два отдельных теста, что приведет к взаимозависимости этих тестов.
    // Тесты должны будут проводится в определенной последовательности
    stop();
    open(url);

    loginPage.validAuth();
    verificationPage.verifyCode();
    dashboardPage.secondCardTopUp();
    transferPage.clearFormFields();
    transferPage.backTransferToSecondCard();
    transferPage.updatedCardBalance();
  }

  // #14
  // Оставляем поле sms кода пустым
  @Order(14)
  @Test
  public void testBlankVerificationCode() {
    loginPage.validAuth();
    verificationPage.emptyFieldCode();

    Assertions.assertEquals(errorField, getNotificationFieldVerifyCode());
  }

  // Тетстируем блокировку пользователя при четырехкратном вводе неверного SMS кода,
  @Order(15)
  @Test
  public void testBlockedAccountAfterFourthInvalidVerificationCodeAttempt() {
    loginPage.validAuth();

    // 1-я попытка ввода невалидного смс кода
    verificationPage.invalidCode();
    Assertions.assertEquals(errorCode, getNotificationErrorCode());
    verificationPage.clearFormFields();

    // 2-я попытка ввода невалидного смс кода
    verificationPage.invalidCode();
    Assertions.assertEquals(errorCode, getNotificationErrorCode());
    verificationPage.clearFormFields();

    // 3-я попытка ввода невалидного смс кода
    verificationPage.invalidCode();
    Assertions.assertEquals(errorCode, getNotificationErrorCode());
    verificationPage.clearFormFields();

    // 4-я попытка ввода невалидного смс кода
    verificationPage.invalidCode();
    Assertions.assertEquals(CodeFourthAttempt, getNotificationErrorCode());
  }
}
