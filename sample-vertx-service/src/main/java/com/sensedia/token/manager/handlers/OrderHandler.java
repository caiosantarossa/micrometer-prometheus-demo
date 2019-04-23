package com.sensedia.token.manager.handlers;

import com.sensedia.token.manager.models.Order;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

public class OrderHandler {

	static Map<Long, Order> ordersData;

	static {
		ordersData = new HashMap<>();
		ordersData.put(1l, new Order(1l, "Order 1"));
		ordersData.put(2l, new Order(2l, "Order 2"));
		ordersData.put(3l, new Order(3l, "Order 3"));
		ordersData.put(4l, new Order(4l, "Order 4"));
		ordersData.put(5l, new Order(5l, "Order 5"));
		ordersData.put(6l, new Order(6l, "Order 6"));
		ordersData.put(7l, new Order(7l, "Order 7"));
		ordersData.put(8l, new Order(8l, "Order 8"));
		ordersData.put(9l, new Order(9l, "Order 9"));
		ordersData.put(10l, new Order(10l, "Order 10"));
	}

	public OrderHandler() {
	}

	public void findAllOrders(RoutingContext rc) {

		rc.response()
				.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.setStatusCode(HttpResponseStatus.OK.code())
				.end(Json.encodePrettily(ordersData));

	}

}