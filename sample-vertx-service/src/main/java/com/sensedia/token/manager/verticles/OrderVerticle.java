package com.sensedia.token.manager.verticles;

import com.sensedia.token.manager.handlers.OrderHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;

public class OrderVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderVerticle.class);

	private HttpServer httpServer;

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
	}

	@Override
	public void start(Future<Void> future) {

		LOGGER.info("Starting Order Verticle");

		final OrderHandler orderHandler = new OrderHandler();

		Router subRouter = Router.router(getVertx());
		subRouter.get("/orders")
				.handler(orderHandler::findAllOrders);

		Router mainRouter = Router.router(getVertx());

		mainRouter.mountSubRouter("/api/v1", subRouter);

		mainRouter.route("/health*")
				.handler(HealthCheckHandler.create(getVertx())
						.register("health", ar -> ar.complete(Status.OK())));
		mainRouter.route("/prometheus*")
				.handler(PrometheusScrapingHandler.create());

		httpServer = getVertx().createHttpServer()
				.requestHandler(mainRouter::accept)
				.listen(config().getInteger("http.port", 8092), ar -> {
					if (ar.succeeded()) {
						future.complete();
						LOGGER.info("HTTP Server running at port {0}", String.valueOf(ar.result()
								.actualPort()));
					} else {
						future.fail(ar.cause()
								.getMessage());
					}
				});
	}

	@Override
	public void stop() {
		LOGGER.info("Stopping Order Verticle");
		httpServer.close();
	}

}
