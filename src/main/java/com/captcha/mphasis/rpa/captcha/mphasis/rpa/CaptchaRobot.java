package com.captcha.mphasis.rpa.captcha.mphasis.rpa;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.captcha.mphasis.rpa.captcha.mphasis.rpa.exceptions.WrongCaptchaEnteredException;
import com.google.common.base.Throwables;


public class CaptchaRobot extends RobotDriverWrapper {
	
	static Logger log = LoggerFactory.getLogger(CaptchaRobot.class);

    public static void initRobot() {

        try {
        	
        	CaptchaClient captchaClient = new CaptchaClient(driver(), fluentWait());
            captchaClient.pageOperations();
            captchaClient.enterDetails();
            
        } catch(Exception e) {
        	log.info("Message: "+e.getMessage()+" \nStackTrace: "+Throwables.getStackTraceAsString(e));        }

    }

    public static void main(String args[]) throws InterruptedException, IOException {
        initRobot();
    }
}
