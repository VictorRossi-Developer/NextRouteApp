package com.example.nextroute.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.nextroute.ui.components.CpfVisualTransformation
import com.example.nextroute.ui.components.NextRouteButton
import com.example.nextroute.ui.components.PhoneVisualTransformation
import com.example.nextroute.ui.components.onlyDigits
import com.example.nextroute.ui.theme.NextRouteBlue
import com.example.nextroute.data.NextRouteDbHelper
import android.content.Context

@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    onLoginSuccess: (
        name: String,
        email: String,
        phone: String
    ) -> Unit = { _, _, _ -> },
    onAccountCreated: () -> Unit = {}
) {
    var isLogin by remember {
        mutableStateOf(true)
    }

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var cpf by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var successMessage by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val dbHelper = remember { NextRouteDbHelper(context) }

    fun saveSession(userId: Long) {
        context.getSharedPreferences(
            "nextroute_session",
            Context.MODE_PRIVATE
        ).edit()
            .putLong("user_id", userId)
            .apply()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 28.dp,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (isLogin) {
                    "Entrar"
                } else {
                    "Criar conta"
                },
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            if (!isLogin) {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Nome completo")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("E-mail")
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            if (!isLogin) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = onlyDigits(it, 11)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Telefone")
                    },
                    singleLine = true,
                    visualTransformation = PhoneVisualTransformation(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = cpf,
                    onValueChange = {
                        cpf = onlyDigits(it, 11)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("CPF")
                    },
                    singleLine = true,
                    visualTransformation = CpfVisualTransformation(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Senha")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(10.dp)
            )

            if (!isLogin) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Confirmar senha")
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            if (successMessage.isNotEmpty()) {

                Text(
                    text = successMessage,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            Button(
                onClick = {
                    val cleanEmail = email.trim()
                    val cleanName = name.trim()
                    val cleanPhone = onlyDigits(phone, 11)
                    val cleanCpf = onlyDigits(cpf, 11)

                    errorMessage = when {
                        cleanEmail.isBlank() ->
                            "Informe o e-mail."

                        password.isBlank() ->
                            "Informe a senha."

                        password.length < 6 ->
                            "A senha deve ter pelo menos 6 caracteres."

                        !isLogin && cleanName.isBlank() ->
                            "Informe seu nome."

                        !isLogin && cleanPhone.length != 11 ->
                            "Informe um telefone válido."

                        !isLogin && cleanCpf.length != 11 ->
                            "Informe um CPF válido."

                        !isLogin && password != confirmPassword ->
                            "As senhas não coincidem."

                        else ->
                            ""
                    }

                    if (errorMessage.isNotEmpty()) {
                        return@Button
                    }

                    successMessage = ""
                    loading = true

                    if (isLogin) {
                        val user = dbHelper.autenticarUsuario(
                            cleanEmail,
                            password
                        )

                        if (user == null) {
                            loading = false
                            errorMessage = "E-mail ou senha incorretos."
                            return@Button
                        }

                        saveSession(user.id)
                        loading = false
                        errorMessage = ""
                        onLoginSuccess(
                            user.name,
                            user.email,
                            user.phone
                        )
                    } else {
                        val newUserId = dbHelper.criarUsuario(
                            nome = cleanName,
                            email = cleanEmail,
                            telefone = cleanPhone,
                            cpf = cleanCpf,
                            senha = password
                        )

                        if (newUserId <= 0L) {
                            loading = false
                            errorMessage =
                                "Este e-mail ou CPF já está cadastrado."
                            return@Button
                        }

                        saveSession(newUserId)
                        loading = false
                        errorMessage = ""
                        successMessage = "Conta criada com sucesso."

                        onLoginSuccess(
                            cleanName,
                            cleanEmail,
                            cleanPhone
                        )

                        onAccountCreated()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NextRouteBlue
                )
            ) {

                Text(
                    text = when {
                        loading -> "Aguarde..."
                        isLogin -> "Entrar"
                        else -> "Cadastrar"
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            TextButton(
                onClick = {
                    isLogin = !isLogin
                    errorMessage = ""
                    successMessage = ""
                }
            ) {

                Text(
                    text = if (isLogin) {
                        "Ainda não tenho uma conta"
                    } else {
                        "Já tenho uma conta"
                    }
                )
            }

            TextButton(
                onClick = onBack
            ) {
                Text("Voltar")
            }
        }
    }
}

