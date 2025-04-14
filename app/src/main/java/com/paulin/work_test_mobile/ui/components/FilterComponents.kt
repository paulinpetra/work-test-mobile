package com.paulin.work_test_mobile.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData

@Composable
fun FilterList(
    filters: List<FilterData>,
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
            FilterItem(filter = filter, onFilterClick = onFilterClick)
        }
    }
}

@Composable
fun FilterItem(
    filter: FilterData,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .clickable { onFilterClick(filter.id) }
    ) {
        AsyncImage(
            model = filter.imageUrl,
            contentDescription = filter.name,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = filter.name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}