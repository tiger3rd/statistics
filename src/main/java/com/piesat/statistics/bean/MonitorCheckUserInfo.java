package com.piesat.statistics.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.Generated;
import java.util.Calendar;
import java.util.Date;

@EntityScan
/**
 * 天擎用户核实信息表
 */
public class MonitorCheckUserInfo {
    private Integer id;
    //用户ID
    private String user_id;
    //核实状态；待核实、已核实
    private String checkStatus;

    //问题类型；
    private String type;
    //年份
    private String year;
    //每年中的第几周
    private String weekOfYear;
    //创建时间
    private Date createTime;
    //创建人
    private String createBy;
    //更新时间
    private Date updateTime;
    //更新人
    private String updateBy;

    //核实时间
    private Date checkTime;
    //限定多少天数内回复
    private String checkDayLimit;
    //核实说明
    private String checkdesc;

    //发送邮件标题
    private String emailTitle;
    //发送邮件地址
    private String targetEmail;
    //发送邮件内容
    private String emailContent;
    //发送邮件状态
    private String emailPushStatus;

    //反馈状态；正常、异常
    private String feedbackStatus;
    //异常处置措施：1：暂时保留-过了本周期后进行冻结；2、长期保留-不进行冻结：3、直接冻结
    private String measureStatus;
    //反馈描述
    private String feedbackdesc;
    //反馈附件文档
    private String feedbackfilepath;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(String weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckDayLimit() {
        return checkDayLimit;
    }

    public void setCheckDayLimit(String checkDayLimit) {
        this.checkDayLimit = checkDayLimit;
    }

    public String getCheckdesc() {
        return checkdesc;
    }

    public void setCheckdesc(String checkdesc) {
        this.checkdesc = checkdesc;
    }

    public String getTargetEmail() {
        return targetEmail;
    }

    public void setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getEmailPushStatus() {
        return emailPushStatus;
    }

    public void setEmailPushStatus(String emailPushStatus) {
        this.emailPushStatus = emailPushStatus;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getMeasureStatus() {
        return measureStatus;
    }

    public void setMeasureStatus(String measureStatus) {
        this.measureStatus = measureStatus;
    }

    public String getFeedbackfilepath() {
        return feedbackfilepath;
    }

    public void setFeedbackfilepath(String feedbackfilepath) {
        this.feedbackfilepath = feedbackfilepath;
    }

    public String getFeedbackdesc() {
        return feedbackdesc;
    }

    public void setFeedbackdesc(String feedbackdesc) {
        this.feedbackdesc = feedbackdesc;
    }
}
