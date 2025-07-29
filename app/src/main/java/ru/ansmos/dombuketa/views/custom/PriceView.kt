package ru.ansmos.dombuketa.views.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import ru.ansmos.dombuketa.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class PriceView @JvmOverloads constructor(context: Context, attrSet: AttributeSet? = null) : View(context, attrSet){
    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()
    private val rect = RectF()
    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var widthView: Float = 0f
    private var heightView: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    //Толщина линии прогресса
    private var stroke = 10f
    //Значение прогресса от 0 - 100
    private var progress = 50
    private var price = 1500
    private var pricePrev = 1750
    //Значения размера текста внутри кольца
    private var scaleSizeText = 50f
    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var pricePaint: Paint
    private lateinit var pricePrevPaint: Paint
    private lateinit var circlePaint: Paint

    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a = context.theme.obtainStyledAttributes(attrSet, R.styleable.RatingDonutView, 0, 0)
        try{
            stroke = a.getFloat(R.styleable.RatingDonutView_stroke, stroke)
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
            price = a.getInt(R.styleable.RatingDonutView_price, price)
            pricePrev = a.getInt(R.styleable.RatingDonutView_pricePrev, pricePrev)
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    override fun onDraw(canvas: Canvas?) {
        //Рисуем кольцо и задний фон
        drawRating(canvas)
        //Рисуем цифры
        drawText(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = if (width > height){
            height.div(2f)
        } else {
            width.div(2f)
        }
        widthView = width.toFloat()
        heightView = height.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)
        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)
        setMeasuredDimension(minSide,minSide)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode){
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    private fun initPaint(){
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        pricePaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            //textSize = scaleSizeText
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        pricePrevPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSizeText
            textSkewX = -0.3f
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
            isStrikeThruText = true
        }

        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when(progress){
        in 0..25 -> Color.parseColor("#e84258")
        in 26..50 -> Color.parseColor("#fd8060")
        in 51..75 -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }

    private fun drawRating(canvas: Canvas?){
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * .8f
        //Сохраняем канвас
        canvas?.save()
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        //canvas?.translate(centerX,centerY)
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        //canvas?.drawCircle(0f, 0f, radius, circlePaint)

        val x = 5f ; val y = 5f ; val padding = 1f
        val halfWidth = rect.width() / 1f
        val halfHeight = rect.height() / 2f
        val left = x //- halfWidth - padding
        val top = y //- halfHeight - padding
        val right = x + 500f //rect.width()
        val bottom = y + 250f //halfHeight //+ padding


        rect.set(left, top, widthView, heightView)
        //rect.set(5f, 5f, 50f, 500f)
        canvas?.drawRoundRect(rect, 50f, 50f, strokePaint)


        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        //canvas?.drawArc(oval, -90f, convertProgressToDegrees(progress), false, strokePaint)
        //Восстанавливаем канвас
        canvas?.restore()
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * 3.6f

    private fun drawText(canvas: Canvas?){
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
        val message = String.format("%.1f", progress / 10f)
        val messagePrice = price.toString()
        val messagePricePrev = DecimalFormat("#,###", DecimalFormatSymbols(Locale.GERMANY))
            .format(pricePrev).replace("."," ")

        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widthsPrice = FloatArray(messagePrice.length)
        val widthsPricePrev = FloatArray(messagePricePrev.length)
        pricePaint.getTextWidths(messagePrice, widthsPrice)
        pricePrevPaint.getTextWidths(messagePricePrev, widthsPricePrev)
        var advance = 0f
        for (width in widthsPrice) advance += width
        pricePaint.textSize = 100f
        //pricePaint.isStrikeThruText = true
        //Рисуем наш текст
        canvas?.drawText(message, centerX - advance / 2, centerY + advance / 4, pricePaint)
        canvas?.drawText(messagePricePrev, 100f, 100f, pricePrevPaint)
        canvas?.drawText(messagePrice, 200f, 200f, pricePaint)
    }

    fun setProgress(rating: Int){
        progress = rating  //Кладем новое значение в наше поле класса
        initPaint()  //Создаем краски с новыми цветами
        invalidate() //вызываем перерисовку View
    }
}


