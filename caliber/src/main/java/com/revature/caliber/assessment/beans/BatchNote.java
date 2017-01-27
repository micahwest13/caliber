package com.revature.caliber.assessment.beans;

public class BatchNote extends Note {

    @Override
	public String toString() {
		return "BatchNote [week=" + week + ", batch=" + batch + ", getWeek()=" + getWeek() + ", getBatch()="
				+ getBatch() + ", getNoteId()=" + getNoteId() + ", getContent()=" + getContent()
				+ ", getSugarCoatedContent()=" + getSugarCoatedContent() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	/**
     * The week of the batch's evaluation
     */
    private int week;

    /**
     * The batch identifier
     */
    private int batch;

    public BatchNote(int week, int batch) {
        super();
        this.week = week;
        this.batch = batch;
    }

    public BatchNote() {
        super();
    }

    public BatchNote(String content, boolean sugarCoated) {
        super(content, sugarCoated);
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public long getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

}
