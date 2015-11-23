package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.operation.Operation;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.dao.BaseDAOTest;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
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

    protected static final Operation INSERT_REFERENCE_DATA = sequenceOf();

    @Override
    protected String getSchema() {
        return "spatial";
    }

    @Override
    protected String getPersistenceUnitName() {
        return "testPU";
    }
}
