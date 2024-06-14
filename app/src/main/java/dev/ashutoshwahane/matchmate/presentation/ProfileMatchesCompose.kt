package dev.ashutoshwahane.matchmate.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class ProfileMatchesCompose {

    @Composable
    fun ProfileMatches(){

        ProfileMatchesStatic()
    }

    @Composable
    fun ProfileMatchesStatic(

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(text = "Profile Matches")
        }
    }


}




@Preview
@Composable
fun ProfileMatchesComposePreview(){
    ProfileMatchesCompose().ProfileMatches()
}