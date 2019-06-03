package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import static org.junit.Assert.assertEquals;

import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.PostGres;
import org.junit.Test;

public class PostgresTest {
    PostGres postgres = new PostGres();

    @Test
    public void testClosestPointToPoint(){
        String query = postgres.closestPointToPoint("PORT", "port", 41.0, -30.5, 10);
        String expectedQuery = "(SELECT 'PORT' as type, gid, code, name, geom, _ST_DistanceUnCached(geom, ST_GeomFromText(CAST ('POINT(-30.5 41.0)' AS TEXT), 4326),true) AS distance " +
                "FROM spatial.port " +
                "WHERE enabled = true AND ST_DWithin(ST_GeomFromText(CAST ('POINT(-30.5 41.0)' AS TEXT), 4326), geom, 22224) " +
                "ORDER BY ST_GeomFromText(CAST ('POINT(-30.5 41.0)' AS TEXT), 4326) <-> geom LIMIT 10 )";
        assertEquals(query, expectedQuery);
    }

    @Test
    public void testClosestAreaToPoint(){
        String query = postgres.closestAreaToPoint(0,"EEZ", "eez", 41.0, -30.5, 1);
        String expectedQuery = "(WITH candidates AS (SELECT cast('EEZ' as varchar) as type, gid, code, name, geom FROM spatial.eez WHERE enabled = true ORDER BY geom <-> ST_GeomFromText(CAST ('POINT(-30.5 41.0)' AS TEXT), 4326) LIMIT 10) SELECT type, gid, code, name, geom as closest, _ST_DistanceUnCached(geom, ST_GeomFromText(CAST ('POINT(-30.5 41.0)' AS TEXT), 4326), true) as dist FROM candidates ORDER BY dist LIMIT 1)";

        assertEquals(query, expectedQuery);
    }
}
