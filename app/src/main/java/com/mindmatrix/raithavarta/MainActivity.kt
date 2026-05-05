package com.mindmatrix.raithavarta

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show()
            }
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
        if (categoryChipGroup.childCount > 0) {
            (categoryChipGroup.getChildAt(0) as Chip).isChecked = true
        }
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
            // Delete existing to update with local images
            database.clearAllTables()
            
            val initialTips = listOf(
                TipEntity(
                    category = "Paddy",
                    instruction = "Apply Neem cake during land preparation. It prevents stem borer attacks.",
                    kannadaInstruction = "ಭೂಮಿ ಸಿದ್ಧಪಡಿಸುವಾಗ ಬೇವಿನ ಹಿಂಡಿ ಹಾಕಿ. ಇದು ಕಾಂಡ ಕೊರೆಯುವ ಹುಳುವನ್ನು ತಡೆಯುತ್ತದೆ.",
                    tipImage = "paddy_tip"
                ),
                TipEntity(
                    category = "Areca nut",
                    instruction = "Spray 1% Bordeaux mixture before monsoon. It prevents fruit rot disease.",
                    kannadaInstruction = "ಮಳೆಗಾಲಕ್ಕೆ ಮುಂಚೆ 1% ಬೋರ್ಡೋ ಮಿಶ್ರಣ ಸಿಂಪಡಿಸಿ. ಇದು ಕೊಳೆ ರೋಗ ತಡೆಯುತ್ತದೆ.",
                    tipImage = "areca_tip",
                    isSuccessStory = true,
                    farmerName = "Siddappa from Shimoga"
                ),
                TipEntity(
                    category = "Tomato",
                    instruction = "Use sticky yellow traps in your field. It catches whiteflies effectively.",
                    kannadaInstruction = "ನಿಮ್ಮ ಹೊಲದಲ್ಲಿ ಹಳದಿ ಬಣ್ಣದ ಅಂಟು ಬಲೆ ಬಳಸಿ. ಇದು ಬಿಳಿನೊಣಗಳನ್ನು ಪರಿಣಾಮಕಾರಿಯಾಗಿ ಹಿಡಿಯುತ್ತದೆ.",
                    tipImage = "tomato_tip"
                ),
                TipEntity(
                    category = "Coconut",
                    instruction = "Provide adequate summer irrigation and mulch the basin. Prevents button dropping.",
                    kannadaInstruction = "ಬೇಸಿಗೆಯಲ್ಲಿ ಸಾಕಷ್ಟು ನೀರು ಒದಗಿಸಿ ಮತ್ತು ಬುಡಕ್ಕೆ ಹೊದಿಕೆ ಹಾಕಿ. ಇದು ಕಾಯಿ ಉದುರುವುದನ್ನು ತಡೆಯುತ್ತದೆ.",
                    tipImage = "coconut_tip"
                )
            )
            database.tipDao().insertTips(initialTips)
            withContext(Dispatchers.Main) {
                loadTips("All")
            }
        }
    }
}
