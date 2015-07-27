package eu.europa.ec.fisheries.uvms.spatial.message.constants;

public class MessageConstants {

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";

    public static final String COMPONENT_MESSAGE_IN_QUEUE = "UVMSMovementEvent";
    public static final String COMPONENT_MESSAGE_IN_QUEUE_NAME = "UVMSMovementEvent";
    public static final String COMPONENT_RESPONSE_QUEUE = "java:/jms/queue/UVMSMovement";
    public static final String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSMovementModel";
    public static final String QUEUE_MODULE_VESSEL = "java:/jms/queue/UVMSVesselEvent";

    public static final String QUEUE_MODULE_MOVEMENT = "java:/jms/queue/UVMSMovementEvent";
}
