package com.restapi.library.service;

import com.restapi.library.domain.Borrower;
import com.restapi.library.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowerDbService extends DbService<Borrower> {

    @Autowired
    public BorrowerDbService(final BorrowerRepository borrowerRepository) {
        super(borrowerRepository);
    }

}
