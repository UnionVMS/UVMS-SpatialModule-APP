package eu.europa.ec.fisheries.uvms.spatial.service.bean.config;

import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.SysConfig;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Georgi on 23-Nov-15.
 */
@Local
@Stateless
@Slf4j // FIXME Dead Code?
public class SystemConfigServiceBean {


    @EJB
    private SpatialRepository repository;


    public List<SysConfig> getSystemConfigs() {
        return SysConfigMapper.INSTANCE.fromEntityToDTO(repository.findSystemConfigs());
    }


    @Transactional
    public void updateSystemConfigs(List<SysConfig> sysConfigs) {
        repository.updateSystemConfigs(SysConfigMapper.INSTANCE.fromDTOToEntity(sysConfigs));
    }
}
