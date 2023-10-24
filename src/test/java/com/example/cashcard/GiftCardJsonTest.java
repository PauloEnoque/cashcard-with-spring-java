package com.example.cashcard;

import com.example.cashcard.Models.GiftCard;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class GiftCardJsonTest {

    @Autowired
    private JacksonTester<GiftCard> json;

    @Autowired
    private JacksonTester<GiftCard[]> jsonList;

    private GiftCard[] cashcards;

    @BeforeEach
    void setUp(){
        cashcards = Arrays.array(
                new GiftCard(99L, 123.45,"sarah1"),
                new GiftCard(100L, 1.00, "sarah1"),
                new GiftCard(101L, 150.00, "sarah1"));
    }

    @Test
    public void myFisrtTest(){
        assertThat(41).isEqualTo(41);
    }

    @Test
    public void cashCardSerializationTest() throws IOException{
        GiftCard giftCard = new GiftCard(99L, 123.45, "sarah1");


        assertThat(json.write(giftCard))
                .isStrictlyEqualToJson("single.json");

        assertThat(json.write(giftCard)).hasJsonPathNumberValue("@.id");

        assertThat(json.write(giftCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);

        assertThat(json.write(giftCard)).hasJsonPathNumberValue("@.amount");

        assertThat(json.write(giftCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashcards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected="""
                       [
                         {"id": 99, "amount": 123.45 , "owner": "sarah1"},
                         {"id": 100, "amount": 1.00 , "owner": "sarah1"},
                         {"id": 101, "amount": 150.00, "owner": "sarah1" }
                       ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashcards);
    }

    @Test
    public void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                  "id": 99,
                  "amount": 123.45,
                  "owner": "sarah1"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new GiftCard(99L, 123.45, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }
}
