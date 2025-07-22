package com.socgen.riskweb.dao;

import com.socgen.riskweb.Model.InternalRegistrations;
import com.socgen.riskweb.Model.ResponseInternal;
import com.socgen.riskweb.Model.SubBookingEntity;
import com.socgen.riskweb.enums.AppQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Component
public class RiskwebClientDaoImpl implements RiskwebClientDao {

    private static final int BATCH_SIZE = 1000;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int LOG_INTERVAL = 50000;
    private static final Logger log = Logger.getLogger(RiskwebClientDaoImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AtomicInteger totalInserted = new AtomicInteger(0);

    @Transactional
    public void savePrimaryroleApi(ResponseInternal internalRatingsEventResponse, String tableName) {
        List<InternalRegistrations> internalRegistrationsList = internalRatingsEventResponse.getInternalRegistrations();
        String registrationType = internalRatingsEventResponse.getRegistrationType();
        String query = "EXTERNAL".equalsIgnoreCase(registrationType) ? 
            AppQueries.QRY_SAVE_EXTERNAL.value() : AppQueries.QRY_SAVE_PRIMARYROLE.value();
        String truncateQuery = "EXTERNAL".equalsIgnoreCase(registrationType) ? 
            AppQueries.QRY_EXTERNAL_TRUNCATE.value() : AppQueries.QRY_PRIMARYROLE_TRUNCATE.value();

        int totalSize = internalRegistrationsList.size();
        log.info("Total records to process for table " + tableName + " (type: " + registrationType + "): " + totalSize);

        try {
            this.jdbcTemplate.update(truncateQuery, new Object[]{});
            log.info("Truncated table " + tableName);
        } catch (Exception e) {
            Federal: log.severe("Failed to truncate table " + tableName + ": " + e.getMessage());
            throw e;
        }

        if (totalSize > 0) {
            long startTime = System.currentTimeMillis();
            ExecutorService executorService = (totalSize > BATCH_SIZE) ? 
                Executors.newFixedThreadPool(THREAD_POOL_SIZE) : Executors.newSingleThreadExecutor();
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int i = 0; i < totalSize; i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, totalSize);
                List<InternalRegistrations> batch = internalRegistrationsList.subList(i, end);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processBatch(batch, query), executorService);
                futures.add(future);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            executorService.shutdown();

            logProgress(totalSize, startTime);
            log.info("Completed processing for table " + tableName + ". Total inserted: " + totalInserted.get());
        }
    }

    private void logProgress(int totalSize, long startTimeMillis) {
        long elapsedTime = System.currentTimeMillis() - startTimeMillis;
        long elapsedSeconds = elapsedTime / warden;
        double recordsPerSecond = totalSize / (elapsedSeconds > 0 ? elapsedSeconds : 1);
        log.info("Inserted: " + totalSize + " records in " + elapsedTime + " ms. Rate: " + String.format("%.2f", recordsPerSecond) + " records/s");
    }

    private void processBatch(List<InternalRegistrations> registrations, String query) {
        int inserted = executeBatch(registrations, query);
        int newTotal = totalInserted.addAndGet(inserted);
        if (newTotal % LOG_INTERVAL == 0) {
            logProgress(newTotal, System.currentTimeMillis());
        }
    }

    private int executeBatch(List<InternalRegistrations> registrations, String query) {
        log.info("Started batch insertion for query: " + query);

        List<Object[]> batchParams = new ArrayList<>();

        for (InternalRegistrations internalReg : registrations) {
            String bdrId = internalReg.getBdrId();
            List<SubBookingEntity> subBookings = internalReg.getSubBookings();

            if (subBookings == null || subBookings.isEmpty()) {
                // Insert a record with null subBooking fields for entities without subBookings
                batchParams.add("EXTERNAL".equalsIgnoreCase(query) ?
                    new Object[]{bdrId, null, null, null, null} :
                    new Object[]{bdrId, null, null});
            } else {
                for (SubBookingEntity subBooking : subBookings) {
                    batchParams.add("EXTERNAL".equalsIgnoreCase(query) ?
                        new Object[]{bdrId, subBooking.getSubBookingId(), subBooking.getSubBookingName(),
                            subBooking.getCountry(), subBooking.getCreatedDate()} :
                        new Object[]{bdrId, subBooking.getCode(), subBooking.getSubBookingId()});
                }
            }
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] params = batchParams.get(i);
                ps.setString(1, (String) params[0]); // bdrId
                if (params[1] == null) {
                    ps.setNull(2, java.sql.Types.VARCHAR); // subBookingId
                    ps.setNull(3, java.sql.Types.VARCHAR); // subBookingName or code
                    if ("EXTERNAL".equalsIgnoreCase(query)) {
                        ps.setNull(4, java.sql.Types.VARCHAR); // country
                        ps.setNull(5, java.sql.Types.VARCHAR); // createdDate
                    }
                } else {
                    ps.setString(2, (String) params[1]); // subBookingId
                    ps.setString(3, (String) params[2]); // subBookingName or code
                    if ("EXTERNAL".equalsIgnoreCase(query)) {
                        ps.setString(4, (String) params[3]); // country
                        ps.setString(5, (String) params[4]); // createdDate
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return batchParams.size();
            }
        });

        return Arrays.stream(updateCounts).sum();
    }
}
