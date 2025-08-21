# 🖼️ Mona Museum｜Java Swing 票務與訂單管理系統
一款用 Java Swing 打造的桌面應用程式，專為美術館票務與訂單管理設計，結合視覺化報表、會員系統、訂單流程與匯出功能。<br>
📄 [查看簡報 PDF（完整介紹）](./MONA MUSEUM｜Java Swing 票務與訂單管理系統.pdf)
待插圖

<hr>


### 🔍 專案簡介
• 提供會員註冊、登入與會員中心管理功能<br>
• 支援票券購買、訂單紀錄、訂單查詢與管理<br>
• 管理員功能包括訂單查詢、編輯、刪除與報表分析<br>
• 圖表報表整合（JFreeChart）＋ Excel 匯出（Apache POI）<br>
• 採用 Java Swing 製作完整 GUI 介面

<hr>

### 💡專案亮點： <br>
• MVC 架構＋DAO / Service 模組分層<br>
• 票券選擇與價格計算即時互動<br>
• 報表以圖表呈現，滑鼠懸停顯示詳細資訊<br>
• 即時更新資料表與 SQL 資料庫（CRUD）<br>
• 訂單細項合併呈現，同票種同票期合併統計<br>


<hr>

### 📸 使用畫面預覽(完整使用畫面請見 PDF)


#### • Login 畫面展示
<img src="./screenshots/Login.png" alt="Login Screenshot" width="300"/>

#### • AddMember 註冊會員
<img src="./screenshots/AddMember.png" alt="AddMember Screenshot" width="300"/>

#### • AddPorder 新增訂單
<img src="./screenshots/AddPorder.png" alt="AddPorder Screenshot" width="300"/>

#### • Finish 訂單完成
<img src="./screenshots/Finish.png" alt="Finish Screenshot" width="300"/>

#### • FindMyPorder 我的訂單
<img src="./screenshots/FindMyPorder.png" alt="FindMyPorder Screenshot" width="300"/>

#### • FindAllPorder 所有訂單
<img src="./screenshots/FindAllPorder.png" alt="FindAllPorder Screenshot" width="300"/>

#### • ReportCharts 銷售報表
<img src="./screenshots/ReportCharts.png" alt="ReportCharts Screenshot" width="300"/>


<hr>

#### 🧱 技術架構
• Java 17<br>
• Swing（桌面 GUI）<br>
• JDBC（MySQL 8）<br>
• Apache POI（.xlsx 匯出）<br>
• JFreeChart（報表視覺化）<br>
• JDatePicker（日期選擇器）<br>
• Maven 管理依賴

<hr>

#### 📦 專案結構（模組）
📁 controller         // 使用者介面邏輯（Swing）<br>
📁 service            // 業務邏輯定義<br>
📁 service.impl       // 業務邏輯實作<br>
📁 dao                // DAO 介面定義<br>
📁 dao.impl           // DAO JDBC 實作<br>
📁 model              // 資料模型（對應 MySQL）<br>
📁 util               // 共用工具模組（匯出、格式、連線、列印等）<br>

<hr>

#### 🚀 執行方式
1. 匯入專案至 IDE（建議使用 IntelliJ 或 Eclipse）<br>
2. 建立 MySQL 資料庫並匯入 schema.sql<br>
3. 調整 DbConnection.java 內連線資訊<br>
4. 執行 Login.java 或 AdminCenter.java 作為主程式啟動點<br>
5. 開始模擬會員與管理員操作流程！<br>

<hr>

#### 📘 使用教學
##### 使用者
• 註冊帳號 → 登入 → 選擇票種/票期 → 確認下單<br>
• 可查詢歷史訂單，匯出與列印明細<br>
##### 管理員
• 管理全部訂單（修改、刪除即時更新）<br>
• 查看圖表報表與下載 Excel<br>
• 權限控管與導引整合於 AdminCenter<br>

<hr>

#### 📊 系統模組關係圖
<img src="./screenshots/System Module Diagram.png" alt="System Module Diagram Screenshot" width="500"/>



<hr>
