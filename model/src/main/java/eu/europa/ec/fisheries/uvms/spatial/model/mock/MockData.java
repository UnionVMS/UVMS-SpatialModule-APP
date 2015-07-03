/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.model.mock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jojoha
 */
public class MockData {

    /**
     * Get mocked data sigle object
     *
     * @param id
     * @return
     */
  /*  public static MovementBaseType getDto(Long id) {
        MovementBaseType dto = new MovementBaseType();
        dto.setId(id.toString());
        dto.setCalculatedSpeed(BigDecimal.valueOf(3.2 + id));
        dto.setCarrier(getVesselId());
        dto.setCourse(12);
        dto.setMeasuredSpeed(BigDecimal.valueOf(0.2 + id));
        dto.setMessageType(MessageType.ENT);
        dto.setMobileTerminal(getMobTermId());
        dto.setPosition(getMovementPoint());
        dto.setSource(MovementSourceType.INMARSAT_C);
        dto.setStatus("God like");
        return dto;
    }*/

    /**
     *
     * @return
     */
  /*  public static CarrierId getVesselId() {
        CarrierId id = new CarrierId();
        id.setCarrierType(CarrierType.VESSEL);
        id.setIdType(CarrierIdType.GUID);
        //id.setValue(UUID.randomUUID().toString());
        id.setValue("f0dad9f1-a59c-44a7-a534-aa7c1e03fe45");
        return id;
    }

    *//**
     *
     * @return
     *//*
    public static MovementPoint getMovementPoint() {
        MovementPoint point = new MovementPoint();
        point.setLatitude(12);
        point.setLongitude(69);
        return point;
    }

    *//**
     *
     * @return
     *//*
    public static MobileTerminalId getMobTermId() {
        MobileTerminalId id = new MobileTerminalId();
        id.setId("ABC-80+");
        return id;
    }

    *//**
     *
     * @return
     *//*
    public static MovementListQuery getQuery() {
        MovementListQuery query = new MovementListQuery();
        query.getMovementSearchCriteria().add(getListCtieria());
        query.setPagination(getListPagination());
        return query;
    }

    *//**
     *
     * @return
     *//*
    public static ListPagination getListPagination() {
        ListPagination criteria = new ListPagination();
        criteria.setListSize(BigInteger.valueOf(10L));
        criteria.setPage(BigInteger.valueOf(1L));
        return criteria;
    }

    *//**
     *
     * @return
     *//*
    public static ListCriteria getListCtieria() {
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.IMO);
        criteria.setValue("value");
        return criteria;
    }

    *//**
     * Get mocked data as a list
     *
     * @param amount
     * @return
     *//*
    public static List<MovementBaseType> getDtoList(Integer amount) {
        List<MovementBaseType> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return dtoList;
    }
*/
}
