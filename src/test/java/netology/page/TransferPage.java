package netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import netology.data.DataHelper;
import org.openqa.selenium.Keys;
import netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static netology.data.DataHelper.AmountAndBalanceInfo.*;
import static netology.data.DataHelper.CardInfo.*;
import static netology.page.DashboardPage.*;

@Data
public class TransferPage {
  DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();

  private static SelenideElement heading = $x("//*[@id='root']/div/h1");
  private static SelenideElement inputFieldAmount = $x("//*[@id='root']/div/form/fieldset/div[1]/div/span/span/span[2]/input");
  private static SelenideElement inputFieldFromCard = $x("//*[@id='root']/div/form/fieldset/div[2]/span/span/span[2]/input");
  private static SelenideElement buttonTopUp = $x("//*[@id='root']/div/form/button[1]");
  private static SelenideElement buttonCancel = $x("//*[@id='root']/div/form/button[2]");
  private static SelenideElement errorNotification = $x("//*[@id='root']/div/div/div[3]");
  private static SelenideElement buttonReload = $x("//*[@id='root']/div/button");
  private static SelenideElement balanceFirstCardAfterPayment = $x("//*[@id='root']/div/ul/li[1]/div");
  private static SelenideElement balanceSecondCardAfterPayment = $x("//*[@id='root']/div/ul/li[2]/div");

  public static String balanceFirstCardAfterPaymentString() {
    SelenideElement balance = balanceFirstCardAfterPayment;
    String text = balance.getText();
    return text.substring(29, text.indexOf(" ", 29));
  }

  public static String balanceSecondCardAfterPaymentString() {
    SelenideElement balance = balanceSecondCardAfterPayment;
    String text = balance.getText();
    return text.substring(29, text.indexOf(" ", 29));
  }

  public static String getErrorNotification() {
    return errorNotification.getText().trim();
  }

  public static String getHeading() {
    return heading.getText();
  }

  public static void waitAppearElement() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {
    }
  }

  public TransferPage transferFromFirstCard() {
    inputFieldAmount.setValue(amountAndBalanceInfo.getCheckAmountFromFirstCardString());
    inputFieldFromCard.setValue(firstCard);
    buttonTopUp.click();
    return this;
  }

  public TransferPage transferFromSecondCard() {
    inputFieldAmount.setValue(amountAndBalanceInfo.getCheckAmountFromSecondCardString());
    inputFieldFromCard.setValue(secondCard);
    buttonTopUp.click();
    return this;
  }

  public TransferPage clearFormFields() {
    inputFieldAmount.shouldBe(Condition.visible);
    inputFieldFromCard.shouldBe(Condition.visible);
    inputFieldAmount.doubleClick().sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
    inputFieldFromCard.doubleClick().sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
    return this;
  }

  public TransferPage updatedCardBalance() {
    buttonReload.shouldBe(Condition.visible);
    buttonReload.shouldBe(Condition.enabled).click();
    actualBalanceFirstCard.setBalanceFirstCard(Integer.parseInt(balanceFirstCardAfterPaymentString()));
    actualBalanceSecondCard.setBalanceSecondCard(Integer.parseInt(balanceSecondCardAfterPaymentString()));
    return this;
  }

  public TransferPage transferFromInvalidFirstCard() {
    inputFieldAmount.setValue(amountFixString);
    inputFieldFromCard.setValue(invalidCardNumber);
    buttonTopUp.click();
    return this;
  }

  public TransferPage transferFromInvalidSecondCard() {
    inputFieldAmount.setValue(amountFixString);
    inputFieldFromCard.setValue(invalidCardNumber);
    buttonTopUp.click();
    return this;
  }

  public TransferPage transferFromFirstCardWithEmptyAmountField() {
    inputFieldAmount.setValue(" ");
    inputFieldFromCard.setValue(firstCard);
    buttonTopUp.click();
    buttonReload.click();
    return this;
  }

  public TransferPage transferFromSecondCardWithEmptyAmountField() {
    inputFieldAmount.setValue(" ");
    inputFieldFromCard.setValue(secondCard);
    buttonTopUp.click();
    buttonReload.click();
    return this;
  }

  public TransferPage transferCancelButton() {
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    inputFieldAmount.setValue(amountAndBalanceInfo.getCheckAmountFromFirstCardString());
    inputFieldFromCard.setValue(firstCard);
    buttonCancel.click();
    return this;
  }

  public TransferPage transferFromFirstCardPaymentOverBalance() {
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    inputFieldAmount.setValue(amountAndBalanceInfo.calculateOverLimitPaymentFromFirstCardString);
    inputFieldFromCard.setValue(firstCard);
    buttonTopUp.click();
    return this;
  }

  public TransferPage transferFromSecondCardPaymentOverBalance() {
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    inputFieldAmount.setValue(amountAndBalanceInfo.calculateOverLimitPaymentFromSecondCardString);
    inputFieldFromCard.setValue(secondCard);
    buttonTopUp.click();
    return this;
  }


  public TransferPage backTransferToFirstCard() {
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    inputFieldAmount.setValue(amountAndBalanceInfo.calculateBackTransferString);
    inputFieldFromCard.setValue(secondCard);
    buttonTopUp.click();
    return this;
  }

  public TransferPage backTransferToSecondCard() {
    DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();
    inputFieldAmount.setValue(amountAndBalanceInfo.calculateBackTransferString);
    inputFieldFromCard.setValue(firstCard);
    buttonTopUp.click();
    return this;
  }
}

