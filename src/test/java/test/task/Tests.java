package test.task;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Tests extends Base {
    String baseUrl = "https://exonum.com/demo/voting/";
    String candidatePageLink = "https://en.wikipedia.org/wiki/Eiki_Nestor";

    @Test
    public void testCandidatePageLink() throws Exception {

        driver.get(baseUrl);
        chooseCandidate();

        String linkFromSite = driver.findElement(By.xpath("//a[@class='list-option-link']")).getAttribute("href");

        Assert.assertEquals(linkFromSite, candidatePageLink);
    }

    @Test
    public void testCandidateDescription() throws Exception {

        driver.get(baseUrl);
        chooseCandidate();

        String descriptionFromSite = driver.findElement(By.xpath("//div[contains(@class, 'list-option-description')]")).getText();

        driver.get(candidatePageLink);

        String descriptionFromWiki = driver.findElement(By.xpath("//div[@id='mw-content-text']/div/p[1]")).getText();

        Assert.assertTrue(descriptionFromWiki.contains(descriptionFromSite));
    }

    private void chooseCandidate() {
        driver.findElement(By.xpath("//div[text()='VOTE IN ELECTION']")).click();
        driver.findElement(By.xpath("//td[text()='Estonian Presidential Election']")).click();
        driver.findElement(By.xpath("//div[text()='VOTE IN ELECTION']")).click();
        driver.findElement(By.xpath("//td[text()='Eiki Nestor']")).click();
    }
}