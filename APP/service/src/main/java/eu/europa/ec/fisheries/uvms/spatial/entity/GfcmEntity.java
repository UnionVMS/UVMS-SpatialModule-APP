package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gfcm")
@NamedQueries({
        @NamedQuery(name = GfcmEntity.DISABLE_GFMC_AREAS, query = "UPDATE GfcmEntity SET enabled = 'N'"),
        @NamedQuery(name = GfcmEntity.GFMC_BY_INTERSECT,
                query = "FROM GfcmEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM, query = "FROM GfcmEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM_NAMES_BY_CODE, query = "From GfcmEntity where code in (SELECT distinct(code) from GfcmEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid)")
})
public class GfcmEntity extends BaseSpatialEntity {

    public static final String DISABLE_GFMC_AREAS = "gfmcEntity.disable";
    public static final String GFMC_BY_INTERSECT = "gfmcEntity.gfmcByIntersect";
    public static final String SEARCH_GFCM = "gfcmEntity.SearchgfcmByNameOrCode";
    public static final String SEARCH_GFCM_NAMES_BY_CODE = "gfcmEntity.searchNamesByCode";

	public GfcmEntity() {
        // why JPA why
    }

    public GfcmEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }
}
