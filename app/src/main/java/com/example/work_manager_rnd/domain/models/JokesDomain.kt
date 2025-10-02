package com.example.work_manager_rnd.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JokesDomain(
    val categories: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("icon_url")
    val iconUrl: String,
    val id: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val url: String,
    val value: String
)
