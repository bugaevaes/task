package test.task;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Tests extends Base {
    String baseUrl = "https://exonum.com/demo/voting/";
    String candidatePageLink = "https://en.wikipedia.org/wiki/Eiki_Nestor";

    @Test
    // тест на сравнение линков
    public void testCandidatePageLink() throws Exception {

        driver.get(baseUrl);
        chooseCandidate();

        String linkFromSite = driver.findElement(By.xpath("//a[@class='list-option-link']")).getAttribute("href");

        Assert.assertEquals(linkFromSite, candidatePageLink);
    }

    @Test
    // тест на сравнение описаний кандидата
    public void testCandidateDescription() throws Exception {

        driver.get(baseUrl);
        chooseCandidate();

        String descriptionFromSite = driver.findElement(By.xpath("//div[contains(@class, 'list-option-description')]")).getText();

        driver.get(candidatePageLink);

        String descriptionFromWiki = driver.findElement(By.xpath("//div[@id='mw-content-text']/div/p[1]")).getText();

        Assert.assertTrue(descriptionFromWiki.contains(descriptionFromSite));
    }

    @Test
    // тест с почтовым ящиком
    public void testEmail() throws Exception {

        //не знаю, как реализовать код взаимодействия с сервисом. Поэтому условно считаю, что получила оттуда email
        String email = "josya.bu@gmail.com";

        driver.get(baseUrl);
        chooseCandidate();
        driver.findElement(By.xpath("//div[text()='VOTE IN ELECTION']")).click();

        // ждем появления всплывающего окна
        Thread.sleep(1500);
        driver.findElement(By.xpath("//div[@class='modal-body']//div[text()='YES']")).click();

        // получение MEMO и HASH в UI
        String memo = driver.findElement(By.xpath("//div[contains(@class, 'code-box')][1]")).getText();
        String hash = driver.findElement(By.xpath("//div[contains(@class, 'code-box')][2]")).getText();

//        driver.findElement(By.xpath("//div[text()='SIGN']")).click();
        // не получалось выполнить клик на SIGN (видимо потому что он вне экрана), нашла такое решение
        JavascriptExecutor je = (JavascriptExecutor) driver;
        WebElement elementSign = driver.findElement(By.xpath("//div[text()='SIGN']"));
        je.executeScript("arguments[0].click()", elementSign);

        // ждем появления окна ввода pin
        Thread.sleep(2000);

        driver.findElement(By.xpath("//div[@class='keyboard-button-digit' and text()='3']")).click();
        driver.findElement(By.xpath("//div[@class='keyboard-button-digit' and text()='7']")).click();
        driver.findElement(By.xpath("//div[@class='keyboard-button-digit' and text()='9']")).click();
        driver.findElement(By.xpath("//div[@class='keyboard-button-digit' and text()='1']")).click();

//        driver.findElement(By.xpath("//div[contains(@class, 'button') and text()='SIGN BALLOT']")).click();
        WebElement elementSignBallot = driver.findElement(By.xpath("//div[contains(@class, 'button') and text()='SIGN BALLOT']"));
        je.executeScript("arguments[0].click()", elementSignBallot);

        // ждем перехода на страницу
        Thread.sleep(1000);

        // вводим полученный ранее email
        driver.findElement(By.xpath("//input")).sendKeys(email);

        driver.findElement(By.xpath("//div[contains(@class, 'button') and text()='SUBMIT BALLOT']")).click();

        // ждем перехода на последнюю страницу
        Thread.sleep(1000);

        // проверка что сценарий выполнен верно
        Assert.assertEquals(driver.findElement(By.xpath("//div[contains(@class, 'grats-title')]")).getText(), "Congratulations!");

        // дальше, полагаю, нужно получить список писем из ящика через API сервиса.
        // ищем письмо от voting2016app@gmail.com (взяла из полученного письма)
        // проверяем, что полученные из UI данные содержатся в письме.


    }

    private void chooseCandidate() {
        driver.findElement(By.xpath("//div[text()='VOTE IN ELECTION']")).click();
        driver.findElement(By.xpath("//td[text()='Estonian Presidential Election']")).click();
        driver.findElement(By.xpath("//div[text()='VOTE IN ELECTION']")).click();
        driver.findElement(By.xpath("//td[text()='Eiki Nestor']")).click();
    }
}