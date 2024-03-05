package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.repository.v1.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRecordService {

    @Autowired
    TransactionRecordRepository transactionRecordRepository;

    public List<TransactionRecord> getTransactioneByStoreId(Integer store_id) {
        return transactionRecordRepository.findByStoreid(store_id);
    }
}
