package com.example.notefirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notefirebase.databinding.ActivityMainBinding
import com.example.notefirebase.databinding.NoteItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var ds:ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        binding.btnadd.setOnClickListener {
            val intent = Intent(this, AddnoteActivity::class.java)
            startActivity(intent)
        }

        // Code Recycle View
        binding.rvnote.layoutManager = LinearLayoutManager(this)
        binding.rvnote.setHasFixedSize(true)
        ds = arrayListOf<EmployeeModel>()

        GetThongTinNote()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun GetThongTinNote() {
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ds.clear()
                if(snapshot.exists()){
                    for(empSnap in snapshot.children){
                        val empdata = empSnap.getValue(EmployeeModel::class.java)
                        ds.add(empdata!!)
                    }
                    val mAdapter = EmpAdapter(ds)
                    binding.rvnote.adapter = mAdapter


                    mAdapter.setOnItemClickListener(
                        object : EmpAdapter.onItemClickListener {
                            @SuppressLint("SuspiciousIndentation")
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                                    // Putextra
                                    intent.putExtra("empId", ds[position].empId)
                                    intent.putExtra("emptitle", ds[position].emptitle)
                                    intent.putExtra("empcontent", ds[position].empcontent)
                                startActivity(intent)
                            }
                        },
                        object : EmpAdapter.onDeleteListener {
                            override fun onDeleteClick(position: Int) {
                                mAdapter.deleteItem(position)
                                val intent = Intent(this@MainActivity,MainActivity::class.java )
                                finish()
                                startActivity(intent)
                            }
                        }
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}