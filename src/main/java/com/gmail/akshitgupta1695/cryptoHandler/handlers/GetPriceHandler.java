package com.gmail.akshitgupta1695.cryptoHandler.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.gmail.akshitgupta1695.cryptoHandler.models.PricesDto;
import com.gmail.akshitgupta1695.cryptoHandler.util.Constants;


public class GetPriceHandler implements RequestHandler{

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("GetPriceIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		 IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		
		 if (intentRequest.getDialogState() == DialogState.STARTED){
//			 Map<String,Slot> slotMap=intentRequest.getIntent().getSlots();
			    // Pre-fill slots: update the intent object with slot values for which
			    // you have defaults, then return Dialog.Delegate with this updated intent
			    // in the updatedIntent property.
			 	return input.getResponseBuilder().addDelegateDirective(null).build();
//			 	return handleIntentAndGenerateResponse(input, intentRequest);
			 	
			} else if (intentRequest.getDialogState() == DialogState.IN_PROGRESS){
				return input.getResponseBuilder().addDelegateDirective(null).build();
			    // return a Dialog.Delegate directive with no updatedIntent property.
			} else if (intentRequest.getDialogState()==DialogState.COMPLETED){
			    return handleIntentAndGenerateResponse(input, intentRequest);

			}
		return null;
		
	}
	

	private Optional<Response> handleIntentAndGenerateResponse(HandlerInput input, IntentRequest intentRequest) {
		
		// Dialog is now complete and all required slots should be filled,
		// so call your normal intent handler. 
		Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
		
	    
		StringBuilder RESPONSE=new StringBuilder();
		StringBuilder DELTA_CHANGE=new StringBuilder();
		StringBuilder CARD_RESPONSE=new StringBuilder();
		Map<String,Slot> slotMap=intentRequest.getIntent().getSlots();
		String crypto="";
		String fiat="";
		
		if(slotMap.get("crypto").getResolutions().getResolutionsPerAuthority().get(0).getStatus().getCode()==StatusCode.ER_SUCCESS_MATCH)
			crypto=slotMap.get("crypto").getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName();
		if(slotMap.get("fiat").getResolutions().getResolutionsPerAuthority().get(0).getStatus().getCode()==StatusCode.ER_SUCCESS_MATCH)
			fiat=slotMap.get("fiat").getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName();
		
		PricesDto prices=null;
		if(Constants.MAPPING.get(crypto.toLowerCase())!=null) {
			prices=getPrice(Constants.MAPPING.get(crypto.toLowerCase()));
		
		
		//Getting Price Diff from last session
		
		if(persistentAttributes.get("PRICE"+Constants.MAPPING.get(crypto.toLowerCase()))!=null) {
			double change=getPriceDiff(Double.parseDouble((String)persistentAttributes.get("PRICE"+Constants.MAPPING.get(crypto.toLowerCase()))),prices.getDollarPrice());
			if(change>0.0) {
				DELTA_CHANGE.append(" Up ").append(change).append(" percent since you last asked.");
			}
			else if(change<0.0) {
				DELTA_CHANGE.append(" Down ").append(Math.abs(change)).append(" percent since you last asked.");
			}
			}
		}
		
		RESPONSE.append("The current "+crypto+" price is ");
		
		if(prices!=null) {
			
		
		switch(fiat.toLowerCase()) {
		case "rupee":RESPONSE.append((int)(prices.getDollarPrice()*getUSDtoINRConversion())+" in rupees.").append(DELTA_CHANGE.toString());
					break;
		case "dollar":RESPONSE.append(prices.getDollarPrice().intValue()+" in dollars.").append(DELTA_CHANGE.toString());
					break;
		case "euro":RESPONSE.append(prices.getEuroPrice().intValue()+" in euros.").append(DELTA_CHANGE.toString());
					break;
		default	:RESPONSE.setLength(0);
				 RESPONSE.append("Sorry, This currency is currently not supported.");
				 break;
					
				}
		}
		else {
			RESPONSE.setLength(0);
			 RESPONSE.append("Sorry, This crypto currency is currently not supported.");
		}
		CARD_RESPONSE.append(RESPONSE.toString());
		
		RESPONSE.append(" Is there anything else i can help you with ?");
		
//		persistentAttributes.put("PRICE", prices.getDollarPrice().intValue());
		if(prices!=null) {
		persistentAttributes.put("PRICE"+Constants.MAPPING.get(crypto.toLowerCase()), prices.getDollarPrice().toString());
		}
		input.getAttributesManager().savePersistentAttributes();
		
		return input.getResponseBuilder()
		        .withSpeech(RESPONSE.toString())
		        .withSimpleCard("Price", CARD_RESPONSE.toString())
		        .withReprompt(Constants.HELP_MESSAGE)
		        .withShouldEndSession(false)
		        .build();
	}
	
	
	
	
	
	
	
	
	private double getPriceDiff(double priceInDB, double currentPrice) {
		
		double change= (((currentPrice-priceInDB)*100)/priceInDB);
		double roundOff = (double) Math.round(change * 100) / 100;
		return roundOff;
		
		
	}

	//********************************************API CALLS*************************************
	private static Double getUSDtoINRConversion()  {
		
		Double USDToINR=null;
		
	    try(CloseableHttpClient httpClient = HttpClientBuilder.create().build())
	    {  
	        HttpGet getRequest = new HttpGet(Constants.CONVERSION_API);
	        HttpResponse response = httpClient.execute(getRequest);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != 200)
	        {
	            throw new RuntimeException("Failed with HTTP error code : " + statusCode);
	        }
	        HttpEntity httpEntity = response.getEntity();
	        String apiOutput = EntityUtils.toString(httpEntity);
	        System.out.println(apiOutput);  
	        JSONParser parser = new JSONParser();
			JSONObject responseObject;
			try {
				responseObject = (JSONObject) parser.parse(apiOutput);
				if (responseObject != null) {
					USDToINR=(Double)responseObject.get("USD_INR");
					}
				} 
			catch (ParseException e) {
				e.printStackTrace();
			}
	        
	    } catch (Exception e) {
			e.printStackTrace();
		}
		return USDToINR;
		
	}
	
	private static PricesDto getPrice(String cryptoCurrencyCode) {
		
		String queryString=String.format(Constants.PRICE_API,cryptoCurrencyCode);
		InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        String text = "";
        try {
            String line;
            URL url = new URL(queryString);
            inputStream = new InputStreamReader(url.openStream(), Charset.forName("US-ASCII"));
            bufferedReader = new BufferedReader(inputStream);
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            text = builder.toString();
        } catch (IOException e) {
            // reset text variable to a blank string
            text = "";
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(bufferedReader);
        }
        if(text.length()==0) {
        	return null;
        }
        else
        return parseJson(text);
	}

	private static PricesDto parseJson(String text) {

			PricesDto prices=null;
			JSONParser parser = new JSONParser();
			JSONObject responseObject;
			try {
				responseObject = (JSONObject) parser.parse(text);
				if (responseObject != null) {
					Double dollarPrice=(Double)responseObject.get("USD");
					Double euroPrice=(Double)responseObject.get("EUR");
					Double rupeePrice=(Double)responseObject.get("INR");
					prices=new PricesDto();
					prices.setDollarPrice(dollarPrice);
					prices.setEuroPrice(euroPrice);
					prices.setRupeePrice(rupeePrice);
					}
				} 
			catch (ParseException e) {
				e.printStackTrace();
			}
			return prices;
	}
	
	public static void main(String[] args) {
		getPrice("BTC");
		getUSDtoINRConversion();
		
	}

}
