package eu.europa.ec.fisheries.uvms.spatial.dao;

public class OracleGis extends AbstractGisFunction {

    @Override
    public String stIntersects(String wkt, Integer crs) {

        return null;
    }

    @Override
    public String stIntersects(Double latitude, Double longitude, Integer crs) {

        return null;
    }

    @Override
    public String stDistance(Double latitude, Double longitude, Integer crs) {

        return null;
    }

    @Override
    public String castAsUnlimitedLength() {

        return "CLOB";
    }

    @Override
    public String toWkt(String value) {

        return null;
    }

    @Override
    public String limit(int i) {

        return "ROWNUM <= " + i;
    }
}
