package com.gmail.akshitgupta1695.cryptoHandler.handlers;

import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;


public class LaunchRequestHandler implements RequestHandler {
	
	private static String WELCOME_MESSAGE="Hi,Welcome to Crypto handler. I can tell you about the prices of various cryptoCurrencies in the currency of your choice.";

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(requestType(LaunchRequest.class));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		// TODO Auto-generated method stub
		return input.getResponseBuilder()
                .withSpeech(WELCOME_MESSAGE)
//               .withReprompt(Constants.HELP_MESSAGE)
                .withShouldEndSession(false)
                .build();
	}

}
