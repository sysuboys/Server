package org.sysuboys.diaryu.business.model;

public class ExchangeModel {

	final String from, to;
	String title1, title2;
	boolean ready = false;
	int position1 = -1, position2 = -1;
	boolean matched = false;

	public ExchangeModel(String from, String to, String title1) {
		this.from = from;
		this.to = to;
		this.title1 = title1;
	}

	public String getInviter() {
		return from;
	}

	public String getInvitee() {
		return to;
	}

	public synchronized void ready(String title2) {
		this.title2 = title2;
		this.ready = true;
	}

	public synchronized boolean match(String username, int position) {
		if (!ready)
			return false;
		if (matched)
			return true;
		if (username.equals(from))
			position1 = position;
		else if (username.equals(to))
			position2 = position;
		else
			throw new RuntimeException("not matching inviter or invitee");
		if (position1 == position2)
			matched = true;
		return matched;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean isMatched() {
		return matched;
	}

	public String getAnother(String one) {
		if (one.equals(from))
			return to;
		if (one.equals(to))
			return from;
		throw new RuntimeException("ExchangeModel: can't get another of " + one);
	}

}
