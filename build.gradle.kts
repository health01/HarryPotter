// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrainsKotlinKapt) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}