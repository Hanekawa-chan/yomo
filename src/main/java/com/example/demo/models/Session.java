package com.example.demo.models;

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

    public void login(){
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
        }

        rly = false;
    }
    public void refresh() {
        driver.navigate().refresh();
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
