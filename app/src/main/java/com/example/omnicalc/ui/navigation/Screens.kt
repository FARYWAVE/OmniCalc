package com.example.omnicalc.ui.navigation

import com.example.omnicalc.ui.components.ActionBarHandler


enum class Screen(val route: String) {
    Calc("calc"),
    FunctionSelector("function_selector"),
    Function("function/{id}"),
    ConvertorSelector("convertor_selector"),
    Convertor("convertor/{id}"),
    ColorPickerDialog("color_picker_dialog"),
    NewItemDialog("new_item_dialog"),
    ConfirmActionDialog("confirm_action_dialog/{vmID}, {key}, {item}"),
    RemoteFolderListDialog("remote_folder_list_dialog");

    fun withId(id: String) : String {
        return route.replace("{id}", id)
    }

    fun withData(vmID: String, key: ActionBarHandler.Key, itemName: String) : String {
        return route
            .replace("{vmID}", vmID)
            .replace("{key}", key.name)
            .replace("{item}", itemName)
    }
}