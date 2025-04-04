package com.example.blogpost.common.model;

import com.example.blogpost.user.dto.UserSessionInfo;
import com.example.blogpost.user.model.User;
import com.fasterxml.jackson.databind.ser.Serializers;

import jakarta.persistence.*;

import java.util.Date;

import static com.example.blogpost.common.constant.CmnConstants.getLoggedInUser;
import static com.example.blogpost.common.constant.CmnConstants.getUserSessionInfo;

@MappedSuperclass
public abstract class AdvancedBaseEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    protected User updatedByUser;

    @Column(name = "updated_date")
    protected Date updatedDate;

    @Column(name = "updated_by_ip")
    protected String updatedByIp;

    @PreUpdate
    private void handleBeforeUpdate() {
        UserSessionInfo userSessionInfo = getUserSessionInfo();

        updatedByUser = getLoggedInUser();
        updatedDate = new Date();
        updatedByIp = userSessionInfo.getRemoteIpAddr();
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedByIp() {
        return updatedByIp;
    }

    public void setUpdatedByIp(String updatedByIp) {
        this.updatedByIp = updatedByIp;
    }
}
