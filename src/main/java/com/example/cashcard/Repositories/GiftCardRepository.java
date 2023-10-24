package com.example.cashcard.Repositories;

import com.example.cashcard.Models.GiftCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GiftCardRepository
        // Extending CrudRepository to get basic CRUD operations for CashCard entities
        extends CrudRepository<GiftCard, Long>,
        // Extending PagingAndSortingRepository to get pagination and sorting capabilities
        PagingAndSortingRepository<GiftCard, Long> {

    // Method declaration to find a CashCard by its ID and owner
    GiftCard findByIdAndOwner(Long id, String owner);

    // Method declaration to find CashCards by their owner with pagination
    Page<GiftCard> findByOwner(String owner, PageRequest amount);

    // Method declaration to check if a CashCard exists by its ID and owner
    boolean existsByIdAndOwner(Long id, String owner);
}
