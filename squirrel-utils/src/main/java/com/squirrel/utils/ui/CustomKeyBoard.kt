package com.squirrel.utils.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squirrel.utils.Constants
import com.squirrel.utils.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomKeyBoard(
    initialValue: String, handleClose: () -> Unit, handleInput: (it: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                handleClose()
            }
        }) {
        MyKeyBoard(initialValue = initialValue, handleClose = {
            coroutineScope.launch {
                sheetState.hide()
                handleClose()
            }
        }) {
            handleInput(it)
        }
        Spacer(
            Modifier.navigationBarsPadding()
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyKeyBoard(initialValue: String, handleClose: () -> Unit, handleInput: (it: String) -> Unit) {
    val placeholderValue by remember {
        mutableStateOf(initialValue)
    }
    var inputValue by remember {
        mutableStateOf(Constants.EMPTY_STRING)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "输入金额",
                fontSize = 24.sp,
                fontWeight = FontWeight.W800,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (inputValue.isEmpty()) {
                // 占位字符，显示已有的金额；当输入后显示输入的金额
                Text(
                    text = "¥${placeholderValue}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W800,
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                Text(
                    text = "¥${inputValue}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W800,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSecondary)
        ) {
            Column(Modifier.weight(1f)) {
                Row {
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "1",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "2",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "3",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                }
                Row {
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "4",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "5",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "6",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })

                }
                Row {
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "7",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "8",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "9",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = ".",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })
                    KeyBoardKey(modifier = Modifier.weight(1f),
                        text = "0",
                        previousValue = inputValue,
                        handlePress = {
                            inputValue = it
                        })

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .combinedClickable(onClick = {
                                inputValue = inputValue
                                    .dropLast(1)

                            }, onLongClick = {
                                inputValue = "0"
                            }), contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.padding(vertical = 18.dp)) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.backspace),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
        }

        /* 按钮 */
        MyConfirmButton(text = "确定", enabled = inputValue != "0") {
            handleClickKeyBoardButton(value = inputValue) {
                handleInput(it)
                handleClose()
            }
        }
    }
}

@Composable
fun KeyBoardKey(
    modifier: Modifier, text: String, previousValue: String, handlePress: (it: String) -> Unit
) {
    Box(
        modifier = modifier.clickable {
            handlePress(keyBoardInputLogic(previousValue = previousValue, inputValue = text))
        }, contentAlignment = Alignment.Center
    ) {
        Box(modifier = modifier.padding(vertical = 18.dp)) {
            Text(
                text = text,
                fontSize = 25.sp,
                maxLines = 1,
                softWrap = false,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

fun keyBoardInputLogic(previousValue: String, inputValue: String): String {
    if (previousValue.contains(".")) {

        // 已经包含. 输入的还是.就返回
        if (inputValue == ".") return previousValue
        // 包含.情况下，位数限制在两位
        if (previousValue.split(".")[1].length !in 0..1) return previousValue
    } else {
        // 不包含.情况下，大于7
        if (previousValue.length > 7 && inputValue != ".") return previousValue
    }
    return if (previousValue == "0" && inputValue != ".") { // 如果初始是0就替换
        previousValue.replace(previousValue, inputValue)
    } else {
        previousValue + inputValue
    }
}

fun handleClickKeyBoardButton(value: String, callBack: (it: String) -> Unit) {
    var lastValue = ""
    if (value.contains(".")) {
        val split = value.split(".")[1]
        when (split.length) {
            0 -> lastValue = value + "00"
            1 -> lastValue = value + "0"
            2 -> lastValue = value
        }
    } else {
        lastValue = "$value.00"
    }
    callBack(lastValue)
}