import com.bloomberglp.blpapi.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BloombergDataFetcher {
    public static void main(String[] args) throws Exception {
        // Define Bloomberg API session
        SessionOptions sessionOptions = new SessionOptions();
        sessionOptions.setServerHost("localhost");
        sessionOptions.setServerPort(8194);
        
        // Create session
        Session session = new Session(sessionOptions);
        if (!session.start()) {
            System.out.println("Failed to start session.");
            return;
        }
        
        // Create service for Bloomberg data
        if (!session.openService("//blp/refdata")) {
            System.out.println("Failed to open service.");
            return;
        }

        // Request stock data (for example, Apple stock)
        Request request = session.getService("//blp/refdata").createRequest("HistoricalDataRequest");
        request.getElement("securities").appendValue("AAPL US Equity");
        request.getElement("fields").appendValue("PX_BID");
        request.getElement("fields").appendValue("PX_ASK");
        request.getElement("fields").appendValue("LAST_PRICE");
        request.set("startDate", "20230101");
        request.set("endDate", "20230115");
        request.set("periodicitySelection", "DAILY");

        // Send request
        session.sendRequest(request, null);

        // Process response
        Event event;
        while (true) {
            event = session.nextEvent();
            if (event.eventType() == Event.EventType.RESPONSE) {
                break;
            }
        }

        // Parse the data and save it to Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Data");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Bid");
        headerRow.createCell(2).setCellValue("Ask");
        headerRow.createCell(3).setCellValue("Last Price");

        // Fill in the data
        int rowNum = 1;
        for (Message message : event) {
            Element securityData = message.getElement("securityData");
            for (int i = 0; i < securityData.numValues(); i++) {
                Element fieldData = securityData.getValueAsElement(i).getElement("fieldData");
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(fieldData.getElementAsString("date"));
                row.createCell(1).setCellValue(fieldData.getElementAsFloat("PX_BID"));
                row.createCell(2).setCellValue(fieldData.getElementAsFloat("PX_ASK"));
                row.createCell(3).setCellValue(fieldData.getElementAsFloat("LAST_PRICE"));
            }
        }

        // Write Excel file
        try (FileOutputStream fileOut = new FileOutputStream("stock_data.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
