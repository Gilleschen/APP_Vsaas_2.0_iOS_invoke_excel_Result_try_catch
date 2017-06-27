# 使用說明

#### 測試前設定：

1. 安裝Appium studio (https://experitest.com/appium-studio/)

2. 新增測試手機至Appium studio並啟動手機 (請參考<a href="https://docs.experitest.com/display/public/AS/iOS+-+Build+your+first+test">Getting started</a>)


#### 測試腳本建立說明：

1. 開啟TestScript.xlsm

2. APP&Device工作表輸入APP Bundle ID、測試手機UDID、測試手機OS版本與待測試腳本 (以_TestCase結尾的工作表)

範例如下圖：

![image](https://github.com/Gilleschen/APP_Vsaas_2.0_iOS_invoke_excel_Result_try_catch/blob/master/picture/app_device_example.PNG)

3. 建立腳本(建立案列Case)：新增一工作表，工作表名稱須以_TestCase為結尾 (e.g. Login_TestCase)，目前支援指令如下: (大小寫有分，使用方式請參考TestScript.xlsm內說明工作表)

        CaseName=>測試案列名稱(各案列開始時第一個填寫項目，必填!!!)
        
        Back=>點擊手機返回鍵

        Byid_Click=>搜尋元件id並點擊元件

        Byid_Result=>搜尋元件id並比對ExpectResult內容

        Byid_SendKey=>搜尋元件id並輸入數值或字串

        Byid_Wait=>等待並搜尋元件id

        ByXpath_Click=>搜尋元件xpath並點擊元件

        ByXpath_Result=>搜尋元件xpath並比對ExpectResult內容

        ByXpath_SendKey=>搜尋元件xpath並輸入數值或字串

        ByXpath_Wait=>等待並搜尋元件xpath

        HideKeyboard=>關閉鍵盤

        Home=>點擊手機Home鍵

        LaunchAPP=>啟動APP&Device工作表指定的Packageanme之Avtivity

        Orientation=>切換手機Landscape及Portrait模式

        Power=>點擊手機電源鍵

        QuitAPP=>關閉APP&Device工作表指定的Packageanme之Avtivity

        ResetAPP=>重置APP(清除APP暫存紀錄)並重新啟動APP

        ScreenShot=>螢幕截圖

        Sleep=>閒置APP n秒鐘
  
範例腳本如下圖：

![image](https://github.com/Gilleschen/APP_Vsaas_2.0_Android_invoke_excel_Result_try_catch/blob/master/picture/Testcase_example.PNG)
  
4. 建立以ExpectResult為名稱工作表(大小寫有分)，輸入各測試案例的期望結果

        3.1 A欄第二列處往下填入案列名稱 (CaseName)
        
        3.2 與案列名稱同列處輸入期望結果
        
 ExpectResult範例如下圖：
 
 ![image](https://github.com/Gilleschen/APP_Vsaas_2.0_Android_invoke_excel_Result_try_catch/blob/master/picture/Result_example.PNG)

5. 最後將TestScript.xlsm複製至C:\ (C:\TestScript.xlsm)(檔名及副檔名請勿更改)

6. 於C:\建立TestReport資料夾 (C:\TestReport)

#### 開始測試

方法一：執行method Class中main方法

方法二：$ java -jar APP_iOS.jar

#### Excel測試報告

1. 開啟C:\TestReport\TestReport.xlsm

2. 根據手機UDID自動建立TestReport工作表，如下圖： (e.g. abc123ABC123_TestReport)

![image](https://github.com/Gilleschen/APP_Vsaas_2.0_Android_invoke_excel_Result_try_catch/blob/master/picture/Testreport_sheet_example.PNG)

範例測試結果如下圖：

![image](https://github.com/Gilleschen/APP_Vsaas_2.0_Android_invoke_excel_Result_try_catch/blob/master/picture/Testreport_example.PNG)

#### experitest測試報告

開啟C:\TestReport\index.html，範例如下圖：

![image](https://github.com/Gilleschen/APP_Vsaas_2.0_Android_invoke_excel_Result_try_catch/blob/master/picture/experitest_report.png)
