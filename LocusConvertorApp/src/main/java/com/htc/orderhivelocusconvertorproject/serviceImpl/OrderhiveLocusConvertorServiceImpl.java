package com.htc.orderhivelocusconvertorproject.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.htc.orderhivelocusconvertorproject.builder.LocusOrderBuilder;
import com.htc.orderhivelocusconvertorproject.constants.OrderhiveLocusConvertorConstants;
import com.htc.orderhivelocusconvertorproject.locusmodel.Body;
import com.htc.orderhivelocusconvertorproject.locusmodel.CancelOrder;
import com.htc.orderhivelocusconvertorproject.locusmodel.CancelOrderStatusMessage;
import com.htc.orderhivelocusconvertorproject.locusmodel.ContactPoint;
import com.htc.orderhivelocusconvertorproject.locusmodel.DropAmount;
import com.htc.orderhivelocusconvertorproject.locusmodel.Filters;
import com.htc.orderhivelocusconvertorproject.locusmodel.LineItems;
import com.htc.orderhivelocusconvertorproject.locusmodel.LocationAddress;
import com.htc.orderhivelocusconvertorproject.locusmodel.Locus;
import com.htc.orderhivelocusconvertorproject.locusmodel.OrderSelectRequest;
import com.htc.orderhivelocusconvertorproject.locusmodel.Parts;
import com.htc.orderhivelocusconvertorproject.locusmodel.PatchBody;
import com.htc.orderhivelocusconvertorproject.locusmodel.PickupSlot;
import com.htc.orderhivelocusconvertorproject.locusmodel.Price;
import com.htc.orderhivelocusconvertorproject.locusmodel.Slot;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.CustomFieldsListing;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.OrderHive;
import com.htc.orderhivelocusconvertorproject.orderhivemodel.OrderItems;
import com.htc.orderhivelocusconvertorproject.service.OrderhiveLocusConvertorService;

/**
 * Represents a Converter Implementation class for Locus Create Order / Update
 * Order / Cancel Order
 * 
 * @author HTC Global Services
 * @version 1.0
 * @since 30-03-2021
 * 
 */

public class OrderhiveLocusConvertorServiceImpl implements OrderhiveLocusConvertorService {

	ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	static final Logger LOGGER = LoggerFactory.getLogger(OrderhiveLocusConvertorServiceImpl.class);

	/**
	 * This method is used to get the Locus response for Create Order
	 * 
	 * @param orderhive
	 * @return
	 */
	@Override
	public String buildLocusCreateOrderRequest(OrderHive orderhive) {

		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String createOrderJSONString = null;
		Map<String, String> customProperty = null;
		Locus locus = new Locus();

		LocusOrderBuilder createOrderBuilder = new LocusOrderBuilder();
		try {
		String order_status = orderhive.getData().getOrder_status();
		if(order_status.equalsIgnoreCase("confirm")|| order_status.equalsIgnoreCase("not_confirm") || order_status.equalsIgnoreCase("Not Confirm") || 
				order_status.equalsIgnoreCase("confirmed") || order_status.equalsIgnoreCase("NotConfirmed")
				||order_status.equalsIgnoreCase("Not Confirmed")) {
			
			System.out.println("Inside If");
		locus.setClientId(OrderhiveLocusConvertorConstants.CLIENT_ID);
		locus.setId(orderhive.getData().getId());
		locus.setStatus(OrderhiveLocusConvertorConstants.STATUS);
		locus.setVersion(OrderhiveLocusConvertorConstants.VERSION);

		System.out.println("pass1");
		
		List<CustomFieldsListing> listOfCustomFieldsListing = orderhive.getData().getCustom_fields_listing();

		// Find the size of CustomFieldsListing if greater then zero then set to custom
		// property

		if (listOfCustomFieldsListing.size() > 0) {
			customProperty = new HashMap<>();
			for (CustomFieldsListing customFieldsListing : listOfCustomFieldsListing) {
				customProperty.put(customFieldsListing.getName(), customFieldsListing.getValue());
			}
			locus.setCustomProperties(customProperty);
		}
		System.out.println("pass2");
		// Find the name from custom field listing and set it to itemid.
		if (listOfCustomFieldsListing.size() > 0) {
			resetCreateCustomFields(locus);
			setCFLForLocusCreateOrder(locus, listOfCustomFieldsListing);
		} else {
			resetCreateCustomFields(locus);
		}

		System.out.println("pass3");
		if (orderhive.getData().getOrder_type() != null)
			locus.setTaksType(orderhive.getData().getOrder_type());
		locus.setStatus(OrderhiveLocusConvertorConstants.STATUS);
		System.out.println("pass4");
		// Iterate the OrderItems and set it to lineItem
		List<OrderItems> order_items = orderhive.getData().getOrder_items();
		List<LineItems> listOfLineItems = new ArrayList<>();
		if (order_items.size() > 0) {

			for (OrderItems orderItems : order_items) {
				LineItems lineItems = new LineItems();
				List<Parts> listOfParts = new ArrayList<Parts>();
				Price price = createOrderBuilder.setPrice(orderItems);
				createOrderBuilder.setLineIems(orderItems, lineItems, listOfParts, price);
				listOfLineItems.add(lineItems);
			}

		}
		System.out.println("pass5");
		locus.setLineItems(listOfLineItems);

		// Set Skills
		// List<Skills> skills = createOrderBuilder.setSkills(orderhive);
		// locus.setSkills(skills);

		// Setting PickupLocationAddress to locus from shipping address of OrderHive
		// LocationAddress pickupLocatonAddress =
		// createOrderBuilder.setPickupLocationAddress(orderhive);
		// locus.setPickupLocationAddress(pickupLocatonAddress);
		// Setting dropLocationAddress to locus from billing address of OrderHive
		LocationAddress dropLocatonAddress = createOrderBuilder.setPickupLocationAddress(orderhive);
		locus.setDropLocationAddress(dropLocatonAddress);
		System.out.println("pass6");
		// Setting dropContactPoint to locus from billing address of OrderHive
		// ContactPoint dropContactPoint =
		// createOrderBuilder.setPickupContactPoint(orderhive);
		// locus.setDropContactPoint(dropContactPoint);
		System.out.println("pass7");
		ContactPoint dropContactPoint = createOrderBuilder.setPickupContactPoint(orderhive);
		locus.setDropContactPoint(dropContactPoint);
		// Setting PickupContactPoint to locus from shipping address of OrderHive
		// ContactPoint pickupContactPoint =
		// createOrderBuilder.setPickupContactPoint(orderhive);
		// locus.setPickupContactPoint(pickupContactPoint);
		// set drop amount
		System.out.println("pass8");
		DropAmount dropAmount = createOrderBuilder.setDropAmount(orderhive);
		locus.setDropAmount(dropAmount);
		// Time being setting shipping due date to drop date
		// String dropDate = orderhive.getData().getShipping_due_date();
		String dropDate = orderhive.getData().getDelivery_date();
		if (dropDate != null)
			locus.setDropDate(dropDate.substring(0, 10));

		// set the drop slot
		if (dropDate != null) {
			Slot dropSlotDateTime = createOrderBuilder.setDropSlot(orderhive, dropDate.substring(0, 10));
			locus.setDropSlot(dropSlotDateTime);

			// set the drop slots list
			// List<Slot> dropSlots =
			// createOrderBuilder.setListOfDropSlots(dropSlotDateTime);
			// locus.setDropSlots(dropSlots);
		}
		System.out.println("pass9");

		// Checking volume or weight from JSON Structure and set the data
		/*
		 * List<OrderItems> order_items1 = orderhive.getData().getOrder_items(); if
		 * (order_items.size() >= 0) { for (OrderItems orderItems : order_items1) {
		 * 
		 * if (orderItems.getVolume() != null) { Volume volume = new Volume();
		 * volume.setValue(orderItems.getVolume().toString());
		 * volume.setUnit(OrderhiveLocusConvertorConstants.VOLUME_UNIT);
		 * locus.setVolume(volume); // break; } else { Volume volume = new Volume();
		 * volume.setValue("0");
		 * volume.setUnit(OrderhiveLocusConvertorConstants.VOLUME_UNIT);
		 * locus.setVolume(volume); // break; }
		 * 
		 * if (orderItems.getWeight() != null) { Weight weight = new Weight();
		 * weight.setValue((orderItems.getWeight().toString()));
		 * weight.setUnit(OrderhiveLocusConvertorConstants.WEIGHT_UNIT);
		 * locus.setWeight(weight); // break; } else { Weight weight = new Weight();
		 * weight.setValue("0");
		 * weight.setUnit(OrderhiveLocusConvertorConstants.WEIGHT_UNIT);
		 * locus.setWeight(weight); // break; }
		 * 
		 * break; } }
		 */

		locus.setCode(orderhive.getData().getChannel_order_id());
		locus.setName(orderhive.getData().getChannel_order_number());
		System.out.println("drop transacion sated from out side of custom fields listing");
		locus.setTransactionDuration(orderhive.getData().getTransacation_duration());
		// Setting the only date in the dd/MM/yyyy format
		locus.setOrderedOn(orderhive.getData().getSync_created().substring(0, 10));
		locus.setHomebaseId(orderhive.getData().getWarehouse_id());
		locus.setPickupVisitName(orderhive.getData().getWarehouse_id());
		locus.setPickupLocationId(orderhive.getData().getWarehouse_id());
		if (orderhive.getData().getShipping_address().getName() != null)
			locus.setDropVisitName(orderhive.getData().getShipping_address().getName());
		locus.setOrderStatus(OrderhiveLocusConvertorConstants.ORDER_STATUS);
		locus.setEffectiveStatus(OrderhiveLocusConvertorConstants.EFFECTIVE_STATUS);
		
		try {
			createOrderJSONString = mapper.writeValueAsString(locus);
			LOGGER.info(
					"CreateOrderJSONString is converted successfully to locus create order : " + createOrderJSONString);
		} catch (JsonProcessingException e) { // TODO Auto-generated catch block
			LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e.getMessage());
		}
		System.out.println("pass5");
		}
		else if (order_status.equalsIgnoreCase("cancel") || order_status.equalsIgnoreCase("cancelled")) {
			System.out.println("Inside else If");
			locus.setStatus(OrderhiveLocusConvertorConstants.CANCELLED);
		}
		else{
			System.out.println("Inside else");
			// Order Status is not valid to do operation
			// set message to response
		}
		}
		catch (Exception e) {
			LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e.getMessage());
		}
		System.out.println("Every thing ok...");
		System.out.println("pass10");
		return createOrderJSONString;

	}

	/**
	 * @param locus
	 * @param listOfCustomFieldsListing
	 */
	private void setCFLForLocusCreateOrder(Locus locus, List<CustomFieldsListing> listOfCustomFieldsListing) {
		for (CustomFieldsListing customFieldsListing : listOfCustomFieldsListing) {
			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.TEAM_ID)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty())
					locus.setTeamId(customFieldsListing.getValue());
			}

			if (customFieldsListing.getName()
					.equalsIgnoreCase(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty()) {
					System.out.println("CreateOrder Duration from Orderhive");
					locus.setDropTransactionDuration(
							String.valueOf(Integer.parseInt(customFieldsListing.getValue()) * 60));
				}
				else {
					System.out.println("CreateOrder Default Duration");
					locus.setDropTransactionDuration(String
							.valueOf(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION_DEFAULT_VALUE));
				}
			}

			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.SCAN_ID)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty())
					locus.setScanId(customFieldsListing.getValue());
			}

			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.SKILLS)) {
				String[] arrayOfSkills = null;
				System.out
						.println(customFieldsListing.getName() + "test skill if " + customFieldsListing.getValue());
				List<String> listOfSkills = new ArrayList<>();
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty()) {
					System.out.println("Inside Skills if part");
					arrayOfSkills = customFieldsListing.getValue().split(",");
					for (String skill : arrayOfSkills) {
						System.out.println("test skill" + skill.toString());
						listOfSkills.add(skill);
					}
					System.out.println("test skill list " + listOfSkills.toString());
					locus.setSkills(listOfSkills);
				}
			}

		}
	}

	/**
	 * This method is used to get the Locus response for Order Update
	 * 
	 * @param orderhive
	 * @return
	 */

	// This method is used to convert the JSON Structure from orderhive to Locus
	@Override
	public String buildLocusUpdateOrderRequest(OrderHive orderhive) {

		Slot dropSlot = null;
		DropAmount dropAmount = null;
		List<Slot> dropSlots = null;

		LocusOrderBuilder locusUpdateOrderBuilder = new LocusOrderBuilder();

		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String editOrderJSONString = null;
		Body body = new Body();
		PatchBody patchBody = new PatchBody();
		List<LineItems> listOfLineItems = new ArrayList<>();
		
		try {
		// Iterating the OrderItems and setting it to LineItems to Locus
			System.out.println("pass1");
		for (OrderItems orderItems : orderhive.getData().getOrder_items()) {
			LineItems lineItems = new LineItems();
			List<Parts> listOfParts = new ArrayList<Parts>();
			Price price = locusUpdateOrderBuilder.setPrice(orderItems);
			lineItems = locusUpdateOrderBuilder.setLineIems(orderItems, lineItems, listOfParts, price);
			listOfLineItems.add(lineItems);

		}
		System.out.println("pass2");
		patchBody.setLineItems(listOfLineItems);
		Map<String, String> customProperty = null;
		List<CustomFieldsListing> listOfCustomFieldsListing = orderhive.getData().getCustom_fields_listing();

		// Find the size of CustomFieldsListing if greater then zero then set to custom
		// property
		if (listOfCustomFieldsListing.size() > 0) {
			customProperty = new HashMap<>();
			for (CustomFieldsListing customFieldsListing : listOfCustomFieldsListing) {
				customProperty.put(customFieldsListing.getName(), customFieldsListing.getValue());
			}
			patchBody.setCustomProperties(customProperty);
		}
		System.out.println("pass3");

		// Find the name from custom field listing and set it to itemid.
		if (listOfCustomFieldsListing.size() > 0) {
			resetCustomFields(patchBody);
			
			setCFLForLocusEditOrder(patchBody, listOfCustomFieldsListing);
		} else {
			resetCustomFields(patchBody);
		}
		System.out.println("pass4");
		// Setting the skills
		/*
		 * List<Skills> listOfSkills = new ArrayList<>();
		 * patchBody.setSkills(listOfSkills);
		 */
		System.out.println("pass5");
		patchBody.setPickupLocationId(orderhive.getData().getWarehouse_id());
		patchBody.setPickupVisitName(orderhive.getData().getWarehouse_id());

		// set pickupSlots
		List<PickupSlot> pickupSlots = new ArrayList<>();
		patchBody.setPickupSlots(pickupSlots);

		if (orderhive.getData().getShipping_address().getName() != null)
			patchBody.setDropVisitName(orderhive.getData().getShipping_address().getName());

		// Setting dropContactPoint to locus from billing address of OrderHive
		ContactPoint dropContactPoint = locusUpdateOrderBuilder.setPickupContactPoint(orderhive);
		patchBody.setDropContactPoint(dropContactPoint);
		// Setting PickupContactPoint to locus from shipping address of OrderHive
		// ContactPoint pickupContactPoint =
		// locusUpdateOrderBuilder.setPickupContactPoint(orderhive);
		// patchBody.setPickupContactPoint(pickupContactPoint);
		// Setting PickupLocationAddress to locus from shipping address of OrderHive
		// LocationAddress pickupLocatonAddress =
		// locusUpdateOrderBuilder.setPickupLocationAddress(orderhive);
		// patchBody.setPickupLocationAddress(pickupLocatonAddress);
		// Setting dropLocationAddress to locus from billing address of OrderHive
		LocationAddress dropLocatonAddress = locusUpdateOrderBuilder.setPickupLocationAddress(orderhive);
		patchBody.setDropLocationAddress(dropLocatonAddress);
		System.out.println("pass6");
		// Time being setting shipping due date to drop date
		String dropDate = orderhive.getData().getDelivery_date();
		if (dropDate != null)
			patchBody.setDropDate(dropDate.substring(0, 10));
		// setting drop slot
		if (dropDate != null) {
			dropSlot = locusUpdateOrderBuilder.setDropSlot(orderhive, dropDate.substring(0, 10));
			patchBody.setDropSlot(dropSlot);
			// setting drop slots
			// dropSlots = locusUpdateOrderBuilder.setListOfDropSlots(dropSlot);
			// patchBody.setDropSlots(dropSlots);
		}

		// setting drop amount
		dropAmount = locusUpdateOrderBuilder.setDropAmount(orderhive);
		patchBody.setDropAmount(dropAmount);
		// setting current date on orderedOn and CreatedOn
		String currentDate = locusUpdateOrderBuilder.findSystemDate();
		patchBody.setOrderedOn(currentDate);
		patchBody.setCreatedOn(orderhive.getData().getCreated_date());
		System.out.println("pass7");
		/*
		 * // Checking volume or weight from JSON Structure and set the data
		 * List<OrderItems> order_items = orderhive.getData().getOrder_items(); if
		 * (order_items.size() >= 0) { for (OrderItems orderItems : order_items) { if
		 * (orderItems.getVolume() != null) { Volume volume = new Volume();
		 * volume.setValue(orderItems.getVolume().toString());
		 * volume.setUnit(OrderhiveLocusConvertorConstants.VOLUME_UNIT);
		 * patchBody.setVolume(volume); // break; } else { Volume volume = new Volume();
		 * volume.setValue("0");
		 * volume.setUnit(OrderhiveLocusConvertorConstants.VOLUME_UNIT);
		 * patchBody.setVolume(volume); // break; }
		 * 
		 * if (orderItems.getWeight() != null) { Weight weight = new Weight();
		 * weight.setValue((orderItems.getWeight().toString()));
		 * weight.setUnit(OrderhiveLocusConvertorConstants.WEIGHT_UNIT);
		 * patchBody.setWeight(weight); // break; } else { Weight weight = new Weight();
		 * weight.setValue("0");
		 * weight.setUnit(OrderhiveLocusConvertorConstants.WEIGHT_UNIT);
		 * patchBody.setWeight(weight); // break; }
		 * 
		 * break; } }
		 */
		System.out.println("pass8");
		patchBody.setCode(orderhive.getData().getChannel_order_id());
		patchBody.setName(orderhive.getData().getChannel_order_number());
		// Setting Client Id
		body.setClientId(OrderhiveLocusConvertorConstants.CLIENT_ID);
		// setting id
		body.setId(orderhive.getData().getId());
		body.setPatchBody(patchBody);
		System.out.println("pass9");
		try {
			editOrderJSONString = mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e.getMessage());
		}
		
		}
		catch (Exception e) {
			LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e.getMessage());
		}
		System.out.println("pass10");
		return editOrderJSONString;

	}

	/**
	 * @param patchBody
	 * @param listOfCustomFieldsListing
	 */
	private void setCFLForLocusEditOrder(PatchBody patchBody, List<CustomFieldsListing> listOfCustomFieldsListing) {
		for (CustomFieldsListing customFieldsListing : listOfCustomFieldsListing) {
			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.TEAM_ID)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty())
					patchBody.setTeamId(customFieldsListing.getValue());
			}

			if (customFieldsListing.getName()
					.equalsIgnoreCase(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty()) {
					System.out.println("EditOrder Duration from Orderhive");
					patchBody.setDropTransactionDuration(
							String.valueOf(Integer.parseInt(customFieldsListing.getValue()) * 60));
				}
				else {
					System.out.println("EditOrder Default Duration");
					patchBody.setDropTransactionDuration(String
							.valueOf(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION_DEFAULT_VALUE));
				}
			}

			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.SCAN_ID)) {
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty())
					patchBody.setScanId(customFieldsListing.getValue());
			}

			if (customFieldsListing.getName().equalsIgnoreCase(OrderhiveLocusConvertorConstants.SKILLS)) {
				String[] arrayOfSkills = null;
				System.out
						.println(customFieldsListing.getName() + "test skill if " + customFieldsListing.getValue());
				List<String> listOfSkills = new ArrayList<>();
				if (customFieldsListing.getValue() != null || !customFieldsListing.getValue().isEmpty()) {
					System.out.println("Inside Skills if part");
					arrayOfSkills = customFieldsListing.getValue().split(",");
					for (String skill : arrayOfSkills) {
						System.out.println("test skill" + skill.toString());
						listOfSkills.add(skill);
					}
					System.out.println("test skill list " + listOfSkills.toString());
					patchBody.setSkills(listOfSkills);
				}
			}

		}
	}

	private void resetCustomFields(PatchBody patchBody) {
		List<String> emptyListOfSkills = new ArrayList<>();
		patchBody.setTeamId(null);
		patchBody.setScanId(null);
		patchBody.setSkills(emptyListOfSkills);
		patchBody.setDropTransactionDuration(
				String.valueOf(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION_DEFAULT_VALUE));
	}

	private void resetCreateCustomFields(Locus locus) {
		List<String> emptyListOfSkills = new ArrayList<>();
		locus.setTeamId(null);
		locus.setScanId(null);
		locus.setSkills(emptyListOfSkills);
		locus.setDropTransactionDuration(
				String.valueOf(OrderhiveLocusConvertorConstants.DROP_TRANSACTION_DURATION_DEFAULT_VALUE));
	}

	/**
	 * This method is used to get the Locus response for Order Cancel
	 * 
	 * @param cancelId
	 * @return
	 */
	@Override
	public String buildLocusCancelOrderRequest(OrderHive orderhive) {

		LocusOrderBuilder locusCancelOrderBuilder = new LocusOrderBuilder();
		CancelOrder cancelOrder = null;
		CancelOrderStatusMessage cancelOrderStatusMessage = null;
		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String cancelOrderJSONString = null;
		// To cancel the order checking the order status and order id
		if (orderhive.getData().getId() != null
				&& (orderhive.getData().getOrder_status().equalsIgnoreCase(OrderhiveLocusConvertorConstants.CANCEL))) {
			cancelOrder = new CancelOrder();

			cancelOrder.setOrderStatus(orderhive.getData().getOrder_status()
					.concat(OrderhiveLocusConvertorConstants.CONCAT_LED_IN_CANCEL).toUpperCase());

			OrderSelectRequest orderSelectRequest = new OrderSelectRequest();

			List<Filters> listOfFilters = new ArrayList<Filters>();
			// setting filters
			Filters filters = locusCancelOrderBuilder.setFilters(orderhive);
			listOfFilters.add(filters);
			orderSelectRequest.setFilters(listOfFilters);
			cancelOrder.setOrderSelectRequest(orderSelectRequest);
			try {
				cancelOrderJSONString = mapper.writeValueAsString(cancelOrder);
				LOGGER.info("CancelOrderJSONString successfully converted: " + cancelOrderJSONString);
			} catch (JsonProcessingException e) {
				LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e);
			}
			return cancelOrderJSONString;
		} else {
			cancelOrderStatusMessage = new CancelOrderStatusMessage();
			cancelOrderStatusMessage.setMessage(OrderhiveLocusConvertorConstants.ORDER_ID_EMPTY_CANCEL);
			LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + cancelOrderStatusMessage);
			try {
				cancelOrderJSONString = mapper.writeValueAsString(cancelOrderStatusMessage);

			} catch (JsonProcessingException e) {
				LOGGER.error(OrderhiveLocusConvertorConstants.ERROR_MESSAGE + e.getMessage());
			}
			return cancelOrderJSONString;
		}
	}

}
