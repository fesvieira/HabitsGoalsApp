package com.fesvieira.habitsgoals.helpers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
/** Use android.Manifest.permission to check */
fun Context.isAllowedTo(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED