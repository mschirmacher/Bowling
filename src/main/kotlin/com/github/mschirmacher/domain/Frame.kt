package com.github.mschirmacher.domain

class Frame(
    val frameNumber: Int,
    private val nextFrame: Frame?,
) {
    var rollsLeft = 2
        private set

    val pinsLeftToHit
        get() = recordedRolls.lastOrNull()?.pinsLeftAfter() ?: 10

    private val recordedRolls = mutableListOf<Roll>()
    private var pointsFromFutureRollsToRecord = 0
    private val extraPointsRecorded = mutableListOf<Int>()

    private val isFinalFrame
        get() = frameNumber == 10

    fun canRoll(pinsHit: Int): Boolean = areRollsLeft() && (pinsLeftToHit - pinsHit) >= 0

    fun getCurrentFrameScore(): Int {
        return recordedRolls.sumOf { it.pinsHit } + extraPointsRecorded.sum()
    }

    fun areRollsLeft() = rollsLeft > 0

    fun collectScore(previousScore: Int): FrameScore =
            FrameScore(recordedRolls.toList(), getCurrentFrameScore() + previousScore)

    fun rollRecorded(event: RollRecorded) {
        if (areRollsLeft()) {

            val roll = createRoll(event.pinsHit)

            rollsLeft--
            recordedRolls += roll

            if (roll.isSpare()) {
                if (isFinalFrame) {
                    rollsLeft++
                } else {
                    pointsFromFutureRollsToRecord = 1
                }
            } else if (roll.isStrike()) {
                if (isFinalFrame) {
                    when (recordedRolls.size) {
                        0 -> rollsLeft = 3
                        1 -> rollsLeft = 2
                        2 -> rollsLeft = 1
                    }
                } else {
                    rollsLeft = 0
                    pointsFromFutureRollsToRecord = 2
                }
            }

            return
        }

        if (!isScoreCompleted()) {
            pointsFromFutureRollsToRecord--
            extraPointsRecorded += event.pinsHit
        }

        nextFrame?.rollRecorded(event)
    }

    private fun createRoll(pinsHit: Int): Roll {
        return Roll(pinsHit = pinsHit,
                    pinsLeftBefore = pinsLeftToHit,
                    rollNumber = recordedRolls.size + 1,
                    frameNumber = frameNumber)
    }

    private fun isScoreCompleted() = !areRollsLeft() && pointsFromFutureRollsToRecord == 0
}
