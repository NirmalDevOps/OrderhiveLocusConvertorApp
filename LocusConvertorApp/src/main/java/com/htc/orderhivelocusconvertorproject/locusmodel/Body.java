/**
 * 
 */
package com.htc.orderhivelocusconvertorproject.locusmodel;

/**
 * Represents a Body model class
 * 
 * @author HTC Global Service
 * @version 1.0
 * @since 30-03-2021
 * 
 */
public class Body {

	private Long id;
	private String clientId;

	PatchBody patchBody;

	public Body() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the patchBody
	 */
	public PatchBody getPatchBody() {
		return patchBody;
	}

	/**
	 * @param patchBody the patchBody to set
	 */
	public void setPatchBody(PatchBody patchBody) {
		this.patchBody = patchBody;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	

}
