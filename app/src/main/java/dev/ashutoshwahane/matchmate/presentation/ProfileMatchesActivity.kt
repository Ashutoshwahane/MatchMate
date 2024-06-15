package dev.ashutoshwahane.matchmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.ashutoshwahane.matchmate.presentation.ProfileMatchesCompose
import dev.ashutoshwahane.matchmate.presentation.ProfileMatchesViewmodel
import dev.ashutoshwahane.matchmate.ui.theme.MatchMateTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewmodel: ProfileMatchesViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatchMateTheme {
                ProfileMatchesCompose().ProfileMatches()
            }
        }

        viewmodel.fetchMatches()
    }
}
