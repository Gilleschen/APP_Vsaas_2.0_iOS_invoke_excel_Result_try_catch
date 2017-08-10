package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadExpectResult {

	public ArrayList<String> ResultList = new ArrayList<String>();// å­˜æ”¾??æ¸¬è©¦æ?ˆå?—ç?„æ?Ÿæ?›ç?æ??

	public void LoadExpectResult(String CaseName) {// ?‚³?…¥æ¸¬è©¦æ¡ˆå?—å?ç¨±
		XSSFWorkbook workbook;
		XSSFSheet sheet;

		try {
			workbook = new XSSFWorkbook(new FileInputStream("C:\\TUTK_QA_TestTool\\TestTool\\TestScript.xlsm"));
			sheet = workbook.getSheet("ExpectResult");// hard code
			ResultList = new ArrayList<String>();
			int i = 1;
			try {
				do {
					if (sheet.getRow(i).getCell(0).toString().equals(CaseName)) {// ??œå?‹å¦æ¸¬è©¦æ¡ˆå?—å?ç¨±
						for (int j = 1; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {

							ResultList.add(sheet.getRow(i).getCell(j).toString());// å­˜æ”¾??Ÿæ?›ç?æ?œè‡³æ¸…å–®
						}
						break;
					}

					i++;
				} while (!sheet.getRow(i).getCell(0).toString().equals(""));

			} catch (Exception e) {
				;
			}

			// System.out.println(ResultList);

			workbook.close();
		} catch (IOException e) {
			;
		}
	}

}
