package org.dmc.services.data.repositories;

import java.util.Date;

import org.dmc.services.data.entities.PaymentReceipt;

public interface PaymentReceiptRepository extends BaseRepository<PaymentReceipt, Integer> {

	Integer countByUserIdAndDate(Integer id, Date date);

	Integer countByUserIdAndDateAndStatus(Integer id, Date date, String status);

}
