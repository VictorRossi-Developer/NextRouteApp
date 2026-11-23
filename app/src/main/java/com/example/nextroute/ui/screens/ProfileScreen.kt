package com.example.nextroute.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nextroute.data.NextRouteDbHelper
import com.example.nextroute.ui.components.CpfVisualTransformation
import com.example.nextroute.ui.components.PhoneVisualTransformation
import com.example.nextroute.ui.components.onlyDigits
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.ui.theme.NextRouteLightPurple
import com.example.nextroute.ui.theme.NextRoutePurple

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val dbHelper = remember { NextRouteDbHelper(context) }
    val preferences = remember {
        context.getSharedPreferences(
            "nextroute_session",
            Context.MODE_PRIVATE
        )
    }

    val userId = preferences.getLong("user_id", -1L)
    val user = remember(userId) {
        if (userId > 0L) dbHelper.obterUsuario(userId) else null
    }

    var name by remember(user?.id) { mutableStateOf(user?.name.orEmpty()) }
    var email by remember(user?.id) { mutableStateOf(user?.email.orEmpty()) }
    var phone by remember(user?.id) { mutableStateOf(user?.phone.orEmpty()) }
    var cpf by remember(user?.id) { mutableStateOf(user?.cpf.orEmpty()) }
    var photoUrl by remember(user?.id) { mutableStateOf(user?.photoUri) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null || userId <= 0L) return@rememberLauncherForActivityResult

        photoUrl = uri.toString()

        val saved = dbHelper.atualizarUsuario(
            id = userId,
            nome = name.trim(),
            email = email.trim(),
            telefone = onlyDigits(phone, 11),
            cpf = onlyDigits(cpf, 11),
            fotoUri = photoUrl
        )

        message = if (saved) {
            "Foto atualizada."
        } else {
            error = "Não foi possível salvar a foto."
            ""
        }
    }

    if (user == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Nenhuma conta conectada.")
        }
        return
    }

    val saveProfile: () -> Unit = {
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        val cleanPhone = onlyDigits(phone, 11)
        val cleanCpf = onlyDigits(cpf, 11)

        when {
            cleanName.isBlank() -> error = "Informe seu nome."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                error = "Informe um e-mail válido."
            cleanPhone.length != 11 ->
                error = "Informe um telefone válido."
            cleanCpf.length != 11 ->
                error = "Informe um CPF válido."
            newPassword.isNotBlank() && newPassword.length < 6 ->
                error = "A nova senha deve ter pelo menos 6 caracteres."
            newPassword.isNotBlank() && newPassword != confirmPassword ->
                error = "As novas senhas não coincidem."
            else -> {
                loading = true
                error = ""
                message = "Salvando..."

                val saved = dbHelper.atualizarUsuario(
                    id = userId,
                    nome = cleanName,
                    email = cleanEmail,
                    telefone = cleanPhone,
                    cpf = cleanCpf,
                    fotoUri = photoUrl
                )

                if (!saved) {
                    loading = false
                    message = ""
                    error = "Não foi possível salvar os dados."
                } else if (
                    newPassword.isNotBlank() &&
                    !dbHelper.atualizarSenha(userId, newPassword)
                ) {
                    loading = false
                    message = ""
                    error = "Não foi possível atualizar a senha."
                } else {
                    loading = false
                    message = "Perfil atualizado."
                    newPassword = ""
                    confirmPassword = ""
                    context.getSharedPreferences(
                        "nextroute_profile",
                        Context.MODE_PRIVATE
                    ).edit()
                        .putString("name", cleanName)
                        .putString("email", cleanEmail)
                        .putString("phone", cleanPhone)
                        .putString("cpf", cleanCpf)
                        .putString("photo", photoUrl)
                        .apply()
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Meu perfil",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (!photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = NextRouteLightPurple
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Adicionar foto",
                            modifier = Modifier.padding(20.dp),
                            tint = NextRouteBlue
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { photoPicker.launch("image/*") },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NextRouteBlue
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Enviar / alterar foto")
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome completo") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("E-mail") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = onlyDigits(it, 11) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefone") },
                singleLine = true,
                visualTransformation = PhoneVisualTransformation(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = onlyDigits(it, 11) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("CPF") },
                singleLine = true,
                visualTransformation = CpfVisualTransformation(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Alterar senha",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nova senha") },
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar nova senha") },
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(20.dp))

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = NextRouteBlue
                )
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = saveProfile,
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NextRouteBlue
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (loading) "Salvando..." else "Salvar informações")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    preferences.edit().clear().apply()
                    context.getSharedPreferences(
                        "nextroute_profile",
                        Context.MODE_PRIVATE
                    ).edit().clear().apply()

                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Sair da conta")
            }
        }
    }
}
