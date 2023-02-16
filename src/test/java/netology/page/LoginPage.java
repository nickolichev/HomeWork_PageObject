package netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$x;
import static netology.data.DataHelper.AuthInfo.*;

@Data
public class LoginPage {
  private static SelenideElement buttonContinue = $x("//*[@id='root']/div/div/form/fieldset/div[3]/button");
  private static SelenideElement notificationErrorLogin = $x("//*[@id='root']/div/div/div/div[3]");
  private static SelenideElement notificationErrorPassword = $x("//*[@id='root']/div/div/div/div[3]");
  private static SelenideElement notificationFieldLogin = $x("//*[@id='root']/div/div/form/fieldset/div[1]/span/span/span[3]");
  private static SelenideElement notificationFieldPassword = $x("//*[@id='root']/div/div/form/fieldset/div[2]/span/span/span[3]");

  public static String getNotificationFieldLogin() {
    return notificationFieldLogin.getText().trim();
  }

  public static String getNotificationFieldPassword() {
    return notificationFieldPassword.getText().trim();
  }

  public static String getNotificationErrorLogin() {
    return notificationErrorLogin.getText().trim();
  }

  public static String getNotificationErrorPassword() {
    return notificationErrorPassword.getText().trim();
  }

  public LoginPage validAuth() {
    fieldLogin.setValue(login);
    fieldPassword.setValue(password);
    buttonContinue.click();
    return this;
  }

  public LoginPage invalidLogin() {
    fieldLogin.setValue(invalidLogin);
    fieldPassword.setValue(password);
    buttonContinue.click();
    return this;
  }

  public LoginPage invalidPassword() {
    fieldLogin.setValue(login);
    fieldPassword.setValue(invalidPassword);
    buttonContinue.click();
    return this;
  }

  public LoginPage cleanFieldsLoginPassword() {
    fieldLogin.shouldBe(Condition.visible).doubleClick().sendKeys(Keys.BACK_SPACE);
    fieldPassword.shouldBe(Condition.visible).doubleClick().sendKeys(Keys.BACK_SPACE);
    buttonContinue.click();
    return this;
  }
}