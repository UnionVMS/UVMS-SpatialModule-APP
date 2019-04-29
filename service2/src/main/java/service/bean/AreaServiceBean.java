/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package service.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import service.dao.AreaLocationTypesDao2;
import service.dao.PortAreaDao2;
import service.dao.PortDao2;
import service.entity.BaseAreaEntity2;
import service.entity.PortAreaEntity2;
import service.entity.PortEntity2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.awt.*;
import java.util.List;

@Stateless
public class AreaServiceBean {

    private static final String MULTIPOINT = "MULTIPOINT";

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    @Inject
    private PortDao2 portDao;

    @Inject
    private PortAreaDao2 portAreaDao;

    @Inject
    private AreaLocationTypesDao2 areaLocationTypes;



    @EJB
    private AreaTypeNamesService areaTypeService;


    @EJB
    private SpatialRepository repository;

    @EJB
    private PropertiesBean properties;



    public List<PortEntity2> getPortsByAreaCodes(List<String> codes){
        return portDao.getPortsByAreaCodes(codes);
    }

    public List<PortAreaEntity2> getPortAreasByPoint(Double lat,  Double lon){
        try {
            Point point = (Point) GeometryUtils.toGeographic(lat, lon, 4326);
            return portAreaDao.getPortAreasByPoint(point);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BaseAreaEntity2> getAreasByPoint(Double lat, Double lon){

        try {
            Point point = (Point) GeometryUtils.toGeographic(lat, lon, 4326);
            return areaLocationTypes.getAreasByPoint(point);




        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

    }

    public PortEntity2 findClosestPortByPosition(){

        return null;

    }





}