package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderDTO implements Serializable {
	private static final long serialVersionUID = 789453137647495579L;

	private Long id;
	private int totalPoints;
	private Date placedTime;
	private Date sentTime;
	private Date receivedTime;
	
	public OrderDTO() { }

	public OrderDTO(Long id, Date placedTime, Date sentTime, Date receivedTime) {
		super();
		this.id = id;
		this.placedTime = placedTime;
		this.sentTime = sentTime;
		this.receivedTime = receivedTime;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Date getPlacedTime() {
		return placedTime;
	}

	public void setPlacedTime(Date placedTime) {
		this.placedTime = placedTime;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	@Override
	public String toString() {
		return "OrderDTO [totalPoints=" + totalPoints + ", placedTime="
				+ placedTime + ", sentTime=" + sentTime + ", receivedTime="
				+ receivedTime + "]";
	}
		
}
