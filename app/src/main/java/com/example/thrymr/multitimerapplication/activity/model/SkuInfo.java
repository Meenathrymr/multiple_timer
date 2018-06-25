package com.example.thrymr.multitimerapplication.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class SkuInfo extends SugarRecord implements Parcelable {

    private String shipmentId;


    private String userId;


    private String skuId;

    private String timeSpent;

    private String totalTime = "00:00:00";

    private String pausedTime = "00:00:00";

    private Long startTimeInMillis = 0L;

    public Boolean getStartedRunning() {
        return isStartedRunning;
    }

    public void setStartedRunning(Boolean startedRunning) {
        isStartedRunning = startedRunning;
    }

    private Boolean isStartedRunning = Boolean.FALSE;

    public SkuInfo() {

    }

    protected SkuInfo(Parcel in) {
        shipmentId = in.readString();
        userId = in.readString();
        skuId = in.readString();
        totalTime = in.readString();
        if (in.readByte() == 0) {
            startTimeInMillis = null;
        } else {
            startTimeInMillis = in.readLong();
        }
        if (in.readByte() == 0) {
            endTimeInMillis = null;
        } else {
            endTimeInMillis = in.readLong();
        }
        if (in.readByte() == 0) {
            completedTaskCount = null;
        } else {
            completedTaskCount = in.readInt();
        }
    }


    public static final Creator<SkuInfo> CREATOR = new Creator<SkuInfo>() {
        @Override
        public SkuInfo createFromParcel(Parcel in) {
            return new SkuInfo(in);
        }

        @Override
        public SkuInfo[] newArray(int size) {
            return new SkuInfo[size];
        }
    };

    public Long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public void setStartTimeInMillis(Long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
    }

    public Long getEndTimeInMillis() {
        return endTimeInMillis;
    }

    public void setEndTimeInMillis(Long endTimeInMillis) {
        this.endTimeInMillis = endTimeInMillis;
    }

    private Long endTimeInMillis = 0l;


    public Integer getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(Integer completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    private Integer completedTaskCount = 0;


    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }


    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentId);
        dest.writeString(userId);
        dest.writeString(skuId);
        dest.writeString(totalTime);
        if (startTimeInMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(startTimeInMillis);
        }
        if (endTimeInMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(endTimeInMillis);
        }
        if (completedTaskCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(completedTaskCount);
        }
    }

    public String getPausedTime() {
        return pausedTime;
    }

    public void setPausedTime(String pausedTime) {
        this.pausedTime = pausedTime;
    }


    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    @Override
    public String toString() {
        return "SkuInfo{" +
                "shipmentId='" + shipmentId + '\'' +
                ", userId='" + userId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", timeSpent='" + timeSpent + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", pausedTime='" + pausedTime + '\'' +
                ", startTimeInMillis=" + startTimeInMillis +
                ", isStartedRunning=" + isStartedRunning +
                ", endTimeInMillis=" + endTimeInMillis +
                ", completedTaskCount=" + completedTaskCount +
                '}';
    }
}