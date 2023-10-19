// Package declaration
package com.example.cashcard;

// Import statements
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

// Annotation to indicate that this class is a REST controller
@RestController
// Base URL for all the endpoints in this controller
@RequestMapping("/cashcards")
public class CashCardController {

    // Dependency on the CashCardRepository for database operations
    private CashCardRepository cashCardRepository;

    // Constructor injection of the CashCardRepository
    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    // Endpoint to get a CashCard by its ID
    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal){
        // Fetch the CashCard by its ID and owner
        CashCard cashCardOptional = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        // Return the CashCard if found, otherwise return a 404 Not Found response
        return cashCardOptional != null ?
                ResponseEntity.ok(cashCardOptional) :
                ResponseEntity.notFound().build();
    }

    // Endpoint to create a new CashCard
    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardReques, UriComponentsBuilder ucb, Principal principal) {
        // Create a new CashCard with the provided amount and the authenticated user as the owner
        CashCard cashCardWithOwner = new CashCard(null, newCashCardReques.amount(), principal.getName());
        // Save the new CashCard to the database
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);

        // Build the URI for the newly created CashCard
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();

        // Return a 201 Created response with the location of the new CashCard
        return ResponseEntity
                .created(locationOfNewCashCard)
                .build();
    }

    // Endpoint to get all CashCards for the authenticated user with pagination
    @GetMapping()
    public ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable, Principal principal) {
        // Fetch the CashCards for the authenticated user with the provided pagination and sorting
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        // Return the fetched CashCards
        return ResponseEntity.ok(page.getContent());
    }

    // Endpoint to update a CashCard by its ID
    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
        // Fetch the CashCard by its ID and owner
        CashCard cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        if (cashCard != null) {
            // Update the CashCard with the provided amount
            CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            // Save the updated CashCard to the database
            cashCardRepository.save(updatedCashCard);
            // Return a 204 No Content response
            return ResponseEntity.noContent().build();
        }
        // If the CashCard is not found, return a 404 Not Found response
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a CashCard by its ID
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {
        // Check if the CashCard exists by its ID and owner
        if (!cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
            // If the CashCard is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        // Delete the CashCard from the database
        cashCardRepository.deleteById(id);
        // Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }
}
