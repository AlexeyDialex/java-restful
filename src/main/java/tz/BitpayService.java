package tz;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Cacheable("simulate")
public class BitpayService {

    public TpResponse getTpResponse(String url, String type, String currency) {

        ResponseEntity<BitpayResponse> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BitpayResponse>() {
                }
        );
        List<BitpayRate> bitpayResponse = responseEntity.getBody().getData();

        String name = "response in " + type;
        List<Double> rates = getRates(bitpayResponse, currency);
        Double averageRate = getAverageRate(rates);
        return new TpResponse(name, averageRate, rates);

    }

    private Double getAverageRate(List<Double> rates) {
        double sumRate = 0;
        for (Double rate : rates) {
            sumRate += rate;
        }
        Double averageRate = Double.valueOf(sumRate / rates.size());

        return averageRate;
    }

    private List<Double> getRates(List<BitpayRate> bitpayResponse, String currency) {

        List<Double> rates = new ArrayList<Double>();
        for (BitpayRate bitpayRate : bitpayResponse) {
            if (currency == null || bitpayRate.getName().matches("(?i).+\\s" + currency)) {
                rates.add(bitpayRate.getRate());
            }
        }
        if (rates.isEmpty()) {
            throw new CurrencyNotFoundException(currency);
        }
        return rates;
    }
}

