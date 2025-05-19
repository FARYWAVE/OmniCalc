package com.example.omnicalc.utils
import android.util.Log
import com.example.omnicalc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

interface MeasurementUnit {
    val ratioToSI: Double
    val unitName: String
}


class Measurement {
    companion object {
        fun getType(name: String) : Type {
            for (unit in Type.entries.toTypedArray()) {
                if (unit.typeName == name) return unit
            }
            throw Exception("No such unit named $name")
        }

        private fun <T> convert(value: Double, from: T, to: T): Double where T : Enum<T>, T : MeasurementUnit {
            return value * from.ratioToSI / to.ratioToSI
        }

        suspend fun convert(
            value: Double,
            from: MeasurementUnit,
            to: MeasurementUnit,
            type: Measurement.Type
        ): Double {
            try {
                return when (type) {
                    Measurement.Type.LENGTH -> Measurement.convert(value, from as Length, to as Length)
                    Measurement.Type.AREA -> Measurement.convert(value, from as Area, to as Area)
                    Measurement.Type.SPEED -> Measurement.convert(value, from as Speed, to as Speed)
                    Measurement.Type.VOLUME -> Measurement.convert(value, from as Volume, to as Volume)
                    Measurement.Type.MASS -> Measurement.convert(value, from as Mass, to as Mass)
                    Measurement.Type.TEMPERATURE -> Measurement.convert(value, from as Temperature, to as Temperature)
                    Measurement.Type.PRESSURE -> Measurement.convert(value, from as Pressure, to as Pressure)
                    Measurement.Type.POWER -> Measurement.convert(value, from as Power, to as Power)
                    Measurement.Type.ENERGY -> Measurement.convert(value, from as Energy, to as Energy)
                    Measurement.Type.TIME -> Measurement.convert(value, from as Time, to as Time)
                    Measurement.Type.DATA -> Measurement.convert(value, from as Data, to as Data)
                    Measurement.Type.NUMBER_SYSTEM -> NumberSystemConverter.convert(from as NumberSystem, to as NumberSystem, value)
                    Measurement.Type.CURRENCY -> {
                        Log.d("Measurement", "conversion called")
                        coroutineScope {
                            CurrencyParser.convertCurrency(
                                (from as Currency).toString(),
                                (to as Currency).toString(),
                                value
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("EXCEPTION IN MEASUREMENT", "${e.cause}\n${e.message}")
                return 0.0
            }
        }
    }



    enum class Type(
        val typeName: String,
        val iconResId: Int,
        val unitEnum: Class<out Enum<*>>
    ) {
        LENGTH("Length", R.drawable.length, Length::class.java),
        AREA("Area", R.drawable.area, Area::class.java),
        SPEED("Speed", R.drawable.speed, Speed::class.java),
        VOLUME("Volume", R.drawable.volume, Volume::class.java),
        MASS("Mass", R.drawable.mass, Mass::class.java),
        TEMPERATURE("Temperature", R.drawable.temp, Temperature::class.java),
        PRESSURE("Pressure", R.drawable.pressure, Pressure::class.java),
        POWER("Power", R.drawable.power, Power::class.java),
        ENERGY("Energy", R.drawable.energy, Energy::class.java),
        TIME("Time", R.drawable.time, Time::class.java),
        DATA("Data", R.drawable.data, Data::class.java),
        NUMBER_SYSTEM("Number System", R.drawable.number_system, NumberSystem::class.java),
        CURRENCY("Currency", R.drawable.currency, Currency::class.java);

        // Helper function to get all units for this type
        fun getUnits(): Array<MeasurementUnit> {
            return unitEnum.enumConstants?.filterIsInstance<MeasurementUnit>()?.toTypedArray()
                ?: emptyArray()

        }
    }

}
enum class Length(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    METER("Meter", 1.0),
    KILOMETER("Kilometer", 1000.0),
    DECIMETER("Decimeter", 0.1),
    CENTIMETER("Centimeter", 0.01),
    MILLIMETER("Millimeter", 0.001),
    MICROMETER("Micrometer", 1e-6),
    NANOMETER("Nanometer", 1e-9),
    MILE("Mile", 1609.34),
    YARD("Yard", 0.9144),
    FOOT("Foot", 0.3048),
    INCH("Inch", 0.0254),
    CHINESE_LI("Chinese Li", 500.0),
    CHINESE_ZHANG("Chinese Zhang", 3.0),
    CHINESE_CUN("Chinese Cun", 0.033)
}

enum class Area(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    SQUARE_METER("Square Meter", 1.0),
    SQUARE_KILOMETER("Square Kilometer", 1e6),
    HECTARE("Hectare", 10000.0),
    ARE("Are", 100.0),
    SQUARE_CENTIMETER("Square Centimeter", 1e-4),
    SQUARE_MILLIMETER("Square Millimeter", 1e-6),
    SQUARE_INCH("Square Inch", 0.00064516),
    SQUARE_FOOT("Square Foot", 0.092903),
    SQUARE_YARD("Square Yard", 0.836127),
    SQUARE_MILE("Square Mile", 2.58999e6),
    CHINESE_MU("Chinese Mu", 666.7)
}

enum class Volume(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    CUBIC_METER("Cubic Meter", 1.0),
    LITER("Liter", 0.001),
    MILLILITER("Milliliter", 1e-6),
    CUBIC_KILOMETER("Cubic Kilometer", 1e9),
    CUBIC_CENTIMETER("Cubic Centimeter", 1e-6),
    CUBIC_MILLIMETER("Cubic Millimeter", 1e-9),
    CUBIC_INCH("Cubic Inch", 1.63871e-5),
    CUBIC_FOOT("Cubic Foot", 0.0283168),
    CUBIC_YARD("Cubic Yard", 0.764555),
    GALLON("Gallon", 0.00378541),
    QUART("Quart", 9.4635e-4),
    PINT("Pint", 4.7318e-4),
    CHINESE_SHENG("Chinese Sheng", 0.002)
}

enum class Speed(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    METER_PER_SECOND("Meter per Second", 1.0),
    KILOMETER_PER_HOUR("Kilometer per Hour", 1000.0 / 3600.0),
    MILE_PER_HOUR("Mile per Hour", 1609.34 / 3600.0),
    FOOT_PER_SECOND("Foot per Second", 0.3048),
    KNOT("Knot", 1852.0 / 3600.0),
    MACH("Mach", 343.0),
    SPEED_OF_LIGHT("Speed of Light", 299792458.0)
}

enum class Mass(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    KILOGRAM("Kilogram", 1.0),
    GRAM("Gram", 0.001),
    MILLIGRAM("Milligram", 1e-6),
    MICROGRAM("Microgram", 1e-9),
    TONNE("Tonne", 1000.0),
    POUND("Pound", 0.453592),
    OUNCE("Ounce", 0.0283495),
    STONE("Stone", 6.35029),
    LONG_TON("Long Ton", 1016.05),
    SHORT_TON("Short Ton", 907.184),
    CARAT("Carat", 2e-5),
    CHINESE_JIN("Chinese Jin", 0.5)
}

enum class Pressure(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    PASCAL("Pascal", 1.0),
    KILOPASCAL("Kilopascal", 1000.0),
    BAR("Bar", 100000.0),
    MILLIBAR("Millibar", 100.0),
    ATMOSPHERE("Atmosphere", 101325.0),
    TORR("Torr", 133.322),
    PSI("PSI", 6894.76),
    INCH_OF_MERCURY("Inch of Mercury", 3386.39)
}

enum class Power(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    WATT("Watt", 1.0),
    KILOWATT("Kilowatt", 1000.0),
    MEGAWATT("Megawatt", 1e6),
    HORSEPOWER("Horsepower", 745.7),
    BTU_PER_HOUR("BTU per Hour", 0.293071),
    CALORIE_PER_SECOND("Calorie per Second", 4.184),
    JOULE_PER_SECOND("Joule per Second", 1.0)
}

enum class Energy(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    JOULE("Joule", 1.0),
    KILOJOULE("Kilojoule", 1000.0),
    CALORIE("Calorie", 4.184),
    KILOCALORIE("Kilocalorie", 4184.0),
    ELECTRON_VOLT("Electron Volt", 1.60218e-19),
    WATT_HOUR("Watt Hour", 3600.0)
}

enum class Time(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    SECOND("Second", 1.0),
    MINUTE("Minute", 60.0),
    HOUR("Hour", 3600.0),
    DAY("Day", 86400.0),
    WEEK("Week", 604800.0)
}

enum class Data(override val unitName: String, override val ratioToSI: Double) : MeasurementUnit {
    BYTE("Byte", 1.0),
    KILOBYTE("Kilobyte", 1024.0),
    MEGABYTE("Megabyte", 1048576.0),
    GIGABYTE("Gigabyte", 1073741824.0),
    TERABYTE("Terabyte", 1099511627776.0)
}

enum class NumberSystem(override val unitName: String, override val ratioToSI: Double = 0.0) : MeasurementUnit {
    BINARY("Binary", 2.0),
    OCTAL("Octal", 8.0),
    DECIMAL("Decimal", 10.0),
    HEXADECIMAL("Hexadecimal", 16.0)
}

enum class Temperature(override val unitName: String, override val ratioToSI: Double = 0.0) : MeasurementUnit {
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit"),
    KELVIN("Kelvin")
}

enum class Currency(private val code: String, override val unitName: String, override val ratioToSI: Double = 0.0) : MeasurementUnit {
    USD("USD", "US Dollar"),
    EUR("EUR", "Euro"),
    GBP("GBP", "British Pound"),
    JPY("JPY", "Japanese Yen"),
    AUD("AUD", "Australian Dollar"),
    CAD("CAD", "Canadian Dollar"),
    CHF("CHF", "Swiss Franc"),
    CNY("CNY", "Chinese Yuan"),
    INR("INR", "Indian Rupee"),
    RUB("RUB", "Russian Ruble"),
    BRL("BRL", "Brazilian Real"),
    ZAR("ZAR", "South African Rand");

    override fun toString(): String = code
}







