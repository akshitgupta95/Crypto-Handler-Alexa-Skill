package com.gmail.akshitgupta1695.cryptoHandler.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
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
import com.amazon.ask.model.Response;
import com.gmail.akshitgupta1695.cryptoHandler.models.PricesDto;
import com.gmail.akshitgupta1695.cryptoHandler.util.Constants;


public class GetPriceHandler implements RequestHandler{

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("GetPriceIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		// TODO Auto-generated method stub
		return null;
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
