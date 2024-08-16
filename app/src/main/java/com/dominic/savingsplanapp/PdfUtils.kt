package com.dominic.savingsplanapp

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.dominic.savingsplanapp.room.Goal
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

object PdfUtils {
    @RequiresApi(Build.VERSION_CODES.Q) // Scoped storage is fully supported from API 29+
    fun generatePdf(context: Context, goal: Goal, savingsPlan: Map<String, String>) {
        val document = PdfDocument()

        // Create a page description
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

        // Start a page
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        // Create a Paint object
        val paint = Paint()
        paint.textSize = 16f

        // Draw the goal details on the PDF
        canvas.drawText("Goal: ${goal.goalName}", 80f, 50f, paint)
        canvas.drawText("Target Amount: $${goal.amountNeeded}", 80f, 80f, paint)
        canvas.drawText("Start Date: ${goal.startDate}", 80f, 110f, paint)
        canvas.drawText("End Date: ${goal.endDate}", 80f, 140f, paint)

        // Draw the savings plan on the PDF
        var yPosition = 170f
        for ((period, amount) in savingsPlan) {
            canvas.drawText("${period.capitalize(Locale.ROOT)} Savings: $amount", 80f, yPosition, paint)
            yPosition += 30f
        }

        document.finishPage(page)

        val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val targetPdf = File(directoryPath, "${goal.goalName}_SavingsPlan.pdf")

        try {
            document.writeTo(FileOutputStream(targetPdf))
            Toast.makeText(context, "PDF saved at ${targetPdf.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }


        document.close()
    }
}
