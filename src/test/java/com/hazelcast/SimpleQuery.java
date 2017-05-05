package com.hazelcast;

/**
 * Created by gokhanoner on 04/05/2017.
 */

import com.google.cloud.bigquery.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sertugkaya on 04/05/17.
 */
public class SimpleQuery {
    public static void main(String[] args)
            throws TimeoutException, InterruptedException {


        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder("select *" +
                "   from callHome.lat_org").setUseLegacySql(false).build();

        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        queryJob = queryJob.waitFor();

        // Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }

        // Get the results.
        QueryResponse response = bigquery.getQueryResults(jobId);
        // [END run_query]

        // [START print_results]
        QueryResult result = response.getResult();

        List<String> lines = new ArrayList<>();
        while (result != null) {
            System.out.println(result.getTotalRows());

            result.getValues().forEach(values -> {
                try {
//                    TestObj ob = new TestObj();
//                    ob.setIp(values.get(0).getStringValue());
//                    ob.setVersion(values.get(1).getStringValue());
//                    long millis = TimeUnit.MILLISECONDS.convert(values.get(2)
//                            .getTimestampValue(), TimeUnit.MICROSECONDS);
//                    ob.setPingTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()));
//                    ob.setMachineId(values.get(3).getStringValue());
//                    ob.setEnterprise(values.get(4).getBooleanValue());
//                    ob.setLicense((String) values.get(5).getValue());
//                    ob.setClusterSize(values.get(8).getStringValue());
//                    ob.setCountry(values.get(12).getStringValue());
//                    ob.setLat(Double.valueOf(values.get(13).getStringValue()));
//                    ob.setLon(Double.valueOf(values.get(14).getStringValue()));
                    //ob.setUpTime(values.get(19).getLongValue());
                    lines.add(String.format("%s|%s|%s", values.get(0).getValue(), values.get(1).getValue(), values.get(2).getValue()));
                }catch (Exception e) {

                }

            });
            result = result.getNextPage();
        }

        try {
            Files.write(Paths.get("clients.txt"), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
