package com.terrranulius.yellowheart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.terrranulius.yellowheart.Constants.initiatives
import com.terrranulius.yellowheart.components.FeedImageCard
import com.terrranulius.yellowheart.components.HelpButton
import com.terrranulius.yellowheart.data.Initiative
import com.terrranulius.yellowheart.firebase.FirebaseAuthUtils
import com.terrranulius.yellowheart.ui.theme.YellowHeartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuthUtils.registerListeners(this)

        setContent {
            val navController = rememberNavController()
            YellowHeartTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primaryVariant) {
                    NavHost(navController = navController, startDestination = "feed") {
                        composable("feed") {
                            Feed(navController = navController, onHelpClick = { onHelpClick() })
                        }
                        composable("initiativeDetail/{id}") {
                            InitiativeDetail(
                                initiativeId = it.arguments?.getString("id"),
                                onHelpClick = { onHelpClick() })
                        }
                    }
                }
            }
        }
    }

    fun onHelpClick() {

    }

}

@Composable
fun Feed(
    navController: NavController,
    onHelpClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        itemsIndexed(initiatives) { pos: Int, initiative: Initiative ->
            Spacer(modifier = Modifier.height(8.dp))
            FeedImageCard(
                initiative = initiative, modifier = Modifier.clickable {
                    navController.navigate("initiativeDetail/${initiative.id}")
                },
                onHelpClick = onHelpClick
            )
        }
    }
}

@Composable
fun InitiativeDetail(
    initiativeId: String?,
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    val initiative = Constants.initiatives.find {
        it.id == initiativeId
    } ?: Initiative(
        title = "error",
        description = "error",
        imgRes = R.drawable.b
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp
            ) {
                Image(
                    painter = painterResource(id = initiative.imgRes),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = initiative.title,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = initiative.description,
                style = MaterialTheme.typography.body1
            )
        }
        HelpButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp), showText = false,
            onClick = onHelpClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YellowHeartTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.primaryVariant)
        {
            Feed(rememberNavController())
        }
    }
}
