package dev.ashutoshwahane.matchmate.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.utils.ProfileStatus
import dev.ashutoshwahane.matchmate.utils.ColorManager
import dev.ashutoshwahane.matchmate.utils.CustomCTA
import dev.ashutoshwahane.matchmate.utils.DataResource
import dev.ashutoshwahane.matchmate.utils.ResourceState
import dev.ashutoshwahane.matchmate.utils.shimmer

@Composable
fun ProfileMatches() {
    val viewmodel: ProfileMatchesViewmodel = viewModel()
    val uiState = viewmodel.uiState.collectAsState()
    ProfileMatchesStatic(
        profiles = uiState.value.profilesResources,
        onRefresh = viewmodel::fetchProfiles,
        onAcceptCTA = {
            viewmodel.updateProfileStatus(profile = it, newStatus = ProfileStatus.ACCEPTED)
        },
        onDeniedCTA = {
            viewmodel.updateProfileStatus(profile = it, newStatus = ProfileStatus.DENIED)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMatchesStatic(
    profiles: DataResource<List<ProfileModel>>,
    onRefresh: () -> Unit = {},
    onAcceptCTA: (ProfileModel) -> Unit,
    onDeniedCTA: (ProfileModel) -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val tintColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "Profile Matches")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme().not()) Color.White else Color.Black,
                ),
                scrollBehavior = scrollBehavior,
                actions = {
                    if (scrollBehavior.state.collapsedFraction == 1f) {
                        IconButton(onClick = {
                            onRefresh()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Profiles",
                                tint = tintColor,
                                modifier = Modifier
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Crossfade(
                targetState = profiles.resourceState,
                label = "Profiles",
                animationSpec = tween(1000)
            ) {
                when (it) {
                    ResourceState.INITIAL,
                    ResourceState.LOADING -> {
                        ProfileCardShimmer()
                    }

                    ResourceState.SUCCESS -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            if (profiles.data != null) {
                                itemsIndexed(profiles.data) { index, profile ->
                                    ProfileCard(
                                        profile = profile,
                                        profileStatus = profile.isAccepted,
                                        onDeniedCTA = { onDeniedCTA(it) },
                                        onAcceptCTA = { onAcceptCTA(it) }
                                    )
                                }

                            }
                        }
                    }

                    ResourceState.ERROR -> {
                        ProfileCardError(
                            onRetry = {
                                onRefresh()
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun ProfileCard(
    profile: ProfileModel,
    profileStatus: ProfileStatus,
    onAcceptCTA: (ProfileModel) -> Unit,
    onDeniedCTA: (ProfileModel) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = profile.profilePic,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x80000000) // 50% opacity black
                                ),
                                startY = size.height / 3,
                                endY = size.height
                            )
                        )
                    },
                contentScale = ContentScale.Crop,
                contentDescription = "profile image"
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${profile.firstName} ${profile.lastName},",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            lineHeight = 16.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(top = 10.dp, start = 8.dp)
                    )
                    Text(
                        text = profile.age,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(top = 10.dp, start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = profile.address,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 10.dp, start = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            var isAccepted by remember { mutableStateOf(false) }
            var isDenied by remember { mutableStateOf(false) }
            when (profileStatus) {
                ProfileStatus.ACCEPTED -> {
                    CustomCTA(
                        text = "Accept",
                        textColor = ColorManager.textColor,
                        backgroundColor = ColorManager.greenBackgroundColor,
                        modifier = Modifier.weight(if (isAccepted) 1f else 1f),
                        isLoading = false,
                        onClick = {
                            onAcceptCTA(profile)
                            isAccepted = true
                        }
                    )
                }

                ProfileStatus.DENIED -> {
                    CustomCTA(
                        text = "Denied",
                        textColor = ColorManager.textColor,
                        backgroundColor = ColorManager.redBackgroundColor,
                        isLoading = false,
                        modifier = Modifier.weight(if (isDenied) 0.01f else 1f),
                        onClick = {
                            onDeniedCTA(profile)
                            isDenied = true
                        }
                    )
                }

                ProfileStatus.PENDING -> {
                    CustomCTA(
                        text = "Denied",
                        textColor = ColorManager.textColor,
                        backgroundColor = ColorManager.redBackgroundColor,
                        isLoading = false,
                        modifier = Modifier.weight(if (isDenied) 0.01f else 1f),
                        onClick = {
                            onDeniedCTA(profile)
                            isDenied = true
                        }
                    )

                    CustomCTA(
                        text = "Accept",
                        textColor = ColorManager.textColor,
                        backgroundColor = ColorManager.greenBackgroundColor,
                        modifier = Modifier.weight(if (isAccepted) 1f else 1f),
                        isLoading = false,
                        onClick = {
                            onAcceptCTA(profile)
                            isAccepted = true
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileCardShimmer() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        repeat(10) {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer()
            )
        }
    }
}

@Composable
fun ProfileCardError(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCTA(
            text = "R E T R Y ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textColor = ColorManager.textColor,
            backgroundColor = ColorManager.redBackgroundColor
        ) {
            onRetry()
        }
    }
}

@Preview
@Composable
fun ProfileMatchesComposePreview() {
    ProfileMatches()
}