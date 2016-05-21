package com.cn.fit.model.healthdiary;

public class BeanAnswerOfEvaluation {
    /**
     * 问题的编号
     */
    private long questionID;
    /**
     * 问题的答案，若是填空题，则是用户填的内容，若是选择题，就是选中选项对应的内容
     */
    private String value;


    /**
     * 问题的编号
     */
    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    /**
     * 问题的答案，若是填空题，则是用户填的内容，若是选择题，就是选中选项对应的内容
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
