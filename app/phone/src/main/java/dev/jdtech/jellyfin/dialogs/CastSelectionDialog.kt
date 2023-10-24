package dev.jdtech.jellyfin.dialogs

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.jdtech.jellyfin.core.R as CoreR

fun getCastSelectionDialog(
    context: Context,
    onItemSelected: (which: Int) -> Unit,
    onCancel: () -> Unit,
): AlertDialog {
    val sessions = arrayOf("Jellyfin Web")
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle("Select device to cast to")
        .setItems(sessions) { _, which ->
            onItemSelected(which)
        }
        .setOnCancelListener {
            onCancel()
        }.create()
    return dialog
}
