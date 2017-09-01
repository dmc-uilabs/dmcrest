package org.dmc.services;

import org.dmc.services.data.entities.Document;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;

@Service
public class DocumentLinkService {

    public Timestamp getDcoumentExpirationTime(Document document) {

        Timestamp expirationDate;

        switch (document.getDocClass()) {
            case OVERVIEW:  expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusWeeks(1));
                            break;
            case STATUS:    expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusWeeks(1));
                            break;
            case FINANCIAL: expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusWeeks(1));
                            break;
            case SCHEDULE:  expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case LOGO:      expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case IMAGE:     expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case VIDEO:     expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case PROFILE:   expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case SUPPORT:   expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
            case QUICK_LINK: expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusWeeks(1));
                             break;
            case FEATURE_IMAGE: expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                                break;
            default:        expirationDate = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1));
                            break;
        }

        return expirationDate;
    }

}
