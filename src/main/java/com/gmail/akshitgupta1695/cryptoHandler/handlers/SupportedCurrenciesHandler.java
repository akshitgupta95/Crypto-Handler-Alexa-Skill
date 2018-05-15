package com.gmail.akshitgupta1695.cryptoHandler.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.gmail.akshitgupta1695.cryptoHandler.util.Constants;

public class SupportedCurrenciesHandler implements RequestHandler {
	 
	@Override
	    public boolean canHandle(HandlerInput input) {
	        return input.matches(intentName("SupportedCurrenciesIntent"));
	    }

	    @Override
	    public Optional<Response> handle(HandlerInput input) {
	        return input.getResponseBuilder()
	                .withSpeech(Constants.SUPPORTED_CURRENCIES)
	                .withReprompt(Constants.HELP_MESSAGE)
	                .withShouldEndSession(false)
	                .build();
	    }

}
