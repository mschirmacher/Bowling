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
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
                FrameScore(listOf(0, 0), 0),
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
                FrameScore(listOf(3, 5), 8),
                FrameScore(listOf(2, 8), 24),
                FrameScore(listOf(6, 3), 33),
                FrameScore(listOf(10), 48),
                FrameScore(listOf(2, 3), 53),
                FrameScore(listOf(0, 3), 56),
                FrameScore(listOf(5, 5), 66),
                FrameScore(listOf(0, 8), 74),
                FrameScore(listOf(5, 2), 81),
                FrameScore(listOf(7, 0), 88),
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
                FrameScore(listOf(8, 2), 15),
                FrameScore(listOf(5, 4), 24),
                FrameScore(listOf(9, 0), 33),
                FrameScore(listOf(10), 58),
                FrameScore(listOf(10), 78),
                FrameScore(listOf(5, 5), 93),
                FrameScore(listOf(5, 3), 101),
                FrameScore(listOf(6, 3), 110),
                FrameScore(listOf(9, 1), 129),
                FrameScore(listOf(9, 1, 10), 149),
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
                FrameScore(listOf(10), 30),
                FrameScore(listOf(10), 60),
                FrameScore(listOf(10), 90),
                FrameScore(listOf(10), 120),
                FrameScore(listOf(10), 150),
                FrameScore(listOf(10), 180),
                FrameScore(listOf(10), 210),
                FrameScore(listOf(10), 240),
                FrameScore(listOf(10), 270),
                FrameScore(listOf(10, 10, 10), 300),
            ), 300
        )
    }
})
