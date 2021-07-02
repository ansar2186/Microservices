
package com.microservice.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservice.bean.CurrencyConversion;
import com.microservice.bean.CurrencyExchange;



//@FeignClient(name = "currency-exchange",url = "localhost:8000")

//we use this before kubernetes.
//@FeignClient(name = "currency-exchange")

//kubernetes changes
//@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")

@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_SERVICE_URI:http://localhost}:8000")
public interface CurrencyExchangeServiceProxy {

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retriveCurrencyExchnageValue(@PathVariable("from") String from,

			@PathVariable("to") String to);

	@PostMapping("/currency-exchange/save")
	public CurrencyConversion saveCurrencyExchangeValue(@RequestBody CurrencyConversion currencyExchange);

	@GetMapping("/currency-exchange/getAllCurrencyExchangeValue")
	public List<CurrencyExchange> getAllCurrencyExchangeValue();

}
