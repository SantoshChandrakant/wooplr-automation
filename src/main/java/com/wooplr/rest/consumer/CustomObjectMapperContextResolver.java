package com.wooplr.rest.consumer;

import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.spi.ContextResolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Custom {@link ContextResolver} for (de)serializing Java 8
 * {@link ZonedDateTime}
 * 
 * @author Santosh
 * 
 */
@Provider
public class CustomObjectMapperContextResolver implements ContextResolvers {

	private final ObjectMapper MAPPER;

	public CustomObjectMapperContextResolver() {
		MAPPER = new ObjectMapper();
		MAPPER.registerModule(new JavaTimeModule());
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	// @Override
	public ObjectMapper getContext(Class<?> type) {
		return MAPPER;
	}

	public <T> ContextResolver<T> resolve(Type arg0, MediaType arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}