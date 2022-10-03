package com.github.mschirmacher.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ScoreTest : FunSpec(body = {
    test("zero scores") {
        val gameOfBowling = GameOfBowling("test")

        repeat(20) {
            gameOfBowling.roll(0)
        }

        gameOfBowling.collectScore() shouldBe Score(
            listOf(
                FrameScore(listOf(0, 0).toRolls(1), 0),
                FrameScore(listOf(0, 0).toRolls(2), 0),
                FrameScore(listOf(0, 0).toRolls(3), 0),
                FrameScore(listOf(0, 0).toRolls(4), 0),
                FrameScore(listOf(0, 0).toRolls(5), 0),
                FrameScore(listOf(0, 0).toRolls(6), 0),
                FrameScore(listOf(0, 0).toRolls(7), 0),
                FrameScore(listOf(0, 0).toRolls(8), 0),
                FrameScore(listOf(0, 0).toRolls(9), 0),
                FrameScore(listOf(0, 0).toRolls(10), 0),
            ), 0
        )
    }

    test("common scores") {
        val gameOfBowling = GameOfBowling("test")

        gameOfBowling.roll(3)
        gameOfBowling.roll(5)

        gameOfBowling.roll(2)
        gameOfBowling.roll(8)

        gameOfBowling.roll(6)
        gameOfBowling.roll(3)

        gameOfBowling.roll(10)

        gameOfBowling.roll(2)
        gameOfBowling.roll(3)

        gameOfBowling.roll(0)
        gameOfBowling.roll(3)

        gameOfBowling.roll(5)
        gameOfBowling.roll(5)

        gameOfBowling.roll(0)
        gameOfBowling.roll(8)

        gameOfBowling.roll(5)
        gameOfBowling.roll(2)

        gameOfBowling.roll(7)
        gameOfBowling.roll(0)

        gameOfBowling.collectScore() shouldBe Score(
            listOf(
                FrameScore(listOf(3, 5).toRolls(1), 8),
                FrameScore(listOf(2, 8).toRolls(2), 24),
                FrameScore(listOf(6, 3).toRolls(3), 33),
                FrameScore(listOf(10).toRolls(4), 48),
                FrameScore(listOf(2, 3).toRolls(5), 53),
                FrameScore(listOf(0, 3).toRolls(6), 56),
                FrameScore(listOf(5, 5).toRolls(7), 66),
                FrameScore(listOf(0, 8).toRolls(8), 74),
                FrameScore(listOf(5, 2).toRolls(9), 81),
                FrameScore(listOf(7, 0).toRolls(10), 88),
            ), 88
        )
    }


    test("score with spare in final frame") {
        val gameOfBowling = GameOfBowling("test")

        gameOfBowling.roll(8)
        gameOfBowling.roll(2)
        gameOfBowling.roll(5)
        gameOfBowling.roll(4)
        gameOfBowling.roll(9)
        gameOfBowling.roll(0)
        gameOfBowling.roll(10)
        gameOfBowling.roll(10)
        gameOfBowling.roll(5)
        gameOfBowling.roll(5)
        gameOfBowling.roll(5)
        gameOfBowling.roll(3)
        gameOfBowling.roll(6)
        gameOfBowling.roll(3)
        gameOfBowling.roll(9)
        gameOfBowling.roll(1)
        gameOfBowling.roll(9)
        gameOfBowling.roll(1)
        gameOfBowling.roll(10)

        gameOfBowling.collectScore() shouldBe Score(
            listOf(
                FrameScore(listOf(8, 2).toRolls(1), 15),
                FrameScore(listOf(5, 4).toRolls(2), 24),
                FrameScore(listOf(9, 0).toRolls(3), 33),
                FrameScore(listOf(10).toRolls(4), 58),
                FrameScore(listOf(10).toRolls(5), 78),
                FrameScore(listOf(5, 5).toRolls(6), 93),
                FrameScore(listOf(5, 3).toRolls(7), 101),
                FrameScore(listOf(6, 3).toRolls(8), 110),
                FrameScore(listOf(9, 1).toRolls(9), 129),
                FrameScore(listOf(9, 1, 10).toRolls(10), 149),
            ), 149
        )
    }

    test("perfect scores") {
        val gameOfBowling = GameOfBowling("test")

        repeat(12) {
            gameOfBowling.roll(10)
        }

        gameOfBowling.collectScore() shouldBe Score(
            listOf(
                FrameScore(listOf(10).toRolls(1), 30),
                FrameScore(listOf(10).toRolls(2), 60),
                FrameScore(listOf(10).toRolls(3), 90),
                FrameScore(listOf(10).toRolls(4), 120),
                FrameScore(listOf(10).toRolls(5), 150),
                FrameScore(listOf(10).toRolls(6), 180),
                FrameScore(listOf(10).toRolls(7), 210),
                FrameScore(listOf(10).toRolls(8), 240),
                FrameScore(listOf(10).toRolls(9), 270),
                FrameScore(listOf(10, 10, 10).toRolls(10), 300),
            ), 300
        )
    }
})

private fun List<Int>.toRolls(frameNumber: Int): List<Roll> {
    var pinsLeft = 10
    return mapIndexed { index, pinsHit ->
        Roll(pinsHit, pinsLeft, index + 1, frameNumber).also {
            pinsLeft -= pinsHit
            if (pinsLeft == 0) {
                pinsLeft = 10
            }
        }
    }
}
