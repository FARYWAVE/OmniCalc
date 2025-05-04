package com.example.omnicalc.ui.navigation


enum class Screen(val route: String) {
    Calc("calc"),
    FunctionSelector("function_selector"),
    Function("function/{id}"),
    ConvertorSelector("convertor_selector"),
    Convertor("convertor/{id}"),
    ColorPickerDialog("color_picker_dialog");

    fun withId(id: Int) : String {
        return route.replace("{id}", id.toString())
    }
    fun withId(id: String) : String {
        return route.replace("{id}", id)
    }
}