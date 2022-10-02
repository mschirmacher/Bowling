package com.github.mschirmacher.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class BowlingGameTests : BehaviorSpec(body = {

    given("a new game of bowling is created") {

        val gameOfBowling = GameOfBowling("test")

        `when`("nothing happened so far") {

            then("there should be 10 frames to play left") {
                gameOfBowling.framesLeftToPlay shouldBe 10
            }

            then("the current frame should match expected values") {
                gameOfBowling.currentFrameNumber shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }

        `when`("something weired happens leading to a negative number of pins hit") {
            then("an exception should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    gameOfBowling.roll(-1)
                }
            }

            and("game state should not have changed") {
                gameOfBowling.currentFrameNumber shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }

        `when`("something weired happens leading to rolling more pins than pins are left in current frame") {
            then("an exception should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    gameOfBowling.roll(11)
                }
            }

            and("game state should not have changed") {
                gameOfBowling.currentFrameNumber shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }

        `when`("the first roll hits 4 pins") {
            gameOfBowling.roll(4)
            then("current score should be recorded correctly") {
                gameOfBowling.framesLeftToPlay shouldBe 10
                gameOfBowling.currentFrameScore shouldBe 4
                gameOfBowling.currentGameScore shouldBe 4
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 1
                gameOfBowling.isCompleted shouldBe false
            }
        }

        `when`("the second roll hits 6 pins") {
            gameOfBowling.roll(6)

            then("current score should be recorded correctly") {
                gameOfBowling.currentGameScore shouldBe 10
                gameOfBowling.framesLeftToPlay shouldBe 9
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }

        `when`("the third roll hits 10 pins") {
            gameOfBowling.roll(10)

            then("current score should be recorded correctly") {
                gameOfBowling.currentGameScore shouldBe 30
                gameOfBowling.framesLeftToPlay shouldBe 8
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }
        `when`("the 4th and 5th rolls hits 3 and 2 pins respectively") {
            gameOfBowling.roll(3)
            gameOfBowling.roll(2)

            then("current score should be recorded correctly") {
                gameOfBowling.currentGameScore shouldBe 40
                gameOfBowling.framesLeftToPlay shouldBe 7
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }
    }

    given("a game with 9 frames completed") {
        val gameOfBowling = GameOfBowling("test")
        repeat(18) {
            gameOfBowling.roll(1)
        }
        `when`("19th and 20th rolls hit 2 pins respectively") {

            gameOfBowling.roll(2)
            gameOfBowling.roll(2)

            then("the game is completed") {
                gameOfBowling.currentGameScore shouldBe 22
                gameOfBowling.framesLeftToPlay shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 4
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 0
                gameOfBowling.isCompleted shouldBe true
            }
        }
    }

    given("a game with a spare in the final frame") {
        val gameOfBowling = GameOfBowling("test")
        repeat(18) {
            gameOfBowling.roll(1)
        }
        `when`("spare is rolled") {

            gameOfBowling.roll(2)
            gameOfBowling.roll(8)

            then("the game is not completed") {
                gameOfBowling.currentGameScore shouldBe 28
                gameOfBowling.framesLeftToPlay shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 10
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 1
                gameOfBowling.isCompleted shouldBe false
            }
        }
        `when`("the third roll in the final frame is done") {
            gameOfBowling.roll(10)
            then("the match should be completed") {
                gameOfBowling.currentGameScore shouldBe 38
                gameOfBowling.framesLeftToPlay shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 20
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 0
                gameOfBowling.isCompleted shouldBe true
            }
        }
    }

    given("a game with 3 Strikes in the final frame") {
        val gameOfBowling = GameOfBowling("test")
        repeat(18) {
            gameOfBowling.roll(1)
        }
        `when`("the first strike is rolled") {

            gameOfBowling.roll(10)

            then("the game is not completed") {
                gameOfBowling.currentGameScore shouldBe 28
                gameOfBowling.framesLeftToPlay shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 10
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 2
                gameOfBowling.isCompleted shouldBe false
            }
        }
        `when`("the second strike is rolled") {

            gameOfBowling.roll(10)

            then("the game is still not completed") {
                gameOfBowling.currentGameScore shouldBe 38
                gameOfBowling.framesLeftToPlay shouldBe 1
                gameOfBowling.currentFrameScore shouldBe 20
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 1
                gameOfBowling.isCompleted shouldBe false
            }
        }
        `when`("the third strike is rolled") {

            gameOfBowling.roll(10)

            then("the game should be completed") {
                gameOfBowling.currentGameScore shouldBe 48
                gameOfBowling.framesLeftToPlay shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 30
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 0
                gameOfBowling.isCompleted shouldBe true
            }
        }
    }
    given("a perfect game") {
        val gameOfBowling = GameOfBowling("test")
        `when`("game is completed") {
            repeat(12) {
                gameOfBowling.roll(10)
            }
            then("the total score should be 300") {
                gameOfBowling.currentGameScore shouldBe 300
                gameOfBowling.framesLeftToPlay shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 30
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 0
                gameOfBowling.isCompleted shouldBe true
            }
        }
    }

    given("i am bowling") {
        val gameOfBowling = GameOfBowling("test")
        `when`("the game is completed") {
            repeat(20) {
                gameOfBowling.roll(0)
            }

            then("the game score should be 0") {
                gameOfBowling.currentGameScore shouldBe 0
                gameOfBowling.framesLeftToPlay shouldBe 0
                gameOfBowling.currentFrameScore shouldBe 0
                gameOfBowling.rollsLeftInCurrentFrame shouldBe 0
                gameOfBowling.isCompleted shouldBe true
            }
        }
    }
})
