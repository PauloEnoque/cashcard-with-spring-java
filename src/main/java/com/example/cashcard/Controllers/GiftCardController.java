package com.example.cashcard.Controllers;

import com.example.cashcard.Models.GiftCard;
import com.example.cashcard.Repositories.GiftCardRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

// Annotation to indicate that this class is a REST controller
@RestController
// Base URL for all the endpoints in this controller
@RequestMapping("/giftcards")
public class GiftCardController {

    // Dependency on the giftcardRepository for database operations
    private GiftCardRepository giftCardRepository;

    // Constructor injection of the giftcardRepository
    public GiftCardController(GiftCardRepository giftCardRepository) {
        this.giftCardRepository = giftCardRepository;
    }

    // Endpoint to get a giftcard by its ID
    @GetMapping("/{requestedId}")
    public ResponseEntity<GiftCard> findById(@PathVariable Long requestedId, Principal principal){
        // Fetch the giftcard by its ID and owner
        GiftCard giftCardOptional = giftCardRepository.findByIdAndOwner(requestedId, principal.getName());
        // Return the giftcard if found, otherwise return a 404 Not Found response
        return giftCardOptional != null ?
                ResponseEntity.ok(giftCardOptional) :
                ResponseEntity.notFound().build();
    }

    // Endpoint to create a new giftcard
    @PostMapping
    private ResponseEntity<Void> creategiftcard(@RequestBody GiftCard newGiftCardReques, UriComponentsBuilder ucb, Principal principal) {
        // Create a new giftcard with the provided amount and the authenticated user as the owner
        GiftCard giftCardWithOwner = new GiftCard(null, newGiftCardReques.amount(), principal.getName());
        // Save the new giftcard to the database
        GiftCard savedGiftCard = giftCardRepository.save(giftCardWithOwner);

        // Build the URI for the newly created giftcard
        URI locationOfNewgiftcard = ucb
                .path("giftcards/{id}")
                .buildAndExpand(savedGiftCard.id())
                .toUri();

        // Return a 201 Created response with the location of the new giftcard
        return ResponseEntity
                .created(locationOfNewgiftcard)
                .build();
    }

    // Endpoint to get all giftcards for the authenticated user with pagination
    @GetMapping()
    public ResponseEntity<Iterable<GiftCard>> findAll(Pageable pageable, Principal principal) {
        // Fetch the giftcards for the authenticated user with the provided pagination and sorting
        Page<GiftCard> page = giftCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        // Return the fetched giftcards
        return ResponseEntity.ok(page.getContent());
    }

    // Endpoint to update a giftcard by its ID
    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putgiftcard(@PathVariable Long requestedId, @RequestBody GiftCard giftCardUpdate, Principal principal) {
        // Fetch the giftcard by its ID and owner
        GiftCard giftCard = giftCardRepository.findByIdAndOwner(requestedId, principal.getName());
        if (giftCard != null) {
            // Update the giftcard with the provided amount
            GiftCard updatedGiftCard = new GiftCard(giftCard.id(), giftCardUpdate.amount(), principal.getName());
            // Save the updated giftcard to the database
            giftCardRepository.save(updatedGiftCard);
            // Return a 204 No Content response
            return ResponseEntity.noContent().build();
        }
        // If the giftcard is not found, return a 404 Not Found response
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a giftcard by its ID
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletegiftcard(@PathVariable Long id, Principal principal) {
        // Check if the giftcard exists by its ID and owner
        if (!giftCardRepository.existsByIdAndOwner(id, principal.getName())) {
            // If the giftcard is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        // Delete the giftcard from the database
        giftCardRepository.deleteById(id);
        // Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }
}
