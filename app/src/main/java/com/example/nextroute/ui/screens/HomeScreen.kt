package com.example.nextroute.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.nextroute.data.Aviso
import com.example.nextroute.data.NextRouteDbHelper
import com.example.nextroute.ui.components.ScrollReveal
import com.example.nextroute.ui.components.NextRouteLogo
import com.example.nextroute.ui.theme.NextRouteBackground
import com.example.nextroute.ui.theme.NextRouteGray
import com.example.nextroute.ui.theme.NextRouteLightBlue
import com.example.nextroute.ui.theme.NextRouteLightPurple
import com.example.nextroute.ui.theme.NextRoutePurple
import com.example.nextroute.ui.theme.NextRouteText



private enum class HomeTab {
    HOME,
    EXPLORE,
    CART,
    NOTIFICATIONS,
    PROFILE
}

/*
 * Estrutura única dos destinos.
 *
 * IMPORTANTE:
 * O campo é "location".
 * Não existe "region" neste arquivo.
 */
private data class Destination(
    val location: String,
    val name: String,
    val price: String,
    val image: String,
    val description: String,
    val category: String,
    val details: String
)

private val destinations = listOf(

    Destination(
        location = "Pernambuco, Brasil",
        name = "Olinda",
        price = "R$ 1.850",
        image = "https://images.unsplash.com/photo-1739370648186-4abbeddaa6da",
        description = "Conheça o centro histórico de Olinda, suas ladeiras, cultura e arquitetura.",
        category = "Cultura",
        details = "Olinda é conhecida pelo seu centro histórico, casarões coloridos, igrejas, manifestações culturais e pelo famoso Carnaval."
    ),

    Destination(
        location = "Berlim, Alemanha",
        name = "Berlim",
        price = "R$ 4.850",
        image = "https://images.unsplash.com/photo-1560969184-10fe8719e047",
        description = "Explore Berlim, uma cidade marcada por sua história, cultura, arte e arquitetura.",
        category = "Cultura",
        details = "Berlim reúne importantes monumentos históricos, museus, galerias de arte, espaços culturais e uma arquitetura que mistura construções históricas com paisagens urbanas modernas."
    ),

    Destination(
        location = "Rio de Janeiro, Brasil",
        name = "Paraty",
        price = "R$ 1.050",
        image = "https://images.unsplash.com/photo-1607029257863-8b2e746453f7",
        description = "Uma cidade histórica cercada por natureza, praias e experiências culturais.",
        category = "Praia",
        details = "Paraty combina praias, natureza, arquitetura colonial e passeios de barco. É uma ótima opção para quem quer descansar e conhecer lugares históricos."
    ),

    Destination(
        location = "Espírito Santo, Brasil",
        name = "Ibiraçu",
        price = "R$ 2.020",
        image = "https://images.unsplash.com/photo-1629949249681-020de0533929?q=80&w=1174&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        description = "Uma opção para quem procura natureza, tranquilidade e experiências diferentes.",
        category = "Natureza",
        details = "Ibiraçu oferece contato com a natureza, trilhas e atrações para quem procura uma viagem tranquila e experiências ao ar livre."
    ),

    Destination(
        location = "São Paulo, Brasil",
        name = "São Paulo",
        price = "R$ 2.450",
        image = "https://images.unsplash.com/photo-1489194889484-95eac94a140f",
        description = "Uma das maiores cidades do Brasil, conhecida por sua gastronomia e vida noturna.",
        category = "Gastronomia",
        details = "São Paulo possui restaurantes de diferentes culinárias, mercados, centros culturais, bares e uma grande variedade de atrações."
    )
)

@Composable
fun HomeScreen(
    onAccountCreated: () -> Unit = {}
) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    var currentTab by rememberSaveable {
        mutableStateOf(HomeTab.HOME)
    }

    val context = LocalContext.current
    val dbHelper = remember { NextRouteDbHelper(context) }
    val sessionPreferences = remember {
        context.getSharedPreferences(
            "nextroute_session",
            Context.MODE_PRIVATE
        )
    }
    var currentUserId by rememberSaveable {
        mutableStateOf(
            sessionPreferences.getLong("user_id", -1L)
        )
    }

    var profileAuthenticated by rememberSaveable {
        mutableStateOf(currentUserId > 0L)
    }

    var selectedCategory by rememberSaveable {
        mutableStateOf("Todos")
    }

    var selectedDestination by remember {
        mutableStateOf<Destination?>(null)
    }

    var selectedTripForPayment by remember {
        mutableStateOf<Destination?>(null)
    }

    var showFaq by remember {
        mutableStateOf(false)
    }

    var showAbout by remember {
        mutableStateOf(false)
    }

    val favorites = remember {
        mutableStateListOf<String>()
    }

    var favoritesOnly by rememberSaveable {
        mutableStateOf(false)
    }

    androidx.compose.runtime.LaunchedEffect(currentUserId) {
        favorites.clear()
        if (currentUserId > 0L) {
            favorites.addAll(
                dbHelper.listarFavoritos(currentUserId)
            )
        }
    }

    val cart = remember {
        mutableStateListOf<Destination>()
    }

    val preferences = remember {
        context.getSharedPreferences(
            "nextroute_profile",
            Context.MODE_PRIVATE
        )
    }

    var profileName by rememberSaveable {
        mutableStateOf(
            preferences.getString("name", "") ?: ""
        )
    }

    var profileEmail by rememberSaveable {
        mutableStateOf(
            preferences.getString("email", "") ?: ""
        )
    }

    var profilePhone by rememberSaveable {
        mutableStateOf(
            preferences.getString("phone", "") ?: ""
        )
    }

    var profilePhoto by rememberSaveable {
        mutableStateOf(
            preferences.getString("photo", null)
        )
    }

    val photoPicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {

                profilePhoto = uri.toString()

                preferences.edit()
                    .putString("photo", uri.toString())
                    .apply()
            }
        }

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NextRouteLogo(
                            contentDescription = "Logotipo NextRoute",
                            modifier = Modifier.size(38.dp)
                        )
                        Text(
                            text = "NextRoute",
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NextRoutePurple
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Caso necessite de ajuda, contate: NextRoute@contato.com",
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    DrawerItem(
                        icon = Icons.Outlined.Home,
                        text = "Início"
                    ) {

                        currentTab = HomeTab.HOME
                        selectedCategory = "Todos"
                        favoritesOnly = false

                        scope.launch {
                            drawerState.close()
                        }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Person,
                        text = "Meu perfil"
                    ) {

                        currentTab = HomeTab.PROFILE
                        favoritesOnly = false

                        scope.launch {
                            drawerState.close()
                        }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Luggage,
                        text = "Minhas viagens"
                    ) {

                        currentTab = HomeTab.CART
                        favoritesOnly = false

                        scope.launch {
                            drawerState.close()
                        }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Favorite,
                        text = "Favoritos"
                    ) {

                        currentTab = HomeTab.EXPLORE
                        favoritesOnly = true

                        scope.launch {
                            drawerState.close()
                        }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Place,
                        text = "Refazer quiz",
                        useBrandLogo = true
                    ) {
                        onAccountCreated()
                        scope.launch { drawerState.close() }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Help,
                        text = "FAQ / Ajuda"
                    ) {

                        showFaq = true

                        scope.launch {
                            drawerState.close()
                        }
                    }

                    DrawerItem(
                        icon = Icons.Outlined.Info,
                        text = "Sobre a NextRoute"
                    ) {

                        showAbout = true

                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            }
        }
    ) {

        NextRouteScaffold(

            currentTab = currentTab,

            onTabSelected = {
                currentTab = it
                if (it == HomeTab.EXPLORE) {
                    favoritesOnly = false
                }
            },

            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            }
        ) {

            when (currentTab) {

                HomeTab.HOME -> {

                    HomeContent(
                        selectedCategory = selectedCategory,

                        onCategorySelected = {
                            selectedCategory = it
                        },

                        favorites = favorites,

                        onFavorite = { destination ->
                            if (favorites.contains(destination.name)) {
                                favorites.remove(destination.name)
                                if (currentUserId > 0L) {
                                    dbHelper.removerFavorito(
                                        currentUserId,
                                        destination.name
                                    )
                                }
                            } else {
                                favorites.add(destination.name)
                                if (currentUserId > 0L) {
                                    dbHelper.adicionarFavorito(
                                        currentUserId,
                                        destination.name
                                    )
                                }
                            }
                        },

                        onDestinationClick = {
                            selectedDestination = it
                        },

                        onTrips = {
                            currentTab = HomeTab.CART
                        }
                    )
                }

                HomeTab.EXPLORE -> {

                    ExploreContent(
                        favorites = favorites,
                        onlyFavorites = favoritesOnly,

                        onFavorite = { destination ->
                            if (favorites.contains(destination.name)) {
                                favorites.remove(destination.name)
                                if (currentUserId > 0L) {
                                    dbHelper.removerFavorito(
                                        currentUserId,
                                        destination.name
                                    )
                                }
                            } else {
                                favorites.add(destination.name)
                                if (currentUserId > 0L) {
                                    dbHelper.adicionarFavorito(
                                        currentUserId,
                                        destination.name
                                    )
                                }
                            }
                        },

                        onDestinationClick = {
                            selectedDestination = it
                        }
                    )
                }

                HomeTab.CART -> {

                    CartContent(
                        cart = cart,

                        onRemove = {
                            cart.remove(it)
                        },

                        onTripClick = {
                            selectedTripForPayment = it
                        }
                    )
                }

                HomeTab.NOTIFICATIONS -> {

                    NotificationsContent()
                }

                HomeTab.PROFILE -> {

                    if (profileAuthenticated) {
                        ProfileScreen(
                            onLogout = {
                                currentUserId = -1L
                                profileAuthenticated = false
                                sessionPreferences.edit().remove("user_id").apply()
                                favorites.clear()
                            }
                        )
                    } else {
                        LoginScreen(
                            onBack = {
                                currentTab = HomeTab.HOME
                            },
                            onLoginSuccess = { _, _, _ ->
                                currentUserId =
                                    sessionPreferences.getLong("user_id", -1L)
                                profileAuthenticated = currentUserId > 0L
                            },
                            onAccountCreated = onAccountCreated
                        )
                    }
                }
            }
        }
    }

    selectedDestination?.let { destination ->

        DestinationDialog(
            destination = destination,

            isFavorite = favorites.contains(
                destination.name
            ),

            onFavorite = {
                if (favorites.contains(destination.name)) {
                    favorites.remove(destination.name)
                    if (currentUserId > 0L) dbHelper.removerFavorito(currentUserId, destination.name)
                } else {
                    favorites.add(destination.name)
                    if (currentUserId > 0L) dbHelper.adicionarFavorito(currentUserId, destination.name)
                }
            },

            onAddCart = {

                if (!cart.contains(destination)) {
                    cart.add(destination)
                }
            },

            onClose = {
                selectedDestination = null
            }
        )
    }

    selectedTripForPayment?.let { trip ->

        PaymentScreen(
            total = trip.price,
            onBack = {
                selectedTripForPayment = null
            },
            onPaymentConfirmed = {
                cart.remove(trip)
                selectedTripForPayment = null
            }
        )
    }

    if (showFaq) {

        AlertDialog(
            onDismissRequest = {
                showFaq = false
            },

            title = {
                Text("Perguntas frequentes")
            },

            text = {

                Column {

                    Text(
                        text = "Como funciona a NextRoute?",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "A NextRoute ajuda você a descobrir destinos e planejar suas viagens."
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "Posso favoritar destinos?",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Sim. Toque no coração de um destino para adicioná-lo aos favoritos."
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showFaq = false
                    }
                ) {
                    Text("Fechar")
                }
            }
        )
    }

    if (showAbout) {

        AlertDialog(
            onDismissRequest = {
                showAbout = false
            },

            title = {
                Text("Sobre a NextRoute")
            },

            text = {
                Text(
                    text = "A NextRoute é uma plataforma criada para ajudar viajantes a encontrar destinos, organizar viagens e descobrir novas experiências."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showAbout = false
                    }
                ) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
private fun NextRouteScaffold(
    currentTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    onMenuClick: () -> Unit,
    content: @Composable () -> Unit
) {

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        topBar = {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = onMenuClick
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Menu",
                            tint = NextRoutePurple
                        )
                    }

                    NextRouteLogo(
                        contentDescription = "NextRoute",
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(Modifier.width(9.dp))

                    Column {
                        Text(
                            text = "NextRoute",
                            color = NextRoutePurple,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Sua próxima experiência começa aqui",
                            color = NextRouteGray,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        },

        bottomBar = {

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {

                NavigationBarItem(
                    selected = currentTab == HomeTab.HOME,

                    onClick = {
                        onTabSelected(HomeTab.HOME)
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Início"
                        )
                    },

                    label = {
                        Text("HOME")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NextRoutePurple,
                        selectedTextColor = NextRoutePurple,
                        unselectedIconColor = NextRouteGray,
                        unselectedTextColor = NextRouteGray,
                        indicatorColor = NextRouteLightPurple
                    )
                )

                NavigationBarItem(
                    selected = currentTab == HomeTab.EXPLORE,

                    onClick = {
                        onTabSelected(HomeTab.EXPLORE)
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Explore,
                            contentDescription = "Explorar"
                        )
                    },

                    label = {
                        Text("EXPLORAR")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NextRoutePurple,
                        selectedTextColor = NextRoutePurple,
                        unselectedIconColor = NextRouteGray,
                        unselectedTextColor = NextRouteGray,
                        indicatorColor = NextRouteLightPurple
                    )
                )

                NavigationBarItem(
                    selected = currentTab == HomeTab.CART,

                    onClick = {
                        onTabSelected(HomeTab.CART)
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Viagens"
                        )
                    },

                    label = {
                        Text("VIAGENS")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NextRoutePurple,
                        selectedTextColor = NextRoutePurple,
                        unselectedIconColor = NextRouteGray,
                        unselectedTextColor = NextRouteGray,
                        indicatorColor = NextRouteLightPurple
                    )
                )

                NavigationBarItem(
                    selected = currentTab == HomeTab.NOTIFICATIONS,

                    onClick = {
                        onTabSelected(HomeTab.NOTIFICATIONS)
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Avisos"
                        )
                    },

                    label = {
                        Text("AVISOS")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NextRoutePurple,
                        selectedTextColor = NextRoutePurple,
                        unselectedIconColor = NextRouteGray,
                        unselectedTextColor = NextRouteGray,
                        indicatorColor = NextRouteLightPurple
                    )
                )

                NavigationBarItem(
                    selected = currentTab == HomeTab.PROFILE,

                    onClick = {
                        onTabSelected(HomeTab.PROFILE)
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Perfil"
                        )
                    },

                    label = {
                        Text("PERFIL")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NextRoutePurple,
                        selectedTextColor = NextRoutePurple,
                        unselectedIconColor = NextRouteGray,
                        unselectedTextColor = NextRouteGray,
                        indicatorColor = NextRouteLightPurple
                    )
                )
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NextRouteBackground)
                .padding(paddingValues)
                .padding(bottom = 8.dp)
        ) {

            content()
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    text: String,
    useBrandLogo: Boolean = false,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                vertical = 14.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        if (useBrandLogo) {
            NextRouteLogo(
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = NextRoutePurple
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun HomeContent(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    favorites: List<String>,
    onFavorite: (Destination) -> Unit,
    onDestinationClick: (Destination) -> Unit,
    onTrips: () -> Unit
) {

    val filteredDestinations =
        if (selectedCategory == "Todos") {
            destinations
        } else {
            destinations.filter {
                it.category == selectedCategory
            }
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        )
    ) {

        item {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Encontre sua próxima viagem",
                        color = NextRoutePurple,
                        fontSize = 23.sp,
                        lineHeight = 29.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Explore destinos selecionados e organize sua próxima experiência.",
                        color = NextRouteGray,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onTrips,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NextRoutePurple,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ver minhas viagens")
                    }
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "Categorias",

                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),

                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),

                contentPadding = PaddingValues(
                    horizontal = 16.dp
                )
            ) {

                item {
                    CategoryItem(
                        icon = Icons.Outlined.Public,
                        label = "Todos",
                        selected = selectedCategory == "Todos",
                        onClick = {
                            onCategorySelected("Todos")
                        }
                    )
                }

                item {

                    CategoryItem(
                        icon = Icons.Outlined.BeachAccess,
                        label = "Praia",
                        selected = selectedCategory == "Praia",
                        onClick = {
                            onCategorySelected("Praia")
                        }
                    )
                }

                item {

                    CategoryItem(
                        icon = Icons.Outlined.Park,
                        label = "Natureza",
                        selected = selectedCategory == "Natureza",
                        onClick = {
                            onCategorySelected("Natureza")
                        }
                    )
                }

                item {

                    CategoryItem(
                        icon = Icons.Outlined.Museum,
                        label = "Cultura",
                        selected = selectedCategory == "Cultura",
                        onClick = {
                            onCategorySelected("Cultura")
                        }
                    )
                }

                item {

                    CategoryItem(
                        icon = Icons.Outlined.Restaurant,
                        label = "Gastronomia",
                        selected = selectedCategory == "Gastronomia",
                        onClick = {
                            onCategorySelected("Gastronomia")
                        }
                    )
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = if (selectedCategory == "Todos") {
                    "Sugestões para você"
                } else {
                    "Destinos de $selectedCategory"
                },

                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),

                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        if (filteredDestinations.isEmpty()) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Nenhum destino encontrado."
                    )
                }
            }

        } else {

            items(
                items = filteredDestinations,
                key = {
                    it.name
                }
            ) { destination ->
                ScrollReveal {
                    Column {
                        DestinationCard(
                            destination = destination,
                            isFavorite = favorites.contains(destination.name),
                            onFavorite = { onFavorite(destination) },
                            onClick = { onDestinationClick(destination) }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(82.dp)
            .clickable {
                onClick()
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier.size(58.dp),
            shape = RoundedCornerShape(14.dp),
            color = if (selected) NextRouteLightBlue else Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = label,

                    modifier = Modifier.size(24.dp),
                    tint = NextRoutePurple
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = label,

            fontSize = 12.sp,

            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },

            color = if (selected) {
                NextRoutePurple
            } else {
                NextRouteGray
            }
        )
    }
}

@Composable
private fun DestinationCard(
    destination: Destination,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
    ) {

        Column {

            Box {

                AsyncImage(
                    model = destination.image,

                    contentDescription = destination.name,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp),

                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onFavorite,

                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.96f),
                            shape = CircleShape
                        )
                ) {

                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },

                        contentDescription = "Favorito",

                        tint = NextRoutePurple
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = destination.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = destination.location,
                    fontSize = 13.sp,
                    color = NextRouteGray
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = destination.description,
                    fontSize = 13.sp,
                    color = NextRouteText
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = destination.price,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NextRoutePurple
                )
            }
        }
    }
}

@Composable
private fun ExploreContent(
    favorites: List<String>,
    onlyFavorites: Boolean,
    onFavorite: (Destination) -> Unit,
    onDestinationClick: (Destination) -> Unit
) {

    val displayedDestinations =
        if (onlyFavorites) {
            destinations.filter {
                favorites.contains(it.name)
            }
        } else {
            destinations
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        )
    ) {

        item {

            Text(
                text = "Explorar destinos",

                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),

                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Encontre o destino ideal para sua próxima viagem.",

                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),

                fontSize = 14.sp,
                color = NextRouteGray
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }

        if (onlyFavorites && displayedDestinations.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = "Nenhum favorito",
                    description = "Toque no coração de um destino para adicioná-lo aos favoritos."
                )
            }
        } else {
            items(
                items = displayedDestinations,
            key = {
                it.name
            }
        ) { destination ->
            ScrollReveal {
                Column {
                    DestinationCard(
                        destination = destination,
                        isFavorite = favorites.contains(destination.name),
                        onFavorite = { onFavorite(destination) },
                        onClick = { onDestinationClick(destination) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
        }
    }
}

@Composable
private fun CartContent(
    cart: List<Destination>,
    onRemove: (Destination) -> Unit,
    onTripClick: (Destination) -> Unit
) {

    if (cart.isEmpty()) {

        EmptyState(
            icon = Icons.Outlined.Luggage,
            title = "Nenhuma viagem planejada",
            description = "Adicione destinos para começar a montar sua viagem."
        )

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),

        verticalArrangement = Arrangement.spacedBy(
            12.dp
        )
    ) {

        item {

            Text(
                text = "Minhas viagens",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(
            items = cart,
            key = {
                it.name
            }
        ) { destination ->
            ScrollReveal {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTripClick(destination)
                    },

                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
            ) {

                Row(
                    modifier = Modifier.padding(12.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = destination.image,

                        contentDescription = destination.name,

                        modifier = Modifier
                            .size(90.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            ),

                        contentScale = ContentScale.Crop
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = destination.name,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = destination.location,
                            fontSize = 12.sp,
                            color = NextRouteGray
                        )

                        Text(
                            text = destination.price,
                            color = NextRoutePurple,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = {
                            onRemove(destination)
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Remover"
                        )
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun NotificationsContent() {
    val context = LocalContext.current
    val dbHelper = remember { NextRouteDbHelper(context) }

    val avisos = remember {
        dbHelper.listarAvisosAtivos()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 20.dp
        )
    ) {
        item {
            Text(
                text = "Notificações",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }

        if (avisos.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Outlined.Notifications,
                    title = "Nenhum aviso",
                    description = "No momento não há novos avisos."
                )
            }
        } else {
            items(
                items = avisos,
                key = { it.id }
            ) { aviso ->
                ScrollReveal {
                    NotificationItem(
                        title = aviso.title,
                        description = aviso.description
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    title: String,
    description: String
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp
            ),

        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = NextRoutePurple
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = description,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    name: String,
    email: String,
    phone: String,
    photo: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPhotoClick: () -> Unit,
    onSave: () -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 20.dp
        )
    ) {

        item {

            Text(
                text = "Meu perfil",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                if (photo != null) {

                    AsyncImage(
                        model = photo,

                        contentDescription = "Foto de perfil",

                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .clickable {
                                onPhotoClick()
                            },

                        contentScale = ContentScale.Crop
                    )

                } else {

                    Surface(
                        modifier = Modifier
                            .size(110.dp)
                            .clickable {
                                onPhotoClick()
                            },

                        shape = CircleShape,
                        color = NextRouteLightPurple,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NextRouteLightBlue)
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,

                            contentDescription = "Adicionar foto",

                            modifier = Modifier.padding(20.dp),

                            tint = NextRoutePurple
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Toque na foto para alterar",

                modifier = Modifier.fillMaxWidth(),

                fontSize = 12.sp,
                color = NextRouteGray
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            OutlinedTextField(
                value = name,

                onValueChange = onNameChange,

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Nome")
                },

                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = email,

                onValueChange = onEmailChange,

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("E-mail")
                },

                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = phone,

                onValueChange = onPhoneChange,

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Telefone")
                },

                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onSave,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = NextRoutePurple
                ),

                shape = RoundedCornerShape(9.dp)
            ) {

                Text("Salvar informações")
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    description: String
) {

    Box(
        modifier = Modifier.fillMaxSize(),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier.padding(30.dp)
        ) {

            Icon(
                imageVector = icon,

                contentDescription = null,

                modifier = Modifier.size(48.dp),
                tint = NextRouteGray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = title,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = NextRouteGray
            )
        }
    }
}

@Composable
private fun DestinationDialog(
    destination: Destination,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onAddCart: () -> Unit,
    onClose: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onClose,

        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = destination.name,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = destination.location,
                        fontSize = 12.sp,
                        color = NextRouteGray
                    )
                }

                IconButton(
                    onClick = onFavorite
                ) {

                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },

                        contentDescription = "Favorito",

                        tint = NextRoutePurple
                    )
                }
            }
        },

        text = {

            Column {

                AsyncImage(
                    model = destination.image,

                    contentDescription = destination.name,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(
                            RoundedCornerShape(14.dp)
                        ),

                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = destination.description,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = destination.details,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Categoria: ${destination.category}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NextRoutePurple
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = destination.price,
                    color = NextRoutePurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    onAddCart()
                    onClose()
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = NextRoutePurple
                )
            ) {

                Text("Adicionar à viagem")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onClose
            ) {

                Text("Fechar")
            }
        }
    )
}