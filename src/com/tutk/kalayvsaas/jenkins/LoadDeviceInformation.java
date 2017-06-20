package com.tutk.kalayvsaas.jenkins;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadDeviceInformation {

	public ArrayList<String> deviceName = new ArrayList<String>();//存放手機UDID
	public ArrayList<String> platformVersion = new ArrayList<String>();//存放手機OS版本
	public ArrayList<String> ScriptList = new ArrayList<String>();//存放待執行的腳本名稱
	public String BundleID;//APP BundleID
	

	public LoadDeviceInformation() {
		XSSFWorkbook workBook;
		XSSFSheet Sheet;

		try {

			workBook = new XSSFWorkbook(new FileInputStream("C:\\TestScript.xlsm"));//hard code
			Sheet = workBook.getSheet("APP&Device");//hard code
			deviceName = new ArrayList<String>();
			platformVersion = new ArrayList<String>();
			ScriptList = new ArrayList<String>();

			
			BundleID = Sheet.getRow(1).getCell(1).toString();//hard code
			int j = 1;
			try {
				do {
					deviceName.add(Sheet.getRow(j).getCell(2).toString());// 手機UDID
					platformVersion.add(Sheet.getRow(j).getCell(3).toString());// 手機OS版本
					j++;
				} while (!Sheet.getRow(j).getCell(2).toString().equals(""));
			} catch (Exception e) {
				;
			}

			int k = 1;
			try {
				do {
					ScriptList.add(Sheet.getRow(k).getCell(4).toString());// 準備要Run的腳本
					k++;
				} while (!Sheet.getRow(k).getCell(4).toString().equals(""));
			} catch (Exception e) {
				;
			}

			System.out.println("測試設備(UDID/Android Version)");

			for (int i = 0; i < deviceName.size(); i++) {
				System.out.print(deviceName.get(i) + "/");
				System.out.println(platformVersion.get(i));
			}
			workBook.close();

		} catch (Exception e) {

		}
	}

}
