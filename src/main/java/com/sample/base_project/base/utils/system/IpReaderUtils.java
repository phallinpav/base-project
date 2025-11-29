package com.sample.base_project.base.utils.system;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Slf4j
public class IpReaderUtils {

    private static final String FILE = "ip/city.mmdb";
    private static final DatabaseReader reader;

    static {
        ClassLoader classLoader = IpReaderUtils.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(FILE)) {
            reader = new DatabaseReader.Builder(inputStream).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static @Nullable String getLocationByIp(@Nullable String ipAddress) {
        CityResponse response = getInfoByIp(ipAddress);
        if (response == null) {
            return null;
        }
        String country = getLocationNotNull(response.getCountry().getName());
        String city = getLocationNotNull(response.getCity().getName());
        return city.concat(", " + country);
    }


    public static @Nullable CityResponse getInfoByIp(@Nullable String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return null;
        }

        InetAddress inetAddress = null;
        CityResponse response;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
            if (inetAddress.isAnyLocalAddress() || inetAddress.isSiteLocalAddress()) {
                log.info(inetAddress.getHostAddress());
                return null;
            }
            response = reader.city(inetAddress);
        } catch (IOException | GeoIp2Exception e) {
            if (inetAddress != null) {
                log.info(inetAddress.getHostAddress());
                return null;
            }
            log.error("fail to read ip address", e);
            return null;
        }
        return response;
    }

    public static String getLocationNotNull(String value) {
        return (value != null ? value : "N/A");
    }
}
