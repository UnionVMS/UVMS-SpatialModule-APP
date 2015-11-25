package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.operation.Operation;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.dao.BaseDAOTest;
import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public class BaseSpatialDaoTest extends BaseDAOTest {

    protected WKTReader wktReader = new WKTReader();
    protected WKTWriter wktWriter = new WKTWriter();

    protected GeometryFactory geometryFactory = new GeometryFactory();

    protected static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("spatial.countries"),
            deleteAllFrom("spatial.eez"),
            deleteAllFrom("spatial.projection"),
            deleteAllFrom("spatial.report_connect_service_areas"),
            deleteAllFrom("spatial.report_connect_spatial"),
            deleteAllFrom("spatial.system_configurations")
    );

    protected static final Operation INSERT_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.projection")
                    .columns("ID", "NAME", "SRS_CODE", "PROJ_DEF", "FORMATS", "UNITS", "WORLD")
                    .values(1L, "Spherical Mercator", 3857, "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs", "m", "m", 'Y')
                    .values(2L, "WGS 84", 4326, "+proj=longlat +datum=WGS84 +no_defs", "dd;dms;ddm;m", "degrees", 'Y')
                    .build()
    );

    @Override
    protected String getSchema() {
        return "spatial";
    }

    @Override
    protected String getPersistenceUnitName() {
        return "testPU";
    }
}
