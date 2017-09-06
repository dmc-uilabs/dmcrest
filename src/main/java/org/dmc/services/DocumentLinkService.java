package org.dmc.services;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;

@Service
public class DocumentLinkService {

    public Timestamp getDcoumentExpirationTime(Document document) {

        Timestamp expirationDate;

        if (document.getParentType() != null) {

            DocumentParentType parentType = document.getParentType();

            if (parentType.equals(DocumentParentType.SERVICE)) {
                expirationDate = getWeeksFromNow(1);
            } else if (parentType.equals(DocumentParentType.APPSUBMISSION)) {
                expirationDate = getMonthsFromNow(1);
            } else {
                if (document.getDocClass() != null) {
                    DocumentClass documentClass = document.getDocClass();
                    expirationDate = getExpirationFromDocClass(documentClass);
                } else {
                    expirationDate = getMonthsFromNow(1);
                }
            }
        } else {
            expirationDate = getMonthsFromNow(1);
        }

        return expirationDate;
    }

    public Timestamp getDMDIIDocumentExpirationTime(DMDIIDocument dmdiiDocument) {

        return getHoursFromNow(1);

    }

    private Timestamp getExpirationFromDocClass(DocumentClass documentClass) {
        Timestamp expirationDate;

        switch (documentClass) {
            case OVERVIEW:
                expirationDate = getWeeksFromNow(1);
                break;
            case STATUS:
                expirationDate = getWeeksFromNow(1);
                break;
            case FINANCIAL:
                expirationDate = getWeeksFromNow(1);
                break;
            case SCHEDULE:
                expirationDate = getMonthsFromNow(1);
                break;
            case LOGO:
                expirationDate = getMonthsFromNow(1);
                break;
            case IMAGE:
                expirationDate = getMonthsFromNow(1);
                break;
            case VIDEO:
                expirationDate = getMonthsFromNow(1);
                break;
            case PROFILE:
                expirationDate = getMonthsFromNow(1);
                break;
            case SUPPORT:
                expirationDate = getMonthsFromNow(1);
                break;
            case QUICK_LINK:
                expirationDate = getWeeksFromNow(1);
                break;
            case FEATURE_IMAGE:
                expirationDate = getMonthsFromNow(1);
                break;
            default:
                expirationDate = getMonthsFromNow(1);
                break;
        }

        return expirationDate;

    }

    public Timestamp getMonthsFromNow(Integer months){
        return Timestamp.valueOf(LocalDate.now().atStartOfDay().plusMonths(months).minusDays(1));
    }

    public Timestamp getWeeksFromNow(Integer weeks) {
        return Timestamp.valueOf(LocalDate.now().atStartOfDay().plusWeeks(weeks));
    }

    public Timestamp getHoursFromNow(Integer hours) {
        return Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(hours));
    }

}
