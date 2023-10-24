package dev.jdtech.jellyfin.models

import org.jellyfin.sdk.model.api.SessionInfo

data class FindroidSession (
    val id: String,
    val deviceName: String,
    val client: String,
    val userName: String,
)

fun SessionInfo.toFindroidSession(): FindroidSession {
    return FindroidSession(
        id = id.orEmpty(),
        deviceName = deviceName.orEmpty(),
        client = client.orEmpty(),
        userName = userName.orEmpty(),
    )
}