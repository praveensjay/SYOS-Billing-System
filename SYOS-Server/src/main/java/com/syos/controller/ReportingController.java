package com.syos.controller;

import com.syos.service.ReportingManager;
import com.syos.service.ReportingManagerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/reports")
public class ReportingController {

    private final ReportingManager reportingManager;

    public ReportingController() {
        this.reportingManager = new ReportingManagerImpl();
    }

    @GET
    @Path("/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalSalesReport(@QueryParam("date") String date) {
        try {
            LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            List<Map<String, Object>> reportData = reportingManager.generateTotalSalesReport(reportDate);

            if (reportData == null || reportData.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("{\"message\": \"No sales data available for the selected date.\"}")
                        .build();
            }

            return Response.ok(reportData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate Total Sales Report: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/reshelving")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReshelvingReport(@QueryParam("date") String date) {
        try {
            LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            List<Map<String, Object>> reportData = reportingManager.generateReshelvingReport(reportDate);

            if (reportData == null || reportData.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("{\"message\": \"No reshelving data available for the selected date.\"}")
                        .build();
            }

            return Response.ok(reportData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate Reshelving Report: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/reorder-level")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReorderLevelReport(@QueryParam("date") String date) {
        try {
            LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            List<Map<String, Object>> reportData = reportingManager.generateReorderLevelReport(reportDate);

            if (reportData == null || reportData.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("{\"message\": \"No reorder level data available for the selected date.\"}")
                        .build();
            }

            return Response.ok(reportData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate Reorder Level Report: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/stock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockReport(@QueryParam("date") String date) {
        try {
            LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            List<Map<String, Object>> reportData = reportingManager.generateStockReport(reportDate);

            if (reportData == null || reportData.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("{\"message\": \"No stock data available for the selected date.\"}")
                        .build();
            }

            return Response.ok(reportData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate Stock Report: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/bill")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillReport(@QueryParam("date") String date) {
        try {
            LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            List<Map<String, Object>> reportData = reportingManager.generateBillReport(reportDate);

            if (reportData == null || reportData.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("{\"message\": \"No bill data available for the selected date.\"}")
                        .build();
            }

            return Response.ok(reportData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate Bill Report: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
