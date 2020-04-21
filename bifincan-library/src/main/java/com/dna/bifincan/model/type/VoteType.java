package com.dna.bifincan.model.type;

public enum VoteType {
	LIKE("voteType.like"), 
	DISLIKE("voteType.dislike");
	
	String label;
	
	VoteType() { }
	
	VoteType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}	
}
