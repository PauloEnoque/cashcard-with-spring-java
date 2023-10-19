package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CashCardRepository
        // Extending CrudRepository to get basic CRUD operations for CashCard entities
        extends CrudRepository<CashCard, Long>,
        // Extending PagingAndSortingRepository to get pagination and sorting capabilities
        PagingAndSortingRepository<CashCard, Long> {

    // Method declaration to find a CashCard by its ID and owner
    CashCard findByIdAndOwner(Long id, String owner);

    // Method declaration to find CashCards by their owner with pagination
    Page<CashCard> findByOwner(String owner, PageRequest amount);

    // Method declaration to check if a CashCard exists by its ID and owner
    boolean existsByIdAndOwner(Long id, String owner);
}
