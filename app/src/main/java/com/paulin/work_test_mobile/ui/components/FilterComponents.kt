package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.ui.theme.FilterShadowColor
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterItem(
                filter = filter,
                isSelected = filter.id in activeFilterIds,
                onFilterClick = onFilterClick
            )
        }
    }
}

@Composable
fun FilterItem(
    filter: FilterData,
    isSelected: Boolean,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier
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

    Box(
        modifier = modifier
            .padding(4.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = FilterShadowColor,
                clip = false,
                ambientColor = FilterShadowColor

            )
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onFilterClick(filter.id) }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = filter.imageUrl,
                contentDescription = filter.name,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp)
            )

            Text(
                text = filter.name,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
    }
}