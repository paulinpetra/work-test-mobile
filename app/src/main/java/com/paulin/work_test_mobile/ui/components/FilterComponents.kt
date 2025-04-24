package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.ui.theme.SelectedFilterBgColor
import com.paulin.work_test_mobile.ui.theme.SelectedFilterTextColor
import com.paulin.work_test_mobile.ui.theme.UnselectedFilterBgColor
import com.paulin.work_test_mobile.ui.theme.UnselectedFilterTextColor

@Composable
fun FilterList(
    filters: List<FilterData>,
    activeFilterIds: Set<String>,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterItem(
                filter = filter, isSelected = filter.id in activeFilterIds, onFilterClick = onFilterClick
            )
        }
    }
}

@Composable
fun FilterItem(
    filter: FilterData, isSelected: Boolean, onFilterClick: (String) -> Unit, modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        SelectedFilterBgColor
    } else {
        UnselectedFilterBgColor
    }

    val textColor = if (isSelected) {
        SelectedFilterTextColor
    } else {
        UnselectedFilterTextColor
    }


    Button(
        onClick = { onFilterClick(filter.id) }, modifier = modifier
            .padding(4.dp)
            .semantics {
                contentDescription = "Filter by ${filter.name}"
            }, shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp, pressedElevation = 12.dp, disabledElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            AsyncImage(
                model = filter.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = filter.name, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp, end = 16.dp)
            )
        }
    }
}