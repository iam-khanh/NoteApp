package com.example.notefirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notefirebase.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

       binding.edtupdatetitle.setText(intent.getStringExtra("emptitle"))
        binding.edtupdatecontent.setText(intent.getStringExtra("empcontent"))
        val empId = intent.getStringExtra("empId")

        binding.btnupdatesave.setOnClickListener {
            val newtitle = binding.edtupdatetitle.text.toString()
            val newcontent = binding.edtupdatecontent.text.toString()
            updatedata(empId.toString(), newtitle, newcontent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updatedata(empId: String, newtitle: String, newcontent: String) {
        val empRef = dbRef.child(empId)

        val updatedata = EmployeeModel(empId, newcontent, newtitle)

        empRef.setValue(updatedata)
            .addOnSuccessListener {
                Toast.makeText(this, "Update data succesfully", Toast.LENGTH_SHORT).show()
                val intent  = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Update data fail", Toast.LENGTH_SHORT).show()
            }

    }

}