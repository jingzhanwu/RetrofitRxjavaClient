package com.jzw.retrofit.base;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/9/20 0020
 * @change
 * @describe 微群实体
 **/
public class MicroGroup{
    //id
    private String id;
    private String createTime;
    private String creatorId;
    private String title;
    private String description;
    private int delete;
    //会议室类型
    private int conferenceType;
    //会议室id
    private String conferenceId;
    private String groupType = "0";
    private String creatorName;
    private String creatorOrgId;
    private String creatorOrgName;
    //群成员id
    private List<String> members;
    private int unReadCount;
    private ChatMessageBody latestMessage;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //比较，按照时间排序
    public int compareTo(MicroGroup paramGroup) {
        try {
            if (paramGroup.getLatestMessage() == null) {
                return -1;
            }
            if (getLatestMessage() == null) {
                return -1;
            }
            long time1 = sdf.parse(paramGroup.getLatestMessage().getCreateTime()).getTime();
            long time = sdf.parse(getLatestMessage().getCreateTime()).getTime();

            if (time1 > time) {
                return 1;
            } else if (time1 < time) {
                return -1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean equals(Object paramObject) {
        if (paramObject == null) {
            return false;
        }
        if (paramObject == this) {
            return true;
        }
        if ((!(paramObject instanceof MicroGroup)) || (this.id != ((MicroGroup) paramObject).getId())) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public int getConferenceType() {
        return conferenceType;
    }

    public void setConferenceType(int conferenceType) {
        this.conferenceType = conferenceType;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(String creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }

    public String getCreatorOrgName() {
        return creatorOrgName;
    }

    public void setCreatorOrgName(String creatorOrgName) {
        this.creatorOrgName = creatorOrgName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ChatMessageBody getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(ChatMessageBody latestMessage) {
        this.latestMessage = latestMessage;
    }
}
