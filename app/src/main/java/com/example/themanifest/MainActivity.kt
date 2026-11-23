package com.example.themanifest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.themanifest.ui.theme.TheManifestTheme
import java.util.Locale

private val ManifestGreen = Color(0xFF8BC34A)
private val LightGreen = Color(0xFFEAF5DD)
private val DarkGreen = Color(0xFF4F772D)

private const val WELCOME_SCREEN = "welcome"
private const val HOME_SCREEN = "home"
private const val ITINERARY_SCREEN = "itinerary"
private const val EXPENSE_SCREEN = "expenses"
private const val PLAN_SCREEN = "plan"

data class ItineraryItem(
    val day: String,
    val date: String,
    val title: String,
    val time: String,
    val location: String,
    val details: String
)

data class Expense(
    val title: String,
    val amount: Double,
    val paidBy: String
)

data class PackingItem(
    val name: String,
    val category: String,
    val isPacked: Boolean = false
)

private val sampleItinerary = listOf(
    ItineraryItem(
        day = "Day 1",
        date = "December 12",
        title = "Arrive in Beijing",
        time = "10:00 AM",
        location = "Beijing Capital International Airport",
        details = "Meet the group, collect luggage, and travel to the hotel."
    ),
    ItineraryItem(
        day = "Day 2",
        date = "December 13",
        title = "Forbidden City",
        time = "9:30 AM",
        location = "Dongcheng District",
        details = "Guided morning tour followed by lunch near Wangfujing."
    ),
    ItineraryItem(
        day = "Day 3",
        date = "December 14",
        title = "Great Wall Day Trip",
        time = "8:00 AM",
        location = "Mutianyu Great Wall",
        details = "Private transportation, cable car tickets, and group photos."
    ),
    ItineraryItem(
        day = "Day 4",
        date = "December 15",
        title = "High-Speed Train",
        time = "11:15 AM",
        location = "Beijing West Railway Station",
        details = "Travel to Xi'an and check into the group hotel."
    ),
    ItineraryItem(
        day = "Day 5",
        date = "December 16",
        title = "Terracotta Warriors",
        time = "9:00 AM",
        location = "Lintong District, Xi'an",
        details = "Museum visit, guided tour, and group dinner."
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TheManifestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ManifestApp()
                }
            }
        }
    }
}

@Composable
fun ManifestApp() {
    var currentScreen by rememberSaveable {
        mutableStateOf(WELCOME_SCREEN)
    }

    var expenses by remember {
        mutableStateOf(
            listOf(
                Expense("Street Food Dinner", 72.00, "Monaco"),
                Expense("Metro Tickets", 24.00, "Maria"),
                Expense("Guesthouse", 480.00, "Alex")
            )
        )
    }

    var packingItems by remember {
        mutableStateOf(
            listOf(
                PackingItem("Passport and visa", "Documents"),
                PackingItem("Travel insurance", "Documents"),
                PackingItem("Phone charger", "Electronics"),
                PackingItem("Power adapter", "Electronics"),
                PackingItem("Comfortable walking shoes", "Clothing"),
                PackingItem("Weather-appropriate jacket", "Clothing"),
                PackingItem("Medication", "Health")
            )
        )
    }

    when (currentScreen) {
        WELCOME_SCREEN -> {
            WelcomeScreen(
                onGetStarted = {
                    currentScreen = HOME_SCREEN
                }
            )
        }

        HOME_SCREEN -> {
            HomeScreen(
                onItineraryClick = {
                    currentScreen = ITINERARY_SCREEN
                },
                onExpensesClick = {
                    currentScreen = EXPENSE_SCREEN
                },
                onPlanClick = {
                    currentScreen = PLAN_SCREEN
                }
            )
        }

        ITINERARY_SCREEN -> {
            ItineraryScreen(
                onBack = {
                    currentScreen = HOME_SCREEN
                }
            )
        }

        EXPENSE_SCREEN -> {
            ExpenseScreen(
                expenses = expenses,
                onAddExpense = { newExpense ->
                    expenses = expenses + newExpense
                },
                onBack = {
                    currentScreen = HOME_SCREEN
                }
            )
        }

        PLAN_SCREEN -> {
            PlanScreen(
                packingItems = packingItems,
                onItemChanged = { index, isPacked ->
                    packingItems = packingItems.mapIndexed { itemIndex, item ->
                        if (itemIndex == index) {
                            item.copy(isPacked = isPacked)
                        } else {
                            item
                        }
                    }
                },
                onBack = {
                    currentScreen = HOME_SCREEN
                }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(210.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✈",
                color = ManifestGreen,
                fontSize = 100.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "The Manifest",
            color = DarkGreen,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Plan together. Travel smarter.",
            color = Color.DarkGray,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManifestGreen,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Get Started",
                modifier = Modifier.padding(horizontal = 48.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HomeScreen(
    onItineraryClick: () -> Unit,
    onExpensesClick: () -> Unit,
    onPlanClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Text(
            text = "The Manifest",
            color = DarkGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Beijing and Beyond",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "December 12–22 • 4 travelers",
            color = Color.DarkGray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        FeatureCard(
            title = "Trip Itinerary",
            description = "View daily activities and reservations",
            symbol = "🗺",
            onClick = onItineraryClick
        )

        FeatureCard(
            title = "Shared Expenses",
            description = "Track purchases and split the total",
            symbol = "$",
            onClick = onExpensesClick
        )

        FeatureCard(
            title = "Trip Planning",
            description = "Manage the group packing checklist",
            symbol = "✓",
            onClick = onPlanClick
        )
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    symbol: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = symbol,
                color = ManifestGreen,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ItineraryScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(top = 40.dp)
    ) {
        ScreenHeader(
            title = "Trip Itinerary",
            onBack = onBack
        )

        Text(
            text = "Beijing and Xi'an • December 12–22",
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color.DarkGray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(sampleItinerary) { item ->
                ItineraryCard(item)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ItineraryCard(item: ItineraryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.day} • ${item.date}",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item.time,
                    color = ManifestGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.location,
                color = Color.DarkGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.details,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ExpenseScreen(
    expenses: List<Expense>,
    onAddExpense: (Expense) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    val total = expenses.sumOf { it.amount }
    val amountPerPerson = total / 4.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(top = 40.dp)
    ) {
        ScreenHeader(
            title = "Shared Expenses",
            onBack = onBack
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkGreen
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Current trip total",
                    color = Color.White,
                    fontSize = 15.sp
                )

                Text(
                    text = money(total),
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${money(amountPerPerson)} per traveler",
                    color = LightGreen,
                    fontSize = 16.sp
                )
            }
        }

        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManifestGreen
            )
        ) {
            Text(
                text = "+ Add Expense",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(expenses) { expense ->
                ExpenseCard(expense)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { expense ->
                onAddExpense(expense)
                showDialog = false
            }
        )
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Paid by ${expense.paidBy}",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )

                Text(
                    text = "${money(expense.amount / 4.0)} per person",
                    color = DarkGreen,
                    fontSize = 13.sp
                )
            }

            Text(
                text = money(expense.amount),
                color = DarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (Expense) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var payer by remember {
        mutableStateOf("")
    }

    val amountValue = amount.toDoubleOrNull()

    val canSave =
        title.isNotBlank() &&
                payer.isNotBlank() &&
                amountValue != null &&
                amountValue > 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add a shared expense")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Expense name")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                    },
                    label = {
                        Text("Amount")
                    },
                    prefix = {
                        Text("$")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = payer,
                    onValueChange = {
                        payer = it
                    },
                    label = {
                        Text("Paid by")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    onSave(
                        Expense(
                            title = title.trim(),
                            amount = amountValue ?: 0.0,
                            paidBy = payer.trim()
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PlanScreen(
    packingItems: List<PackingItem>,
    onItemChanged: (Int, Boolean) -> Unit,
    onBack: () -> Unit
) {
    val packedCount = packingItems.count { it.isPacked }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(top = 40.dp)
    ) {
        ScreenHeader(
            title = "Trip Planning",
            onBack = onBack
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkGreen
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Packing progress",
                    color = Color.White,
                    fontSize = 15.sp
                )

                Text(
                    text = "$packedCount of ${packingItems.size} items packed",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Packing Checklist",
            modifier = Modifier.padding(horizontal = 20.dp),
            color = DarkGreen,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(packingItems) { index, item ->
                PackingItemCard(
                    item = item,
                    onCheckedChange = { isChecked ->
                        onItemChanged(index, isChecked)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PackingItemCard(
    item: PackingItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCheckedChange(!item.isPacked)
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isPacked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = ManifestGreen
                )
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = item.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = item.category,
                    color = Color.DarkGray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun ScreenHeader(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onBack) {
            Text(
                text = "‹ Home",
                color = DarkGreen,
                fontSize = 17.sp
            )
        }

        Text(
            text = title,
            modifier = Modifier.padding(start = 12.dp),
            color = DarkGreen,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun money(amount: Double): String {
    return String.format(
        Locale.US,
        "\$%.2f",
        amount
    )
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    TheManifestTheme {
        WelcomeScreen(onGetStarted = {})
    }
}