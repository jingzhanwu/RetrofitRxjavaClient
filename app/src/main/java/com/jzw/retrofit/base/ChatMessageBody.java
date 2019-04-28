package com.jzw.retrofit.base;


/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/9/28 0028
 * @change
 * @describe 消息实体
 **/
public class ChatMessageBody {
    private String id;
    /*内容*/
    private String content;
    /*创建者id*/
    private String createId;
    /*创建者名称*/
    private String createName;
    /*创建时间*/
    private String createTime;
    /**/
    private String headUrl;
    /*接受消息的微群id*/
    private String microGroupId;
    /*图片、视频、音频文件地址*/
    private String path;
    /*自定义字段 文件本地路劲*/
    private String localPath;
    /*音视频，文件的附加数据*/
    private String obj;
    /**
     * 消息类型
     * -1 系统消息
     * 0 文本消息
     * 1 图片
     * 2 音频
     * 3 视频
     * 4 附加文件
     */
    private int type;

    //下面是自定义自定，本地有效
    private int hasPlayed;
    private int isCrowd;
    /**
     * 是否已经下载到本地 0=未下载 1=已经下载
     */
    private int isDownload;
    /**
     * 是否正在播放 0=没播放 1=播放中
     */
    private int isPlaying = 0;
    //发送 接受状态 1=创建，2=发送成功 3=发送失败 4=发送中 5=接受未读 6=接受已读
    private int isSend = 0;
    //方向 0==发送 1==接受
    private int direct = 0;

    public ChatMessageBody() {

    }

    public ChatMessageBody(String msgId, String content, String createId, String createName,
                           String createTime, String groupId,
                           int direct,
                           String path, int type,
                           int isSend, int isCrowd) {
        setId(msgId);
        this.content = content;
        this.createId = createId;
        this.createName = createName;
        this.createTime = createTime;
        this.microGroupId = groupId;
        this.direct = direct;
        this.path = path;
        this.type = type;

        this.isSend = isSend;
        this.isCrowd = isCrowd;
    }

    public int getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(int hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public int getIsCrowd() {
        return isCrowd;
    }

    public void setIsCrowd(int isCrowd) {
        this.isCrowd = isCrowd;
    }


    public int getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(int isPlaying) {
        this.isPlaying = isPlaying;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }


    public String getMicroGroupId() {
        return microGroupId;
    }

    public void setMicroGroupId(String microGroupId) {
        this.microGroupId = microGroupId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }


    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "ChatMessageBody{" +
                "content='" + content + '\'' +
                ", createId='" + createId + '\'' +
                ", createName='" + createName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", microGroupId='" + microGroupId + '\'' +
                ", path='" + path + '\'' +
                ", localPath='" + localPath + '\'' +
                ", obj=" + obj +
                ", type=" + type +
                ", hasPlayed=" + hasPlayed +
                ", isCrowd=" + isCrowd +
                ", isDownload=" + isDownload +
                ", isPlaying=" + isPlaying +
                ", isSend=" + isSend +
                ", direct=" + direct +
                '}';
    }

    public int getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(int isDownload) {
        this.isDownload = isDownload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
