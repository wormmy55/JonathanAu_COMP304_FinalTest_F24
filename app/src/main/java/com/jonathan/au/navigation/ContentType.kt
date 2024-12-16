package com.jonathan.au.navigation

sealed interface ContentType {
    object List: ContentType
    object Detail: ContentType
}