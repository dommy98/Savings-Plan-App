package com.dominic.savingsplanapp

import android.content.Context
import android.os.Environment
import com.dominic.savingsplanapp.room.Goal
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PdfUtils {

    fun generatePdf(context: Context, goal: Goal, savingsPlan: Map<String, Double>) {
        val document = Document()
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "SavingsPlan.pdf")
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            document.add(Paragraph("Financial Goal: ${goal.goalName}"))
            document.add(Paragraph("Start Date: ${goal.startDate}"))
            document.add(Paragraph("End Date: ${goal.endDate}"))
            document.add(Paragraph("Amount Needed: ${goal.amountNeeded}"))
            document.add(Paragraph("Daily Savings: ${savingsPlan["daily"]}"))
            document.add(Paragraph("Weekly Savings: ${savingsPlan["weekly"]}"))
            document.add(Paragraph("Monthly Savings: ${savingsPlan["monthly"]}"))
            document.add(Paragraph("Annual Savings: ${savingsPlan["annual"]}"))
            document.close()
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
