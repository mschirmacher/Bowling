package com.github.mschirmacher.infrastructure

import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.VerticalAlign
import com.github.ajalt.mordant.table.*
import com.github.ajalt.mordant.terminal.Terminal
import com.github.mschirmacher.domain.FrameScore
import com.github.mschirmacher.domain.Score

class ScorePrinter(private val terminal: Terminal) {

    fun print(scoresMap: Map<String, Score>) {
        val table = table {

            align = TextAlign.CENTER
            verticalAlign = VerticalAlign.MIDDLE

            buildHeader()

            body {
                scoresMap.entries.forEachIndexed { index, (player, score) ->
                    buildPlayerLine(player, score, index + 1)
                }
            }
        }

        terminal.println(table)
    }

    private fun TableBuilder.buildHeader() {
        header {
            row {
                cell("Name") {
                    columnSpan = 2
                }
                repeat(10) {
                    cell("${it + 1}") { columnSpan = 3 }
                }
                cell("Total")
            }
        }
    }

    private fun SectionBuilder.buildPlayerLine(player: String, score: Score, index: Int) {

        row {
            cell(index) { rowSpan = 2 }
            cell(player) { rowSpan = 2 }

            score.frameScores.forEachIndexed { index, frameScore ->
                printFrameScore(frameScore, index == score.frameScores.lastIndex)
            }
            cell(score.total) {
                rowSpan = 2
            }
        }
        row {
            score.frameScores.forEach { frameScore ->
                cell(frameScore.subTotal) {
                    cellBorders = Borders.LEFT_RIGHT_BOTTOM
                    columnSpan = 3
                }
            }
        }
    }

    private fun RowBuilder.printFrameScore(score: FrameScore, isFinalFrameScore: Boolean) {
        if (isFinalFrameScore) {
            repeat(3) {
                cell(score.rolls.getOrNull(it).toDisplayString())
            }
        } else {
            cell(" ") {
                this.cellBorders = Borders.LEFT
            }
            cell(score.rolls.firstOrNull().toDisplayString())
            cell(score.rolls.getOrNull(1).toDisplayString())
        }
    }

    private fun Int?.toDisplayString(): String {
        return when (this) {
            null -> " "
            10   -> "╳"
            else -> "$this"
        }
    }
}
