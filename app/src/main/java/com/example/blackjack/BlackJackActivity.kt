package com.example.blackjack

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


class BlackJackActivity : AppCompatActivity() {

    var game = Game()
    var end = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)

        game = Game()
        var p = Player("Ving")
        game.addPlayer(p)
        game.start()
        showDealerCards()
        showPlayerCards()
    }

    fun showDealerCards(){
        var cardlayout = findViewById<LinearLayout>(R.id.dealerLayout)
        cardlayout.removeAllViewsInLayout()

        var dealerhand = game.dealer.player.hand.cards

        for(c in dealerhand){
            var imageview1 = ImageView(this.applicationContext)
            imageview1.setImageResource(resources.getIdentifier(c!!.getImgName(),"drawable", this.packageName))
            cardlayout.addView(imageview1)

            //set layout
            imageview1.layoutParams.width = 100
            imageview1.layoutParams.height = 150
        }
    }

    fun showPlayerCards(){
        var playerhand = game.players[0].hand.cards

        var cardlayout = findViewById<LinearLayout>(R.id.cardlayout)
        cardlayout.removeAllViewsInLayout()

        for(c in playerhand){
            var imageview1 = ImageView(this.applicationContext)
            imageview1.setImageResource(resources.getIdentifier(c!!.getImgName(),"drawable", this.packageName))

            cardlayout.addView(imageview1)

            //set layout
            imageview1.layoutParams.width = 100
            imageview1.layoutParams.height = 150
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
    }

    fun hidevalue(){
        var ptext = findViewById<TextView>(R.id.playervaluetext)
        var dtext = findViewById<TextView>(R.id.dealervaluetext)
        ptext.text = ""
        dtext.text = ""
    }

    fun showvalue(){
        var ptext = findViewById<TextView>(R.id.playervaluetext)
        var dtext = findViewById<TextView>(R.id.dealervaluetext)
        ptext.text = game.players[0].hand.value().toString()
        dtext.text = game.dealer.value().toString()
    }

    fun restartClick(view: View){
        end = false
        game.restart()
        hidevalue()

        var rbutton = findViewById<Button>(R.id.restartbutton)
        rbutton.visibility = View.INVISIBLE

        showDealerCards()
        showPlayerCards()
    }

    fun showRestart(){
        var rbutton = findViewById<Button>(R.id.restartbutton)
        rbutton.visibility = View.VISIBLE

    }

}
