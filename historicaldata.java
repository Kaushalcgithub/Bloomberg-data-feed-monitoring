import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.Name;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapiexamples.demoapps.util.RequestOptions;

public class HistoricalDataRequests {
    private static final Name SECURITIES = Name.getName("securities");
    private static final Name PERIODICITY_ADJUSTMENT = Name.getName("periodicityAdjustment");
    private static final Name PERIODICITY_SELECTION = Name.getName("periodicitySelection");
    private static final Name START_DATE = Name.getName("startDate");
    private static final Name END_DATE = Name.getName("endDate");
    private static final Name MAX_DATA_POINTS = Name.getName("maxDataPoints");
    private static final Name RETURN_EIDS = Name.getName("returnEids");
    private static final Name FIELDS = Name.getName("fields");

    public static Request createRequest(Service service, RequestOptions options) {
        Request request = service.createRequest("HistoricalDataRequest");

        Element securitiesElement = request.getElement(SECURITIES);
        for (String security : options.securities) {
            securitiesElement.appendValue(security);
        }

        Element fieldsElement = request.getElement(FIELDS);
        for (String field : options.fields) {
            fieldsElement.appendValue(field);
        }

        request.set(PERIODICITY_ADJUSTMENT, "ACTUAL");
        request.set(PERIODICITY_SELECTION, "MONTHLY");
        request.set(START_DATE, "20200101");
        request.set(END_DATE, "20201231");
        request.set(MAX_DATA_POINTS, 100);
        request.set(RETURN_EIDS, true);

        return request;
    }

    public static void processResponseEvent(Event event) {
        for (Message msg : event) {
            System.out.println("Received response to request " + msg.getRequestId());
            System.out.println(msg);
        }
    }
}
