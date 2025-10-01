package com.f8fit.bambutestandroid.presentation.productsModule.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.f8fit.bambutestandroid.navigation.NavRoutes
import com.f8fit.bambutestandroid.presentation.cartModule.viewModel.CartViewModel
import com.f8fit.bambutestandroid.presentation.loginModule.view.LoginActivity
import com.f8fit.bambutestandroid.presentation.loginModule.viewModel.AuthViewModel
import com.f8fit.bambutestandroid.presentation.productsModule.components.ProductCard
import com.f8fit.bambutestandroid.presentation.productsModule.viewModel.ProductUiState
import com.f8fit.bambutestandroid.presentation.productsModule.viewModel.ProductViewModel
import com.f8fit.bambutestandroid.utils.ProfilePreferences
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    vm: ProductViewModel = hiltViewModel(),
    cartVm: CartViewModel = hiltViewModel(),
    authVm: AuthViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current
    val searchQuery by vm.searchQuery.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var menuExpanded by remember { mutableStateOf(false) }
    val user = FirebaseAuth.getInstance().currentUser
    val cartItems by cartVm.cartItems.collectAsState()
    val cartCount = cartItems.sumOf { it.quantity }
    val products = vm.productsFlow.collectAsLazyPagingItems()
    val profileUri by ProfilePreferences.getProfileUri(context).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    IconButton(onClick = {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            navController.navigate(NavRoutes.Cart.route)
                        } else {
                            Toast.makeText(context, "Inicia sesión para ver el carrito", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Text(
                                        text = if (cartCount > 9) "+9" else "$cartCount",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .background(Color.Red, shape = CircleShape)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }

                    if (user != null) {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Perfil") },
                                    onClick = {
                                        menuExpanded = false
                                        navController.navigate(NavRoutes.Profile.route)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cerrar sesión") },
                                    onClick = {
                                        menuExpanded = false
                                        authVm.logout()
                                        FirebaseAuth.getInstance().signOut()
                                        val intent = Intent(context, LoginActivity::class.java)
                                        context.startActivity(intent)
                                        (context as? Activity)?.finish()
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (profileUri != null) {
                    AsyncImage(
                        model = profileUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(60.dp) // un poco más pequeño para verse mejor en el row
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Sin foto",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.2f)),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Bienvenido ${user?.displayName ?: "Invitado"}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { vm.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Buscar productos...") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { vm.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                        }
                    }
                }
            )

            when (state) {
                is ProductUiState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ProductUiState.Success -> {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products.itemCount) { index ->
                            products[index]?.let { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {
                                        navController.navigate(NavRoutes.ProductDetail.createRoute(product.id))
                                    },
                                    onAddToCart = {
                                        try {
                                            cartVm.addToCart(
                                                product.id,
                                                product.title,
                                                product.price.toDouble(),
                                                1,
                                                product.thumbnail
                                            )
                                            Toast.makeText(
                                                context,
                                                "Producto agregado al carrito",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Error al agregar el producto: ${e.localizedMessage}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )
                            }
                        }

                        // Estado de carga
                        products.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item { CircularProgressIndicator(Modifier.fillMaxWidth().padding(16.dp)) }
                                }
                                loadState.append is LoadState.Loading -> {
                                    item { CircularProgressIndicator(Modifier.fillMaxWidth().padding(16.dp)) }
                                }
                                loadState.append is LoadState.Error -> {
                                    val e = loadState.append as LoadState.Error
                                    item { Text("Error: ${e.error.localizedMessage}") }
                                }
                            }
                        }
                    }
                }
                is ProductUiState.Error -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${(state as ProductUiState.Error).message}")
                    }
                }
            }
        }
    }
}


