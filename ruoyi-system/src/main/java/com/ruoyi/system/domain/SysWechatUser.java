package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 微信用户关联对象 sys_wechat_user
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public class SysWechatUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 微信openid */
    private String openid;

    /** 微信unionid */
    private String unionid;

    /** 微信昵称 */
    private String nickname;

    /** 微信头像 */
    private String avatar;

    /** 性别 */
    private String gender;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 国家 */
    private String country;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("openid", getOpenid())
            .append("unionid", getUnionid())
            .append("nickname", getNickname())
            .append("avatar", getAvatar())
            .append("gender", getGender())
            .append("province", getProvince())
            .append("city", getCity())
            .append("country", getCountry())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
