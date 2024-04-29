package com.squirrel.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squirrel.utils.Categories
import com.squirrel.utils.Constants
import com.squirrel.utils.R
import com.squirrel.utils.formatDate
import com.squirrel.utils.formatDouble
import com.squirrel.utils.types.Transaction
import com.squirrel.utils.ui.ItemAccounts
import com.squirrel.utils.ui.SquirrelAlertDialog
import com.squirrel.utils.ui.TransactionCategory

@Composable
fun HomeItemDetail(
    item: Transaction,
    previousItem: Transaction,
    temporaryMoney: String,
    alertDialogShow: Boolean = false,
    toggleAccount: (account: Int) -> Unit,
    toggleAlertDialogShow: ((show: Boolean) -> Unit) = {},
    toggleItemMoney: (money: String) -> Unit,
    toggleItemRemark: (remark: String) -> Unit,
    toggleDatePickerShow: ((show: Boolean) -> Unit) = {},
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        ItemAccounts(
            accountIndex = item.account, toggleAccount = toggleAccount
        )

        TransactionCategory(modifier = Modifier.size(50.dp), category = Categories[item.category])


        /* 输入金额 */
        HomeItemDetailMoneyTextField(
            money = previousItem.amount,
            temporaryMoney = temporaryMoney,
            toggleItemMoney = { input ->
                if (input.length < 12) {
                    toggleItemMoney(input)
                }
            })


        /* 输入备注 */
        HomeItemDetailRemarkTextField(
            remark = item.remark, toggleDetailRemark = toggleItemRemark
        )


        /* 日期 */
        Row(
            modifier = Modifier.clickable(
                enabled = onDelete == null, interactionSource = interactionSource, indication = null
            ) { toggleDatePickerShow(true) }, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.calendar_thin),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatDate(
                    year = item.year,
                    month = item.month,
                    day = item.day,
                    time = item.time,
                    formatter = Constants.YEAR_MONTH_DAY
                ),
                color = MaterialTheme.colorScheme.primary,
                textDecoration = if (onDelete == null) TextDecoration.Underline else null
            )
        }

        /* 删除按钮 */
        onDelete?.let {
            Row(
                Modifier.clickable(
                    interactionSource = interactionSource, indication = null
                ) { toggleAlertDialogShow(true) }, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "删除",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }


        /* 保存按钮 */
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .clickable { onSave() }, horizontalArrangement = Arrangement.Center
        ) {
            Box(Modifier.padding(10.dp)) {
                Text(text = "保存", color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.size(15.dp))

        if (alertDialogShow && onDelete != null) {
            SquirrelAlertDialog(title = "删除支出项",
                text = "此操作会永久删除支出项，确定吗？",
                onConfirmRequest = { onDelete() },
                onDismissRequest = { toggleAlertDialogShow(false) })
        }
    }
}

@Composable
fun HomeItemDetailMoneyTextField(
    money: Double,
    temporaryMoney: String,
    toggleItemMoney: (money: String) -> Unit,
) {

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "¥",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        HomeItemDetailTextField(
            text = temporaryMoney, style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ), decorationBox = { innerTextField ->
                if (temporaryMoney.isEmpty()) {
                    Text(
                        text = formatDouble(money),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                innerTextField()
            }, modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            toggleItemMoney(it)
        }

    }
}

@Composable
fun HomeItemDetailRemarkTextField(remark: String, toggleDetailRemark: (remark: String) -> Unit) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        HomeItemDetailTextField(text = remark.ifEmpty { if (isFocused) "" else "备注" },
            style = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }) {
            toggleDetailRemark(it)
        }
    }
}

@Composable
fun HomeItemDetailTextField(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    decorationBox: @Composable (@Composable () -> Unit) -> Unit = @Composable { innerTextField -> innerTextField() },
    onChangeCallBack: (value: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    BasicTextField(value = text,
        textStyle = style,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        decorationBox = decorationBox,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        onValueChange = { onChangeCallBack(it) })
}


