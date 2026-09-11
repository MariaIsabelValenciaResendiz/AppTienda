package com.example.appmovil_hu11

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovil_hu11.data.AppRepository
import com.example.appmovil_hu11.data.UserUI
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var tvCount: TextView
    private val repository = AppRepository()
    private val userList = mutableListOf<UserUI>()
    private lateinit var adapter: RecyclerView.Adapter<UserViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCount = findViewById(R.id.tvCount)
        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(this)

        adapter = object : RecyclerView.Adapter<UserViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
                val user = userList[position]
                holder.tvInitials.text = user.initials
                holder.tvName.text = user.name
                holder.tvUserDetails.text = "Usuario #${user.id}  @${user.username}"
                holder.tvEmail.text = user.email
                holder.tvPhone.text = user.phone
            }

            override fun getItemCount(): Int = userList.size
        }

        rvUsers.adapter = adapter
        fetchUsers()
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                val users = repository.getUsersUI()
                userList.clear()
                userList.addAll(users)
                tvCount.text = "${users.size} cuentas"
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvInitials: TextView = view.findViewById(R.id.tvInitials)
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvUserDetails: TextView = view.findViewById(R.id.tvUserDetails)
    val tvEmail: TextView = view.findViewById(R.id.tvEmail)
    val tvPhone: TextView = view.findViewById(R.id.tvPhone)
}