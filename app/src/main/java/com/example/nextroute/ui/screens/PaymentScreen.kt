package com.example.nextroute.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.ui.theme.NextRouteBorder
import com.example.nextroute.ui.theme.NextRoutePurple

@Composable
fun PaymentScreen(
    total: String,
    onBack: () -> Unit,
    onPaymentConfirmed: () -> Unit
) {
    var method by remember { mutableStateOf("Pix") }
    var showConfirmation by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar")
                }
                Text(
                    text = "Finalizar viagem",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteBorder)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Resumo da seleção", color = NextRoutePurple)
                    Spacer(Modifier.height(8.dp))
                    Text(total, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Valor demonstrativo por destino", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text("Forma de pagamento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = method == "Pix",
                    onClick = { method = "Pix" },
                    label = { Text("Pix") },
                    leadingIcon = { Icon(Icons.Outlined.QrCode, contentDescription = null, tint = NextRoutePurple) }
                )
                FilterChip(
                    selected = method == "Cartão",
                    onClick = { method = "Cartão" },
                    label = { Text("Cartão") },
                    leadingIcon = { Icon(Icons.Outlined.CreditCard, contentDescription = null, tint = NextRoutePurple) }
                )
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = NextRouteBlue)
                    Spacer(Modifier.height(0.dp))
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("Ambiente demonstrativo", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Esta versão não processa pagamentos nem cobra valores reais. A confirmação serve apenas para demonstrar o fluxo do aplicativo.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NextRoutePurple)
            ) {
                Text("Confirmar demonstração · $method")
            }
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Demonstração concluída") },
            text = { Text("A etapa de confirmação foi concluída localmente. Nenhum pagamento foi realizado.") },
            confirmButton = {
                TextButton(onClick = onPaymentConfirmed) { Text("Concluir") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) { Text("Voltar") }
            }
        )
    }
}
