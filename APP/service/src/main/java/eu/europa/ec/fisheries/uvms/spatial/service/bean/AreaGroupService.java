package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupDto;

import java.util.List;

/**
 * Created by padhyad on 1/25/2016.
 */
public interface AreaGroupService {

    List<AreaGroupDto> getAreaGroups(String userName);

    void deleteAreaGroup(Long groupId);
}
