package com.microservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.bean.CurrencyConversion;
import com.microservice.bean.CurrencyExchange;
import com.microservice.proxy.CurrencyExchangeServiceProxy;




@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);



	//private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);


	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion currencyCalculator(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity) {

		logger.info("currencyCalculator called with {} to {}", from, to);
		
		//CHANGE-KUBERNETES
	logger.info("calculateCurrencyConversion called with {} to {} with {}", from, to, quantity);
		
		HashMap<String, String> uriVariable = new HashMap<>();

		uriVariable.put("from", from);
		uriVariable.put("to", to);

		ResponseEntity<CurrencyConversion> forEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariable);
		CurrencyConversion currencyConversion = forEntity.getBody();

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getPort());
	}


	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion currencyCalculatorFeign(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity) {
		System.out.println("-------------------------- Hello currencyCalculatorFeign---------------------");
		
		//CHANGE-KUBERNETES
		logger.info("calculateCurrencyConversion called with {} to {} with {}", from, to, quantity);
		
		CurrencyConversion currencyConversion = proxy.retriveCurrencyExchnageValue(from, to);

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getPort());
	}


	@PostMapping("/currency-conversion-feign/save")
	public CurrencyConversion saveCurrencyExchangeValue(@RequestBody CurrencyConversion currencyConversion) {

		logger.info(" ID---" +currencyConversion.getId()); 
		logger.info(" ID---" +currencyConversion.getFrom()); 
		logger.info(" ID---" +currencyConversion.getTo()); 
		logger.info(" ID---" +currencyConversion.getConversionMultiple()); 

		CurrencyConversion currencyConversionSaved = proxy.saveCurrencyExchangeValue(currencyConversion);
		
		currencyConversionSaved.setQuantity(BigDecimal.ZERO);
		currencyConversionSaved.setTotalCalculatedAmount(BigDecimal.ZERO);

		return currencyConversionSaved;

	}
	
	@GetMapping("currency-conversion-feign/get")
	public List<CurrencyExchange> getDataValues(){
		
		List<CurrencyExchange> allCurrencyExchangeValue = proxy.getAllCurrencyExchangeValue();
		
		return allCurrencyExchangeValue;
		
	}
	
}
