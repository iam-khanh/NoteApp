package com.example.notefirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notefirebase.databinding.NoteItemBinding
import com.google.firebase.database.FirebaseDatabase

// Bài 16
private lateinit var binding: NoteItemBinding
class EmpAdapter(private var ds: ArrayList<EmployeeModel>): RecyclerView.Adapter<EmpAdapter.Viewholder>() {
    // code lắng nghe suwj kiện trên
    private lateinit var mListener: onItemClickListener
    private lateinit var dListener: onDeleteListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    interface onDeleteListener{
        fun onDeleteClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener, deleteListenr: onDeleteListener){
        mListener = clickListener
        dListener = deleteListenr
    }
    fun deleteItem(position: Int) {
        // Xác định nút cần xóa trong cơ sở dữ liệu Firebase
        val selectedItem = ds[position]
        val empId = selectedItem.empId

        // Reference đến nút cần xóa trong cơ sở dữ liệu Firebase
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(empId.toString())

        // Xóa bản ghi
        dbRef.removeValue()
    }


    class Viewholder(binding: NoteItemBinding, clickListener: onItemClickListener, deleteListenr: onDeleteListener): RecyclerView.ViewHolder(binding.root){
        init {
                binding.btnupdate.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
                binding.btndelete.setOnClickListener {
                    deleteListenr.onDeleteClick(adapterPosition)
                }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(parent.context)
        binding = NoteItemBinding.inflate(itemView, parent, false)
        return Viewholder(binding, mListener, dListener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.apply {
            binding.txttitleitem.text = ds[position].emptitle
            binding.txtcontentitem.text = ds[position].empcontent
        }
    }

    override fun getItemCount(): Int {
        return ds.size
    }
}