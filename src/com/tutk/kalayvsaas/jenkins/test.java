package com.tutk.kalayvsaas.jenkins;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.experitest.appium.SeeTestCapabilityType;
import com.experitest.appium.SeeTestIOSDriver;

import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class test {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		SeeTestIOSDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability(SeeTestCapabilityType.WAIT_FOR_DEVICE_TIMEOUT_MILLIS, 60 * 1000);
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 30);
		cap.setCapability(MobileCapabilityType.UDID, "7e9529ce5c4ee1c7de7598e7ac26e25e2d1f8700");
		cap.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.tutk.kalayvsaas.v3.jenkins");

		driver = new SeeTestIOSDriver(new URL("http://localhost:4723/wd/hub"), cap);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("xpath=//*[@text='device btn setting n' and ./preceding-sibling::*[@text='已連線']]")));
		driver.findElement(By.xpath("xpath=//*[@text='device btn setting n' and ./preceding-sibling::*[@text='已連線']]")).click();

		Thread.sleep(6000);
		driver.swipe(228, 2000, 228, 445, 1000);
		driver.findElement(By.xpath("//*[@text='時區']")).click();
		String targetText = "Mexico-CST";
		String targetXpath = "//*[@text='Mexico-CST']";
		String Scroll_list = "//*[@class='UIATable']";
		String scroll = "UP";

		try {

			WebElement ScrollBar, targetElement;
			Point ScrollBarP, targetElementP;// 元件座標
			Dimension ScrollBarS, targetElementS;

			ScrollBar = driver.findElement(By.xpath(Scroll_list));// Scroll bar
			targetElement = driver.findElement(By.xpath(targetXpath));

			ScrollBarP = ScrollBar.getLocation();
			targetElementP = targetElement.getLocation();

			ScrollBarS = ScrollBar.getSize();
			targetElementS = targetElement.getSize();

			int errory = (int) Math.round(ScrollBarS.height * 0.1);
			int errorx = (int) Math.round(ScrollBarS.width * 0.1);

			while (targetElementP.y == 0) {

				switch (scroll.toString()) {

				case "DOWN":
					driver.swipe(ScrollBarP.x, ScrollBarS.height + ScrollBarP.y - errory, ScrollBarP.x, ScrollBarP.y,
							2000);
				case "UP":
					driver.swipe(ScrollBarP.x, ScrollBarP.y + errory, ScrollBarP.x,
							ScrollBarP.y + ScrollBarS.height - errory, 2000);
				case "LEFT":

				case "RIGHT":

				}
				targetElement = driver.findElement(By.xpath(targetXpath));
				targetElementP = targetElement.getLocation();
			}

			switch (scroll.toString()) {

			case "DOWN":
				driver.swipe(targetElementP.x, targetElementP.y - errory, targetElementP.x, ScrollBarP.y + errory,
						2000);
			case "UP":
				driver.swipe(targetElementP.x, targetElementP.y + targetElementS.height + errory, targetElementP.x,
						ScrollBarS.height, 2000);
			case "LEFT":

			case "RIGHT":

			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(targetXpath)));
			driver.findElement(By.xpath(targetXpath)).click();

		} catch (Exception w) {
			System.out.println("Can't find " + targetText);
		}

	}

}
