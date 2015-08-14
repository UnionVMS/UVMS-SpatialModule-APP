package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

public enum ResponseCode implements RestResponseCode {

    OK("200"),
    ERROR("500");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    public static ResponseCode map(String errorCode) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.getCode().equals(errorCode)) {
                return responseCode;
            }
        }
        return ERROR;
    }

    @Override
    public String getCode() {
        return code;
    }
}
