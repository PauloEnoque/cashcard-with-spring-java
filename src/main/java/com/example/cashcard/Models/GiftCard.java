package com.example.cashcard.Models;

import org.springframework.data.annotation.Id;

public record GiftCard(
        // Annotation to indicate that this field is the primary key (ID) in the database
        @Id Long id,
        // Field to store the amount associated with the CashCard
        Double amount,
        // Field to store the owner's name or identifier associated with the CashCard
        String owner
) {
}
