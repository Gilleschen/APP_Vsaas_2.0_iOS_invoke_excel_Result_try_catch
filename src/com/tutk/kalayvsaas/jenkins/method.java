package com.tutk.kalayvsaas.jenkins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.experitest.appium.SeeTestCapabilityType;
import com.experitest.appium.SeeTestIOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class method {
	int port = 8889;// Appium studio port
	int device_timeout = 60;// 60sec
	int command_timeout = 30;// 30sec
	LoadExpectResult ExpectResult = new LoadExpectResult();
	static LoadTestCase TestCase = new LoadTestCase();
	static SeeTestIOSDriver driver[] = new SeeTestIOSDriver[TestCase.DeviceInformation.deviceName.size()];
	WebDriverWait[] wait = new WebDriverWait[driver.length];
	static XSSFWorkbook workBook;
	static String appElemnt;// APP����W��
	static String appInput;// ��J��
	String element[] = new String[driver.length];
	static int CurrentCaseNumber = 0;// �ثe�����ĴX�Ӵ��ծצC
	XSSFSheet Sheet;

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		invokeFunction();
		System.out.println("���յ���!!!!!!!!");

	}

	public static void invokeFunction() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		Object obj = new method();
		Class c = obj.getClass();
		String methodName = null;

		for (int i = 0; i < TestCase.StepList.size(); i++) {

			switch (TestCase.StepList.get(i).toString()) {

			case "LaunchAPP":
				methodName = "LaunchAPP";
				break;

			case "ByXpath_SendKey":
				methodName = "ByXpath_SendKey";
				appElemnt = TestCase.StepList.get(i + 1);
				appInput = TestCase.StepList.get(i + 2);
				i = i + 2;
				break;

			case "ByXpath_Click":
				methodName = "ByXpath_Click";
				appElemnt = TestCase.StepList.get(i + 1);
				i = i + 1;
				break;

			case "ByXpath_Wait":
				methodName = "ByXpath_Wait";
				appElemnt = TestCase.StepList.get(i + 1);
				i = i + 1;
				break;

			case "HideKeyboard":
				methodName = "HideKeyboard";
				break;

			case "ByXpath_Result":
				methodName = "ByXpath_Result";
				appElemnt = TestCase.StepList.get(i + 1);
				i = i + 1;
				break;

			case "Sleep":
				methodName = "Sleep";
				appInput = TestCase.StepList.get(i + 1);
				i = i + 1;
				break;

			case "ScreenShot":
				methodName = "ScreenShot";
				break;

			case "Orientation":
				methodName = "Orientation";
				appInput = TestCase.StepList.get(i + 1);
				i = i + 1;
				break;

			case "Back":
				methodName = "Back";
				break;

			case "Home":
				methodName = "Home";
				break;

			case "Power":
				methodName = "Power";
				break;

			case "ResetAPP":
				methodName = "ResetAPP";
				break;

			case "QuitAPP":
				methodName = "QuitAPP";
				break;
			}

			Method method;
			method = c.getMethod(methodName, new Class[] {});
			method.invoke(c.newInstance());

		}
	}

	public void ByXpath_Wait() {
		
		for (int i = 0; i < driver.length; i++) {
			wait[i] = new WebDriverWait(driver[i], device_timeout);
			try {
				wait[i].until(ExpectedConditions.presenceOfElementLocated(By.xpath(appElemnt)));
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
			}
		}
	}

	public void ByXpath_Result() {
		boolean result[] = new boolean[driver.length];// �����wBoolean�ȡA�w�]��False
		boolean ErrorResult[] = new boolean[driver.length];

		for (int i = 0; i < driver.length; i++) {

			try {
				element[i] = driver[i].findElement(By.xpath(appElemnt)).getAttribute("text");
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				element[i] = "ERROR";// �䤣��Ӫ���A�^��Error
			}

			if (element[i].equals("ERROR")) {
				ErrorResult[i] = true;

			} else {
				// �^�Ǵ��ծרҲM�檺�W�ٵ�ExpectResult.LoadExpectResult�A�æs����浲�G��ResultList�M��
				ExpectResult.LoadExpectResult(TestCase.CaseList.get(CurrentCaseNumber).toString());
				for (int j = 0; j < ExpectResult.ResultList.size(); j++) {
					if (element[i].equals(ExpectResult.ResultList.get(j)) == true) {
						result[i] = true;
						break;
					} else {
						result[i] = false;
					}
				}
			}
		}
		// �}��Excel

		try {
			workBook = new XSSFWorkbook(new FileInputStream("C:\\TestReport\\TestReport.xlsm"));
		} catch (Exception e) {
			System.out.println("[Error] Can't find C:\\TestReport\\TestReport.xlsm");
		}

		for (int i = 0; i < driver.length; i++) {

			if (TestCase.DeviceInformation.deviceName.get(i).toString().length() > 20) {// Excel�u�@��W�ٳ̱`31�r���]�A�G�ݧP�_UDID���׬O�_�j��31
				char[] NewUdid = new char[20];// �]�ݥ]�t_TestReport�r��(�@11�r��)�A�G�]�w20��r���}�C(31-11)
				TestCase.DeviceInformation.deviceName.get(i).toString().getChars(0, 20, NewUdid, 0);// ���XUDID�e20�r����NewUdid
				Sheet = workBook.getSheet(String.valueOf(NewUdid) + "_TestReport");// �ھ�NewUdid�A���w�Y�x�˸m��TestReport
																					// sheet
			} else {
				Sheet = workBook.getSheet(TestCase.DeviceInformation.deviceName.get(i).toString() + "_TestReport");// ���w�Y�x�˸m��TestReport
																													// sheet
			}

			if (ErrorResult[i] == true) {
				Sheet.getRow(CurrentCaseNumber + 1).createCell(1).setCellValue("Error!!");
			} else if (result[i] == true) {
				Sheet.getRow(CurrentCaseNumber + 1).createCell(1).setCellValue("Pass");
			} else if (result[i] == false) {
				Sheet.getRow(CurrentCaseNumber + 1).createCell(1).setCellValue("Fail");
			}
		}
		// ����g�JExcel�᪺�s�ɰʧ@

		try {
			FileOutputStream out = new FileOutputStream(new File("C:\\TestReport\\TestReport.xlsm"));
			workBook.write(out);
			out.close();
			workBook.close();

		} catch (Exception e) {
			System.out.println("[Error] Can't find C:\\TestReport\\TestReport.xlsm");
		}

		CurrentCaseNumber = CurrentCaseNumber + 1;

	}

	public void ByXpath_SendKey() {
		for (int i = 0; i < driver.length; i++) {
			try {
				driver[i].findElement(By.xpath(appElemnt)).sendKeys(appInput);
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);

			}

		}
	}

	public void ByXpath_Click() {
		for (int i = 0; i < driver.length; i++) {
			try {
				driver[i].findElement(By.xpath(appElemnt)).click();
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
			}

		}
	}

	public void HideKeyboard() {
		for (int i = 0; i < driver.length; i++) {

			try {
				driver[i].hideKeyboard();
			} catch (Exception ex) {
				;
			}
		}
	}

	public void QuitAPP() {
		for (int i = 0; i < driver.length; i++) {
			driver[i].quit();
		}
	}

	public void Sleep() {
		String NewString = "";// �s�r��
		char[] r = { '.' };// �p���I�r��
		char[] c = appInput.toCharArray();// �N�r���ন�r���}�C
		for (int i = 0; i < c.length; i++) {
			if (c[i] != r[0]) {// �P�_�r���O�_���p���I
				NewString = NewString + c[i];// �_�A�N�r���զX���s�r��
			} else {
				break;// �O�A���X�j��
			}
		}

		try {
			System.out.println("[driver] [start] sleep(): " + NewString + " second...");
			Thread.sleep(Integer.valueOf(NewString) * 1000);// �N�r���ন���
			System.out.println("[driver] [end] sleep");
		} catch (Exception e) {
			;
		}
	}

	public void ScreenShot() {
		Calendar date = Calendar.getInstance();
		String month = Integer.toString(date.get(Calendar.MONTH) + 1);
		String day = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(date.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(date.get(Calendar.MINUTE));
		String sec = Integer.toString(date.get(Calendar.SECOND));
		for (int i = 0; i < driver.length; i++) {
			File screenShotFile = (File) driver[i].getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(screenShotFile, new File(
						TestCase.CaseList.get(CurrentCaseNumber) + "_" + month + day + hour + min + sec + ".jpg"));
				System.out.println("[Log] " + "ScreenShoot Successfully!! (CaseName+Month+Day+Hour+Minus+Second)");
			} catch (IOException e) {
				;
			}
		}
	}

	public void Orientation() {
		for (int i = 0; i < driver.length; i++) {
			if (appInput.equals("Landscape")) {
				driver[i].rotate(ScreenOrientation.LANDSCAPE);
			} else if (appInput.equals("Portrait")) {
				driver[i].rotate(ScreenOrientation.PORTRAIT);
			}

		}
	}

	public void ResetAPP() {
		for (int i = 0; i < driver.length; i++) {
			driver[i].resetApp();
		}
	}

	public void LaunchAPP() {
		DesiredCapabilities cap[] = new DesiredCapabilities[TestCase.DeviceInformation.deviceName.size()];

		for (int i = 0; i < TestCase.DeviceInformation.deviceName.size(); i++) {
			cap[i] = new DesiredCapabilities();
		}

		for (int i = 0; i < TestCase.DeviceInformation.deviceName.size(); i++) {

			for (int j = i; j < TestCase.DeviceInformation.platformVersion.size(); j++) {

				cap[i].setCapability(SeeTestCapabilityType.WAIT_FOR_DEVICE_TIMEOUT_MILLIS, device_timeout * 1000);
				cap[i].setCapability(MobileCapabilityType.UDID, TestCase.DeviceInformation.deviceName.get(i));
				cap[i].setCapability(IOSMobileCapabilityType.BUNDLE_ID, TestCase.DeviceInformation.BundleID);
				cap[i].setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, command_timeout);

				try {
					driver[j] = new SeeTestIOSDriver<>(new URL("http://localhost:" + port + "/wd/hub"), cap[j]);
				} catch (MalformedURLException e) {
					System.out.print("[Error] Can't find UDID: " + TestCase.DeviceInformation.deviceName.get(i));
					System.out.println("or can't find BundleID: " + TestCase.DeviceInformation.BundleID);
				}
				break;
			}
			// port = port + 2;
		}
	}

	public void Back() {
		for (int i = 0; i < driver.length; i++) {
			// driver[i].pressKeyCode(AndroidKeyCode.BACK);

		}
	}

	public void Home() {
		for (int i = 0; i < driver.length; i++) {

			// driver[i].pressKeyCode(AndroidKeyCode.HOME);
		}
	}

	public void Power() {
		for (int i = 0; i < driver.length; i++) {

			// driver[i].pressKeyCode(AndroidKeyCode.KEYCODE_POWER);

		}
	}

}
