package com.asiainfo.draw.domain;

public class DrawScene {
    private Integer sceneId;

    private String sceneName;

    private Integer sceneOrder;

    private Integer state;

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName == null ? null : sceneName.trim();
    }

    public Integer getSceneOrder() {
        return sceneOrder;
    }

    public void setSceneOrder(Integer sceneOrder) {
        this.sceneOrder = sceneOrder;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}