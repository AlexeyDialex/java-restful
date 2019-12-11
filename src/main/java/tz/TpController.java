package tz;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class TpController {

    @Cacheable("simulate")
    @RequestMapping("/rates")
    public TpResponse getRates(@RequestParam(value = "type") String type
            , @RequestParam(value = "currency", required = false) String currency) {
        //currency - для выборки валют, содержащих в названии заданное слово, например Dollar, Peso или Franc

        final TpResponse tpResponse;
        String bitpayUrl = "https://bitpay.com/rates/"+ type;
        if (type.equalsIgnoreCase("BTC") || type.equalsIgnoreCase("BCH")) {

            ResponseEntity<BitpayResponse> responseEntity = new RestTemplate().exchange(
                    bitpayUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<BitpayResponse>() {
                    }
            );

            //берём массив data из полученного json
            List<BitpayRate> bitpayResponseData = responseEntity.getBody().getData();
            List<Double> rates = new ArrayList<>();
            Double averageRate = Double.valueOf(0);
            for (BitpayRate bitpayRate : bitpayResponseData) {
                if (currency == null || bitpayRate.getName().matches("(?i).+\\s" + currency)) {
                    System.out.println(bitpayRate.getName());
                    rates.add(bitpayRate.getRate());
                    averageRate += bitpayRate.getRate();
                }
            }
            averageRate = averageRate / bitpayResponseData.size();
            if (rates.isEmpty()) {
                throw new CurrencyNotFoundException(currency);
            }
            simulateSlowService(); //имитируем длительную загрузку для проверки кеширования
            String name = "response in " + type;
            tpResponse = new TpResponse(name, averageRate, rates);
        } else {
            throw new TypeNotFoundException(type);
        }

        return tpResponse;
    }

    @GetMapping("{path}")
    void unexpectedPath(@PathVariable String path){
        System.out.println(path);
        if (!path.equalsIgnoreCase("rates"))
            //выбрасываем ошибку для всех вариантов get запросов начинающихся не с rates
            throw new UnexpectedPathException(path);
    }
    private void simulateSlowService() {
        try {
            long time = (long) (5000L);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


}
