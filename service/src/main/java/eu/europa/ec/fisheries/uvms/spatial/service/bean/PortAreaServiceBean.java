package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import lombok.extern.slf4j.Slf4j;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Local(PortAreaService.class)
@Transactional
@Slf4j
public class PortAreaServiceBean implements PortAreaService {
}
