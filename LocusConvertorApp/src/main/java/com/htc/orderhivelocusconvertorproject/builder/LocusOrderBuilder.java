package com.htc.orderhivelocusconvertorproject.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.htc.orderhivelocusconvertorproject.constants.OrderhiveLocusConvertorConstants;
import com.htc.orderhivelocusconvertorproject.locusmodel.Amount;
import com.htc.orderhivelocusconvertorproject.locusmodel.ContactPoint;
import com.htc.orderhivelocusconvertorproject.locusmodel.DropAmount;
import com.htc.orderhivelocusconvertorproject.locusmodel.Filters;
import com.htc.orderhivelocusconvertorproject.locusmodel.LineItems;
import com.htc.orderhivelocusconvertorproject.locusmodel.LocationAddress;
import com.htc.orderhivelocusconvertorproject.locusmodel.Parts;
import com.htc.orderhivelocusconvertorproject.locusmodel.Price;
import com.htc.orderhivelocusconvertorproject.locusmodel.Skills;
import com.htc.orderhivelocusconvertorproject.locusmodel.Slot;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.CustomFieldsListing;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.OrderHive;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.OrderItems;

/**
 * Represents a Builder class for Locus Create Order
 * 
 * @author HTC Global Services
 * @version 1.0
 * @since 30-03-2021
 * 
 */
public class LocusOrderBuilder {

	Logger logger = LoggerFactory.getLogger(LocusOrderBuilder.class);

	public LocusOrderBuilder() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Note : Exchange Type allowed only : [NONE/COLLECT/GIVE]
	 * 
	 * @param : This method receives the orderhive object as args.
	 * @return : list of dropAmounts
	 *//*
		 * private List<DropAmount> setDropAmounts(OrderHive orderhive) {
		 * List<DropAmount> dropAmounts = new ArrayList<DropAmount>(); DropAmount
		 * dropAmount = new DropAmount(); dropAmount=setAmount(orderhive, dropAmount);
		 * dropAmount.setExchangeType(LocusConstants.EXCHANGE_TYPE);
		 * dropAmounts.add(dropAmount); return dropAmounts; }
		 */

	/**
	 * 
	 * /** Note : Exchange Type allowed only : [NONE/COLLECT/GIVE]
	 * 
	 * @param : This method receives the orderhive object as args.
	 * @return : dropAmount
	 */
	public DropAmount setDropAmount(OrderHive orderhive) {
		DropAmount dropAmount = new DropAmount();
		dropAmount = setAmount(orderhive, dropAmount);
		dropAmount.setExchangeType(OrderhiveLocusConvertorConstants.EXCHANGE_TYPE);
		return dropAmount;
	}

	/**
	 * @param : This method receives the orderhive object as args.
	 */
	public DropAmount setAmount(OrderHive orderhive, DropAmount dropAmount) {
		Amount amount = new Amount();
		amount.setAmount(orderhive.getData().getTotal());
		amount.setCurrency(OrderhiveLocusConvertorConstants.CURRENCY_SYMBOL);
		amount.setSymbol(OrderhiveLocusConvertorConstants.CURRENCY_SYMBOL);
		dropAmount.setAmount(amount);
		return dropAmount;
	}

	/**
	 * @param : This method receives the dropSlotDateTime object as args.
	 * @return : dropSlots as array list
	 */
	public List<Slot> setListOfDropSlots(Slot dropSlotDateTime) {
		List<Slot> dropSlots = new ArrayList<Slot>();
		dropSlots.add(dropSlotDateTime);
		dropSlots.add(dropSlotDateTime);
		return dropSlots;
	}

	/**
	 * @return : dropSlot as obj.
	 */
	public Slot setDropSlot(OrderHive orderhive, String dropDate) {
		Slot dropSlot = new Slot();
		Date inputDropDate = null;
		// Date date2 = null;

		try {
			inputDropDate = new SimpleDateFormat("yyyy-MM-dd").parse(dropDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<CustomFieldsListing> listOfCustomFieldsListing = orderhive.getData().getCustom_fields_listing();
		// Find the name from custom field listing and set it to itemid.
		System.out.println("listOfCustomFieldsListing.size() :" + listOfCustomFieldsListing.size());
		if (listOfCustomFieldsListing.size() > 0) {
			System.out.println("Inside custom listing");
			System.out.println("listOfCustomFieldsListing : " + listOfCustomFieldsListing);
			resetTimeSlots(dropSlot, inputDropDate);
			for (CustomFieldsListing customFieldsListing : listOfCustomFieldsListing) {
				System.out.println("customFieldsListing " + customFieldsListing);
				String utcDate = null;

				if (customFieldsListing.getName()
						.equalsIgnoreCase(OrderhiveLocusConvertorConstants.DELIVERY_START_TIME)) {
					System.out.println("Delivery start time forund");
					if (customFieldsListing.getValue() != null) {
						Date date2 = null;
						System.out.println("inside if");
						int hours = Integer.parseInt(customFieldsListing.getValue().substring(0, 2));
						int minuets = Integer.parseInt(customFieldsListing.getValue().substring(3, 5));
						date2 = subtractHoursToJavaUtilDate(inputDropDate, hours, minuets);
						utcDate = stringToUTCDateConverter(date2);
						dropSlot.setStart(utcDate);

					} else {
						Date date2 = null;
						System.out.println("Delivery start time not forund inside else");
						date2 = subtractHoursToJavaUtilDate(inputDropDate,
								OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_HOURS,
								OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_MINUTES);
						utcDate = stringToUTCDateConverter(date2);
						dropSlot.setStart(utcDate);

					}

				}

				/*
				 * else { System.out.println("Delivery start time not forund inside else");
				 * date2 = subtractHoursToJavaUtilDate(date1,
				 * OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_HOURS,
				 * OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_MINUTES);
				 * utcDate = stringToUTCDateConverter(date2); dropSlot.setStart(utcDate);
				 * 
				 * }
				 */

				if (customFieldsListing.getName()
						.equalsIgnoreCase(OrderhiveLocusConvertorConstants.DELIVERY_END_TIME)) {

					if (customFieldsListing.getValue() != null) {
						Date date2 = null;
						System.out.println("delivery end date found");
						int hours = Integer.parseInt(customFieldsListing.getValue().substring(0, 2));
						int minutes = Integer.parseInt(customFieldsListing.getValue().substring(3, 5));
						date2 = addHoursToJavaUtilDate(inputDropDate, hours, minutes);
						utcDate = stringToUTCDateConverter(date2);
						dropSlot.setEnd(utcDate);
					} else {
						Date date2 = null;
						System.out.println("delivery end date not found ");
						date2 = addHoursToJavaUtilDate(inputDropDate,
								OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_HOURS,
								OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_MINUTES);
						utcDate = stringToUTCDateConverter(date2);
						dropSlot.setEnd(utcDate);
					}

				} /*
					 * else { System.out.println("delivery end date not found "); date2 =
					 * addHoursToJavaUtilDate(date1,
					 * OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_HOURS,
					 * OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_MINUTES);
					 * utcDate = stringToUTCDateConverter(date2); dropSlot.setEnd(utcDate); }
					 */

				// break;
			}

		} else if (listOfCustomFieldsListing.isEmpty()) {
			resetTimeSlots(dropSlot, inputDropDate);
		}
		return dropSlot;
	}

	private void resetTimeSlots(Slot dropSlot, Date inputDropDate) {
		System.out.println("Inside LocusOrderBuilder resetTimeSlots");
		Date startDateTime = null;
		Date endDateTime = null;
		System.out.println("custom fields listing is empty");
		String defaultStartTime = null;
		String defaultEndTime = null;

		startDateTime = subtractHoursToJavaUtilDate(inputDropDate,
				OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_HOURS,
				OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_START_TIME_IN_MINUTES);
		defaultStartTime = stringToUTCDateConverter(startDateTime);
		dropSlot.setStart(defaultStartTime);

		endDateTime = addHoursToJavaUtilDate(inputDropDate,
				OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_HOURS,
				OrderhiveLocusConvertorConstants.DEFAULT_DELIVERY_END_TIME_IN_MINUTES);
		defaultEndTime = stringToUTCDateConverter(endDateTime);
		dropSlot.setEnd(defaultEndTime);
	}

	// This method is used to convert the date in UTC format, UTC time is -5:30 less
	// then IST
	private String stringToUTCDateConverter(Date date) {

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String nowAsISO = df.format(date);
		System.out.println(nowAsISO);
		return nowAsISO;
	}

	private Date subtractHoursToJavaUtilDate(Date date, int hours, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours - OrderhiveLocusConvertorConstants.HOURS_TO_SUBTRACT);
		// calendar.add(Calendar.HOUR_OF_DAY, hours);
		calendar.add(Calendar.MINUTE, minutes - OrderhiveLocusConvertorConstants.MINUTES_TO_SUBTRACT);
		return calendar.getTime();
	}

	private Date addHoursToJavaUtilDate(Date date, int hours, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours - OrderhiveLocusConvertorConstants.HOURS_TO_SUBTRACT + 12);
		// calendar.add(Calendar.HOUR_OF_DAY, hours);
		calendar.add(Calendar.MINUTE, minutes - OrderhiveLocusConvertorConstants.MINUTES_TO_SUBTRACT);
		return calendar.getTime();
	}

	/**
	 * Setting Pickup location address from shipping to orderhive
	 * 
	 * @param :This method receives the orderhive object as args.
	 * @return : pickupLocationAddress
	 */
	public LocationAddress setPickupLocationAddress(OrderHive orderhive) {

		LocationAddress pickupLocationAddress = new LocationAddress();
		pickupLocationAddress.setPlaceName(orderhive.getData().getShipping_address().getCompany());
		String address2 = orderhive.getData().getShipping_address().getAddress2();
		if (!address2.isEmpty() || address2 != null)
			pickupLocationAddress.setLocalityName(address2);
		pickupLocationAddress.setFormattedAddress(orderhive.getData().getShipping_address().getAddress1());
		pickupLocationAddress.setPincode(orderhive.getData().getShipping_address().getZipcode());
		pickupLocationAddress.setCity(orderhive.getData().getShipping_address().getCity());
		pickupLocationAddress.setState(orderhive.getData().getShipping_address().getState());

//		pickupLocationAddress.setCountryCode(orderhive.getData().getShipping_address().getCountry_code());
		pickupLocationAddress.setCountryCode(OrderhiveLocusConvertorConstants.DEFAULT_COUNTRY_CODE);
		try {
			System.out.println("Inside country setup try...");
			if (!orderhive.getData().getShipping_address().getCountry_code().isEmpty()
					&& !orderhive.getData().getShipping_address().getCountry().isEmpty()) {
				System.out.println("Inside country setup...");
				if (orderhive.getData().getShipping_address().getCountry_code()
						.length() == OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH) {
					System.out.println("if...");
					pickupLocationAddress.setCountryCode(orderhive.getData().getShipping_address().getCountry_code());
				} else if (orderhive.getData().getShipping_address().getCountry_code()
						.length() != OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH
						&& orderhive.getData().getShipping_address().getCountry()
								.length() == OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH) {
					System.out.println("else if...");
					pickupLocationAddress.setCountryCode(orderhive.getData().getShipping_address().getCountry());
				}
			}
		} catch (Exception e) {
			logger.error("Inside pickupLocationAddress countryCode catch block" +e.getMessage());
		}
		return pickupLocationAddress;
	}

	/**
	 * Setting Drop Location Address from shipping to orderhive
	 * 
	 * @param : This method receives the orderhive object as args.
	 * @return : dropLocationAddress
	 */
	public LocationAddress setDropLocationAddress(OrderHive orderhive) {
		LocationAddress dropLocationAddress = new LocationAddress();
		dropLocationAddress.setPlaceName(orderhive.getData().getBilling_address().getCompany());
		String address2 = orderhive.getData().getShipping_address().getAddress2();
		if (!address2.isEmpty() || address2 != null)
			dropLocationAddress.setLocalityName(address2);
		dropLocationAddress.setFormattedAddress(orderhive.getData().getBilling_address().getAddress1());
		dropLocationAddress.setPincode(orderhive.getData().getBilling_address().getZipcode());
		dropLocationAddress.setCity(orderhive.getData().getBilling_address().getCity());
		dropLocationAddress.setState(orderhive.getData().getBilling_address().getState());
//		dropLocationAddress.setCountryCode(orderhive.getData().getBilling_address().getCountry_code());
		dropLocationAddress.setCountryCode(OrderhiveLocusConvertorConstants.DEFAULT_COUNTRY_CODE);
		try {
			System.out.println("Inside country setup try...");
			if (!orderhive.getData().getShipping_address().getCountry_code().isEmpty()
					&& !orderhive.getData().getShipping_address().getCountry().isEmpty()) {
				System.out.println("Inside country setup...");
				if (orderhive.getData().getShipping_address().getCountry_code()
						.length() == OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH) {
					System.out.println("Inside if...");
					dropLocationAddress.setCountryCode(orderhive.getData().getShipping_address().getCountry_code());
				} else if (orderhive.getData().getShipping_address().getCountry_code()
						.length() != OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH
						&& orderhive.getData().getShipping_address().getCountry()
								.length() == OrderhiveLocusConvertorConstants.COUNTRY_CODE_DEFAULT_LENGTH) {
					System.out.println("Inside else if...");
					dropLocationAddress.setCountryCode(orderhive.getData().getShipping_address().getCountry());
				}
			}
		} catch (Exception e) {
			logger.error("Inside dropLocationAddress countryCode catch block" +e.getMessage());
		}

		return dropLocationAddress;
	}

	/**
	 * Find out current date in the dd/MM/yyyy format.
	 * 
	 * @return : deliveryDate
	 */
	public String findSystemDate() {
		Date date = new Date();
		String currentDate = new SimpleDateFormat(OrderhiveLocusConvertorConstants.DATE_FORMAT).format(date);
		return currentDate;
	}

	/**
	 * @param :This method receives the orderhive object as args.
	 * @return : contactPoint
	 */
	public ContactPoint setPickupContactPoint(OrderHive orderhive) {
		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setName(orderhive.getData().getShipping_address().getName());
		contactPoint.setNumber(orderhive.getData().getShipping_address().getContact_number());
		return contactPoint;
	}

	/**
	 * @param :This method receives the orderhive object as args.
	 * @return : contactPoint
	 */
	public ContactPoint setDropContactPoint(OrderHive orderhive) {
		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setName(orderhive.getData().getBilling_address().getName());
		contactPoint.setNumber(orderhive.getData().getBilling_address().getContact_number());
		return contactPoint;
	}

	/**
	 * @return skills
	 */
	public List<Skills> setSkills(OrderHive orderHive) {
		List<Skills> listOfSkills = new ArrayList<>();
		if (orderHive.getData().getSkills() != null) {
			String[] arrayOfSkills = orderHive.getData().getSkills().split(",");
			for (String skill : arrayOfSkills) {
				Skills skillObj = new Skills();
				skillObj.setSkills(skill);
				listOfSkills.add(skillObj);

			}
		}
		System.out.println("final skills : " + listOfSkills);
		return listOfSkills;
	}

	/**
	 * @param orderItems
	 * @param lineItems
	 * @param listOfParts
	 * @param price
	 * @return
	 */
	public LineItems setLineIems(OrderItems orderItems, LineItems lineItems, List<Parts> listOfParts, Price price) {
		lineItems.setPrice(price);
		if (orderItems.getSku() != null)
			lineItems.setId(orderItems.getSku());
		if (orderItems.getNote() != null)
			lineItems.setNote(orderItems.getNote());
		if (orderItems.getItem_id() != null)
			lineItems.setLineItemId(orderItems.getItem_id().toString());
		if (orderItems.getName() != null)
			lineItems.setName(orderItems.getName());
		if (orderItems.getQuantity_ordered() != null)
			lineItems.setQuantity(Double.parseDouble(orderItems.getQuantity_ordered().toString()));
		lineItems.setQuantityUnit(OrderhiveLocusConvertorConstants.QUANTITY_UNIT);

		lineItems.setParts(listOfParts);

		return lineItems;
	}

	/**
	 * @param orderItems
	 * @return price
	 */
	public Price setPrice(OrderItems orderItems) {
		Price price = new Price();
		price.setAmount((double) orderItems.getPrice());
		price.setCurrency(OrderhiveLocusConvertorConstants.CURRENCY_SYMBOL);
		price.setSymbol(OrderhiveLocusConvertorConstants.CURRENCY_SYMBOL);
		return price;
	}

	/**
	 * This method is used to set the filter
	 * 
	 * @param orderhive
	 * @return
	 */
	public Filters setFilters(OrderHive orderhive) {
		Filters filters = new Filters();
		filters.setName(OrderhiveLocusConvertorConstants.CAN_FILTER_NAME);
		filters.setOperation(OrderhiveLocusConvertorConstants.CAN_FILTER_OPERATION);
		String[] values = new String[1];
		values[0] = String.valueOf(orderhive.getData().getId());
		filters.setValues(values);
		return filters;
	}
}