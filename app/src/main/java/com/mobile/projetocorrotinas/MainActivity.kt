package com.mobile.projetocorrotinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AplicativoSemaforo()
            }
        }
    }
}

// Possíveis estados do semáforo
enum class EstadoSemaforo {
    VERDE,
    AMARELO,
    VERMELHO
}

@Composable
fun AplicativoSemaforo() {

    // Guarda qual luz está ativa no momento
    var estadoAtual by remember {
        mutableStateOf(EstadoSemaforo.VERDE)
    }

    // Guarda se o modo amarelo piscante está ativado
    var modoPiscante by remember {
        mutableStateOf(false)
    }

    // No modo piscante, controla se a luz está acesa ou apagada
    var amareloAceso by remember {
        mutableStateOf(true)
    }

    /*
     * Aqui inicia uma corrotina.
     *
     * Sempre que modoPiscante mudar, a corrotina anterior será
     * cancelada e uma nova será iniciada.
     */
    LaunchedEffect(modoPiscante) {

        if (modoPiscante) {

            // Modo amarelo piscante
            estadoAtual = EstadoSemaforo.AMARELO
            amareloAceso = true

            while (true) {
                delay(500)
                amareloAceso = !amareloAceso
            }

        } else {

            // Funcionamento normal
            amareloAceso = false

            while (true) {
                estadoAtual = EstadoSemaforo.VERDE
                delay(5000)

                estadoAtual = EstadoSemaforo.AMARELO
                delay(2000)

                estadoAtual = EstadoSemaforo.VERMELHO
                delay(5000)
            }
        }
    }

    // Define o texto apresentado na tela
    val textoEstado = if (modoPiscante) {
        if (amareloAceso) {
            "Amarelo piscante: aceso"
        } else {
            "Amarelo piscante: apagado"
        }
    } else {
        when (estadoAtual) {
            EstadoSemaforo.VERDE -> "Estado atual: Verde"
            EstadoSemaforo.AMARELO -> "Estado atual: Amarelo"
            EstadoSemaforo.VERMELHO -> "Estado atual: Vermelho"
        }
    }

    // Interface do aplicativo
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F2F2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Semáforo com Corrotinas",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Estrutura externa do semáforo
            Column(
                modifier = Modifier
                    .background(
                        color = Color(0xFF242424),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Luz vermelha
                LuzSemaforo(
                    cor = Color.Red,
                    acesa = !modoPiscante &&
                            estadoAtual == EstadoSemaforo.VERMELHO
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Luz amarela
                LuzSemaforo(
                    cor = Color.Yellow,
                    acesa = if (modoPiscante) {
                        amareloAceso
                    } else {
                        estadoAtual == EstadoSemaforo.AMARELO
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Luz verde
                LuzSemaforo(
                    cor = Color.Green,
                    acesa = !modoPiscante &&
                            estadoAtual == EstadoSemaforo.VERDE
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = textoEstado,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    modoPiscante = !modoPiscante
                }
            ) {
                Text(
                    text = if (modoPiscante) {
                        "Voltar ao modo normal"
                    } else {
                        "Ativar amarelo piscante"
                    }
                )
            }
        }
    }
}

@Composable
fun LuzSemaforo(
    cor: Color,
    acesa: Boolean
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .background(
                color = if (acesa) {
                    cor
                } else {
                    cor.copy(alpha = 0.15f)
                },
                shape = CircleShape
            )
    )
}