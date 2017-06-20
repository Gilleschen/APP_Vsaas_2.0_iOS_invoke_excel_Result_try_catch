package com.tutk.kalayvsaas.jenkins;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadDeviceInformation {

	public ArrayList<String> deviceName = new ArrayList<String>();//�s����UDID
	public ArrayList<String> platformVersion = new ArrayList<String>();//�s����OS����
	public ArrayList<String> ScriptList = new ArrayList<String>();//�s��ݰ��檺�}���W��
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
					deviceName.add(Sheet.getRow(j).getCell(2).toString());// ���UDID
					platformVersion.add(Sheet.getRow(j).getCell(3).toString());// ���OS����
					j++;
				} while (!Sheet.getRow(j).getCell(2).toString().equals(""));
			} catch (Exception e) {
				;
			}

			int k = 1;
			try {
				do {
					ScriptList.add(Sheet.getRow(k).getCell(4).toString());// �ǳƭnRun���}��
					k++;
				} while (!Sheet.getRow(k).getCell(4).toString().equals(""));
			} catch (Exception e) {
				;
			}

			System.out.println("���ճ]��(UDID/Android Version)");

			for (int i = 0; i < deviceName.size(); i++) {
				System.out.print(deviceName.get(i) + "/");
				System.out.println(platformVersion.get(i));
			}
			workBook.close();

		} catch (Exception e) {

		}
	}

}
