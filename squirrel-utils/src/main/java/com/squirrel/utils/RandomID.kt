package com.squirrel.utils

fun randomId(length: Int): String =
    buildString {
        repeat(length) {
            append((0 until 36).random().toString(36))
        }
    }