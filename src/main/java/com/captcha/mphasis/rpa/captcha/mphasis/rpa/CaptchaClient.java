package com.captcha.mphasis.rpa.captcha.mphasis.rpa;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.captcha.mphasis.rpa.captcha.mphasis.rpa.exceptions.WrongCaptchaEnteredException;

public class CaptchaClient {

	WebDriver driver;
	FluentWait<WebDriver> wait;

	static Logger log = LoggerFactory.getLogger(CaptchaClient.class);

	public CaptchaClient(WebDriver driver, FluentWait<WebDriver> wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void pageOperations() throws InterruptedException {

		long page_load_wait = 5000;

		driver.get("https://irctc.co.in");
		Thread.sleep(page_load_wait);
		log.info("IRCTC Page is loaded.");

		WebElement okButton = driver.findElement(By.xpath("//button[@type='submit' and text()='OK']"));
		wait.until(ExpectedConditions.elementToBeClickable(okButton));
		okButton.click();
		log.info("OK popup button clicked");

		WebElement loginButton = driver.findElement(By.xpath("//a[text()=' LOGIN ']"));
		wait.until(ExpectedConditions.visibilityOf(loginButton));
		loginButton.click();
		log.info("Login popup opened after button click");

		Thread.sleep(page_load_wait);
	}

	public void enterDetails() throws IOException {

		String username = "snigdhayalamarthi";
		String password = "Snigdha@8";

		WebElement irctcUsername = driver.findElement(By.xpath("//input[@formcontrolname='userid']"));
		wait.until(ExpectedConditions.elementToBeClickable(irctcUsername));
		irctcUsername.sendKeys(username);
		log.info("Username entered");

		WebElement irctcPassword = driver.findElement(By.xpath("//input[@formcontrolname='password']"));
		wait.until(ExpectedConditions.elementToBeClickable(irctcPassword));
		irctcPassword.sendKeys(password);
		log.info("Password entered");

		findCaptcha();
	}

	public void findCaptcha() throws IOException, NoSuchElementException {

		try {
			
			if (!driver.findElements(By.id("nlpImgContainer")).isEmpty()) {
				WebElement captchaImage = driver.findElement(
						By.xpath("//img[starts-with(@src, 'https://irctclive.nlpcaptcha.in/index.php/media/getTC')]"));
				int value = 3;
				log.info("Captcha found. Case 3");
				downloadCaptcha(captchaImage, value);
			}
			if (!driver.findElements(By.id("nlpCaptchaImg")).isEmpty()) {
				WebElement captchaImage = driver.findElement(By.xpath("//img[@id='nlpCaptchaImg']"));
				int value = 1;
				log.info("Captcha found. Case 1");
				downloadCaptcha(captchaImage, value);
			}
			if (!driver.findElements(By.className("captcha-img")).isEmpty()) {
				WebElement captchaImage = driver.findElement(By.xpath("//img[@class='captcha-img']"));
				int value = 2;
				log.info("Captcha found. Case 2");
				downloadCaptcha(captchaImage, value);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadCaptcha(WebElement captchaImage, int value) throws IOException {
		String home = System.getProperty("user.home");
		String imageSrc = captchaImage.getAttribute("src");
		String imageLocation = home + "\\Downloads\\captcha.png";
		URL imageURL = new URL(imageSrc);
		BufferedImage saveImage = ImageIO.read(imageURL);
		ImageIO.write(saveImage, "png", new File(imageLocation));
		log.info("Captcha has been saved to: " + imageLocation);

		callPythonScript(imageLocation, value);
	}

	public void callPythonScript(String fileLocation, int value) {

		try {
			String output = null;
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
					"cd C:\\Users\\varun\\PycharmProjects\\captcha\\output\\main && main " + fileLocation + " "
							+ value);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			log.info("Builder has started.");
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null)
					break;
				log.info(line);
				output = line;
			}

			if (output != null) {
				finalSteps(output);
			}
		} catch (Exception e) {

		}
	}

	public void finalSteps(String captchaCode) throws InterruptedException, WrongCaptchaEnteredException, IOException {

		WebElement captchaInput = driver.findElement(By.xpath("//input[@id='nlpAnswer']"));
		wait.until(ExpectedConditions.elementToBeClickable(captchaInput));
		captchaInput.sendKeys(captchaCode);
		log.info("Captcha entered into the textbox.");

		WebElement loginButton = driver.findElement(By.xpath("//button[text()='SIGN IN']"));
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();
		log.info("Login button entered.");

		Thread.sleep(5000);

		if (!driver.findElements(By.xpath("//div[@class='loginError']")).isEmpty()) {
			String message = driver.findElement(By.xpath("//div[@class='loginError']")).getText();
//			throw new WrongCaptchaEnteredException("Error Occured: "+message);
			findCaptcha();
		}
	}

}
