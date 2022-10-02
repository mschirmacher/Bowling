package com.github.mschirmacher.domain

class GameOfBowling(gameId: String) {

    private var version = 0
    private lateinit var gameId: String
    private val appliedEvents = mutableListOf<BowlingEvent>()

    private lateinit var frames: List<Frame>

    val framesLeftToPlay: Int
        get() = frames.count { it.areRollsLeft() }

    init {
        apply(GameOfBowlingInitialized(gameId))
    }

    private val currentFrame: Frame
        get() = frames.firstOrNull { it.areRollsLeft() } ?: frames.last()

    val currentFrameNumber: Int
        get() = currentFrame.frameNumber
    val currentFrameScore
        get() = currentFrame.getCurrentFrameScore()
    val rollsLeftInCurrentFrame
        get() = currentFrame.rollsLeft
    val pinsLeftToHit
        get() = currentFrame.pinsLeftToHit

    val currentGameScore
        get() = frames.sumOf { it.getCurrentFrameScore() }

    val isCompleted
        get() = !currentFrame.areRollsLeft()

    fun roll(pinsHit: Int) {
        require(pinsHit in 0..10) {
            "lol"
        }
        require(currentFrame.canRoll(pinsHit)) {
            "No rolls at or not enough pins left to roll $pinsHit pins"
        }

        apply(RollRecorded(pinsHit, version + 1, gameId))
    }

    fun collectScore(): Score {
        var subTotal = 0
        val frameScores = frames.map { frame ->
            frame.collectScore(subTotal).also { frameScore ->
                subTotal = frameScore.subTotal
            }
        }

        return Score(frameScores, subTotal)
    }

    private fun mutateWhen(event: BowlingEvent) {
        version = event.version
        when (event) {
            is GameOfBowlingInitialized -> `when`(event)
            is RollRecorded             -> `when`(event)
        }
    }

    private fun `when`(event: GameOfBowlingInitialized) {

        gameId = event.gameId

        val frames = mutableListOf(Frame(10, null))

        (9 downTo 1).forEach() {
            Frame(
                frameNumber = it,
                nextFrame = frames[0],
            ).also { frame ->
                frames.add(0, frame)
            }
        }

        this.frames = frames
    }

    private fun `when`(event: RollRecorded) {
        frames.first().rollRecorded(event)
    }

    private fun apply(event: BowlingEvent) {
        appliedEvents += event
        mutateWhen(event)
    }
}
