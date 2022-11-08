package com.leonardo.bungee.lib;

import io.ipdata.client.Ipdata;
import io.ipdata.client.error.IpdataException;
import io.ipdata.client.service.IpdataService;
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Data
public class GeoIP {

    private static GeoIP instance;
    private IPInfo ipInfo;
    private IpdataService ipdataService;

    @SneakyThrows
    private GeoIP() {
        ipInfo = IPInfo.builder().setToken("95794de92053b8").build();
        ipdataService = Ipdata.builder().url(new URL("https://api.ipdata.co"))
                .withCache()
                .timeout(30, TimeUnit.MINUTES)
                .maxSize(500)
                .registerCacheConfig()
                .key("e82a3f037777065d116e37c24f6671f770a0c51b2341d82cdd897a4b")
                .get();
    }

    public static GeoIP get() {
        return instance == null ? instance = new GeoIP() : instance;
    }

    public String getCountry(String addressIP) {
        try {
            return getIpInfo().lookupIP(addressIP).getCountryName();
        } catch (ErrorResponseException | RateLimitedException e) {
            try {
                return getIpdataService().getCountryName(addressIP);
            } catch (IpdataException ignored) {
            }
        }
        return "";
    }
}
