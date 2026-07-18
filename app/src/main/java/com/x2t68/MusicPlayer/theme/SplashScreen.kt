package com.x2t68.MusicPlayer.theme

import android.content.Intent
import android.net.Uri // <-- هذا هو الاستيراد الصحيح
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // <-- استيراد مهم لتشغيل الروابط
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
            painter = painterResource(id = R.drawable.splashbg),
            contentDescription = null,
            modifier = Modifier
                .align (Alignment.TopCenter)
                .fillMaxSize()
                .blur(5.dp)
            , contentScale = ContentScale.FillHeight

        )
        Column(
            modifier = Modifier
                . fillMaxSize()
            , verticalArrangement = Arrangement.Center
        ) {


        }
        Text(
            text= "Play Your Local Music in Simple Way Without Ads!!",
            fontSize = 45.sp,
            modifier = Modifier
                .padding(top =48.dp, start = 24.dp, end = 24.dp, bottom = 20.dp)
                .align (Alignment.TopCenter)
                ,
            color = Color.White,
            fontWeight = FontWeight.Bold

        )
        Button(
       onClick = onStartClick,
            modifier = Modifier
                .padding(top =48.dp, start = 24.dp, end = 24.dp, bottom = 50.dp)
                .align (Alignment.BottomCenter)
                .height(50.dp)
                .width(330.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ), shape = RoundedCornerShape(25.dp)
        ){
            Text(
                text= stringResource(R.string.get_started),
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

        }
    }

}