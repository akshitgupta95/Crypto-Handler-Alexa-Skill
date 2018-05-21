package com.gmail.akshitgupta1695.cryptoHandler.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.gmail.akshitgupta1695.cryptoHandler.util.Constants;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
    		String help="You can say things like:"+"\n"+"\"What is the price of Ethereum\"" +"\n"+"\"Price of one Bitcoin\""+"\n"+"\"what all currencies are supported\"";
        return input.getResponseBuilder()
                .withSpeech(Constants.HELP_MESSAGE)
                .withReprompt(Constants.HELP_MESSAGE)
                .withSimpleCard("Using This Skill", help)
                .withShouldEndSession(false)
                .build();
    }

}