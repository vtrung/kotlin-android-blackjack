package com.vingapp.blackjack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_black_jack.*
import java.io.File


class BlackJackActivity : AppCompatActivity() {

    var game = Game()
    var end = false
    var credit = 100
    var bet = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)

        game = Game()
        var p = Player("Ving")
        game.addPlayer(p)
        game.start()
        readData()
        showDealerCards()
        showPlayerCards()
        showCredit()

    }

    private fun showDealerCards(){
        var cardlayout = findViewById<LinearLayout>(R.id.dealerLayout)
        cardlayout.removeAllViewsInLayout()

        var dealerhand = game.dealer.hand.cards

        for(c in dealerhand){
            var imageview1 = ImageView(this.applicationContext)
            imageview1.setImageResource(resources.getIdentifier(c!!.getImgName(),"drawable", this.packageName))
            cardlayout.addView(imageview1)

            //set layout
            imageview1.layoutParams.width = 150
            //imageview1.layoutParams.height = 250
        }
    }

    private fun showPlayerCards(){
        var playerhand = game.players[0].hand.cards

        var cardlayout = findViewById<LinearLayout>(R.id.cardlayout)
        cardlayout.removeAllViewsInLayout()

        for(c in playerhand){
            var imageview1 = ImageView(this.applicationContext)
            imageview1.setImageResource(resources.getIdentifier(c!!.getImgName(),"drawable", this.packageName))

            cardlayout.addView(imageview1)

            //set layout
            imageview1.layoutParams.width = 150
            //imageview1.layoutParams.height = 250
        }
    }


    fun hitClick(view: View){
        if(!end){
            var c = game.dealer.dealCard()
            game.players[0].receiveCard(c)
            if(game.players[0].hand.value() > 21){
                endgame()
            }
            showPlayerCards()
        }
    }

    fun holdClick(view: View){
        endgame()
    }

    fun endgame(){

        game.end()
        showDealerCards()
        end = true

        showvalue()
        showRestart()
        showCredit()
        saveData()
    }

    private fun hidevalue(){
        var ptext = findViewById<TextView>(R.id.playervaluetext)
        var dtext = findViewById<TextView>(R.id.dealervaluetext)
        var ftext = findViewById<TextView>(R.id.finaltext)
        var btext = findViewById<TextView>(R.id.bettext)
        ptext.text = ""
        dtext.text = ""
        ftext.text = ""
        btext.text = ""
    }

    private fun showvalue(){
        var ptext = findViewById<TextView>(R.id.playervaluetext)
        var pvalue = game.players[0].hand

        if (pvalue.value() <= 21) {
            if(pvalue.isBlackJack()) {
                ptext.text = "BlackJack"
            } else {
                ptext.text = pvalue.value().toString()
            }
        } else {
            ptext.text = pvalue.value().toString() + " Over"
        }


        var dtext = findViewById<TextView>(R.id.dealervaluetext)
        var dvalue = game.dealer.hand
        if (dvalue.value() <= 21) {
            if(dvalue.isBlackJack()){
                dtext.text = "BlackJack"
            } else {
                dtext.text = dvalue.value().toString()
            }
        } else {
            dtext.text = dvalue.value().toString() + " Over"
        }

        var ftext = findViewById<TextView>(R.id.finaltext)

        var result = pvalue.compare(dvalue)
        when(result){
            1 ->{
                ftext.text = "Player Win"
                credit += 2*bet
                bet = 0
            }
            0 -> {
                ftext.text = "Draw"
                credit += bet
                bet = 0
            }
            -1 -> {
                ftext.text = "Player Lose"
                bet = 0
            }
        }

    }

    fun restartClick(view: View){
        end = false
        game.restart()
        hidevalue()

        var rbutton = findViewById<Button>(R.id.restartbutton)
        rbutton.visibility = View.INVISIBLE

        showDealerCards()
        showPlayerCards()
        showCredit()
    }

    fun showRestart(){
        var rbutton = findViewById<Button>(R.id.restartbutton)
        rbutton.visibility = View.VISIBLE

    }

    fun enterBet(view: View){

        if(bet < 1){
            credit -= 10
            bet += 10
        }
        showCredit()
    }

    fun showCredit(){
        var btext = findViewById<TextView>(R.id.bettext)
        var ctext = findViewById<TextView>(R.id.credittext)


        btext.text = "Bet: ${bet.toString()}"
        ctext.text = "Credit: ${credit.toString()}"

    }

    fun saveData(){
        val file = File(this.applicationContext.filesDir, "savedata")

        file.writeText(credit.toString())

    }

    fun readData(){
        val file = File(this.applicationContext.filesDir, "savedata")
        if(file.exists()) {
            val content = file.readText()
            credit = content.toInt()
        }
    }
}
