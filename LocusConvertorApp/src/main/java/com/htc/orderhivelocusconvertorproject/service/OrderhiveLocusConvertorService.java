package com.htc.orderhivelocusconvertorproject.service;

import com.htc.orderhivelocusconvertorproject.orderhivemodel.OrderHive;

/**
 * Represents a Converter Interface for Locus Create Order / Update Order /
 * Cancel Order / TrackIQ
 * 
 * @author HTC Global Services
 * @version 1.0
 * @since 30-03-2021
 * 
 */

public interface OrderhiveLocusConvertorService {

	/**
	 * This method is used to get the Locus response for Order Create
	 * 
	 * @param cancelId
	 * @return
	 */
	
	public String buildLocusCreateOrderRequest(OrderHive orderhive);

	
	/**
	 * This method is used to get the Locus response for Order Edit
	 * 
	 * @param cancelId
	 * @return
	 */

	public String buildLocusUpdateOrderRequest(OrderHive orderhive);

	/**
	 * This method is used to get the Locus response for Order Cancel
	 * 
	 * @param cancelId
	 * @return
	 */
	String buildLocusCancelOrderRequest(OrderHive orderhive);
}