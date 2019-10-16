package com.example.blackjack

import kotlin.random.Random


class Card constructor(val name: String,val suit: String,val cardvalue: Int){

    fun print(){
        println("${this.name} of ${this.suit}")
    }

    fun getImgName():String{
        var imgname:String = this.name[0].toString()
        if(this.name == "10")
            imgname = "10"
        var result = "${this.suit[0]}$imgname"
        return (result.toLowerCase())
    }
}

class Deck {
    var cards: MutableList<Card> = mutableListOf()
    var suits: Array<String> = arrayOf("Diamonds","Clubs","Hearts","Spades")

    init {
        for (i in 1..13){
            for(j in suits){
                var name:String = i.toString()
                var imgname:String = ""
                var bjv = i
                when(i){
                    1 -> {
                        name = "Ace"
                        bjv = 11
                    }
                    11 -> {
                        name = "Jack"
                        bjv = 10
                    }
                    12 -> {
                        name = "Queen"
                        bjv = 10
                    }
                    13 -> {
                        name = "King"
                        bjv = 10
                    }
                }


                cards.add(Card(name, j, bjv))
            }
        }
    }

    fun print(){
        for(i in cards){
            i.print()
        }
    }

    fun shuffle(){
        var rand = Random(System.currentTimeMillis())

        for(i in 0 until (cards.size-1)){
            var rindex = rand.nextInt(cards.size)
            //swap cards
            var temp = cards[i]
            cards[i] = cards[rindex]
            cards[rindex] = temp
        }
    }

    fun drawCard():Card?{
        if(cards.size > 0){
            var result = cards[0]
            cards.removeAt(0)
            return result
        } else {
            return null
        }
    }
}

class Hand{
    var cards: MutableList<Card> = mutableListOf<Card>()

    fun receiveCard(card:Card?){
        if(card != null)
            cards.add(card)
    }

    //get hand value
    fun value():Int{
        var result: Int = 0
        var aces: Int = 0
        for(i in cards){
            result += i.cardvalue
            if(i.cardvalue == 11){
                aces++
            }
        }
        // Convert ace value to 1 if sum is greater than 21
        while (result > 21 && aces > 0){
            result -= 10
            aces--
        }
        return result
    }

    //check blackjack
    fun isBlackJack():Boolean {
        if(cards.size == 2 && value() == 21) {
            return true
        }
        return false
    }

    //bool isOver
    fun isOver():Boolean{
        return (value() > 21)
    }

    //print hand
    fun print(){
        for(i in cards){
            i.print()
        }
    }
    //get count
    //discard hand
    fun discard(){
        cards.clear()
    }

    //return a int value
    // 0 for draw
    // 1 this hand wins
    // -1 this hand loses
    fun compare(h:Hand):Int{
        // Check for blackjack win
        if(this.isBlackJack()){
            return if(h.isBlackJack()){
                0
            } else {
                1
            }
        }

        // Check over 21 lost
        if(this.isOver()){
            return if(h.isOver()){
                0
            } else {
                -1
            }
        }

        if(h.isOver()){
            return 1
        }

        // Check for draw
        if(this.value() == h.value()){
            return 0
        }

        // Check for higher value
        return if(this.value() > h.value()){
            1
        }else{
            -1
        }

        return 0
    }
}

class Player constructor(val name: String){
    var hand = Hand()
    var hold = true

    fun receiveCard(card:Card?){
        hand.receiveCard(card)
    }

    //discard hand
    fun discard(){
        hand.discard()
        hold = false
    }
    //print hand
    fun print(){
        hand.print()
    }

    fun hold(){
        hold = true
    }
}

class Dealer{
    val name = "Dealer"
    var deck = Deck()
    var hand = Hand()

    init{
        deck.shuffle()
    }

    //deal card
    fun dealCard():Card?{
        var card:Card? = deck.drawCard()
        return card
    }

    fun dealCard(p:Player){
        p.receiveCard(deck.drawCard())
    }

    fun discard(){
        hand.discard()
    }

    fun value():Int{
        return hand.value()
    }

    //get new deck
    fun newDeck(){
        deck = Deck()
    }
}

class Game{
    var dealer = Dealer()
    var players:MutableList<Player> = mutableListOf<Player>()

    //add player
    fun addPlayer(p:Player){
        players.add(p)
    }

    //start a game
    fun start(){
        //dealer receives 2 cards
        dealer.hand.receiveCard(dealer.dealCard())
        dealer.hand.receiveCard(dealer.dealCard())
        //each player receives 2 cards
        for(p in players){
            dealer.dealCard(p)
            dealer.dealCard(p)
            //p.receiveCard(dealer.dealCard())
            //p.receiveCard(dealer.dealCard())
        }
    }

    //player count
    fun countPlayer():Int{
        return players.size
    }

    //evaluate final
    fun eval(){

    }

    //end game
    fun end(){
        while(dealer.value() < 16){
            dealer.hand.receiveCard(dealer.dealCard())
        }
    }

    //restart game
    fun restart(){
        dealer.discard()
        dealer.newDeck()
        dealer.deck.shuffle()
        for(p in players){
            p.discard()
        }
        start()
    }
}