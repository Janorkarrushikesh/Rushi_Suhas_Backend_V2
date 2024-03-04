package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    Optional <Invoice> findInvoiceByStoreId(Integer storeId);

    @Query("SELECT MAX(b.invoiceId) FROM Invoice b WHERE b.storeId = :storeId")
    Integer findLastNumberForStore(Integer storeId);
}
