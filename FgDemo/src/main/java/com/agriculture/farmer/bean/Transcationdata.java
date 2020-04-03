package com.agriculture.farmer.bean;

import com.google.gson.annotations.SerializedName;

public class Transcationdata {
    @SerializedName("交易日期")
    private String date;
    @SerializedName("作物代號")
    private String cropnum;
    @SerializedName("作物名稱")
    private String cropname;
    @SerializedName("市場代號")
    private String marketnum;
    @SerializedName("市場名稱")
    private String marketname;
    @SerializedName("上價")
    private String topprice;
    @SerializedName("中價")
    private String middleprice;
    @SerializedName("下價")
    private String downprice;
    @SerializedName("平均價")
    private String averageprice;
    @SerializedName("交易量")
    private String tradingvolume;

    public String getDate() {
        return date;
    }
    public String getCropnum() {
        return cropnum;
    }
    public String getCropname() {
        return cropname;
    }
    public String getMarketnum() {
        return marketnum;
    }
    public String getMarketname() {
        return marketname;
    }
    public String getTopprice() {
        return topprice;
    }
    public String getMiddleprice() {
        return middleprice;
    }
    public String getDownprice() {
        return downprice;
    }
    public String getAverageprice() {
        return averageprice;
    }
    public String getTradingvolume() {
        return tradingvolume;
    }

}
