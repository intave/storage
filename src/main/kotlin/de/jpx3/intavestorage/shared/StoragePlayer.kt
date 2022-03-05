package de.jpx3.intavestorage.shared

import java.util.UUID

interface StoragePlayer {
    fun name(): String
    fun id(): UUID
}
