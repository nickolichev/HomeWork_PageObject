package netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import netology.data.DataHelper;
import org.openqa.selenium.Keys;
import netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static netology.data.DataHelper.CodeInfo.fieldVerification;
import static netology.data.DataHelper.CodeInfo.invalidCode;
import static netology.data.DataHelper.CodeInfo.*;

@Data
public class VerificationPage {
//  private static SelenideElement verificationPage = $x("//*[@id='root']/div/h2");
  private static SelenideElement buttonContinue = $x("//*[@id='root']/div/div/form/fieldset/div[2]/button/span/span");
  private static SelenideElement notificationErrorCode = $x("//*[@id='root']/div/div/div/div[3]");
  private static SelenideElement notificationErrorCodeFour = $x("//*[@id='root']/div/div/div/div[3]");
  private static SelenideElement notificationFieldVerifyCode = $x("//*[@id='root']/div/div/form/fieldset/div[1]/span/span/span[3]");

  public static String getNotificationFieldVerifyCode() {
    return notificationFieldVerifyCode.getText().trim();
  }

  public static String getNotificationErrorCode() {
    notificationErrorCode.shouldBe(Condition.visible);
    return notificationErrorCode.getText().trim();
  }

  public static void waitAppearElement() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {
    }
  }

  public VerificationPage clearFormFields() {
    fieldVerification.shouldBe(Condition.visible);
    fieldVerification.doubleClick().sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
    return this;
  }

  public VerificationPage verifyCode() {
    fieldVerification.shouldBe(Condition.visible);
    fieldVerification.setValue(DataHelper.CodeInfo.code);
    buttonContinue.click();
    return this;
  }

  public VerificationPage invalidCode() {
    fieldVerification.shouldBe(Condition.visible);
    fieldVerification.setValue(invalidCode);
    buttonContinue.click();
    return this;
  }

  public VerificationPage emptyFieldCode() {
    buttonContinue.click();
    return this;
  }
}
