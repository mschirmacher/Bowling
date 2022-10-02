package com.github.mschirmacher.infrastructure

import com.github.ajalt.mordant.terminal.Terminal
import com.github.mschirmacher.domain.GameOfBowling
import java.util.UUID
import kotlin.random.Random

fun main() {

    val terminal = Terminal(width = 1024)
    val numberOfPlayers =
        terminal.prompt("Number of players?", choices = (1..8).map { it.toString() })?.toInt() ?: return

    terminal.println("$numberOfPlayers selected")

    val playerNames = (1..numberOfPlayers).map {
        "Player$it"
    }

    val games = playerNames.associateWith {
        GameOfBowling(UUID.randomUUID().toString())
    }


    while (true) {
        val activeGames = games.filterNot { it.value.isCompleted }

        if (activeGames.isEmpty()) {
            break;
        }

        activeGames.forEach { player, gameOfBowling ->
            val pinsLeftToHit = gameOfBowling.pinsLeftToHit
            val pinsHitInRoll = Random.nextInt(0, pinsLeftToHit + 1)
            gameOfBowling.roll(pinsHitInRoll)
            terminal.println("Player $player rolled $pinsHitInRoll pins")
        }
    }

    ScorePrinter(terminal).print(games.mapValues { it.value.collectScore() })
}
