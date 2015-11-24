package eu.europa.ec.fisheries.uvms.spatial.dao;


import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.Assert.assertEquals;

public class ReportConnectSpatialDaoTest extends BaseSpatialDaoTest {

    private ReportConnectSpatialDao dao = new ReportConnectSpatialDao(em);
    @Before
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldCreateReportConnect(){

        ReportConnectSpatialEntity build = ReportConnectSpatialEntity.builder()
                .appVersion("2.6")
                .displayFormatType(CoordinatesFormat.DDM)
                .scaleBarType(ScaleBarUnits.IMPERIAL)
                .mapCenter("mandatory")
                .build();

        assertEquals(CoordinatesFormat.DDM, dao.createEntity(build).getDisplayFormatType());
        assertEquals(ScaleBarUnits.IMPERIAL, dao.createEntity(build).getScaleBarType());

    }
}
