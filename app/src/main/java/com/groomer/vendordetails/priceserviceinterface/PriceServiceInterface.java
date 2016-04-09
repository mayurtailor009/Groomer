package com.groomer.vendordetails.priceserviceinterface;

import com.groomer.model.ServiceDTO;

import java.util.List;

/**
 * Created by Deepak Singh on 09-Apr-16.
 */
public interface PriceServiceInterface {

    void getPriceSum(String sum);
    void getServiceCount(String serviceCount);
    void getSelectedServiceList(List<ServiceDTO> serviceDTOList);
}
