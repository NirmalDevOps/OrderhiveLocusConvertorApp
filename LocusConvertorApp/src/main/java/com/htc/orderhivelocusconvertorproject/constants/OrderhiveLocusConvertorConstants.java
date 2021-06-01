package com.htc.orderhivelocusconvertorproject.constants;

/**
 * Represents a Constant class for Order Create / Order Update / Order Cancel /
 * Track IQ
 * 
 * @author HTC Global Services
 * @version 1.0
 * @since 30-03-2021
 * 
 */

public class OrderhiveLocusConvertorConstants {

	// Common for all
	public static final String CLIENT_ID = "arki-devo";
	public static final String CLIENT_AUTHENTICATION = "167eb8d0-aab4-44e0-99c4-0469945d2bae";
	public static final String STATUS = "ACTIVE";

	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final int CONNECTION_TIME_OUT = 20_000;
	public static final int READING_TIME_OUT = 20_000;
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String SUCCESS_STATUS_CODE = "Success Status Code : ";
	public static final String ERROR_STATUS_CODE = "Error Status Code : ";
	public static final String ERROR_MESSAGE = "Error : ";
	
	

	// ORDER CREATE

	public static final String CONFIRMED_ORDER_STATUS = "CONFIRMED";
	public static final String NOTCONFIRMED_ORDER_STATUS = "NOT CONFIRMED";
	

	public static final String FILE_NAME = "application.properties";

	public static final String ORDER_STATUS = "QUOTE_REQUESTED";
	public static final String EFFECTIVE_STATUS = "PLANNING";
	public static final int VERSION = 1;

	public static final int ORDER_CANCELCREATE_COUNT = 4;
	public static final int HOURS_TO_SUBTRACT = 10;
	public static final int MINUTES_TO_SUBTRACT = 0;
	
	public static final String QUANTITY_UNIT = "PC";
	public static final String UNIT = "CM";
	public static final String VOLUME_UNIT = "CM";
	public static final String WEIGHT_UNIT = "KG";
	public static final String CURRENCY_SYMBOL = "AUD";

	public static final String ORDER_CREATE_SUCCESS_MESSAGE = "Order created successfully!!!";
	public static final String ORDER_CREATE_FAILED_MESSAGE = "Order creation failed!!!";
	// Select any one for exchange type [NONE/COLLECT/GIVE]
	public static final String EXCHANGE_TYPE = "NONE";

	public static final String CONVERSION_FAILED = "OrderHive to Locus conversion failed";


	// EDIT ORDER
	public static final int ORDER_CANCEL_COUNT = 4;
	public static final String ORDER_EDIT_SUCCESS_MESSAGE = "Order edited successfully";
	public static final String ORDER_EDIT_FAILED_MESSAGE = "Order edition failed!!!";
	// Select any one for exchange type [NONE/COLLECT/GIVE]
	public static final String EXCHANGE_TYPE_NONE = "NONE";
	public static final String EXCHANGE_TYPE_COLLECT = "COLLECT";
	public static final String EXCHANGE_TYPE_GIVE = "GIVE";
	
	// DANGER : Don't change it. Take approval to change or think thrice
	//public static final String TEAM_NAME = "Locus Team";
	public static final String TEAM_ID = "Team ID";
//	public static final String DROP_TRANSACTION_DURATION= "Drop Transaction Duration";
	public static final String DROP_TRANSACTION_DURATION= "Drop Transaction Duration (mins)";
	public static final int DROP_TRANSACTION_DURATION_DEFAULT_VALUE= 1200;
	public static final String DELIVERY_START_TIME = "Delivery Start Time";
	public static final String DELIVERY_END_TIME ="Delivery End Time";
	//public static final String SCAN_ID= "Drop Transaction Duration";
	public static final String SCAN_ID= "Scan ID";
	public static final String SKILLS= "Skills";
	
	public static final int DEFAULT_DELIVERY_START_TIME_IN_HOURS = 7;
	public static final int DEFAULT_DELIVERY_START_TIME_IN_MINUTES = 30;
	
	public static final int DEFAULT_DELIVERY_END_TIME_IN_HOURS = 4;
	public static final int DEFAULT_DELIVERY_END_TIME_IN_MINUTES = 00;
	public static final int COUNTRY_CODE_DEFAULT_LENGTH = 2;
	public static final String DEFAULT_COUNTRY_CODE = "AU";
	

	// CANCEL ORDER
	public static final String CAN_FILTER_NAME = "id";
	public static final String CAN_FILTER_OPERATION = "EQUALS";
	public static final String ORDER_ID_EMPTY_CANCEL = "Order Id should not be empty and status should have cancel";
	public static final String CANCEL = "cancel";
	public static final String CONCAT_LED_IN_CANCEL = "LED";
	
	public static final String CANCELLED = "cancelled";


}