package com.example.demo.models;

import com.example.demo.controllers.MainController;
import com.example.demo.dao.DAO;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;

import java.util.concurrent.TimeUnit;

public class Session {
    String login;
    String pass;
    String auth;
    WebDriver driver;
    String urlLogin = "https://steamcommunity.com/login/home/";
    String url = "https://steamcommunity.com/id/sorryihavenoname/gcpd/730/?tab=matchhistorycompetitive";
    public boolean logout = false;
    private boolean rly = false;
    private boolean hasLast = true;

    public Session() { start(); }

    public void setName(String login) {
        this.login = login;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void start() {
        System.setProperty("webdriver.opera.driver", "C:\\Program Files\\operadriver_win64\\operadriver.exe");
        driver = new OperaDriver();
        driver.get(urlLogin);
    }

    public void login(DAO dao){
        driver.get(urlLogin);

        WebElement log = driver.findElement(By.id("input_username"));
        WebElement pas = driver.findElement(By.id("input_password"));
        WebElement enterDiv = driver.findElement(By.id("login_btn_signin"));
        WebElement enterBut = enterDiv.findElement(By.tagName("button"));

        log.sendKeys(login);
        pas.sendKeys(pass);
        enterBut.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement twoAuth = driver.findElement(By.id("twofactorcode_entry"));
        WebElement authDiv = driver.findElement(By.id("login_twofactorauth_buttonset_entercode")).findElement(By.className("auth_button_h3"));

        twoAuth.sendKeys(auth);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        new Actions(driver).moveToElement(authDiv).click().perform();

        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().refresh();
        driver.get(url);

        try {
            WebElement element = driver.findElement(By.id("account_pulldown"));
        }
        catch(NoSuchElementException e) {
            rly = true;
        }

        if(!rly) {
            logout = true;
            refresh(dao);
        }

        rly = false;

    }
    public void refresh(DAO dao) {
        driver.navigate().refresh();

        for (int i = 2; i <= 9; i++) {
            String date = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                    + i + "]/td[1]/table/tbody/tr[2]/td")).getText();
            System.out.println(date + " А вот номер = " + (i - 1));
            Stat stat = new Stat();
            if (!dao.has(date)) {
                System.out.println("проверка выявила, что в системе нет такого матча");
                for (int j = 2; j <= 6; j++) {
                    String id = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[1]/div[2]/a")).getAttribute("href").split("/")[4];
                    int k,d,a;
                    k = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[3]")).getText());
                    d = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[5]")).getText());
                    a = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[4]")).getText());
                    double rate = Math.round((k + a * 0.41) / d * 100);
                    System.out.println("id=" + id + " k=" + k + " a=" + a + " d=" + d);
                    if (dao.id(id)) {
                        stat.add(id, rate / 100);
                    }
                }
                for (int j = 8; j <= 12; j++) {
                    String id = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[1]/div[2]/a")).getAttribute("href").split("/")[4];
                    int k,d,a;
                    k = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[3]")).getText());
                    d = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[5]")).getText());
                    a = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[2]/div/div[2]/div/div[5]/table/tbody/tr["
                            + i + "]/td[2]/table/tbody/tr[" + j + "]/td[4]")).getText());
                    double rate = Math.round((k + a * 0.41) / d * 100);
                    System.out.println("id=" + id + " k=" + k + " a=" + a + " d=" + d);
                    if (dao.id(id)) {
                        stat.add(id, rate / 100);
                    }
                }
                if(stat.check()) {
                    stat.setDate(new Date(date));
                    dao.add(stat);
                    System.out.println("добавили стату в систему");
                    try {dao.getLast();}
                    catch(Exception e){
                        hasLast = false;
                    }
                    if(hasLast) {
                        dao.deleteLast();
                        System.out.println("уничтожили последнюю стату в системе");
                    }
                }
                hasLast = true;
            }

        }
    }

    public void logout() {
        WebElement logoutThing = driver.findElement(By.id("account_pulldown"));
        new Actions(driver).moveToElement(logoutThing).click().perform();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/div[3]/div/a[3]"));
        new Actions(driver).moveToElement(logoutButton).click().perform();

        logout = false;
    }
}
