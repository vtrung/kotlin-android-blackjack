package com.vingapp.blackjack

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_card(){
        var card = Card("Ace","Spades",1)
        var imgv = card.getImgName()
        card.print()
        println(imgv)
        assertEquals("sa",imgv)
    }
    @Test
    fun deck_check(){
        var deck = Deck()
        println("=== Testing Deck Creation ====")
        deck.print()
        assertEquals(52,deck.cards.size)
        deck.shuffle()
        println("=== Testing Deck Shuffle ====")
        deck.print()
    }


}
