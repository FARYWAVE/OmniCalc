package com.example.omnicalc.utils

class Measurement(val type: Measurement.Type) {
    enum class Type {
        LENGTH,
        AREA,
        VOLUME,
        SPEED,
        MASS,
        TEMPERATURE,
        PRESSURE,
        POWER,
        ENERGY,
        TIME,
        DATA,
        NUMBER_SYSTEM,
        CURRENCY
    }
}
enum class Length(val unitName: String, val ratioToSI: Float) {
    METER("Meter", 1f),
    KILOMETER("Kilometer", 1000f),
    DECIMETER("Decimeter", 0.1f),
    CENTIMETER("Centimeter", 0.01f),
    MILLIMETER("Millimeter", 0.001f),
    MICROMETER("Micrometer", 1e-6f),
    NANOMETER("Nanometer", 1e-9f),
    MILE("Mile", 1609.34f),
    YARD("Yard", 0.9144f),
    FOOT("Foot", 0.3048f),
    INCH("Inch", 0.0254f),
    CHINESE_LI("Chinese Li", 500f),
    CHINESE_ZHANG("Chinese Zhang", 3f),
    CHINESE_CUN("Chinese Cun", 0.033f)
}

enum class Area(val unitName: String, val ratioToSI: Float) {
    SQUARE_METER("Square Meter", 1f),
    SQUARE_KILOMETER("Square Kilometer", 1e6f),
    HECTARE("Hectare", 10000f),
    ARE("Are", 100f),
    SQUARE_CENTIMETER("Square Centimeter", 1e-4f),
    SQUARE_MILLIMETER("Square Millimeter", 1e-6f),
    SQUARE_INCH("Square Inch", 0.00064516f),
    SQUARE_FOOT("Square Foot", 0.092903f),
    SQUARE_YARD("Square Yard", 0.836127f),
    SQUARE_MILE("Square Mile", 2.58999e6f),
    CHINESE_MU("Chinese Mu", 666.7f) // Chinese unit for area (Mu)
}

enum class Volume(val unitName: String, val ratioToSI: Float) {
    CUBIC_METER("Cubic Meter", 1f),
    LITER("Liter", 0.001f),
    MILLILITER("Milliliter", 1e-6f),
    CUBIC_KILOMETER("Cubic Kilometer", 1e9f),
    CUBIC_CENTIMETER("Cubic Centimeter", 1e-6f),
    CUBIC_MILLIMETER("Cubic Millimeter", 1e-9f),
    CUBIC_INCH("Cubic Inch", 1.63871e-5f),
    CUBIC_FOOT("Cubic Foot", 0.0283168f),
    CUBIC_YARD("Cubic Yard", 0.764555f),
    GALLON("Gallon", 0.00378541f),
    QUART("Quart", 9.4635e-4f),
    PINT("Pint", 4.7318e-4f),
    CHINESE_SHENG("Chinese Sheng", 0.002f)
}

enum class Speed(val unitName: String, val ratioToSI: Float) {
    METER_PER_SECOND("Meter per Second", 1f),
    KILOMETER_PER_HOUR("Kilometer per Hour", 1000f / 3600f),
    MILE_PER_HOUR("Mile per Hour", 1609.34f / 3600f),
    FOOT_PER_SECOND("Foot per Second", 0.3048f),
    KNOT("Knot", 1852f / 3600f),
    MACH("Mach", 343f),
    SPEED_OF_LIGHT("Speed of Light", 299792458f)
}


enum class Mass(val unitName: String, val ratioToSI: Float) {
    KILOGRAM("Kilogram", 1f),
    GRAM("Gram", 0.001f),
    MILLIGRAM("Milligram", 1e-6f),
    MICROGRAM("Microgram", 1e-9f),
    TONNE("Tonne", 1000f),
    POUND("Pound", 0.453592f),
    OUNCE("Ounce", 0.0283495f),
    STONE("Stone", 6.35029f),
    LONG_TON("Long Ton", 1016.05f),
    SHORT_TON("Short Ton", 907.184f),
    CARAT("Carat", 2e-5f),
    CHINESE_JIN("Chinese Jin", 0.5f) // Chinese unit for mass (Jin)
}

enum class Temperature(val unitName: String) {
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit"),
    KELVIN("Kelvin")
}

enum class Pressure(val unitName: String, val ratioToSI: Float) {
    PASCAL("Pascal", 1f),
    KILOPASCAL("Kilopascal", 1000f),
    BAR("Bar", 100000f),
    MILLIBAR("Millibar", 100f),
    ATMOSPHERE("Atmosphere", 101325f),
    TORR("Torr", 133.322f),
    PSI("PSI", 6894.76f),
    INCH_OF_MERCURY("Inch of Mercury", 3386.39f)
}

enum class Power(val unitName: String, val ratioToSI: Float) {
    WATT("Watt", 1f),
    KILOWATT("Kilowatt", 1000f),
    MEGAWATT("Megawatt", 1e6f),
    HORSEPOWER("Horsepower", 745.7f),
    BTU_PER_HOUR("BTU per Hour", 0.293071f),
    CALORIE_PER_SECOND("Calorie per Second", 4.184f),
    JOULE_PER_SECOND("Joule per Second", 1f)
}

enum class Energy(val unitName: String, val ratioToSI: Float) {
    JOULE("Joule", 1f),
    KILOJOULE("Kilojoule", 1000f),
    CALORIE("Calorie", 4.184f),
    KILOCALORIE("Kilocalorie", 4184f),
    ELECTRON_VOLT("Electron Volt", 1.60218e-19f),
    WATT_HOUR("Watt Hour", 3600f)
}

enum class Time(val unitName: String, val ratioToSI: Float) {
    SECOND("Second", 1f),
    MINUTE("Minute", 60f),
    HOUR("Hour", 3600f),
    DAY("Day", 86400f),
    WEEK("Week", 604800f)
}

enum class Data(val unitName: String, val ratioToSI: Float) {
    BYTE("Byte", 1f),
    KILOBYTE("Kilobyte", 1024f),
    MEGABYTE("Megabyte", 1048576f), // 1024 * 1024
    GIGABYTE("Gigabyte", 1073741824f), // 1024 * 1024 * 1024
    TERABYTE("Terabyte", 1099511627776f) // 1024 * 1024 * 1024 * 1024
}

enum class NumberSystem(val systemName: String) {
    BINARY("Binary"),
    OCTAL("Octal"),
    DECIMAL("Decimal"),
    HEXADECIMAL("Hexadecimal")
}

enum class Currency(val currencyName: String) {
    USD("US Dollar"),
    EUR("Euro"),
    GBP("British Pound"),
    JPY("Japanese Yen"),
    AUD("Australian Dollar"),
    CAD("Canadian Dollar"),
    CHF("Swiss Franc"),
    CNY("Chinese Yuan"),
    INR("Indian Rupee"),
    RUB("Russian Ruble"),
    BRL("Brazilian Real"),
    ZAR("South African Rand")
}







