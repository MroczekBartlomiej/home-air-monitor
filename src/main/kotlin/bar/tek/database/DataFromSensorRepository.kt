package bar.tek.database

import bar.tek.model.DataFromSensorDocument
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.ZoneOffset


class DataFromSensorRepository(firebaseApp: FirebaseApp?) {

    private val database = FirebaseDatabase.getInstance(firebaseApp)
    private var ref = database.getReference("data/")

    fun saveTemperature(dataFromSensor: DataFromSensorDocument) {
        val dataFromSensorsReference = ref.child("dataFromSensors")
        val dataFromSensors = HashMap<String, Any>()
        dataFromSensors.put(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli().toString(), dataFromSensor)
        dataFromSensorsReference.updateChildrenAsync(dataFromSensors)
    }

    fun readLastTemperature() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post: DataFromSensorDocument = dataSnapshot.getValue(DataFromSensorDocument::class.java)
                println(post)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

}