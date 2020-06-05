package eu.europa.ec.fisheries.uvms.spatial.rest.util;

public class AppError {

    public Integer code;
    public String  description;

    public AppError() {
        this.code = Integer.MIN_VALUE;
        this.description = "";

    }


    public AppError(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
