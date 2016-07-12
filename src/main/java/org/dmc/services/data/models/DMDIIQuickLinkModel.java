package org.dmc.services.data.models;

public class DMDIIQuickLinkModel extends BaseModel {

	private String text;
	
	private String link;
	
	private DMDIIDocumentModel doc;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public DMDIIDocumentModel getDoc() {
		return doc;
	}

	public void setDoc(DMDIIDocumentModel doc) {
		this.doc = doc;
	}
}
