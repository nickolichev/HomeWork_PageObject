package netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.Value;
import netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Data
public class DashboardPage {

  private SelenideElement buttonTopUpFirstCard = $x("//*[@id='root']/div/ul/li[1]/div/button/span/span").shouldBe(visible);
  private SelenideElement buttonTopUpSecondCard = $x("//*[@id='root']/div/ul/li[2]/div/button/span/span").shouldBe(visible);
  private static SelenideElement buttonReload = $x("//*[@id='root']/div/button");
  private static SelenideElement elementBalanceFirstCard = $x("//*[@id='root']/div/ul/li[1]/div");
  private static SelenideElement elementBalanceSecondCard = $x("//*[@id='root']/div/ul/li[2]/div");

  @Value
  public static class ActualBalanceFirstCard {
    public static int balanceFirstCardInt;
    public static String balanceFirstCardString;

    public ActualBalanceFirstCard() {
    }

    public ActualBalanceFirstCard(SelenideElement elementBalanceFirstCard) {
      String balanceFirstCard = elementBalanceFirstCard.getText()
              .substring(29, elementBalanceFirstCard.getText().indexOf(" ", 29));
      balanceFirstCardString = balanceFirstCard;
      balanceFirstCardInt = Integer.parseInt(balanceFirstCard);
    }

    public int getBalanceFirstCard() {
      return balanceFirstCardInt;
    }

    public void setBalanceFirstCard(int i) {
      balanceFirstCardInt = i;
    }
  }

  @Value
  public static class ActualBalanceSecondCard {
    public static int balanceSecondCardInt;
    public static String balanceSecondCardString;

    public ActualBalanceSecondCard() {
    }

    public ActualBalanceSecondCard(SelenideElement elementBalanceSecondCard) {
      String balanceSecondCard = elementBalanceSecondCard.getText()
              .substring(29, elementBalanceSecondCard.getText().indexOf(" ", 29));
      balanceSecondCardString = balanceSecondCard;
      balanceSecondCardInt = Integer.parseInt(balanceSecondCard);
    }

    public int getBalanceSecondCard() {
      return balanceSecondCardInt;
    }

    public void setBalanceSecondCard(int i) {
      balanceSecondCardInt = i;
    }
  }

  static ActualBalanceFirstCard actualBalanceFirstCard = new ActualBalanceFirstCard(elementBalanceFirstCard);
  static ActualBalanceSecondCard actualBalanceSecondCard = new ActualBalanceSecondCard(elementBalanceSecondCard);
  DataHelper.AmountAndBalanceInfo amountAndBalanceInfo = new DataHelper.AmountAndBalanceInfo();

  public DashboardPage firstCardTopUp() {
    amountAndBalanceInfo.setBalanceBeforePaymentFromOrToFirstCard(actualBalanceFirstCard.getBalanceFirstCard());
    amountAndBalanceInfo.setBalanceBeforePaymentFromOrToSecondCard(actualBalanceSecondCard.getBalanceSecondCard());
    buttonTopUpFirstCard.click();
    return this;
  }

  public DashboardPage secondCardTopUp() {
    amountAndBalanceInfo.setBalanceBeforePaymentFromOrToFirstCard(actualBalanceFirstCard.getBalanceFirstCard());
    amountAndBalanceInfo.setBalanceBeforePaymentFromOrToSecondCard(actualBalanceSecondCard.getBalanceSecondCard());
    buttonTopUpSecondCard.click();
    return this;
  }
}