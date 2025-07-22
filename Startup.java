package com.socgen.riskweb;

import com.socgen.riskweb.Model.ResponseInternal;
import com.socgen.riskweb.dao.RiskwebClientDao;
import com.socgen.riskweb.util.RestClientUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Logger;

public class RiskwebMaestroStartup {
    @Autowired
    private RiskwebClientDao clientDao;

    @Autowired
    private RestClientUtility restClientUtility;

    private static final Logger LOGGER = Logger.getLogger(RiskwebMaestroStartup.class.getName());

    public static void main(String[] args) throws SQLException, ParseException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(/* your configuration class */);
        RiskwebMaestroStartup startup = context.getBean(RiskwebMaestroStartup.class);

        try {
            // Process internal registrations
            ResponseInternal internalResponse = startup.restClientUtility.sendPrimaryroleApi("internalRegistration", "INTERNAL");
            if (internalResponse != null) {
                LOGGER.info("Calling savePrimaryroleApi for internal registrations");
                startup.clientDao.savePrimaryroleApi(internalResponse, "TMAESNUMIPL");
            }

            // Process external registrations
            ResponseInternal externalResponse = startup.restClientUtility.sendPrimaryroleApi("externalRegistration", "EXTERNAL");
            if (externalResponse != null) {
                LOGGER.info("Calling savePrimaryroleApi for external registrations");
                startup.clientDao.savePrimaryroleApi(externalResponse, "TMAESNUMIPL_EXTERNAL");
            }
        } catch (Exception e) {
            LOGGER.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
