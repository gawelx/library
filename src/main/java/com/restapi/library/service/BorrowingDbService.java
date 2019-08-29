package com.restapi.library.service;

import com.restapi.library.domain.Borrowing;
import com.restapi.library.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowingDbService extends DbService<Borrowing> {

    @Autowired
    public BorrowingDbService(final BorrowingRepository borrowingRepository) {
        super(borrowingRepository);
    }


}
