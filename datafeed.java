import com.bloomberglp.blpapi.Datetime;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.Name;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapiexamples.demoapps.util.RequestOptions;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class IntradayTickRequests {
    private static final Name TICK_DATA = new Name("tickData");
    private static final Name CONDITION_CODES = new Name("conditionCodes");
    private static final Name SIZE = new Name("size");
    private static final Name TIME = new Name("time");
    private static final Name TYPE = new Name("type");
    private static final Name VALUE = new Name("value");
    private static final Name RESPONSE_ERROR = new Name("responseError");
    private static final Name SECURITY = Name.getName("security");
    private static final Name EVENT_TYPES = Name.getName("eventTypes");
    private static final Name START_DATE_TIME = Name.getName("startDateTime");
    private static final Name END_DATE_TIME = Name.getName("endDateTime");
    private static final Name INCLUDE_CONDITION_CODES = Name.getName("includeConditionCodes");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy k:mm");
    private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat();

    public static Request createRequest(Service service, RequestOptions options) {
        Request request = service.createRequest("IntradayTickRequest");

        request.set(SECURITY, options.securities.get(0));

        // Add fields to request
        Element eventTypes = request.getElement(EVENT_TYPES);
        for (String event : options.eventTypes) {
            eventTypes.appendValue(event);
        }

        // All times are in GMT
        request.set(START_DATE_TIME, options.startDateTime);
        request.set(END_DATE_TIME, options.endDateTime);

        if (options.includeConditionCodes) {
            request.set(INCLUDE_CONDITION_CODES, true);
        }

        return request;
    }

    public static void processResponseEvent(Event response) {
        for (Message msg : response) {
            System.out.println("Received response to request " + msg.getRequestId());

            if (msg.hasElement(RESPONSE_ERROR)) {
                Element responseError = msg.getElement(RESPONSE_ERROR);
                System.out.println("REQUEST FAILED: " + responseError);
                continue;
            }

            Element data = msg.getElement(TICK_DATA).getElement(TICK_DATA);
            int numItems = data.numValues();
            System.out.println("TIME\t\t\tTYPE\t\tVALUE\t\tSIZE\tCONDITION_CODES");
            System.out.println("----\t\t\t----\t\t-----\t\t----\t--");
            for (int i = 0; i < numItems; ++i) {
                Element item = data.getValueAsElement(i);
                Datetime time = item.getElementAsDate(TIME);
                String type = item.getElementAsString(TYPE);
                double value = item.getElementAsFloat64(VALUE);
                int size = item.getElementAsInt32(SIZE);
                String conditionCodes = "";
                if (item.hasElement(CONDITION_CODES)) {
                    conditionCodes = item.getElementAsString(CONDITION_CODES);
                }

                System.out.println(
                        DATE_FORMAT.format(time.calendar().getTime())
                                + "\t"
                                + type
                                + "\t"
                                + DECIMAL_FORMAT.format(value)
                                + "\t\t"
                                + DECIMAL_FORMAT.format(size)
                                + "\t"
                                + conditionCodes);
            }
        }
    }
}
