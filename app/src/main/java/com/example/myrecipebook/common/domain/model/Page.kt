package com.example.myrecipebook.common.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Page<T>(
    @SerialName("recipes")
    val items: List<T>,
    val total: Int,
    val skip: Int,
    val limit: Int
) {
    val hasNextPage: Boolean get() = skip + limit < total
}
