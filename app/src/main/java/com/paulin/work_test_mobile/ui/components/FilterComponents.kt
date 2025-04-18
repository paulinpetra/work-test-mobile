package com.paulin.work_test_mobile.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.ui.theme.selectedFilterBgColor

@Composable
fun FilterList(
    filters: List<FilterData>,
    activeFilterId: String?,
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
                isSelected = filter.id == activeFilterId,
                onFilterClick = onFilterClick

            )
        }
    }
}

@Composable
fun FilterItem(
    filter: FilterData,
    isSelected: Boolean, // to show selected state
    onFilterClick: (String) -> Unit, //takes string as input, returns void (but in kotlin an actual type) to satisfy type system even if this does not need to return anything
    modifier: Modifier = Modifier
) {
    // change colors based on selection state
    val backgroundColor = if (isSelected) {
        selectedFilterBgColor
    } else {
        Color.White.copy(alpha = 0.4f)
    }
    val textColor = if (isSelected) {
        Color.White
    } else {
        Color.Black
    }
    Surface(//styled container
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.04f)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable { onFilterClick(filter.id) },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(32.dp),
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
