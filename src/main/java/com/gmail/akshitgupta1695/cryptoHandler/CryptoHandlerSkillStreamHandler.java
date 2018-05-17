package com.gmail.akshitgupta1695.cryptoHandler;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.gmail.akshitgupta1695.cryptoHandler.handlers.*;

public class CryptoHandlerSkillStreamHandler extends SkillStreamHandler {

	public CryptoHandlerSkillStreamHandler() {
		super(Skills.standard()
                .addRequestHandlers(new LaunchRequestHandler(),new ExitSkillHandler(),new HelpIntentHandler(),new SessionEndedHandler(),new SupportedCurrenciesHandler(),new GetPriceHandler())
                .withAutoCreateTable(true)
                .withTableName("cryptoHandlerData")
                .withDynamoDbClient(AmazonDynamoDBClientBuilder.defaultClient())
                .build());
		
		
	}

}
