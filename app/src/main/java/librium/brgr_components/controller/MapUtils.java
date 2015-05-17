package librium.brgr_components.controller;

import android.location.Address;

/**
 * Created by Librium on 2015/5/17.
 */
public class MapUtils {
    public static String fromAddressToString(Address address){
        StringBuilder sb = new StringBuilder();
        if(null != address.getLocality())
            sb.append(address.getLocality()).append(", ");

        if(null != address.getPostalCode())
            sb.append(address.getPostalCode()).append(", ");

        if(null != address.getAdminArea())
            sb.append(address.getAdminArea ()).append(", ");

        if(null != address.getCountryName())
            sb.append(address.getCountryName()).append(",");

        return sb.toString();

    }
}
