package bar.tek.database

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.FileInputStream


class DatabaseConfiguration {

    fun initDatabase(): FirebaseApp? {
        val fileInputStream =
            FileInputStream("/Users/bartlomiejmroczek/IdeaProjects/home-air-monitor/src/main/resources/db.json")
        // Fetch the service account key JSON file contents
        // Fetch the service account key JSON file contents
        // Initialize the app with a service account, granting admin privileges

        // Initialize the app with a service account, granting admin privileges
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(fileInputStream)) // The database URL depends on the location of the database
            .setDatabaseUrl("https://temperature-monitor-5b98f-default-rtdb.europe-west1.firebasedatabase.app")
            .build()
        val firebaseApp = FirebaseApp.initializeApp(options)

        // As an admin, the app has access to read and write all data, regardless of Security Rules

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        val ref = FirebaseDatabase.getInstance()
            .getReference("restricted_access/secret_document")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val document = dataSnapshot.value
                println(document)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return firebaseApp
    }
}