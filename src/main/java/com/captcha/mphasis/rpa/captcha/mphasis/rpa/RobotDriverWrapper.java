package com.captcha.mphasis.rpa.captcha.mphasis.rpa;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

public class RobotDriverWrapper {

	public static WebDriver driver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\varun\\OneDrive\\Desktop\\mphasis\\chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.addArguments("headless");
		chromeOptions.addArguments("--ignore-certificate-errors");
		chromeOptions.addArguments("--disable-gpu");
		chromeOptions.addArguments("window-size-1920,1080");
		chromeOptions.addArguments("--start-maximized");
		chromeOptions.addArguments("--incognito");
		chromeOptions.addArguments("--allow-insecure-localhost");
		chromeOptions.addArguments("--allow-running-insecure-content");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--ignore-ssl-errors");
		chromeOptions.setAcceptInsecureCerts(true);

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		WebDriver driver = new ChromeDriver(chromeOptions);

		return driver;
	}

	@SuppressWarnings("deprecation")
	public static FluentWait<WebDriver> fluentWait() {

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver()).withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		return wait;
	}

}
