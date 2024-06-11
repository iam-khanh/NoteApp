package com.example.notefirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notefirebase.databinding.ActivityAddnoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddnoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddnoteBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddnoteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        // Xử lí sự kiện save
        binding.btnsave.setOnClickListener {
            saveEmplydata()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveEmplydata() {
        // gettring data
        val emptitle = binding.edttitle.text.toString()
        val empcontent = binding.edtcontent.text.toString()

        // Đẩy dữ liệu
        val empid = dbRef.push().key!!
        val employee = EmployeeModel(empid, emptitle, empcontent)

        // Kiểm tra các  dữ liêu đã có  hay chưa
        if(emptitle.isEmpty()) {
            binding.edttitle.error = "Please enter title"
            return
        }
        if(empcontent.isEmpty()) {
            binding.edtcontent.error = "Please enter content"
            return
        }

        dbRef.child(empid).setValue(employee)
            .addOnSuccessListener {
                Toast.makeText(this, "insert data success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}