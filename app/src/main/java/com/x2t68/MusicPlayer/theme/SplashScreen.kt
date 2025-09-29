package com.x2t68.MusicPlayer.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.x2t68.MusicPlayer.R

@Composable
@Preview
fun SplashScreen(onStartClick: () -> Unit = {}) {
    Box(modifier =
        Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backgh),
            contentDescription = null,
            modifier = Modifier
                .align (Alignment.TopCenter)
                .fillMaxSize()
            , contentScale = ContentScale.FillHeight

        )
        Button(
       onClick = onStartClick,
            modifier = Modifier
                .padding(top =48.dp, start = 24.dp, end = 24.dp)
                .align (Alignment.TopCenter)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.orange)
            ), shape = RoundedCornerShape(25.dp)
        ){
            Text(
                text= stringResource(R.string.get_started),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }
    }

}