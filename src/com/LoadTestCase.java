package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadTestCase {

	public ArrayList<ArrayList<String>> StepList = new ArrayList<ArrayList<String>>();// 所有測試案例的動作清單(2維陣列)
	ArrayList<String> StepListData = new ArrayList<String>();//單一測試案例的動作清單
	public ArrayList<String> CaseList = new ArrayList<String>();// 所有測試案例的名稱清單
	LoadDeviceInformation DeviceInformation = new LoadDeviceInformation();

	public LoadTestCase() {
		XSSFWorkbook workbook = null;
		XSSFSheet sheet;

		try {
			workbook = new XSSFWorkbook(new FileInputStream("C:\\TUTK_QA_TestTool\\TestTool\\TestScript.xlsm"));

			CaseList = new ArrayList<String>();
			StepList = new ArrayList<ArrayList<String>>();
			StepListData = new ArrayList<String>();
			for (int k = 0; k < DeviceInformation.ScriptList.size(); k++) {

				sheet = workbook.getSheet(DeviceInformation.ScriptList.get(k).toString());// 指定待測試腳本的sheet
				int i = 0;

				try {
					
					do {// column Number

						for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {

							if (sheet.getRow(i).getCell(j) != null) {// Apache
																		// POI
																		// 讀取Excel儲存格時，有機率將空白儲存格讀入，因此需判斷儲存格是否為空白，皆null

								if (sheet.getRow(i).getCell(j).toString().equals("CaseName")) {
									CaseList.add(sheet.getRow(i).getCell(1).toString());// 從指定待測試腳本的sheet中儲存測試案例的名稱
									break;
								} else {

									StepListData.add(sheet.getRow(i).getCell(j).getStringCellValue());// 從指定待測試腳本的sheet中儲存測試案例的步驟
																										// Excel數字要轉成字串型態
								}
							}
						}
						//判斷單一測試案例是否結束，若是，則StepListData加入StepList
						if (sheet.getRow(i).getCell(0).toString().equals("QuitAPP")
								|| sheet.getRow(i).getCell(0).toString().equals("Quit")) {
							StepList.add(StepListData);
							StepListData = new ArrayList<String>();
						}
						i++;
					} while (!sheet.getRow(i).getCell(0).toString().equals(""));
				} catch (Exception e) {
					;
				}
			}

		} catch (Exception e) {
			;
		}

		System.out.println("測試步驟：" + StepList);
		// 建立各裝置的Test Report
		for (int i = 0; i < DeviceInformation.deviceName.size(); i++) {

			if (DeviceInformation.deviceName.get(i).toString().length() > 20) {// Excel工作表名稱最常31字元因，故需判斷UDID長度是否大於31
				char[] NewUdid = new char[20];// 因需包含_TestReport字串(共11字元)，故設定20位字元陣列(31-11)
				DeviceInformation.deviceName.get(i).toString().getChars(0, 20, NewUdid, 0);// 取出UDID前20字元給NewUdid
				sheet = workbook.createSheet(String.valueOf(NewUdid) + "_TestReport");// 使用NewUdid命名新工作表
			} else {
				sheet = workbook.createSheet(DeviceInformation.deviceName.get(i).toString() + "_TestReport");
			}

			sheet.createRow(0).createCell(0).setCellValue("CaseName");
			sheet.getRow(0).createCell(1).setCellValue("Result");
			for (int k = 0; k < CaseList.size(); k++) {
				sheet.createRow(k + 1).createCell(0).setCellValue(CaseList.get(k).toString());//填入各案例名稱
				sheet.getRow(k + 1).createCell(1).setCellValue("Error");//預測各案列結果為Error
				
			}
		}
		// 執行寫入Excel後的存檔動作
		FileOutputStream out;
		try {
			out = new FileOutputStream(new File("C:\\TUTK_QA_TestTool\\TestReport\\TestReport.xlsm"));// 另存新檔
			workbook.write(out);
			out.close();
			workbook.close();
		} catch (Exception e) {
			;
		}

	}

}
