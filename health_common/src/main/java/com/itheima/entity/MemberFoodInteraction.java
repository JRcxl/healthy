package com.itheima.entity;

import java.io.Serializable;
import java.util.Date;

public class MemberFoodInteraction implements Serializable {
    private Long interactionId;
    private Integer memberId;
    private Integer foodId;
    private Integer rating;
    private Integer clickCount;
    private Boolean favorite;
    private Date timestamp;

    public Long getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(Long interactionId) {
        this.interactionId = interactionId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer userId) {
        this.memberId = userId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MemberFoodInteraction{" +
                "interactionId=" + interactionId +
                ", memberId=" + memberId +
                ", foodId=" + foodId +
                ", rating=" + rating +
                ", clickCount=" + clickCount +
                ", favorite=" + favorite +
                ", timestamp=" + timestamp +
                '}';
    }
}
