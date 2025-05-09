package com.example.omnicalc.ui.screens.convertor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.R
import com.example.omnicalc.ui.theme.FarywaveTypo
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.vw

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSelectorBar(type: Measurement.Type) {
    val viewModel: ConvertorViewModel = viewModel()
    val options = type.getUnits()
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selected1 by remember { mutableStateOf(options[0]) }
    var selected2 by remember { mutableStateOf(options[1]) }
    viewModel.updateFrom(selected1)
    viewModel.updateTo(selected2)

    Row {
        ExposedDropdownMenuBox(
            modifier = Modifier.unitSelectorBox(),
            expanded = expanded1,
            onExpandedChange = { expanded1 = it }
        ) {
            Surface(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxSize()
                    .clickable { expanded1 = true },
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 3.vw()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selected1.unitName,
                        style = FarywaveTypo.bodyLarge.merge(
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded1,
                onDismissRequest = { expanded1 = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selected1 = item
                            expanded1 = false
                            viewModel.updateFrom(item)
                        },
                        text = {
                            Text(
                                item.unitName,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            }
        }

        Button(
            onClick = {viewModel.swap()},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .height(16.666f.vw())
                .padding(0.dp)
                .aspectRatio(1f),
            shape = CutCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(
                painter = painterResource(if (viewModel.reversed) R.drawable.arrow_back else R.drawable.arrow_forward),
                contentDescription = "Direction",
                tint = MaterialTheme.colorScheme.background
            )
        }

        ExposedDropdownMenuBox(
            modifier = Modifier.unitSelectorBox(),
            expanded = expanded2,
            onExpandedChange = { expanded2 = it }
        ) {
            Surface(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxSize()
                    .clickable { expanded2 = true },
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 3.vw()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selected2.unitName,
                        style = FarywaveTypo.bodyLarge.merge(
                            textAlign = TextAlign.End,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = { expanded2 = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selected2 = item
                            expanded2 = false
                            viewModel.updateTo(item)
                        },
                        text = {
                            Text(
                                item.unitName,
                                fontSize = 16.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Modifier.unitSelectorBox() = this
    .width(41.666f.vw())
    .height(16.666f.vw())

@Preview
@Composable
fun Preview() {
    UnitSelectorBar(Measurement.Type.SPEED)
}