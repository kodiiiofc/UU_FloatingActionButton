package com.kodiiiofc.urbanuniversity.jetpackcompose.floatingactionbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodiiiofc.urbanuniversity.jetpackcompose.floatingactionbutton.ui.theme.FloatingActionButtonTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val notes = rememberSaveable(
                saver = listSaver<SnapshotStateList<String>, String>(
                    save = {
                        if (it.isNotEmpty()) {
                            it.toList()
                        } else {
                            listOf("")
                        }
                    },
                    restore = { restored ->
                        val list = mutableStateListOf<String>()
                        restored.forEach {
                            list.add(it)
                        }
                        list
                    }
                )
            ) {
                mutableStateListOf()
            }

            var inputText by rememberSaveable {
                mutableStateOf("")
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Header()

                InputField(value = inputText,
                    onValueChanged = {
                        inputText = it
                    },
                    onAddButtonClick = {
                        notes.add(inputText)
                        inputText = ""
                    })

                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    itemsIndexed(notes) { index, item ->
                        NoteView(item, onIconDeleteClick = {
                            notes.removeAt(index)
                        })
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
            .height(80.dp)
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Заметки",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun InputField(value: String, onValueChanged: (String) -> Unit, onAddButtonClick: () -> Unit) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChanged(it) },
            shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {

            FloatingActionButton(
                onClick = onAddButtonClick,
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Add, "Добавить заметку")
                    Text("Добавить заметку")
                }
            }
        }
    }
}

@Composable
fun NoteView(text: String, onIconDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        IconButton(onClick = onIconDeleteClick) {
            Icon(
                Icons.Filled.Delete,
                "Удалить заметку"
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewScreen() {
    FloatingActionButtonTheme {

        var inputText by remember {
            mutableStateOf("")
        }

        val notes = remember {
            mutableStateListOf<String>()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Header()

            InputField(value = inputText,
                onValueChanged = {
                    inputText = it
                },
                onAddButtonClick = { notes.add(inputText) })

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                itemsIndexed(notes) { index, item ->
                    NoteView(item, onIconDeleteClick = {
                        notes.removeAt(index)
                    })
                    Spacer(Modifier.height(8.dp))
                }
            }

        }

    }
}