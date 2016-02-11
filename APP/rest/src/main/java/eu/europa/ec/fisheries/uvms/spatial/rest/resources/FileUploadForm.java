package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

public class FileUploadForm {

    private int crsCode;
    @NotEmpty
    private String areaType;
    @NotEmpty
    private byte[] data;

    public FileUploadForm() {
    }

    public int getCrsCode() {
        return crsCode;
    }

    @FormParam("crs")
    public void setCrsCode(int crsCode) {
        this.crsCode = crsCode;
    }

    public String getAreaType() {
        return areaType;
    }

    @FormParam("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public byte[] getData() {
        return data;
    }

    @FormParam("uploadedFile")
    @PartType("application/octet-stream")
    public void setData(byte[] data) {
        this.data = data;
    }
}