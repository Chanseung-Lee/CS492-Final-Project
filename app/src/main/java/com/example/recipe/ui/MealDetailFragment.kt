package com.example.recipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.recipe.data.Meal
import com.example.recipe.R

import android.widget.Button
import android.widget.Toast
import com.itextpdf.html2pdf.HtmlConverter
import android.content.Context
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

import java.io.IOException


import java.io.OutputStream

class MealDetailFragment : Fragment(R.layout.fragment_meal_detail) {
    private val viewModel: MealViewModel by viewModels()

    private var selectedMeal: Meal? = null

    private lateinit var imageViewMeal: ImageView
    private lateinit var textViewMealName: TextView
    private lateinit var textViewMealDetails: TextView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var textViewInstructions: TextView
    private lateinit var textViewTags: TextView
    private lateinit var textViewYouTubeLink: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        imageViewMeal = view.findViewById(R.id.mealImage)
        textViewMealName = view.findViewById(R.id.mealName)
        textViewMealDetails = view.findViewById(R.id.mealDetails)
        linearLayoutIngredients = view.findViewById(R.id.ingredientsList)
        textViewInstructions = view.findViewById(R.id.instructionsText)
        textViewTags = view.findViewById(R.id.tagsText)
        textViewYouTubeLink = view.findViewById(R.id.youtubeLinkText)


        viewModel.meal.observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                bind(meal)
                imageViewMeal.visibility = View.VISIBLE
                selectedMeal = meal
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {error ->
            if (error != null) {
                Log.e(tag, "Error fetching forecast: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                imageViewMeal.visibility = View.INVISIBLE
            }
        }

        view.findViewById<Button>(R.id.export_pdf_button).setOnClickListener {
            generatePdfFromHtml()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MealDetailFragment", "resuming")

        // Retrieve the meal name from the arguments
        val mealName = arguments?.getString("mealName") ?: "Arrabiata" // Default value if no argument is passed
        viewModel.searchMeal(mealName)
    }

    private fun bind(selectedMeal: Meal) {
        // Load meal image using Glide
        Glide.with(this)
            .load(selectedMeal.strMealThumb)
            .placeholder(R.drawable.meal_placeholder)
            .error(R.drawable.error_placeholder)
            .into(imageViewMeal)

        // Set Meal Name and Details
        textViewMealName.text = selectedMeal.strMeal
        textViewMealDetails.text = "${selectedMeal.strCategory} · ${selectedMeal.strArea}"

        // Set Ingredients
        linearLayoutIngredients.removeAllViews()
        getIngredientsList(selectedMeal).forEach { ingredient ->
            val textView = TextView(context)
            textView.text = ingredient
            linearLayoutIngredients.addView(textView)
        }

        // Set Instructions
        textViewInstructions.text = selectedMeal.strInstructions

        // Set Tags
        textViewTags.text = "Tags: ${selectedMeal.strTags}"

        // Set YouTube Link
        textViewYouTubeLink.apply {
            text = "YouTube Link"
            paint.isUnderlineText = true
            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedMeal.strYoutube))
                startActivity(intent)
            }
        }
    }

    private fun getIngredientsList(meal: Meal): List<String> {
        return listOfNotNull(
            if (!meal.strIngredient1.isNullOrBlank()) "${meal.strIngredient1}: ${meal.strMeasure1}" else null,
            if (!meal.strIngredient2.isNullOrBlank()) "${meal.strIngredient2}: ${meal.strMeasure2}" else null,
            if (!meal.strIngredient3.isNullOrBlank()) "${meal.strIngredient3}: ${meal.strMeasure3}" else null,
            if (!meal.strIngredient4.isNullOrBlank()) "${meal.strIngredient4}: ${meal.strMeasure4}" else null,
            if (!meal.strIngredient5.isNullOrBlank()) "${meal.strIngredient5}: ${meal.strMeasure5}" else null,
            if (!meal.strIngredient6.isNullOrBlank()) "${meal.strIngredient6}: ${meal.strMeasure6}" else null,
            if (!meal.strIngredient7.isNullOrBlank()) "${meal.strIngredient7}: ${meal.strMeasure7}" else null,
            if (!meal.strIngredient8.isNullOrBlank()) "${meal.strIngredient8}: ${meal.strMeasure8}" else null,
            if (!meal.strIngredient9.isNullOrBlank()) "${meal.strIngredient9}: ${meal.strMeasure9}" else null,
            if (!meal.strIngredient10.isNullOrBlank()) "${meal.strIngredient10}: ${meal.strMeasure10}" else null,
            if (!meal.strIngredient11.isNullOrBlank()) "${meal.strIngredient11}: ${meal.strMeasure11}" else null,
            if (!meal.strIngredient12.isNullOrBlank()) "${meal.strIngredient12}: ${meal.strMeasure12}" else null,
            if (!meal.strIngredient13.isNullOrBlank()) "${meal.strIngredient13}: ${meal.strMeasure13}" else null,
            if (!meal.strIngredient14.isNullOrBlank()) "${meal.strIngredient14}: ${meal.strMeasure14}" else null,
            if (!meal.strIngredient15.isNullOrBlank()) "${meal.strIngredient15}: ${meal.strMeasure15}" else null,
            if (!meal.strIngredient16.isNullOrBlank()) "${meal.strIngredient16}: ${meal.strMeasure16}" else null,
            if (!meal.strIngredient17.isNullOrBlank()) "${meal.strIngredient17}: ${meal.strMeasure17}" else null,
            if (!meal.strIngredient18.isNullOrBlank()) "${meal.strIngredient18}: ${meal.strMeasure18}" else null,
            if (!meal.strIngredient19.isNullOrBlank()) "${meal.strIngredient19}: ${meal.strMeasure19}" else null,
            if (!meal.strIngredient20.isNullOrBlank()) "${meal.strIngredient20}: ${meal.strMeasure20}" else null
        )
    }

    private fun generatePdfFromHtml() {
        val htmlTemplate = loadHtmlTemplate(requireContext())

        val meal = selectedMeal ?: return

        val modifiedHtmlContent = htmlTemplate
            .replace("{{Title}}", meal.strMeal)
            .replace("{{Image}}", "${meal.strMealThumb}")
            .replace("{{Tags}}", "${meal.strArea} · ${meal.strCategory} · ${meal.strTags}")
            .replace("{{Instructions}}", "${meal.strInstructions}")
            .replace("{{strIngredient1}}", "${meal.strIngredient1 ?: ""}: ${meal.strMeasure1 ?: ""}")
            .replace("{{strIngredient2}}", "${meal.strIngredient2 ?: ""}: ${meal.strMeasure2 ?: ""}")
            .replace("{{strIngredient3}}", "${meal.strIngredient3 ?: ""}: ${meal.strMeasure3 ?: ""}")
            .replace("{{strIngredient4}}", "${meal.strIngredient4 ?: ""}: ${meal.strMeasure4 ?: ""}")
            .replace("{{strIngredient5}}", "${meal.strIngredient5 ?: ""}: ${meal.strMeasure5 ?: ""}")
            .replace("{{strIngredient6}}", "${meal.strIngredient6 ?: ""}: ${meal.strMeasure6 ?: ""}")
            .replace("{{strIngredient7}}", "${meal.strIngredient7 ?: ""}: ${meal.strMeasure7 ?: ""}")
            .replace("{{strIngredient8}}", "${meal.strIngredient8 ?: ""}: ${meal.strMeasure8 ?: ""}")
            .replace("{{strIngredient9}}", "${meal.strIngredient9 ?: ""}: ${meal.strMeasure9 ?: ""}")
            .replace("{{strIngredient10}}", "${meal.strIngredient10 ?: ""}: ${meal.strMeasure10 ?: ""}")
            .replace("{{strIngredient11}}", "${meal.strIngredient11 ?: ""}: ${meal.strMeasure11 ?: ""}")
            .replace("{{strIngredient12}}", "${meal.strIngredient12 ?: ""}: ${meal.strMeasure12 ?: ""}")
            .replace("{{strIngredient13}}", "${meal.strIngredient13 ?: ""}: ${meal.strMeasure13 ?: ""}")
            .replace("{{strIngredient14}}", "${meal.strIngredient14 ?: ""}: ${meal.strMeasure14 ?: ""}")
            .replace("{{strIngredient15}}", "${meal.strIngredient15 ?: ""}: ${meal.strMeasure15 ?: ""}")
            .replace("{{strIngredient16}}", "${meal.strIngredient16 ?: ""}: ${meal.strMeasure16 ?: ""}")
            .replace("{{strIngredient17}}", "${meal.strIngredient17 ?: ""}: ${meal.strMeasure17 ?: ""}")
            .replace("{{strIngredient18}}", "${meal.strIngredient18 ?: ""}: ${meal.strMeasure18 ?: ""}")
            .replace("{{strIngredient19}}", "${meal.strIngredient19 ?: ""}: ${meal.strMeasure19 ?: ""}")
            .replace("{{strIngredient20}}", "${meal.strIngredient20 ?: ""}: ${meal.strMeasure20 ?: ""}")


        Log.d("Log1", "${meal.strMealThumb}")
        Log.d("Log3", "${meal.strCategory} · ${meal.strArea}")

        Log.d("Log2", modifiedHtmlContent)
        createPdfFromHtml(requireContext(), modifiedHtmlContent)
    }

    fun loadHtmlTemplate(context: Context): String {
        val assetManager = context.assets
        val inputStream = assetManager.open("template.html")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return String(buffer, Charsets.UTF_8)
    }

    private fun createPdfFromHtml(context: Context, htmlContent: String) {
        val pdfFileName = "myPdf_${System.currentTimeMillis()}.pdf"

        try {
            val outputStream: OutputStream
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                val uri = context.contentResolver.insert(contentUri, contentValues)
                outputStream = context.contentResolver.openOutputStream(uri ?: throw IOException("Failed to create new MediaStore record."))!!
            } else {
                @Suppress("DEPRECATION")
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = java.io.File(downloadsDir, pdfFileName)
                if (!downloadsDir.exists()) downloadsDir.mkdir()
                outputStream = java.io.FileOutputStream(file)
            }

            HtmlConverter.convertToPdf(htmlContent, com.itextpdf.kernel.pdf.PdfWriter(outputStream))
            Toast.makeText(context, "PDF saved successfully to Downloads", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}