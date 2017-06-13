package org.sysuboys.diaryu.business.model;

public class ExchangeModel {

	final String inviter, invitee;
	String title1, title2;
	boolean ready = false;
	int position1 = -1, position2 = -1;
	boolean matched = false;

	public ExchangeModel(String inviter, String invitee, String title1) {
		this.inviter = inviter;
		this.invitee = invitee;
		this.title1 = title1;
	}

	public String getInviter() {
		return inviter;
	}

	public String getInvitee() {
		return invitee;
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
		if (username.equals(inviter))
			position1 = position;
		else if (username.equals(invitee))
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
		if (one.equals(inviter))
			return invitee;
		if (one.equals(invitee))
			return inviter;
		throw new RuntimeException("ExchangeModel: neither inviter nor invitee is " + one);
	}

	public String getFriendTitle(String me) {
		if (me.equals(inviter))
			return title2;
		if (me.equals(invitee))
			return title1;
		throw new RuntimeException("ExchangeModel: neither inviter nor invitee is " + me);
	}

}
