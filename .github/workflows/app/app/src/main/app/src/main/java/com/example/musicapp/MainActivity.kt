package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        
        setContent {
            MusicAppUI()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicAppUI() {
    var url by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("等待输入...") }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("音乐下载器") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("粘贴视频链接") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { 
                    status = "正在下载..."
                    Thread {
                        try {
                            val py = Python.getInstance()
                            val module = py.getModule("downloader")
                            val result = module.callAttr("download", url)
                            status = result.toString()
                        } catch (e: Exception) {
                            status = "错误: ${e.message}"
                        }
                    }.start()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("开始下载")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = status)
        }
    }
}
