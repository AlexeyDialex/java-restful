package tz;

import java.util.List;

public class TpResponse {
    private String name;
    private Double averageRate;
    private List<Double> rates;

    public TpResponse(String name, Double averageRate, List<Double> rates) {
        this.name = name;
        this.averageRate = averageRate;
        this.rates = rates;
    }

    public String getName() {
        return name;
    }

    public Double getAverageRate() {
        return averageRate;
    }
    public List<Double> getRates() {
        return rates;
    }

}
