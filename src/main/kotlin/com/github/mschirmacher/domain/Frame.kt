package com.github.mschirmacher.domain

class Frame(
        val frameNumber: Int,
        private val nextFrame: Frame?,
) {
    var rollsLeft = 2
        private set

    var pinsLeftToHit = 10

    private val recordedRolls = mutableListOf<Int>()
    private var pointsFromFutureRollsToRecord = 0
    private val extraPointsRecorded = mutableListOf<Int>()

    private val isSpare
        get() = recordedRolls.size == 2 && recordedRolls.sum() == 10

    private val isFinalFrame
        get() = frameNumber == 10

    fun canRoll(pinsHit: Int): Boolean = areRollsLeft() && (pinsLeftToHit - pinsHit) >= 0

    fun getCurrentFrameScore(): Int {
        return recordedRolls.sum() + extraPointsRecorded.sum()
    }

    fun areRollsLeft() = rollsLeft > 0

    fun collectScore(previousScore: Int): FrameScore =
        FrameScore(recordedRolls.toList(), getCurrentFrameScore() + previousScore)

    fun rollRecorded(event: RollRecorded) {
        if (areRollsLeft()) {
            rollsLeft--
            recordedRolls += event.pinsHit
            pinsLeftToHit -= event.pinsHit

            if (isSpare) {
                if (isFinalFrame) {
                    rollsLeft++
                    pinsLeftToHit = 10
                } else {
                    pointsFromFutureRollsToRecord = 1
                }
            } else if (event.pinsHit == 10) {
                if (isFinalFrame) {
                    when (recordedRolls.size) {
                        0 -> rollsLeft = 3
                        1 -> rollsLeft = 2
                        2 -> rollsLeft = 1
                    }
                    pinsLeftToHit = 10
                } else {
                    rollsLeft--
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

    private fun isScoreCompleted() = !areRollsLeft() && pointsFromFutureRollsToRecord == 0
}
