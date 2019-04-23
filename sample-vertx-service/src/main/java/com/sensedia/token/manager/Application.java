package com.sensedia.token.manager;

import com.sensedia.token.manager.verticles.OrderVerticle;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;

import java.util.Arrays;
import java.util.List;

public class Application {

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
				new MicrometerMetricsOptions().setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
						.setEnabled(true)));

		configureMetrics();

		ConfigRetriever retriever = ConfigRetriever.create(vertx);
		retriever.getConfig(json -> {
			DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(json.result());
			vertx.deployVerticle(OrderVerticle.class, deploymentOptions);
		});

	}

	private static void configureMetrics() {
		MeterRegistry registry = BackendRegistries.getDefaultNow();

		List<Tag> tags = Arrays.asList(Tag.of("application", "sample-vertx-service"));

		registry.config()
				.meterFilter(new RenameVertxFilter())
				.commonTags(tags);

		new UptimeMetrics(tags).bindTo(registry);
		new FileDescriptorMetrics(tags).bindTo(registry);
		new ClassLoaderMetrics(tags).bindTo(registry);
		new JvmMemoryMetrics(tags).bindTo(registry);
		new JvmGcMetrics(tags).bindTo(registry);
		new ProcessorMetrics(tags).bindTo(registry);
		new JvmThreadMetrics(tags).bindTo(registry);
	}

	private static class RenameVertxFilter implements MeterFilter {

		@Override
		public Meter.Id map(Meter.Id id) {
			if (id.getName()
					.startsWith("vertx")) {
				return id.withName(id.getName()
						.substring(6));
			}

			return id;
		}
	}

}
