package com.tpc.trikride.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import kotlin.math.ceil
import kotlin.math.max

/**
 * Chart drawing for the exported PDF reports.
 *
 * The reports print on white paper, so everything here is measured against a
 * white surface rather than the app's theme. Marks are thin, gridlines are
 * hairlines a single step off the surface, and every bar carries its own value
 * as a direct label — on paper there is no tooltip to fall back on.
 *
 * Every chart is a single series in one hue. That is not a stylistic choice: a
 * bar chart whose categories have no natural order should use one colour for
 * every bar, because colouring each bar by its own size just re-encodes the
 * length that is already there. It also keeps the palette clear of the
 * colour-blindness problems that come with putting several hues side by side.
 */
object PdfChart {

    // --- Ink -----------------------------------------------------------------
    // Validated against a white surface: the series green clears 3:1 contrast,
    // sits inside the lightness band, and meets the chroma floor.
    const val SERIES = 0xFF16A34A.toInt()
    const val INK_PRIMARY = 0xFF0B0B0B.toInt()
    const val INK_SECONDARY = 0xFF52514E.toInt()
    const val INK_MUTED = 0xFF898781.toInt()
    const val GRIDLINE = 0xFFE1E0D9.toInt()
    const val AXIS = 0xFFC3C2B7.toInt()
    const val SURFACE = Color.WHITE
    const val TILE_FILL = 0xFFF4F7F5.toInt()

    /** A crowded axis holds bars to this width; a sparse one is allowed to grow. */
    private const val BAR_BASE_THICKNESS = 22f
    private const val BAR_MAX_THICKNESS = 54f
    private const val DATA_END_RADIUS = 4f
    private const val SURFACE_GAP = 2f

    /**
     * Bar thickness for a given slot.
     *
     * A chart of four categories across half a landscape page has slots wide
     * enough that a 22-point bar reads as a stray tick rather than a quantity,
     * so thickness rises with the slot up to a ceiling. Crowded axes are
     * unaffected: thirty-one days still land on the base width or below.
     */
    private fun barThickness(slot: Float, floor: Float): Float {
        val target = max(BAR_BASE_THICKNESS, minOf(BAR_MAX_THICKNESS, slot * 0.55f))
        return max(floor, minOf(target, slot - SURFACE_GAP * 2))
    }

    /** Rounding grows a little with the bar, so a wide bar is not squared off. */
    private fun endRadius(thickness: Float) = minOf(6f, max(DATA_END_RADIUS, thickness * 0.14f))

    fun paint(
        color: Int,
        size: Float = 9f,
        bold: Boolean = false,
        align: Paint.Align = Paint.Align.LEFT
    ) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        textSize = size
        textAlign = align
        typeface = Typeface.create(Typeface.SANS_SERIF, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun fill(color: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    private fun hairline(color: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = 0.6f
    }

    /** A headline number with its label. The number is the chart. */
    fun statTile(canvas: Canvas, area: RectF, label: String, value: String, note: String? = null) {
        canvas.drawRoundRect(area, 6f, 6f, fill(TILE_FILL))
        val x = area.left + 10f
        canvas.drawText(label.uppercase(), x, area.top + 15f, paint(INK_MUTED, 6.5f, bold = true))
        canvas.drawText(value, x, area.top + 34f, paint(INK_PRIMARY, 17f, bold = true))
        note?.let { canvas.drawText(it, x, area.top + 46f, paint(INK_SECONDARY, 6.5f)) }
    }

    /**
     * Vertical columns over an ordered axis — days of a month, hours of a day.
     *
     * Only every nth tick is labelled when the axis is crowded, because a label
     * on every column is unreadable and a rotated label is worse.
     */
    fun columnChart(
        canvas: Canvas,
        area: RectF,
        title: String,
        subtitle: String,
        values: List<Int>,
        labels: List<String>,
        labelEvery: Int = 1
    ) {
        drawFrameTitle(canvas, area, title, subtitle)
        val plot = plotArea(area)
        if (values.isEmpty() || values.all { it == 0 }) {
            drawEmpty(canvas, plot)
            return
        }

        val maxValue = niceCeiling(values.max())
        drawYGrid(canvas, plot, maxValue)

        val slot = plot.width() / values.size
        val thickness = barThickness(slot, floor = 2f)

        values.forEachIndexed { i, v ->
            if (v <= 0) return@forEachIndexed
            val centre = plot.left + slot * (i + 0.5f)
            val h = plot.height() * (v.toFloat() / maxValue)
            val bar = RectF(centre - thickness / 2, plot.bottom - h, centre + thickness / 2, plot.bottom)
            drawColumn(canvas, bar)
        }

        // Axis labels below the baseline.
        labels.forEachIndexed { i, label ->
            if (i % labelEvery != 0) return@forEachIndexed
            canvas.drawText(
                label,
                plot.left + slot * (i + 0.5f),
                plot.bottom + 11f,
                paint(INK_MUTED, 6.5f, align = Paint.Align.CENTER)
            )
        }
        canvas.drawLine(plot.left, plot.bottom, plot.right, plot.bottom, hairline(AXIS))
    }

    /**
     * Horizontal bars for named categories, longest first. Horizontal because
     * the names are long enough that vertical columns would need rotated
     * labels, and a rotated label is a label nobody reads.
     */
    fun barChart(
        canvas: Canvas,
        area: RectF,
        title: String,
        subtitle: String,
        rows: List<Pair<String, Int>>,
        valueSuffix: String = "",
        axisMax: Int? = null
    ) {
        drawFrameTitle(canvas, area, title, subtitle)
        val plot = plotArea(area, bottomAxis = false)
        if (rows.isEmpty()) {
            drawEmpty(canvas, plot)
            return
        }

        // A percentage chart is measured against its own ceiling, not against
        // the best row, or a field where everyone finished 95% of their rides
        // would draw one full bar and four short ones.
        val maxValue = max(1, axisMax ?: rows.maxOf { it.second })
        val slot = plot.height() / rows.size
        val thickness = barThickness(slot, floor = 3f)
        // Names on the left, values direct-labelled at the end of each bar.
        val nameWidth = plot.width() * 0.42f
        val trackLeft = plot.left + nameWidth + 6f
        val trackWidth = plot.right - trackLeft - 26f

        rows.forEachIndexed { i, (name, value) ->
            val centreY = plot.top + slot * (i + 0.5f)
            val namePaint = paint(INK_SECONDARY, 7f)
            canvas.drawText(
                ellipsize(name, namePaint, nameWidth),
                plot.left, centreY + 2.5f, namePaint
            )

            val w = trackWidth * (value.toFloat() / maxValue)
            if (w > 0.5f) {
                val bar = RectF(trackLeft, centreY - thickness / 2, trackLeft + w, centreY + thickness / 2)
                drawBarRightRounded(canvas, bar)
            }
            canvas.drawText(
                "$value$valueSuffix",
                trackLeft + w + 5f, centreY + 2.5f,
                paint(INK_PRIMARY, 7f, bold = true)
            )
        }
    }

    // --- internals -----------------------------------------------------------

    private fun drawFrameTitle(canvas: Canvas, area: RectF, title: String, subtitle: String) {
        canvas.drawText(title, area.left, area.top + 9f, paint(INK_PRIMARY, 9f, bold = true))
        if (subtitle.isNotBlank()) {
            canvas.drawText(subtitle, area.left, area.top + 19f, paint(INK_MUTED, 6.5f))
        }
    }

    private fun plotArea(area: RectF, bottomAxis: Boolean = true) = RectF(
        area.left,
        area.top + 26f,
        area.right,
        area.bottom - if (bottomAxis) 14f else 0f
    )

    /** Square at the baseline, rounded at the data end. */
    private fun drawColumn(canvas: Canvas, bar: RectF) {
        val r = minOf(endRadius(bar.width()), bar.width() / 2f, bar.height())
        canvas.drawRoundRect(bar, r, r, fill(SERIES))
        canvas.drawRect(
            RectF(bar.left, bar.bottom - r, bar.right, bar.bottom), fill(SERIES)
        )
    }

    private fun drawBarRightRounded(canvas: Canvas, bar: RectF) {
        val r = minOf(endRadius(bar.height()), bar.height() / 2f, bar.width())
        canvas.drawRoundRect(bar, r, r, fill(SERIES))
        canvas.drawRect(RectF(bar.left, bar.top, bar.left + r, bar.bottom), fill(SERIES))
    }

    private fun drawYGrid(canvas: Canvas, plot: RectF, maxValue: Int) {
        val steps = 4
        repeat(steps + 1) { i ->
            val y = plot.bottom - plot.height() * i / steps
            if (i > 0) canvas.drawLine(plot.left, y, plot.right, y, hairline(GRIDLINE))
            canvas.drawText(
                (maxValue * i / steps).toString(),
                plot.left - 4f, y + 2.5f,
                paint(INK_MUTED, 6.5f, align = Paint.Align.RIGHT)
            )
        }
    }

    private fun drawEmpty(canvas: Canvas, plot: RectF) {
        canvas.drawText(
            "No activity in this period",
            plot.centerX(), plot.centerY(),
            paint(INK_MUTED, 7.5f, align = Paint.Align.CENTER)
        )
    }

    /**
     * Picks an axis maximum that divides evenly by the number of gridlines, so
     * the ticks come out as whole round numbers rather than 7, 15, 22, 30.
     */
    private fun niceCeiling(value: Int, steps: Int = 4): Int {
        if (value <= steps) return steps
        val candidates = intArrayOf(
            1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 20, 25, 30, 40, 50, 60, 75,
            100, 125, 150, 200, 250, 300, 400, 500, 750, 1000, 2000, 5000
        )
        val needed = value.toDouble() / steps
        val step = candidates.firstOrNull { it >= needed } ?: ceil(needed / 1000).toInt() * 1000
        return step * steps
    }

    fun ellipsize(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var cut = text.length
        while (cut > 1 && paint.measureText(text.take(cut) + "…") > maxWidth) cut--
        return text.take(cut) + "…"
    }

    fun textBounds(text: String, paint: Paint): Rect =
        Rect().also { paint.getTextBounds(text, 0, text.length, it) }
}
