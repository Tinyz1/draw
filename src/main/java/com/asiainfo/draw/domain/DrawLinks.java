package com.asiainfo.draw.domain;

public class DrawLinks {
    private Integer linkId;

    private String linkName;

    private Integer openState;

    private Integer state;

    private Integer linkOrder;

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public Integer getOpenState() {
        return openState;
    }

    public void setOpenState(Integer openState) {
        this.openState = openState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getLinkOrder() {
        return linkOrder;
    }

    public void setLinkOrder(Integer linkOrder) {
        this.linkOrder = linkOrder;
    }
}