package com.gmail.akshitgupta1695.cryptoHandler.util;

public class Constants {
	
	//TODO:RANDOMIZE these strings to Add Personality
	public static String WELCOME_MESSAGE="Hi,Welcome to Crypto Handler.You can ask me about prices of popular cryptocurrencies in indian rupees,u. s. dollars and euros .How can I help you today?";
	public static String HELP_MESSAGE="You can ask me aboout prices of popular Cryptocurrencies in indian rupees,u. s. dollars and euros.For example,You may simply ask  </say-as><break strength='strong'/> What is the price of Bitcoin?";
	public static String EXIT_MESSAGE="Okay,See you soon.";
	public static String SUPPORTED_CURRENCIES="Currently, I can give info about Bitcoin,Ethereum,Litecoin,ripple,iota and bitcoin cash.I can tell the prices in Indian rupees,United States Dollars and Euros.";
	
	//Replace %s with cryptocurrency of choice
	public static String PRICE_API="https://min-api.cryptocompare.com/data/price?fsym=%s&tsyms=USD,INR,EUR&extraParams=Crypto_Handler";
	//TODO:ADD conversion if needed
	public static String CONVERSION_API="https://free.currencyconverterapi.com/api/v5/convert?q=USD_INR&compact=ultra";
	
	

}
