package com.jzw.retrofit.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/1/4 0004
 * @change
 * @describe 警情信息
 **/
public class PoliceInfo implements Parcelable {
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Police> getList() {
        return list;
    }

    public void setList(List<Police> list) {
        this.list = list;
    }

    private List<Police> list;

    protected PoliceInfo(Parcel in) {
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
    }

    public static final Creator<PoliceInfo> CREATOR = new Creator<PoliceInfo>() {
        @Override
        public PoliceInfo createFromParcel(Parcel in) {
            return new PoliceInfo(in);
        }

        @Override
        public PoliceInfo[] newArray(int size) {
            return new PoliceInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(total);
        }
    }

    public class Police implements Parcelable {
        /**
         * 报警方式
         */
        private String reportAlarmMode;
        /** 处警单编号 */
        private String id;
        /** 事件单编号 */
        private String incidentId;
        /** 接警单编号 */
        private String receiveAlarmId;
        /** 行政区划代码 */
        private String adminRegionId;
        /** 处警单位代码 */
        private String dispatchUnitCode;
        /** 处警员编号 */
        private String dispatchOfficerId;
        /** 处警员姓名 */
        private String dispatchOfficerName;
        /** 处警台编号 */
        private String dispatchAlarmDeskId;
        /** 处警台IP地址 */
        private String dispatchAlarmDeskIP;
        /** 处警时间 */
        private String dispatchTime;
        /** 处警录音号 */
        private String dispatchRecordId;
        /** 处警意见 */
        private String dispatchOpinion;
        /** 出警单位类别代码 */
        private String outUnitType;
        /** 出动（出警）单位代码 */
        private String outUnitCode;
        /** 出动警员编号 */
        private String outOfficerId;
        /** 出动警员姓名 */
        private String outOfficerName;
        /** 处警结束时间 */
        private String dispatchEndTime;
        /** 派单到达时间 */
        private String sendArriveTime;
        /** 派单接收时间 */
        private String sendReceiveTime;
        /** 警情状态代码 */
        private Integer alarmStatus;
        /** 出动人员 */
        private String outPerson;
        /** 出动车辆 */
        private String outCar;
        /** 入库时间戳 */
        public String createTime;
        /** 更新时间戳 */
        public String updateTime;

        public String getReportAlarmMode() {
            return reportAlarmMode;
        }

        public void setReportAlarmMode(String reportAlarmMode) {
            this.reportAlarmMode = reportAlarmMode;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIncidentId() {
            return incidentId;
        }

        public void setIncidentId(String incidentId) {
            this.incidentId = incidentId;
        }

        public String getReceiveAlarmId() {
            return receiveAlarmId;
        }

        public void setReceiveAlarmId(String receiveAlarmId) {
            this.receiveAlarmId = receiveAlarmId;
        }

        public String getAdminRegionId() {
            return adminRegionId;
        }

        public void setAdminRegionId(String adminRegionId) {
            this.adminRegionId = adminRegionId;
        }

        public String getDispatchUnitCode() {
            return dispatchUnitCode;
        }

        public void setDispatchUnitCode(String dispatchUnitCode) {
            this.dispatchUnitCode = dispatchUnitCode;
        }

        public String getDispatchOfficerId() {
            return dispatchOfficerId;
        }

        public void setDispatchOfficerId(String dispatchOfficerId) {
            this.dispatchOfficerId = dispatchOfficerId;
        }

        public String getDispatchOfficerName() {
            return dispatchOfficerName;
        }

        public void setDispatchOfficerName(String dispatchOfficerName) {
            this.dispatchOfficerName = dispatchOfficerName;
        }

        public String getDispatchAlarmDeskId() {
            return dispatchAlarmDeskId;
        }

        public void setDispatchAlarmDeskId(String dispatchAlarmDeskId) {
            this.dispatchAlarmDeskId = dispatchAlarmDeskId;
        }

        public String getDispatchAlarmDeskIP() {
            return dispatchAlarmDeskIP;
        }

        public void setDispatchAlarmDeskIP(String dispatchAlarmDeskIP) {
            this.dispatchAlarmDeskIP = dispatchAlarmDeskIP;
        }

        public String getDispatchTime() {
            return dispatchTime;
        }

        public void setDispatchTime(String dispatchTime) {
            this.dispatchTime = dispatchTime;
        }

        public String getDispatchRecordId() {
            return dispatchRecordId;
        }

        public void setDispatchRecordId(String dispatchRecordId) {
            this.dispatchRecordId = dispatchRecordId;
        }

        public String getDispatchOpinion() {
            return dispatchOpinion;
        }

        public void setDispatchOpinion(String dispatchOpinion) {
            this.dispatchOpinion = dispatchOpinion;
        }

        public String getOutUnitType() {
            return outUnitType;
        }

        public void setOutUnitType(String outUnitType) {
            this.outUnitType = outUnitType;
        }

        public String getOutUnitCode() {
            return outUnitCode;
        }

        public void setOutUnitCode(String outUnitCode) {
            this.outUnitCode = outUnitCode;
        }

        public String getOutOfficerId() {
            return outOfficerId;
        }

        public void setOutOfficerId(String outOfficerId) {
            this.outOfficerId = outOfficerId;
        }

        public String getOutOfficerName() {
            return outOfficerName;
        }

        public void setOutOfficerName(String outOfficerName) {
            this.outOfficerName = outOfficerName;
        }

        public String getDispatchEndTime() {
            return dispatchEndTime;
        }

        public void setDispatchEndTime(String dispatchEndTime) {
            this.dispatchEndTime = dispatchEndTime;
        }

        public String getSendArriveTime() {
            return sendArriveTime;
        }

        public void setSendArriveTime(String sendArriveTime) {
            this.sendArriveTime = sendArriveTime;
        }

        public String getSendReceiveTime() {
            return sendReceiveTime;
        }

        public void setSendReceiveTime(String sendReceiveTime) {
            this.sendReceiveTime = sendReceiveTime;
        }

        public Integer getAlarmStatus() {
            return alarmStatus;
        }

        public void setAlarmStatus(Integer alarmStatus) {
            this.alarmStatus = alarmStatus;
        }

        public String getOutPerson() {
            return outPerson;
        }

        public void setOutPerson(String outPerson) {
            this.outPerson = outPerson;
        }

        public String getOutCar() {
            return outCar;
        }

        public void setOutCar(String outCar) {
            this.outCar = outCar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }



        protected Police(Parcel in) {
            id = in.readString();
            incidentId = in.readString();
            receiveAlarmId = in.readString();
            adminRegionId = in.readString();
            dispatchUnitCode = in.readString();
            dispatchOfficerId = in.readString();
            dispatchOfficerName = in.readString();
            dispatchAlarmDeskId = in.readString();
            dispatchAlarmDeskIP = in.readString();
            dispatchTime = in.readString();
            dispatchRecordId = in.readString();
            dispatchOpinion = in.readString();
            outUnitType = in.readString();
            outUnitCode = in.readString();
            outOfficerId = in.readString();
            outOfficerName = in.readString();
            dispatchEndTime = in.readString();
            sendArriveTime = in.readString();
            sendReceiveTime = in.readString();
            alarmStatus = in.readInt();
            outPerson = in.readString();
            outCar = in.readString();
            createTime = in.readString();
            updateTime = in.readString();
        }

        public final Creator<Police> CREATOR = new Creator<Police>() {
            @Override
            public Police createFromParcel(Parcel in) {
                return new Police(in);
            }

            @Override
            public Police[] newArray(int size) {
                return new Police[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(incidentId);
            dest.writeString(receiveAlarmId);
            dest.writeString(adminRegionId);
            dest.writeString(dispatchUnitCode);
            dest.writeString(dispatchOfficerId);
            dest.writeString(dispatchOfficerName);
            dest.writeString(dispatchAlarmDeskId);
            dest.writeString(dispatchAlarmDeskIP);
            dest.writeString(dispatchTime);
            dest.writeString(dispatchRecordId);
            dest.writeString(dispatchOpinion);
            dest.writeString(outUnitType);
            dest.writeString(outUnitCode);
            dest.writeString(outOfficerId);
            dest.writeString(outOfficerName);
            dest.writeString(dispatchEndTime);
            dest.writeString(sendArriveTime);
            dest.writeString(sendReceiveTime);
            dest.writeInt(alarmStatus);
            dest.writeString(outPerson);
            dest.writeString(outCar);
            dest.writeString(createTime);
            dest.writeString(updateTime);
        }
    }
}
