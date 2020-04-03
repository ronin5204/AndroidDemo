package com.agriculture.farmer.bean;

import com.google.gson.annotations.SerializedName;

public class Collegedata {
    @SerializedName("課程名稱")
    private String name;
    @SerializedName("訓練別")
    private String categary;
    @SerializedName("產業別")
    private String categary2;
    @SerializedName("課程型態")
    private String categary3;
    @SerializedName("課程日期")
    private String date;
    @SerializedName("課程費用")
    private String price;
    @SerializedName("報名時間")
    private String signdate;
    @SerializedName("開課狀態")
    private String status;
    @SerializedName("名額")
    private String quota;
    @SerializedName("報名人數")
    private String signnum;
    @SerializedName("課程內容")
    private String content;
    @SerializedName("參加對象")
    private String participant;
    @SerializedName("資格條件")
    private String condition;
    @SerializedName("聯絡資訊")
    private String connection;

    public String getName() {
        return name;
    }
    public String getCategary() {
        return categary;
    }
    public String getCategary2() {
        return categary2;
    }
    public String getCategary3() {
        return categary3;
    }
    public String getDate() {
        return date;
    }
    public String getPrice() {
        return price;
    }
    public String getSigndate() {
        return signdate;
    }
    public String getStatus() {
        return status;
    }
    public String getQuota() {
        return quota;
    }
    public String getSignnum() {
        return signnum;
    }
    public String getContent() {
        return content;
    }
    public String getParticipant() {
        return participant;
    }
    public String getCondition() {
        return condition;
    }
    public String getConnection() {
        return connection;
    }

}
