package tz;

import org.springframework.web.bind.annotation.*;

@RestController
public class TpController {

    @RequestMapping("/rates")
    public TpResponse getRates(@RequestParam(value = "type") String type
            , @RequestParam(value = "currency", required = false) String currency) {
        //currency - для выборки валют, содержащих в названии заданное слово, например Dollar, Peso или Franc
        final TpResponse tpResponse;

        if (type.equalsIgnoreCase("BTC") || type.equalsIgnoreCase("BCH")) {

            String url = "https://bitpay.com/rates/" + type;
            tpResponse = new BitpayService().getTpResponse(url, type, currency);
        } else {
            throw new TypeNotFoundException(type);
        }

        return tpResponse;
    }


}
