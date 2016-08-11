package com.hqdna.common.page;

import java.io.Serializable;

/**
 * 
 * @author Joey
 *
 * 2014-8-20上午11:29:50
 */
public class Combobox implements Serializable{
	private static final long serialVersionUID = 635873657609066762L;
	private String id;
	private String text;
	private boolean selected;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
