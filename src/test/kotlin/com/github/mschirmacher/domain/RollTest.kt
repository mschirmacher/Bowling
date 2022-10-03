package com.github.mschirmacher.domain

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.datatest.withData
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class RollTest : ExpectSpec(body = {
    context("The first roll with less than 10 pins hit is neither spare nor strike") {

        val rollNumbers = listOf(1, 2, 3)
        val frameNumbers = (1..10).toList()
        val pinsHitList = (1..9).toList()

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                withData(nameFn = { "Pins hit: $it" },
                         pinsHitList) { pinsHit ->
                    val roll = Roll(
                        pinsHit = pinsHit,
                        pinsLeftBefore = 10,
                        rollNumber = rollNumber,
                        frameNumber = frame,
                    )
                    roll.isSpare() shouldBe false
                    roll.isStrike() shouldBe false
                }
            }
        }
    }

    context("A roll hitting 10 pins is a strike but not a spare regardless of frame or roll index") {

        val rollNumbers = listOf(1, 2, 3)
        val frameNumbers = (1..10).toList()

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                val roll = Roll(
                    pinsHit = 10,
                    pinsLeftBefore = 10,
                    rollNumber = rollNumber,
                    frameNumber = frame,
                )

                roll.isSpare() shouldBe false
                roll.isStrike() shouldBe true
            }
        }
    }

    context("A roll hitting all the pins left while there are less than 10 pins left is considered a spare but not a strike") {

        val frameNumbers = (1..10).toList()
        val rollNumbers = listOf(2, 3)
        val pinsHitList = (0..9).toList()

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                withData(nameFn = { "Pins $it" },
                         pinsHitList) { pinsHit ->
                    val roll = Roll(
                        pinsHit = pinsHit,
                        pinsLeftBefore = pinsHit,
                        rollNumber = rollNumber,
                        frameNumber = frame,
                    )

                    roll.isSpare() shouldBe true
                    roll.isStrike() shouldBe false
                }
            }
        }
    }

    context("After a strike there are 10 pins left to hit again") {
        val frameNumbers = (1..10).toList()
        val rollNumbers = listOf(2, 3)

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                val roll = Roll(
                    pinsHit = 10,
                    pinsLeftBefore = 10,
                    rollNumber = rollNumber,
                    frameNumber = frame,
                )

                roll.pinsLeftAfter() shouldBe 10
            }
        }
    }

    context("After a spare there are 10 pins left to hit again") {

        val frameNumbers = (1..10).toList()
        val rollNumbers = listOf(2, 3)
        val pinsHitList = (1..9).toList()

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                withData(nameFn = { "Pins $it" },
                         pinsHitList) { pinsHit ->
                    val roll = Roll(
                        pinsHit = pinsHit,
                        pinsLeftBefore = pinsHit,
                        rollNumber = rollNumber,
                        frameNumber = frame,
                    )

                    roll.pinsLeftAfter() shouldBe 10
                }
            }
        }
    }

    context("After a roll that's neither a spare nor a strike there should be `pinsLeftBefore - pinsHit` pins left after") {

        val rollNumbers = listOf(1, 2, 3)
        val frameNumbers = (1..10).toList()
        val pinsHitList = (0..9).toList()

        withData(nameFn = { "Frame $it" },
                 frameNumbers) { frame ->
            withData(nameFn = { "Roll $it" },
                     rollNumbers) { rollNumber ->
                withData(nameFn = { "Pins hit: $it" },
                         pinsHitList) { pinsHit ->

                    val pinsLeftBefore = (pinsHit + 1..10).toList()

                    withData(nameFn = { "pins left before: $it" },
                             pinsLeftBefore) {

                        val roll = Roll(
                            pinsHit = pinsHit,
                            pinsLeftBefore = it,
                            rollNumber = rollNumber,
                            frameNumber = frame,
                        )

                        roll.pinsLeftAfter() shouldBeGreaterThan 0
                        roll.pinsLeftAfter() shouldBe (it - pinsHit)
                    }
                }
            }
        }
    }
})
