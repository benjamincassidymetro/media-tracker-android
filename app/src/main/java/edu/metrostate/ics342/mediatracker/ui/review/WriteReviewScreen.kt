package edu.metrostate.ics342.mediatracker.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.creatorCredit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    viewModel: WriteReviewViewModel = viewModel()
) {
    val media by viewModel.media.collectAsState()
    val rating by viewModel.rating.collectAsState()
    val reviewText by viewModel.reviewText.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    LaunchedEffect(mediaId) {
        viewModel.loadMedia(mediaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.media_write_review)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            media?.let { m ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = m.coverUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp, 90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = m.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = m.creatorCredit(LocalContext.current),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            Text(
                text = "How would you rate this?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Star Rating Selector
            StarRatingSelector(
                rating = rating,
                onRatingChange = viewModel::onRatingChange
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = viewModel::onReviewTextChange,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                placeholder = { Text("Write your thoughts here...") },
                label = { Text("Your Review") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    viewModel.submitReview(mediaId) {
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = rating > 0 && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Submit Review")
                }
            }
        }
    }
}

@Composable
fun StarRatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "$i stars",
                tint = if (i <= rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onRatingChange(i) }
            )
        }
    }
}
