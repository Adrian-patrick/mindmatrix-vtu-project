package com.mindmatrix.raithavarta

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.mindmatrix.raithavarta.data.AppDatabase
import com.mindmatrix.raithavarta.data.TipEntity
import com.mindmatrix.raithavarta.ui.TipAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var fabAskExpert: ExtendedFloatingActionButton
    private lateinit var tipAdapter: TipAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)
        
        viewPager = findViewById(R.id.viewPager)
        categoryChipGroup = findViewById(R.id.categoryChipGroup)
        fabAskExpert = findViewById(R.id.fabAskExpert)

        tipAdapter = TipAdapter()
        viewPager.adapter = tipAdapter

        setupChips()
        populateDatabase()
        
        fabAskExpert.setOnClickListener {
            Toast.makeText(this, "Expert Ask: Simulated feature. Please capture photo.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChips() {
        val categories = listOf("All", "Paddy", "Areca nut", "Coconut", "Tomato")
        for (category in categories) {
            val chip = Chip(this).apply {
                text = category
                isCheckable = true
                isCheckedIconVisible = true
            }
            categoryChipGroup.addView(chip)
        }

        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds.first())
                loadTips(chip.text.toString())
            }
        }
        
        // Select 'All' by default
        val firstChip = categoryChipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true
    }

    private fun loadTips(category: String) {
        lifecycleScope.launch {
            database.tipDao().getTipsByCategory(category).collectLatest { tips ->
                tipAdapter.submitList(tips)
            }
        }
    }

    private fun populateDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.tipDao().getCount() == 0) {
                val initialTips = listOf(
                    TipEntity(
                        category = "Paddy",
                        instruction = "Apply Neem cake during land preparation. It prevents stem borer attacks.",
                        kannadaInstruction = "ಭೂಮಿ ಸಿದ್ಧಪಡಿಸುವಾಗ ಬೇವಿನ ಹಿಂಡಿ ಹಾಕಿ. ಇದು ಕಾಂಡ ಕೊರೆಯುವ ಹುಳುವನ್ನು ತಡೆಯುತ್ತದೆ.",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Paddy_field_in_Bangladesh.jpg/800px-Paddy_field_in_Bangladesh.jpg"
                    ),
                    TipEntity(
                        category = "Areca nut",
                        instruction = "Spray 1% Bordeaux mixture before monsoon. It prevents fruit rot disease.",
                        kannadaInstruction = "ಮಳೆಗಾಲಕ್ಕೆ ಮುಂಚೆ 1% ಬೋರ್ಡೋ ಮಿಶ್ರಣ ಸಿಂಪಡಿಸಿ. ಇದು ಕೊಳೆ ರೋಗ ತಡೆಯುತ್ತದೆ.",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Arecanut_on_Tree.JPG/800px-Arecanut_on_Tree.JPG",
                        isSuccessStory = true,
                        farmerName = "Siddappa from Shimoga"
                    ),
                    TipEntity(
                        category = "Tomato",
                        instruction = "Use sticky yellow traps in your field. It catches whiteflies effectively.",
                        kannadaInstruction = "ನಿಮ್ಮ ಹೊಲದಲ್ಲಿ ಹಳದಿ ಬಣ್ಣದ ಅಂಟು ಬಲೆ ಬಳಸಿ. ಇದು ಬಿಳಿನೊಣಗಳನ್ನು ಪರಿಣಾಮಕಾರಿಯಾಗಿ ಹಿಡಿಯುತ್ತದೆ.",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/800px-Tomato_je.jpg"
                    ),
                    TipEntity(
                        category = "Coconut",
                        instruction = "Provide adequate summer irrigation and mulch the basin. Prevents button dropping.",
                        kannadaInstruction = "ಬೇಸಿಗೆಯಲ್ಲಿ ಸಾಕಷ್ಟು ನೀರು ಒದಗಿಸಿ ಮತ್ತು ಬುಡಕ್ಕೆ ಹೊದಿಕೆ ಹಾಕಿ. ಇದು ಕಾಯಿ ಉದುರುವುದನ್ನು ತಡೆಯುತ್ತದೆ.",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Coconut_tree_Kerala.jpg/800px-Coconut_tree_Kerala.jpg"
                    )
                )
                database.tipDao().insertTips(initialTips)
                withContext(Dispatchers.Main) {
                    // Reload 'All' category after populating
                    loadTips("All")
                }
            }
        }
    }
}
