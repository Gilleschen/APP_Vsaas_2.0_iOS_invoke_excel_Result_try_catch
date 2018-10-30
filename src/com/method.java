package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.experitest.appium.SeeTestCapabilityType;
import com.experitest.appium.SeeTestIOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class method {

	int port = 4723;// Appium studio port
	int device_timeout = 60;// 60sec
	int command_timeout = 30;// 30sec
	LoadExpectResult ExpectResult = new LoadExpectResult();
	static LoadTestCase TestCase = new LoadTestCase();
	static String CaseErrorList[][] = new String[TestCase.CaseList.size()][TestCase.DeviceInformation.deviceName
			.size()];// 紀錄各案例於各裝置之指令結果 (2維陣列)CaseErrorList[CaseList][Devices]
	String ErrorList[] = new String[TestCase.DeviceInformation.deviceName.size()];// 紀錄各裝置之指令結果
	static SeeTestIOSDriver driver[] = new SeeTestIOSDriver[TestCase.DeviceInformation.deviceName.size()];
	WebDriverWait[] wait = new WebDriverWait[driver.length];
	static XSSFWorkbook workBook;
	XSSFSheet Sheet;
	static String appElemnt;// APP元件名稱
	static String appInput;// 輸入值
	static String appInputXpath;// 輸入值的Xpath格式
	static String toElemnt;// APP元件名稱
	static int startx, starty, endx, endy;// Swipe移動座標
	static int iterative;// 畫面滑動次數
	static String scroll;// 畫面捲動方向 (DOWN/UP/LEFT/RIGHT)
	// static String appElemntarray;// 搜尋的多筆類似元件
	String element[] = new String[driver.length];
	static int CurrentCaseNumber = -1;// 目前執行到第幾個測試案列
	static Boolean CommandError = true;// 判定執行的指令是否出現錯誤；ture為正確；false為錯誤

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException, IOException {
		initial();
		invokeFunction();
		System.out.println("測試結束!!!!!!!!");
		Process proc = Runtime.getRuntime().exec("explorer C:\\TUTK_QA_TestTool\\TestReport");// 開啟TestReport資料夾

	}

	public static void initial() {// 初始化CaseErrorList矩陣
		for (int i = 0; i < CaseErrorList.length; i++) {
			for (int j = 0; j < CaseErrorList[i].length; j++) {
				CaseErrorList[i][j] = "";//填入空字串，避免取值時，出現錯誤
			}
		}
	}

	public static void invokeFunction() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		Object obj = new method();
		Class c = obj.getClass();
		String methodName = null;

		for (int CurrentCase = 0; CurrentCase < TestCase.StepList.size(); CurrentCase++) {
			CommandError = true;// 預設CommandError為True

			for (int CurrentCaseStep = 0; CurrentCaseStep < TestCase.StepList.get(CurrentCase)
					.size(); CurrentCaseStep++) {
				if (!CommandError) {
					break;// 若目前測試案例出現CommandError=false，則跳出目前案例並執行下一個案例
				}
				switch (TestCase.StepList.get(CurrentCase).get(CurrentCaseStep).toString()) {
				case "LaunchAPP":
					methodName = "LaunchAPP";
					break;

				case "ByXpath_SendKey":
					methodName = "ByXpath_SendKey";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					appInput = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2);
					CurrentCaseStep = CurrentCaseStep + 2;
					break;

				case "ByXpath_Click":
					methodName = "ByXpath_Click";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					CurrentCaseStep = CurrentCaseStep + 1;
					break;

				case "ByXpath_Wait":
					methodName = "ByXpath_Wait";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					CurrentCaseStep = CurrentCaseStep + 1;
					break;

				case "ByXpath_Swipe":
					methodName = "ByXpath_Swipe";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					toElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2);
					CurrentCaseStep = CurrentCaseStep + 2;
					break;

				case "HideKeyboard":
					methodName = "HideKeyboard";
					break;

				case "ByXpath_Result":
					methodName = "ByXpath_Result";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					CurrentCaseStep = CurrentCaseStep + 1;
					break;

				case "Sleep":
					methodName = "Sleep";
					appInput = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					CurrentCaseStep = CurrentCaseStep + 1;
					break;

				case "ScreenShot":
					methodName = "ScreenShot";
					break;

				case "Orientation":
					methodName = "Orientation";
					appInput = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					CurrentCaseStep = CurrentCaseStep + 1;
					break;

				case "Swipe":
					methodName = "Swipe";
					startx = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1));
					starty = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2));
					endx = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 3));
					endy = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 4));
					iterative = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 5));
					CurrentCaseStep = CurrentCaseStep + 5;
					break;

				case "ByXpath_Swipe_Vertical":
					methodName = "ByXpath_Swipe_Vertical";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					scroll = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2);
					iterative = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 3));
					CurrentCaseStep = CurrentCaseStep + 3;
					break;

				case "ByXpath_Swipe_Horizontal":
					methodName = "ByXpath_Swipe_Horizontal";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					scroll = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2);
					iterative = Integer.valueOf(TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 3));
					CurrentCaseStep = CurrentCaseStep + 3;
					break;

				case "ByXpath_Swipe_FindText_Click_iOS":
					methodName = "ByXpath_Swipe_FindText_Click_iOS";
					appElemnt = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 1);
					scroll = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 2);
					appInput = TestCase.StepList.get(CurrentCase).get(CurrentCaseStep + 3);
					CurrentCaseStep = CurrentCaseStep + 3;
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

				case "Menu":
					methodName = "Menu";
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
	}

	public void ByXpath_Wait() {

		for (int i = 0; i < driver.length; i++) {
			wait[i] = new WebDriverWait(driver[i], device_timeout);
			try {
				wait[i].until(ExpectedConditions.presenceOfElementLocated(By.xpath(appElemnt)));
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}
		}
	}

	public void ByXpath_Result() {
		boolean result[] = new boolean[driver.length];// 未給定Boolean值，預設為False
		boolean ErrorResult[] = new boolean[driver.length];

		for (int i = 0; i < driver.length; i++) {

			try {
				element[i] = driver[i].findElement(By.xpath(appElemnt)).getAttribute("text");
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				element[i] = "ERROR";// 找不到該物件，回傳Error
			}

			if (element[i].equals("ERROR")) {
				ErrorResult[i] = true;

			} else {
				// 回傳測試案例清單的名稱給ExpectResult.LoadExpectResult，並存放期望結果至ResultList清單
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
		// 開啟Excel

		try {
			workBook = new XSSFWorkbook(new FileInputStream("C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm"));
		} catch (Exception e) {
			System.out.println("[Error] Can't find C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm");
		}

		for (int i = 0; i < driver.length; i++) {

			if (TestCase.DeviceInformation.deviceName.get(i).toString().length() > 20) {// Excel工作表名稱最常31字元因，故需判斷UDID長度是否大於31
				char[] NewUdid = new char[20];// 因需包含_TestReport字串(共11字元)，故設定20位字元陣列(31-11)
				TestCase.DeviceInformation.deviceName.get(i).toString().getChars(0, 20, NewUdid, 0);// 取出UDID前20字元給NewUdid
				Sheet = workBook.getSheet(String.valueOf(NewUdid) + "_TestReport");// 根據NewUdid，指定某台裝置的TestReport
																					// sheet
			} else {
				Sheet = workBook.getSheet(TestCase.DeviceInformation.deviceName.get(i).toString() + "_TestReport");// 指定某台裝置的TestReport
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
		// 執行寫入Excel後的存檔動作

		try {
			FileOutputStream out = new FileOutputStream(new File("C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm"));
			workBook.write(out);
			out.close();
			workBook.close();

		} catch (Exception e) {
			System.out.println("[Error] Can't find C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm");
		}

		// CurrentCaseNumber = CurrentCaseNumber + 1;

	}

	public void ByXpath_SendKey() {
		for (int i = 0; i < driver.length; i++) {
			try {
				driver[i].findElement(By.xpath(appElemnt)).sendKeys(appInput);
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}
		}
	}

	public void ByXpath_Click() {
		for (int i = 0; i < driver.length; i++) {
			try {
				driver[i].findElement(By.xpath(appElemnt)).click();
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}

		}
	}

	public void ByXpath_Swipe() {
		Point p1, p2;// p1 為起點;p2為終點

		for (int i = 0; i < driver.length; i++) {
			try {
				p2 = driver[i].findElement(By.xpath(toElemnt)).getLocation();
				p1 = driver[i].findElement(By.xpath(appElemnt)).getLocation();
				driver[i].swipe(p1.x, p1.y, p1.x, p1.y - (p1.y - p2.y), 1000);
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception e) {
				System.out.println("[Error] Can't find " + appElemnt);
				System.out.println("[Error] or Can't find " + toElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}
		}
	}

	public void Swipe() {
		for (int i = 0; i < driver.length; i++) {
			for (int j = 0; j < iterative; j++) {
				driver[i].swipe(startx, starty, endx, endy, 500);
			}
		}
	}

	/*
	 * 上下隨機滑動n次 public void Swipe() { Random rand = new Random(); boolean
	 * items[] = { true, false }; for (int i = 0; i < driver.length; i++) { for
	 * (int j = 0; j < iterative; j++) { if (items[rand.nextInt(items.length)])
	 * { driver[i].swipe(startx, starty, endx, endy, 500); }else{
	 * driver[i].swipe(endx, endy, startx , starty , 500); } } } }
	 * 
	 */

	public void ByXpath_Swipe_Vertical() {
		Point p;// 元件座標
		Dimension s;// 元件大小
		WebElement e;
		for (int i = 0; i < driver.length; i++) {

			try {
				e = driver[i].findElement(By.xpath(appElemnt));
				s = e.getSize();
				p = e.getLocation();
				int errorX = (int) Math.round(s.width * 0.1);
				int errorY = (int) Math.round(s.height * 0.1);
				for (int j = 0; j < iterative; j++) {
					if (scroll.equals("DOWN")) {// 畫面向下捲動
						driver[i].swipe(p.x + errorX, p.y + s.height - errorY, p.x + errorX, p.y + errorY, 1000);
					} else if (scroll.equals("UP")) {// 畫面向上捲動
						driver[i].swipe(p.x + errorX, p.y + errorY, p.x + errorX, p.y + s.height - errorY, 1000);
					}
				}
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception w) {
				System.out.println("[Error] Can't find " + appElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}

		}
	}

	public void ByXpath_Swipe_Horizontal() {
		Point p;// 元件座標
		Dimension s;// 元件大小
		WebElement e;
		for (int i = 0; i < driver.length; i++) {
			try {
				e = driver[i].findElement(By.xpath(appElemnt));
				s = e.getSize();
				p = e.getLocation();
				int errorX = (int) Math.round(s.getWidth() * 0.1);
				int errorY = (int) Math.round(s.getHeight() * 0.1);
				for (int j = 0; j < iterative; j++) {
					if (scroll.equals("RIGHT")) {// 畫面向右捲動
						driver[i].swipe(p.x + errorX, p.y + errorY, p.x + s.width - errorX, p.y + errorY, 1000);
					} else if (scroll.equals("LEFT")) {// 畫面向左捲動
						driver[i].swipe(p.x + s.width - errorX, p.y + errorY, p.x + errorX, p.y + errorY, 1000);
					}
				}
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception w) {
				System.out.println("[Error] Can't find " + appElemnt);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
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

	public void ByXpath_Swipe_FindText_Click_iOS() {

		for (int i = 0; i < driver.length; i++) {
			wait[i] = new WebDriverWait(driver[i], device_timeout);
			try {

				WebElement ScrollBar, targetElement;// 定義卷軸與準備搜尋的元件
				Point ScrollBarP, targetElementP;// 卷軸與準備搜尋元件的座標
				Dimension ScrollBarS, targetElementS;// 卷軸與準備搜尋元件的長及寬

				ScrollBar = wait[i].until(ExpectedConditions.visibilityOfElementLocated(By.xpath(appElemnt)));
				targetElement = wait[i].until(ExpectedConditions.visibilityOfElementLocated(By.xpath(appInput)));

				ScrollBarP = ScrollBar.getLocation();// 卷軸的座標
				targetElementP = targetElement.getLocation();// 準備搜尋元件的座標

				ScrollBarS = ScrollBar.getSize();// 卷軸元件的長及寬
				targetElementS = targetElement.getSize();// 準備搜尋元件的長及寬

				int errory = (int) Math.round(ScrollBarS.height * 0.1);
				int errorx = (int) Math.round(ScrollBarS.width * 0.1);

				while (targetElementP.y == 0 || targetElementP.x < 0
						|| targetElementP.x + targetElementS.width > ScrollBarP.x + ScrollBarS.width) {// 根據搜尋元件的y座標或x座標是否為0，來判斷尋元件是否顯示於行動裝置畫面上

					switch (scroll.toString()) {

					case "DOWN":// 向下搜尋
						driver[i].swipe(ScrollBarP.x, ScrollBarS.height + ScrollBarP.y - errory, ScrollBarP.x,
								ScrollBarP.y + errory, 2000);
						break;

					case "UP":// 向上搜尋
						driver[i].swipe(ScrollBarP.x, ScrollBarP.y + errory, ScrollBarP.x,
								ScrollBarP.y + ScrollBarS.height - errory, 2000);
						break;

					case "LEFT":// 畫面向左捲動(觀看畫面右方內容)
						driver[i].swipe(ScrollBarP.x + ScrollBarS.width - errorx, ScrollBarP.y, ScrollBarP.x + errorx,
								ScrollBarP.y, 2000);
						break;

					case "RIGHT":// 畫面向右捲動(觀看畫面左方內容)
						driver[i].swipe(ScrollBarP.x + errorx, ScrollBarP.y, ScrollBarP.x + ScrollBarS.width - errorx,
								ScrollBarP.y, 2000);
						break;
					}
					targetElement = wait[i].until(ExpectedConditions.visibilityOfElementLocated(By.xpath(appInput)));
					targetElementP = targetElement.getLocation();
				}

				// 當搜尋元件出現於畫面上後，再將搜尋元件完整移動至捲軸的UI範圍內，目的：避免搜尋元件部分UI被捲軸的UI擋住，導致無法正確Click()元件
				switch (scroll.toString()) {

				case "DOWN":
					// 判斷搜尋元件UI是否被捲軸的UI擋住
					if (targetElementP.y + targetElementS.height > ScrollBarP.y + ScrollBarS.height) {
						driver[i].swipe(targetElementP.x, targetElementP.y - errory, targetElementP.x,
								ScrollBarP.y + errory, 2000);
					}
					break;

				case "UP":
					// 判斷搜尋元件UI是否被捲軸的UI擋住
					if (targetElementP.y < ScrollBarP.y) {
						driver[i].swipe(targetElementP.x, targetElementP.y + targetElementS.height + errory,
								targetElementP.x, ScrollBarP.y + ScrollBarS.height - errory, 2000);
					}
					break;

				case "LEFT":
					if (targetElementP.x + targetElementS.width > ScrollBarP.x + ScrollBarS.width) {
						driver[i].swipe(targetElementP.x - errorx, ScrollBarP.y, ScrollBarP.x + errorx, ScrollBarP.y,
								2000);
					}

					break;

				case "RIGHT":
					if (targetElementP.x < 0) {
						driver[i].swipe(targetElementP.x + targetElementS.width + errorx, ScrollBarP.y,
								ScrollBarP.x + ScrollBarS.width - errorx, ScrollBarP.y, 2000);
					}
					break;
				}

				wait[i].until(ExpectedConditions.visibilityOfElementLocated(By.xpath(appInput))).click();
				// driver[i].findElement(By.xpath(appInput)).click();
				ErrorList[i] = "Pass";
				CaseErrorList[CurrentCaseNumber] = ErrorList;
			} catch (Exception w) {
				System.out.print("[Error] Can't find " + appElemnt);
				System.out.println("[Error] Can't find " + appInput);
				CommandError = false;// 若找不到指定元件，則設定CommandError=false
			}
		}

	}

	public void QuitAPP() {
		for (int j = 0; j < driver.length; j++) {
			driver[j].quit();// 離開APP後，寫入測試結果Pass或Error

			// 開啟Excel
			try {
				workBook = new XSSFWorkbook(new FileInputStream("C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm"));
			} catch (Exception e) {
				System.out.println("[Error] Can't find C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm");
			}
			for (int i = 0; i < driver.length; i++) {

				if (TestCase.DeviceInformation.deviceName.get(i).toString().length() > 20) {// Excel工作表名稱最常31字元因，故需判斷UDID長度是否大於31
					char[] NewUdid = new char[20];// 因需包含_TestReport字串(共11字元)，故設定20位字元陣列(31-11)
					TestCase.DeviceInformation.deviceName.get(i).toString().getChars(0, 20, NewUdid, 0);// 取出UDID前20字元給NewUdid
					Sheet = workBook.getSheet(String.valueOf(NewUdid) + "_TestReport");// 根據NewUdid，指定某台裝置的TestReport
																						// sheet
				} else {
					Sheet = workBook.getSheet(TestCase.DeviceInformation.deviceName.get(i).toString() + "_TestReport");// 指定某台裝置的TestReport
																														// sheet
				}

				if (CaseErrorList[CurrentCaseNumber][i].equals("Pass")) {// 取出CaseErrorList之第CurrentCaseNumber個測項中的第i台行動裝置之結果
					Sheet.getRow(CurrentCaseNumber + 1).getCell(1).setCellValue("Pass");// 填入第i台行動裝置之第CurrentCaseNumber個測項結果Pass
				}

			}
			// 執行寫入Excel後的存檔動作
			try {
				FileOutputStream out = new FileOutputStream(
						new File("C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm"));
				workBook.write(out);
				out.close();
				workBook.close();
			} catch (Exception e) {
				System.out.println("[Error] Can't find C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm");
			}

		}
	}

	public void Sleep() {
		String NewString = "";// 新字串
		char[] r = { '.' };// 小數點字元
		char[] c = appInput.toCharArray();// 將字串轉成字元陣列
		for (int i = 0; i < c.length; i++) {
			if (c[i] != r[0]) {// 判斷字元是否為小數點
				NewString = NewString + c[i];// 否，將字元組合成新字串
			} else {
				break;// 是，跳出迴圈
			}
		}

		try {
			System.out.println("[driver] [start] Sleep(): " + NewString + " second...");
			Thread.sleep(Integer.valueOf(NewString) * 1000);// 將字串轉成整數
			System.out.println("[driver] [end] Sleep");
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
				FileUtils.copyFile(screenShotFile, new File("C:\\TUTK_QA_TestTool\\TestReport\\"
						+ TestCase.CaseList.get(CurrentCaseNumber) + "_" + month + day + hour + min + sec + ".jpg"));
				System.out.println("[Log] " + "ScreenShot Successfully!! (CaseName+Month+Day+Hour+Minus+Second)");
			} catch (IOException e) {
				System.out.println("[Error]Fail to ScreenShot");
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
		CurrentCaseNumber = CurrentCaseNumber + 1;
		for (int i = 0; i < TestCase.DeviceInformation.deviceName.size(); i++) {
			cap[i] = new DesiredCapabilities();
		}

		for (int i = 0; i < TestCase.DeviceInformation.deviceName.size(); i++) {

			for (int j = i; j < TestCase.DeviceInformation.platformVersion.size(); j++) {

				cap[i].setCapability(SeeTestCapabilityType.WAIT_FOR_DEVICE_TIMEOUT_MILLIS, device_timeout * 1000);
				cap[i].setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, command_timeout);
				cap[i].setCapability(MobileCapabilityType.UDID, TestCase.DeviceInformation.deviceName.get(i));
				cap[i].setCapability(IOSMobileCapabilityType.BUNDLE_ID, TestCase.DeviceInformation.BundleID);
				cap[i].setCapability(SeeTestCapabilityType.REPORT_FORMAT, "xml");
				cap[i].setCapability(SeeTestCapabilityType.REPORT_DIRECTORY, "C:\\TUTK_QA_TestTool\\TestReport");// Report路徑
				cap[i].setCapability(SeeTestCapabilityType.TEST_NAME, TestCase.CaseList.get(CurrentCaseNumber));// TestCase名稱

				try {
					driver[j] = new SeeTestIOSDriver(new URL("http://localhost:" + port + "/wd/hub"), cap[j]);
				} catch (Exception e) {
					System.out.print("[Error] Can't find UDID:" + TestCase.DeviceInformation.deviceName.get(i));
					System.out.println(" or can not find BundleID: " + TestCase.DeviceInformation.BundleID);
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

			driver[i].deviceAction("Home");
		}
	}

	public void Power() {
		for (int i = 0; i < driver.length; i++) {

			driver[i].deviceAction("Power");

		}
	}

	public void Menu() {
		for (int i = 0; i < driver.length; i++) {

			driver[i].deviceAction("Recent Apps");
		}
	}

}
