package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by padhyad on 1/25/2016.
 */
@Stateless
@Local(AreaGroupService.class)
@Transactional
public class AreaGroupServiceBean implements AreaGroupService {

    @EJB
    private SpatialRepository repository;

    @EJB
    private SqlPropertyHolder sqlPropertyHolder;

    private static String GIDS = "{gids}";
    private static String FIND_AREA_GROUP_BY_GIDS = "sql.areasByGidList";
    private static String TABLE_NAME = "{tableName}";

    @Override
    @SneakyThrows
    public List<AreaGroupDto> getAreaGroups(String userName) {
        List<AreaGroupDto> areaGroupDtos = new ArrayList<AreaGroupDto>();
       /* List<AreaGroupEntity> areaGroups = repository.getAreaGroups(userName);
        if (areaGroups == null || areaGroups.isEmpty()) {
            return areaGroupDtos;
        }
        for (AreaGroupEntity areaGroupEntity : areaGroups) {
            Set<AreaConnectGroupEntity> areaConnectGroups = areaGroupEntity.getAreaConnectGroups();
            Map<AreaConnectGroupEntity, List<String>> areaGroupMap = getAreaGroupMap(areaConnectGroups);
            List<AreaGroupTypeDto> areaGroupTypeDtos = getAreaGroupTypes(areaGroupMap);
            areaGroupDtos.add(new AreaGroupDto(areaGroupEntity.getId(), areaGroupEntity.getGroupName(), areaGroupEntity.getDescription(), areaGroupTypeDtos));
        }*/
        return areaGroupDtos;
    }

    @Override
    @SneakyThrows
    public void deleteAreaGroup(Long groupId) {
       /* AreaGroupEntity areaGroupEntity = repository.getAreaGroup(groupId);
        if (areaGroupEntity != null) {
            repository.deleteEntity(areaGroupEntity);
        }*/
    }

   /*   private List<AreaGroupTypeDto> getAreaGroupTypes(Map<AreaConnectGroupEntity, List<String>> areaGroupMap) {
        List<AreaGroupTypeDto> areaGroupTypeDtos = new ArrayList<>();
      for (Map.Entry<AreaConnectGroupEntity, List<String>> entry : areaGroupMap.entrySet()) {
            String tableName = entry.getKey().getAreaLocationTypes().getAreaDbTable();
            String gids = getIds(entry.getValue());
            if (gids == null) {
                continue;
            }
            String query = getQueryString(tableName, gids);
            List<AreaGroupTypeDto> tempAreaGroupTypeDtos = repository.getAreasByGid(query);
            for (AreaGroupTypeDto areaGroupTypeDto : tempAreaGroupTypeDtos) {
                areaGroupTypeDto.setAreaType(entry.getKey().getAreaLocationTypes().getTypeName());
                areaGroupTypeDtos.add(areaGroupTypeDto);
            }
        }
        return areaGroupTypeDtos;
    }

    private Map<AreaConnectGroupEntity, List<String>> getAreaGroupMap(Set<AreaConnectGroupEntity> areaConnectGroups) {
        Map<AreaConnectGroupEntity, List<String>> areaGroupMap = new HashMap<AreaConnectGroupEntity, List<String>>();
        for (AreaConnectGroupEntity areaConnectGroupEntity : areaConnectGroups) {
            if(areaGroupMap.get(areaConnectGroupEntity.getAreaLocationTypes().getAreaDbTable()) != null) {
                List<String> areaList = areaGroupMap.get(areaConnectGroupEntity.getAreaLocationTypes().getAreaDbTable());
                areaList.add(String.valueOf(areaConnectGroupEntity.getAreaId()));
                areaGroupMap.put(areaConnectGroupEntity, areaList);
            } else {
                List<String> areaList = Arrays.asList(String.valueOf(areaConnectGroupEntity.getAreaId()));
                areaGroupMap.put(areaConnectGroupEntity, areaList);
            }
        }
        return areaGroupMap;
    }
    */
    private String getIds(List<String> ids) {
        StringBuilder gids = new StringBuilder();
        Iterator<String> iterator = ids.iterator();
        while(iterator.hasNext()) {
            gids.append(iterator.next()).append(",");
        }
        return gids.substring(0, gids.lastIndexOf(","));
    }

    private String getQueryString(String tableName, String gids) {
        String queryString = sqlPropertyHolder.getProperty(FIND_AREA_GROUP_BY_GIDS);
        return queryString.replace(TABLE_NAME, tableName).replace(GIDS, gids);
    }
}
