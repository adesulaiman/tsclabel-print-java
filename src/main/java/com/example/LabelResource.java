package com.example;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@Path("/print")
public class LabelResource {

    private static final Logger LOG = Logger.getLogger(LabelResource.class);

    @Inject
    PrinterService printerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response printLabel(LabelRequest request) {
        boolean success = printerService.sendDoubleLabelToPrinter(
            request.getLeftBarcode(),
            request.getLeftValue(),
            request.getLeftInfo(),
            request.getRightBarcode(),
            request.getRightValue(),
            request.getRightInfo(),
            request.getCount()
        );
        if (success) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response printBatchLabels(List<Map<String, Object>> requests) {
        LOG.info("Received batch print request with " + requests.size() + " items");
        // First, expand items based on their printCount
        List<Map<String, Object>> expandedItems = new ArrayList<>();
        List<Map<String, Object>> listLabelRequest = new ArrayList<>();

        for (Map<String, Object> item : requests) {
            String barcode = (String) item.getOrDefault("barcode", "");
            String value = (String) item.getOrDefault("value", "");
            String info = (String) item.getOrDefault("info", "");
            int printCount = item.get("printCount") != null ? ((Number) item.get("printCount")).intValue() : 1;
            
            // Add this item 'printCount' times to the expanded list
            for (int j = 0; j < printCount; j++) {
                Map<String, Object> expandedItem = new HashMap<>();
                expandedItem.put("barcode", barcode);
                expandedItem.put("value", value);
                expandedItem.put("info", info);
                expandedItems.add(expandedItem);
            }
        }
        
        // Now pair up the expanded items for double label printing
        for (int i = 0; i < expandedItems.size(); i += 2) {
            Map<String, Object> left = expandedItems.get(i);
            Map<String, Object> right = (i + 1 < expandedItems.size()) ? expandedItems.get(i + 1) : null;
            
            String leftBarcode = (String) left.getOrDefault("barcode", "");
            String leftValue = (String) left.getOrDefault("value", "");
            String leftInfo = (String) left.getOrDefault("info", "");
            
            String rightBarcode = right != null ? (String) right.getOrDefault("barcode", "") : "";
            String rightValue = right != null ? (String) right.getOrDefault("value", "") : "";
            String rightInfo = right != null ? (String) right.getOrDefault("info", "") : "";
            
            // Map<String, Object> labelRequest = new HashMap<>();
            // labelRequest.put("leftBarcode", leftBarcode);
            // labelRequest.put("leftValue", leftValue);
            // labelRequest.put("leftInfo", leftInfo);
            // labelRequest.put("rightBarcode", rightBarcode);
            // labelRequest.put("rightValue", rightValue);
            // labelRequest.put("rightInfo", rightInfo);
            // labelRequest.put("count", 1);
            // listLabelRequest.add(labelRequest);

            boolean success = printerService.sendDoubleLabelToPrinter(
                leftBarcode, leftValue, leftInfo,
                rightBarcode, rightValue, rightInfo,
                1 // Each pair is printed once
            );
            if (!success) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.ok().build();
    }
}
