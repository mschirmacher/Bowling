package com.github.mschirmacher.domain


sealed class BowlingEvent {
    abstract val version: Int
    abstract val gameId: String
}

class GameOfBowlingInitialized(
        override val gameId: String,
) : BowlingEvent() {
    override val version = 1
}

class RollRecorded(
        val pinsHit: Int,
        override val version: Int,
        override val gameId: String,
) : BowlingEvent()
