package netology.data;


import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;
import netology.page.DashboardPage;

import java.util.Locale;

import static com.codeborne.selenide.Selenide.$x;

@Data
public class DataHelper {

  @Value
  public static class AuthInfo {
    public static String login = "vasya";
    public static String password = "qwerty123";
    public static String invalidLogin = generateInvalidLogin();
    public static String invalidPassword = generateInvalidPass();
    public static SelenideElement fieldLogin = $x("//*[@id='root']/div/div/form/fieldset/div[1]/span/span/span[2]/input");
    public static SelenideElement fieldPassword = $x("//*[@id='root']/div/div/form/fieldset/div[2]/span/span/span[2]/input");

    private static String generateInvalidLogin() {
      while (true) {
        Faker faker = new Faker(new Locale("en"));
        if (checkRandomLogin(faker.name().username())) {
          return faker.name().username();
        }
      }
    }

    private static boolean checkRandomLogin(String username) {
      return !username.equals(login);
    }

    private static String generateInvalidPass() {
      while (true) {
        Faker faker = new Faker();
        if (checkRandomPass(faker.internet().password(1, 6))) {
          return faker.internet().password();
        }
      }
    }

    private static boolean checkRandomPass(String pass) {
      return !pass.equals(password);
    }
  }

  @Value
  public static class CodeInfo {

    public static String code = "12345";
    public static String invalidCode = generateInvalidCode();
    public static SelenideElement fieldVerification
            = $x("//*[@id='root']/div/div/form/fieldset/div[1]/span/span/span[2]/input");

    private static String generateInvalidCode() {
      while (true) {
        Faker faker = new Faker();
        if (checkRandomCode(faker.numerify("#####"))) {
          return faker.internet().password();
        }
      }
    }

    public static boolean checkRandomCode(String numbers) {
      return !numbers.equals(code);
    }
  }

  @Value
  public static class CardInfo {
    public static String firstCard = "5559000000000001";
    public static String secondCard = "5559000000000002";
    public static String invalidCardNumber = generateInvalidCardNumber();

    private static String generateInvalidCardNumber() {
      while (true) {
        Faker faker = new Faker();
        if (checkRandomCardNumber(faker.business().creditCardNumber())) {
          return faker.business().creditCardNumber();
        }
      }
    }

    private static boolean checkRandomCardNumber(String card) {
      return !card.equals(firstCard) && !card.equals(secondCard);
    }
  }

  @Value
  public static class AmountAndBalanceInfo {
    DashboardPage.ActualBalanceFirstCard actualBalanceFirstCard = new DashboardPage.ActualBalanceFirstCard();
    DashboardPage.ActualBalanceSecondCard actualBalanceSecondCard = new DashboardPage.ActualBalanceSecondCard();

    int checkAmountFromFirstCard;
    int checkAmountFromSecondCard;
    static int balanceBeforePaymentFromOrToFirstCard;
    static int balanceBeforePaymentFromOrToSecondCard;
    int balanceAfterPaymentFromFirstCard;
    int balanceAfterPaymentFromSecondCard;
    int balanceAfterPaymentToFirstCard;
    int balanceAfterPaymentToSecondCard;
    int calculateOverLimitPaymentFromFirstCard;
    int calculateOverLimitPaymentFromSecondCard;
    int calculateBackTransfer;

    public String checkAmountFromFirstCardString = Integer.toString(checkAmountFromFirstCard());
    public String checkAmountFromSecondCardString = Integer.toString(checkAmountFromSecondCard());
    public String balanceBeforePaymentFromOrToFirstCardString = Integer.toString(getBalanceBeforePaymentFromOrToFirstCard());
    public String balanceBeforePaymentFromOrToSecondCardString = Integer.toString(getBalanceBeforePaymentFromOrToSecondCard());
    public String balanceAfterPaymentFromFirstCardString = Integer.toString(getBalanceAfterPaymentFromFirstCard());
    public String balanceAfterPaymentFromSecondCardString = Integer.toString(balanceAfterPaymentFromSecondCard());
    public String balanceAfterPaymentToFirstCardString = Integer.toString(balanceAfterPaymentToFirstCard());
    public String balanceAfterPaymentToSecondCardString = Integer.toString(balanceAfterPaymentToSecondCard());
    public String calculateOverLimitPaymentFromFirstCardString = Integer.toString(calculateOverLimitPaymentFromFirstCard());
    public String calculateOverLimitPaymentFromSecondCardString = Integer.toString(calculateOverLimitPaymentFromSecondCard());
    public String calculateBackTransferString = Integer.toString(calculateBackTransfer());

    public AmountAndBalanceInfo() {
      this.checkAmountFromFirstCard = checkAmountFromFirstCard();
      this.checkAmountFromSecondCard = checkAmountFromSecondCard();
      balanceBeforePaymentFromOrToFirstCard = getBalanceBeforePaymentFromOrToFirstCard();
      balanceBeforePaymentFromOrToSecondCard = getBalanceBeforePaymentFromOrToSecondCard();
      this.balanceAfterPaymentFromFirstCard = balanceAfterPaymentFromFirstCard();
      this.balanceAfterPaymentFromSecondCard = balanceAfterPaymentFromSecondCard();
      this.balanceAfterPaymentToFirstCard = balanceAfterPaymentToFirstCard();
      this.balanceAfterPaymentToSecondCard = balanceAfterPaymentToSecondCard();
      this.calculateOverLimitPaymentFromFirstCard = calculateOverLimitPaymentFromFirstCard();
      this.calculateOverLimitPaymentFromSecondCard = calculateOverLimitPaymentFromSecondCard();
      this.calculateBackTransfer = calculateBackTransfer();
    }

    private static int amountFixInt = 9999;
    public static String amountFixString = Integer.toString(amountFixInt);

    public void setBalanceBeforePaymentFromOrToFirstCard(int i) {
      balanceBeforePaymentFromOrToFirstCard = i;
    }

    public void setBalanceBeforePaymentFromOrToSecondCard(int i) {
      balanceBeforePaymentFromOrToSecondCard = i;
    }

    public String getCheckAmountFromFirstCardString() {
      AmountAndBalanceInfo amountAndBalanceInfo = new AmountAndBalanceInfo();
      return amountAndBalanceInfo.checkAmountFromFirstCardString;
    }

    public String getCheckAmountFromSecondCardString() {
      AmountAndBalanceInfo amountAndBalanceInfo = new AmountAndBalanceInfo();
      return amountAndBalanceInfo.checkAmountFromSecondCardString;
    }

    public int getBalanceBeforePaymentFromOrToFirstCard() {
      return balanceBeforePaymentFromOrToFirstCard;
    }

    public int getBalanceBeforePaymentFromOrToSecondCard() {
      return balanceBeforePaymentFromOrToSecondCard;
    }

    // Верификация суммы платежа с 1-й карты
    private int checkAmountFromFirstCard() {
      if (amountFixInt <= balanceBeforePaymentFromOrToFirstCard) {
        return amountFixInt;
      } else {
        return 0;
      }
    }

    // Верификация суммы платежа со 2-й карты
    private int checkAmountFromSecondCard() {
      if (amountFixInt <= balanceBeforePaymentFromOrToSecondCard) {
        return amountFixInt;
      } else {
        return 0;
      }
    }

    // "Баланс 1-й карты после платежа" с нее через проверку на условие
    public int balanceAfterPaymentFromFirstCard() {
      int tmpBalanceFirstCard = getBalanceBeforePaymentFromOrToFirstCard();
      int checkAmount = getCheckAmountFromFirstCard();
      if (checkAmount == 0) {
        return tmpBalanceFirstCard;
      } else {
        return tmpBalanceFirstCard - amountFixInt;
      }
    }

    // "Баланс 2-й карты после платежа" с нее через проверку на условие
    public int balanceAfterPaymentFromSecondCard() {
      int tmpBalanceSecondCard = getBalanceBeforePaymentFromOrToSecondCard();
      int checkAmount = getCheckAmountFromSecondCard();
      if (checkAmount == 0) {
        return tmpBalanceSecondCard;
      } else {
        return tmpBalanceSecondCard - amountFixInt;
      }
    }

    // "Баланс 1-й карты после платежа" НА нее через проверку на условие
    public int balanceAfterPaymentToFirstCard() {
      int tmpBalanceFirstCard = getBalanceBeforePaymentFromOrToFirstCard();
      int checkAmount = getCheckAmountFromSecondCard();
      if (checkAmount == 0) {
        return tmpBalanceFirstCard;
      } else {
        return tmpBalanceFirstCard + amountFixInt;
      }
    }

    // "Баланс 2-й карты после платежа" на нее через проверку на условие
    public int balanceAfterPaymentToSecondCard() {
      int tmpBalanceSecondCard = getBalanceBeforePaymentFromOrToSecondCard();
      int checkAmount = getCheckAmountFromFirstCard();
      if (checkAmount == 0) {
        return tmpBalanceSecondCard;
      } else {
        return tmpBalanceSecondCard + amountFixInt;
      }
    }

    // Сумма платежа, превышающего остаток 1-й карты
    private int calculateOverLimitPaymentFromFirstCard() {
      return balanceBeforePaymentFromOrToFirstCard + 1;
    }

    // Сумма платежа, превышающего остаток 2-й карты
    private int calculateOverLimitPaymentFromSecondCard() {
      return balanceBeforePaymentFromOrToSecondCard + 1;
    }

    // Вычисление баланса 1-й карты, после платежа (в сумме превышающей баланс) с нее
    public String balanceAfterOverLimitPaymentFromFirstCard() {
      return Integer.toString(balanceBeforePaymentFromOrToFirstCard - calculateOverLimitPaymentFromFirstCard());
    }

    // Вычисление баланса 1-й карты, после платежа (в сумме превышающей баланс) на нее
    public String balanceAfterOverLimitPaymentToFirstCard() {
      return Integer.toString(balanceBeforePaymentFromOrToFirstCard + calculateOverLimitPaymentFromSecondCard());
    }

    // Вычисление баланса 2-й карты, после платежа (в сумме превышающей баланс) с нее
    public String balanceAfterOverLimitPaymentFromSecondCard() {
      return Integer.toString(balanceBeforePaymentFromOrToSecondCard - calculateOverLimitPaymentFromSecondCard());
    }

    // Вычисление баланса 2-й карты, после платежа (в сумме превышающей баланс) на нее
    public String balanceAfterOverLimitPaymentToSecondCard() {
      return Integer.toString(balanceBeforePaymentFromOrToSecondCard + calculateOverLimitPaymentFromFirstCard());
    }

    // расчет возвратного платежа, в тесты с платежами превышающими баланс,
    // для приведения балансов карт в исходное состояние
    private int calculateBackTransfer() {
      int tmpBalanceFirstCard = balanceBeforePaymentFromOrToFirstCard;
      int tmpBalanceSecondCard = balanceBeforePaymentFromOrToSecondCard;
      return ((tmpBalanceFirstCard + tmpBalanceSecondCard) / 2) + 1;
    }
  }
}