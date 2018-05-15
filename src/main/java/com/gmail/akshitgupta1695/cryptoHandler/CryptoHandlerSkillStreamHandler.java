package com.gmail.akshitgupta1695.cryptoHandler;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.gmail.akshitgupta1695.cryptoHandler.handlers.LaunchRequestHandler;


public class CryptoHandlerSkillStreamHandler extends SkillStreamHandler {

	public CryptoHandlerSkillStreamHandler() {
		super(Skills.standard()
                .addRequestHandlers(new LaunchRequestHandler())
                .build());
		// TODO Auto-generated constructor stub
	}

}
