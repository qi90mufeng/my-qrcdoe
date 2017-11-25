package com.snfq.module.flow.model;

import java.util.Date;

//import javax.persistence.Table;

//import lombok.Data;

//@Table(name = "a_business_log")
//@Data
public class BusinessLog {
    // 主键ID，自增序列
    private Integer id;

    // 用户ID
    private Long userId;

    // 接口代码:6位
    private String methodCode;

    // 接口名称
    private String methodName;

    // 入参
    private String inParam;

    // 出参
    private String outParam;

    // 状态
    private Integer status;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;

    // 创建日期
    private String createYmd;

    // 备注
    private String remark;

    /**
     * 返回主键ID，自增序列
     * @return 主键ID，自增序列
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID，自增序列
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 返回用户ID
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 返回接口代码:6位
     * @return 接口代码:6位
     */
    public String getMethodCode() {
        return methodCode;
    }

    /**
     * 设置接口代码:6位
     */
    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode == null ? null : methodCode.trim();
    }

    /**
     * 返回接口名称
     * @return 接口名称
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置接口名称
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    /**
     * 返回入参
     * @return 入参
     */
    public String getInParam() {
        return inParam;
    }

    /**
     * 设置入参
     */
    public void setInParam(String inParam) {
        this.inParam = inParam == null ? null : inParam.trim();
    }

    /**
     * 返回出参
     * @return 出参
     */
    public String getOutParam() {
        return outParam;
    }

    /**
     * 设置出参
     */
    public void setOutParam(String outParam) {
        this.outParam = outParam == null ? null : outParam.trim();
    }

    /**
     * 返回状态
     * @return 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 返回创建时间
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 返回更新时间
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 返回创建日期
     * @return 创建日期
     */
    public String getCreateYmd() {
        return createYmd;
    }

    /**
     * 设置创建日期
     */
    public void setCreateYmd(String createYmd) {
        this.createYmd = createYmd == null ? null : createYmd.trim();
    }

    /**
     * 返回备注
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}