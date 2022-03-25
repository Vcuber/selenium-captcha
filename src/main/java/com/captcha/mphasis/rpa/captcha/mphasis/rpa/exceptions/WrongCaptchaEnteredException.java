package com.captcha.mphasis.rpa.captcha.mphasis.rpa.exceptions;

public class WrongCaptchaEnteredException extends Exception {
	
	public String message;
	
	public WrongCaptchaEnteredException(String message) {
		this.message = message;
	}
}
