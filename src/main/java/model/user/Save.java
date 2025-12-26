package model.user;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import model.game.Map;

/**
 * save类型对象将包含关卡的完成情况与当前所在关卡的相关数据
 * 
 * @author 秦嘉曜 刘乙霏
 */

class Save implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int totStages = 0;

    private class StageInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Serial
        private final int stageID;
        @Serial
        private boolean stageCompleted;
        @Serial
        private String author;
        @Serial
        private int minSteps;
        @Serial
        private int minTime;

        protected StageInfo(int stageID, String author) {
            this.stageID = stageID;
            this.stageCompleted = false;
            this.stageCompleted = false;
            this.minSteps = Integer.MAX_VALUE;
            this.minTime = Integer.MAX_VALUE;
        }

        protected StageInfo(int stageID, boolean stageCompleted, String author, int minSteps, int minTime) {
            this.stageID = stageID;
            this.stageCompleted = stageCompleted;
            this.author = author;
            this.minSteps = minSteps;
            this.minTime = minTime;
        }

        protected void updateStageInfo(int minSteps, int minTime) {
            this.stageCompleted = true;
            this.minSteps = Math.min(this.minSteps, minSteps);
            this.minTime = Math.min(this.minTime, minTime);
        }

        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            sb.append(stageID).append("   ");
            sb.append(stageCompleted).append("   ");
            sb.append(minSteps).append("   ");
            sb.append(minTime).append("   \n");
            return sb.toString();
        }
    }

    @Serial
    private ArrayList<StageInfo> stageList = new ArrayList<>();

    @Serial
    private Map currMap;

    public Save() {
        this.totStages = 5;

        for (int i = 0; i < totStages; i++) {
            stageList.add(new StageInfo(i, false, "thereIsAStudio", Integer.MAX_VALUE, Integer.MAX_VALUE));
        }
    }

    public Map getCurrMap() {
        return this.currMap;
    }

    void setCurrMap(Map currMap) {
        this.currMap = currMap;
    }

    public String getStageInfo(){
        String str="";
        
        for(var info: stageList){
            str+=(info.toString());
        }

        return str;
    }

    void updateStageInfo(int stageID, int minSteps, int minTime) {
        stageList.get(stageID).updateStageInfo(minSteps, minTime);
    }
}
