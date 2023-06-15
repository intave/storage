package de.jpx3.intavestorage.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.jpx3.intavestorage.ExpiringStorageGateway
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.Binary
import java.nio.ByteBuffer
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Storage gateway between Intave and a MongoDB database.
 *
 * @property config The [MongoDbConfiguration].
 */
class MongoDbStorageGateway(
    private val config: MongoDbConfiguration
) : ExpiringStorageGateway {
    private val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(
            PojoCodecProvider.builder().automatic(true).build()
        )
    )
    private val connectionSettings = createSettings()
    private val connection = MongoClients.create(connectionSettings)
    private val database = connection
        .getDatabase(config.database)
        .withCodecRegistry(pojoCodecRegistry)
    private val storageCollection = database
        .getCollection("storage")
        .withCodecRegistry(pojoCodecRegistry)

    private fun createSettings(): MongoClientSettings {
        return if (config.authorization) {
            createSettingsWithAuthorization()
        } else {
            createSettingsWithoutAuthorization()
        }
    }

    private fun createSettingsWithAuthorization(): MongoClientSettings {
        val user = config.user!!
        val password = config.password!!
        val defaultDatabase = config.defaultDb!!

        val mongoCredential = MongoCredential.createCredential(
            user,
            defaultDatabase,
            password.toCharArray()
        )
        return MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://${config.host}"))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .codecRegistry(pojoCodecRegistry)
            .credential(mongoCredential)
            .build()
    }

    private fun createSettingsWithoutAuthorization(): MongoClientSettings {
        return MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://${config.host}"))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .codecRegistry(pojoCodecRegistry)
            .build()
    }

    override fun clearOldEntries() {
        val expirationThreshold = TimeUnit.DAYS.toMillis(config.expirationThreshold)
        if (expirationThreshold < 0) {
            return
        }
        val expirationDelta = System.currentTimeMillis() - expirationThreshold
        storageCollection.deleteMany(
            Filters.lt("last_used", expirationDelta)
        )
    }

    override fun requestStorage(uuid: UUID, consumer: Consumer<ByteBuffer>) {
        val emptyArray = ByteArray(0)
        // If no player is in the database return empty data
        val user = storageCollection.find(
            Filters.eq("player", uuid)
        ).first() ?: run {
            consumer.accept(ByteBuffer.wrap(emptyArray))
            return
        }

        // Fetch the binary data of the player
        val binary = user.get("data", Binary::class.java)
        consumer.accept(ByteBuffer.wrap(binary.data))
    }

    override fun saveStorage(uuid: UUID, buffer: ByteBuffer) {
        val requiresInsert = storageCollection.find(
            Filters.eq("player", uuid)
        ).first() == null

        // If no player is in the database, insert a new one
        if (requiresInsert) {
            val playerData = hashMapOf<String, Any>(
                "player" to uuid,
                "data" to buffer.array(),
                "last_used" to System.currentTimeMillis()
            )
            val document = Document(playerData)
            storageCollection.insertOne(document)
        } else {
            storageCollection.updateOne(
                Filters.eq("player", uuid),
                Updates.set("data", buffer.array())
            )
        }
    }
}
